package de.adorsys.opba.fireflyexporter.controller.rest;

import de.adorsys.opba.fireflyexporter.client.TppBankSearchClient;
import de.adorsys.opba.tpp.banksearch.api.model.generated.BankSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final TppBankSearchClient bankSearchApi;

    @GetMapping("/search")
    public ResponseEntity<BankSearchResponse> searchBank(@RequestParam(value = "q", defaultValue = "") String query) {
        return bankSearchApi.bankSearchGET(UUID.randomUUID(), query, null, null, null, 0, DEFAULT_PAGE_SIZE, false);
    }
}
