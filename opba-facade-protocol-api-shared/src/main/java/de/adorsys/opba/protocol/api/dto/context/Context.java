package de.adorsys.opba.protocol.api.dto.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Context<REQUEST> {

    /**
     * Bank protocol ID that is used to serve this request, corresponds to {@code de.adorsys.opba.db.domain.entity.BankProtocol}
     * that is used to service this request.
     */
    private final Long serviceBankProtocolId;

    /**
     * Authorization protocol ID that is used to serve this request, corresponds to {@code de.adorsys.opba.db.domain.entity.BankProtocol}
     * that is used to authorize this request.
     */
    private final Long authorizationBankProtocolId;

    /**
     * Bank Profile ID for this request {@code de.adorsys.opba.db.domain.entity.BankProfile} (uuid field). Uniquely identifies
     * available actions for current protocol.
     */
    private final UUID bankProfileId;

    /**
     * The ID of this session.
     */
    @NonNull
    private final UUID serviceSessionId;

    /**
     * The ID of the authorization session associated with this request.
     */
    private final UUID authSessionId;

    /**
     * The IDs of all authorization session(s) associated with this request.
     */
    private final Set<UUID> associatedAuthSessionIds;

    /**
     * Will be used as redirect code when coming back from ASPSP.
     * (it happens after we can act in protocol)
     */
    private final UUID futureAspspRedirectCode;

    /**
     * Will be used as redirect code only when authorization session is opened or continued.
     * (it happens after we can act in protocol)
     */
    private final UUID futureRedirectCode;

    /**
     * Will be used as new authorization session id only when authorization session is opened.
     * (it happens after we can act in protocol)
     */
    private final UUID futureAuthSessionId;

    /**
     * Users' (PSU or FinTech) request that is to be serviced by protocol.
     */
    @NonNull
    private final REQUEST request;

    /**
     * Non-sensitive data like some database id that will be persisted along authorization session and can
     * be retrieved on future invocations.
     */
    private final String authContext;

    public String loggableBankId() {
        return String.format(
                "[protocol id: %s / bank profile id: %s]",
                getServiceBankProtocolId(),
                getBankProfileId()
        );
    }
}
