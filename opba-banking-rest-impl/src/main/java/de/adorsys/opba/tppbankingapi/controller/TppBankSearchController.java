package de.adorsys.opba.tppbankingapi.controller;

import de.adorsys.opba.tppbankingapi.search.model.generated.BankDescriptor;
import de.adorsys.opba.tppbankingapi.search.model.generated.BankProfileDescriptor;
import de.adorsys.opba.tppbankingapi.search.model.generated.BankProfileResponse;
import de.adorsys.opba.tppbankingapi.search.model.generated.BankSearchResponse;
import de.adorsys.opba.tppbankingapi.search.resource.generated.TppBankSearchApi;
import de.adorsys.opba.tppbankingapi.service.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*") //FIXME move CORS at gateway/load balancer level
@SuppressWarnings("checkstyle:ParameterNumber")
public class TppBankSearchController implements TppBankSearchApi {
    private static final String EMPTY_STRING = "";

    @Value("${bank-search.start:0}") int defaultStart;
    @Value("${bank-search.max:10}") int defaultMax;

    private final BankService bankService;

    @Override
    public ResponseEntity<BankSearchResponse> bankSearchGET(
            UUID xRequestID,
            String xTimestampUTC,
            String xRequestSignature,
            String fintechId,
            String keyword,
            Integer start,
            Integer max,
            Boolean onlyActive
            ) {

        log.debug("Bank search get request. keyword:{}, start:{}, max:{}, onlyActive:{}, xRequestID:{}", keyword, start, max, onlyActive, xRequestID);
        if (keyword == null) {
            keyword = EMPTY_STRING;
        }
        if (start == null) {
            start = defaultStart;
        }
        if (max == null) {
            max = defaultMax;
        }
        if (onlyActive == null) {
            onlyActive = true;
        }

        List<BankDescriptor> banks = bankService.getBanks(keyword, start, max, onlyActive);

        BankSearchResponse response = new BankSearchResponse();
        response.bankDescriptor(banks);
        response.setKeyword(keyword);
        response.setMax(max);
        response.setStart(start);
        log.debug("Bank search response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BankProfileResponse> bankProfileGET(
            UUID xRequestID,
            UUID bankProfileId,
            String xTimestampUTC,
            String xRequestSignature,
            String fintechId,
            Boolean onlyActive) {

        log.debug("Bank profile request. bankProfileId:{}, onlyActive:{}, xRequestID:{}", bankProfileId, onlyActive, xRequestID);
        if (onlyActive == null) {
            onlyActive = true;
        }

        Optional<BankProfileDescriptor> bankProfile = bankService.getBankProfile(bankProfileId, onlyActive);
        if (!bankProfile.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BankProfileResponse response = new BankProfileResponse();
        response.setBankProfileDescriptor(bankProfile.get());
        log.debug("Bank profile response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

