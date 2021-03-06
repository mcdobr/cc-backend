package me.mircea.cc.backend.service;

import lombok.RequiredArgsConstructor;
import me.mircea.cc.backend.model.Expense;
import me.mircea.cc.backend.repository.ExpenseRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public Flux<Expense> findAll(DefaultOAuth2AuthenticatedPrincipal principal) {
        return expenseRepository.findAllByEmail(principal.getAttribute("email"));
    }

    public Mono<Expense> findById(DefaultOAuth2AuthenticatedPrincipal principal, Long expenseId) {
        return expenseRepository.findById(expenseId)
                .switchIfEmpty(Mono.error(new NoSuchElementException()))
                .flatMap(expense -> {
                    if (!Objects.equals(principal.getAttribute("email"), expense.getEmail())) {
                        return Mono.error(new AccessDeniedException("Access denied"));
                    } else {
                        return Mono.just(expense);
                    }
                });
    }

    public Mono<Expense> create(DefaultOAuth2AuthenticatedPrincipal principal, Expense expense) {
        expense.setEmail(principal.getAttribute("email"));
        return expenseRepository.save(expense);
    }

    public Mono<Expense> update(Long expenseId, Expense expense) {
        return expenseRepository.findById(expenseId)
                .switchIfEmpty(Mono.error(new NoSuchElementException()))
                .flatMap(exp -> expenseRepository.save(expense));
    }

    public Mono<Void> delete(Long expenseId) {
        return expenseRepository.findById(expenseId)
                .switchIfEmpty(Mono.error(new NoSuchElementException()))
                .flatMap(expenseRepository::delete);
    }
}
