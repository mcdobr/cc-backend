package me.mircea.cc.backend.service;

import lombok.RequiredArgsConstructor;
import me.mircea.cc.backend.model.Transaction;
import me.mircea.cc.backend.repository.TransactionRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public Flux<Transaction> findAll(JwtAuthenticationToken principal) {
        return transactionRepository.findAll();
    }

    public Mono<Transaction> findById(JwtAuthenticationToken principal, UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .switchIfEmpty(Mono.error(new NoSuchElementException()))
                .flatMap(Mono::just);
    }

    public Mono<Transaction> create(JwtAuthenticationToken principal, Transaction transaction) {
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
