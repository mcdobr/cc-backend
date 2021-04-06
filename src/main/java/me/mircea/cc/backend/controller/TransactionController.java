package me.mircea.cc.backend.controller;

import lombok.RequiredArgsConstructor;
import me.mircea.cc.backend.model.Transaction;
import me.mircea.cc.backend.service.TransactionService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping(produces = {
            MediaType.APPLICATION_NDJSON_VALUE,
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.TEXT_EVENT_STREAM_VALUE
    })
    @PreAuthorize("hasAuthority('SCOPE_view_transactions')")
    public Flux<Transaction> findAll(@AuthenticationPrincipal JwtAuthenticationToken principal) {
        return transactionService.findAll(principal);
    }

    @GetMapping("/{transaction-id}")
    public Mono<Transaction> findById(@AuthenticationPrincipal JwtAuthenticationToken principal,
                                      @PathVariable("transaction-id") @NotNull UUID transactionId) {
        return transactionService.findById(principal, transactionId);
    }

    @PostMapping
    public Mono<Transaction> create(@AuthenticationPrincipal JwtAuthenticationToken principal,
                                    @RequestBody Transaction transaction) {
        return transactionService.create(principal, transaction);
    }

    @PutMapping("/{transaction-id}")
    public Mono<Transaction> update(@AuthenticationPrincipal JwtAuthenticationToken principal,
                                    @PathVariable("transaction-id") UUID transactionId,
                                    @RequestBody Transaction transaction) {
        return transactionService.update(transactionId, transaction);
    }

    @DeleteMapping("/{transaction-id}")
    public Mono<Void> delete(@AuthenticationPrincipal JwtAuthenticationToken principal,
                             @PathVariable("transaction-id") UUID transactionId) {
        return transactionService.delete(transactionId);
    }
}
