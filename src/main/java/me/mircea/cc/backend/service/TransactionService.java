package me.mircea.cc.backend.service;

import lombok.RequiredArgsConstructor;
import me.mircea.cc.backend.model.Transaction;
import me.mircea.cc.backend.repository.TransactionRepository;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public Flux<Transaction> findAll(DefaultOAuth2AuthenticatedPrincipal principal) {
        return transactionRepository.findAllForUser(principal.getAttribute("id"));
    }

    public Mono<Transaction> findById(DefaultOAuth2AuthenticatedPrincipal principal, UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .switchIfEmpty(Mono.error(new NoSuchElementException()))
                .flatMap(Mono::just);
    }

    public Mono<Transaction> create(DefaultOAuth2AuthenticatedPrincipal principal, Transaction transaction) {


        return transactionRepository.save(transaction);
    }

    public Mono<Transaction> update(UUID transactionId, Transaction transaction) {
        return transactionRepository.findById(transactionId)
                .switchIfEmpty(Mono.error(new NoSuchElementException()))
                .flatMap(inc -> transactionRepository.save(transaction));
    }

    public Mono<Void> delete(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .switchIfEmpty(Mono.error(new NoSuchElementException()))
                .flatMap(transactionRepository::delete);
    }
}
