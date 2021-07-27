package de.adorsys.opba.protocol.xs2a.service.xs2a.oauth2;

import de.adorsys.opba.protocol.bpmnshared.dto.DtoMapper;
import de.adorsys.opba.protocol.bpmnshared.service.context.ContextUtil;
import de.adorsys.opba.protocol.bpmnshared.service.exec.ValidatedExecution;
import de.adorsys.opba.protocol.xs2a.context.Xs2aContext;
import de.adorsys.opba.protocol.xs2a.service.dto.ValidatedQueryHeaders;
import de.adorsys.opba.protocol.xs2a.service.mapper.QueryHeadersMapperTemplate;
import de.adorsys.opba.protocol.xs2a.service.xs2a.dto.oauth2.Xs2aOauth2Headers;
import de.adorsys.opba.protocol.xs2a.service.xs2a.dto.oauth2.Xs2aOauth2WithCodeParameters;
import de.adorsys.opba.protocol.xs2a.util.logresolver.Xs2aLogResolver;
import de.adorsys.xs2a.adapter.api.EmbeddedPreAuthorisationService;
import de.adorsys.xs2a.adapter.api.model.EmbeddedPreAuthorisationRequest;
import de.adorsys.xs2a.adapter.api.model.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;

@Service("xs2aEmbeddedPreAuthorization")
@RequiredArgsConstructor
public class Xs2aEmbeddedPreAuthorization extends ValidatedExecution<Xs2aContext> {

    private final Extractor extractor;
    private final EmbeddedPreAuthorisationService embeddedPreAuthorisationService;
    private final Xs2aLogResolver logResolver = new Xs2aLogResolver(getClass());

    @Override
    @SneakyThrows
    protected void doRealExecution(DelegateExecution execution, Xs2aContext context) {
        logResolver.log("doRealExecution: execution ({}) with context ({})", execution, context);

        ValidatedQueryHeaders<Xs2aOauth2WithCodeParameters, Xs2aOauth2Headers> validated = extractor.forExecution(context);

        logResolver.log("getToken with parameters: {}", validated.getQuery(), validated.getHeaders());
        EmbeddedPreAuthorisationRequest embeddedPreAuthorisationRequest = new EmbeddedPreAuthorisationRequest();
        embeddedPreAuthorisationRequest.setPassword(context.getPsuPassword());
        embeddedPreAuthorisationRequest.setUsername(context.getPsuId());
        TokenResponse response = this.embeddedPreAuthorisationService.getToken(embeddedPreAuthorisationRequest, validated.getHeaders().toHeaders());
        logResolver.log("getToken response: {}", response);
            ContextUtil.getAndUpdateContext(
                    execution,
                    (Xs2aContext ctx) -> {
                        ctx.setOauth2Token(response);
                        ctx.setOauth2PreStepNeeded(false);
                    }
            );


    }

    @Override
    protected void doValidate(DelegateExecution execution, Xs2aContext context) {
        logResolver.log("doValidate: execution ({}) with context ({})", execution, context);

     //   validator.validate(execution, context, this.getClass(), extractor.forValidation(context));
    }

    @Service
    public static class Extractor extends QueryHeadersMapperTemplate<Xs2aContext, Xs2aOauth2WithCodeParameters, Xs2aOauth2Headers> {

        public Extractor(
                DtoMapper<Xs2aContext, Xs2aOauth2Headers> toHeaders,
                DtoMapper<Xs2aContext, Xs2aOauth2WithCodeParameters> toQuery) {
            super(toHeaders, toQuery);
        }
    }
}
