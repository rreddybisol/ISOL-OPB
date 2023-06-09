package de.adorsys.opba.protocol.xs2a.service.xs2a.authenticate.embedded;

import de.adorsys.opba.protocol.api.common.ProtocolAction;
import de.adorsys.opba.protocol.bpmnshared.service.context.ContextUtil;
import de.adorsys.opba.protocol.bpmnshared.service.exec.ValidatedExecution;
import de.adorsys.opba.protocol.xs2a.config.protocol.ProtocolUrlsConfiguration;
import de.adorsys.opba.protocol.xs2a.context.Xs2aContext;
import de.adorsys.opba.protocol.xs2a.domain.dto.forms.ScaMethod;
import de.adorsys.opba.protocol.xs2a.service.xs2a.Xs2aRedirectExecutor;
import de.adorsys.opba.protocol.xs2a.util.logresolver.Xs2aLogResolver;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import static de.adorsys.opba.protocol.xs2a.service.xs2a.consent.ConsentConst.CONSENT_FINALIZED;

/**
 * Asks PSU for his SCA challenge result by redirect him to password input page. Suspends process to wait for users' input.
 */
@Service("xs2aAskForScaChallenge")
@RequiredArgsConstructor
public class Xs2aAskForScaChallenge extends ValidatedExecution<Xs2aContext> {

    private final RuntimeService runtimeService;
    private final Xs2aRedirectExecutor redirectExecutor;
    private final Xs2aLogResolver logResolver = new Xs2aLogResolver(getClass());

    @Override
    protected void doRealExecution(DelegateExecution execution, Xs2aContext context) {
        logResolver.log("doRealExecution: execution ({}) with context ({})", execution, context);
        context.setSelectedScaType(getSelectedAuthenticationType(context));
        redirectExecutor.redirect(execution, context, urls -> {
            ProtocolUrlsConfiguration.UrlSet urlSet = ProtocolAction.SINGLE_PAYMENT.equals(context.getAction())
                    ? urls.getPis() : urls.getAis();
            return urlSet.getParameters().getReportScaResult();
        });
    }

    @Override
    protected void doMockedExecution(DelegateExecution execution, Xs2aContext context) {
        logResolver.log("doMockedExecution: execution ({}) with context ({})", execution, context);

        ContextUtil.getAndUpdateContext(
            execution,
            (Xs2aContext ctx) -> {
                ctx.setLastScaChallenge("mock-challenge");
                ctx.setScaStatus(CONSENT_FINALIZED);
            }
        );
        runtimeService.trigger(execution.getId());
    }

    private String getSelectedAuthenticationType(Xs2aContext context) {
        if (CollectionUtils.isEmpty(context.getAvailableSca())) {
            return context.getScaSelected().getAuthenticationType().toString();
        } else {
            return context.getAvailableSca().stream()
                .filter(it -> context.getUserSelectScaId().equals(it.getKey()))
                .map(ScaMethod::getType)
                .findFirst().orElse(null);
        }
    }
}
