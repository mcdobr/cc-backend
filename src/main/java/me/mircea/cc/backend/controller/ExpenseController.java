package me.mircea.cc.backend.controller;

import lombok.RequiredArgsConstructor;
import me.mircea.cc.backend.model.Expense;
import me.mircea.cc.backend.service.ExpenseService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
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

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @GetMapping
    public Flux<Expense> findAll(@AuthenticationPrincipal DefaultOAuth2AuthenticatedPrincipal principal) {
        return expenseService.findAll(principal);
    }

    @GetMapping("/{expense-id}")
    public Mono<Expense> findById(@AuthenticationPrincipal DefaultOAuth2AuthenticatedPrincipal principal,
                                  @PathVariable("expense-id") @NotBlank String expenseId) {
        return expenseService.findById(principal, expenseId);
    }

    @PostMapping
    public Mono<Expense> create(@AuthenticationPrincipal DefaultOAuth2AuthenticatedPrincipal principal,
                                @RequestBody Expense expense) {
        return expenseService.create(principal, expense);
    }

    @PutMapping("/{expense-id}")
    public Mono<Expense> update(@AuthenticationPrincipal DefaultOAuth2AuthenticatedPrincipal principal,
                                @PathVariable("expense-id") String expenseId,
                                @RequestBody Expense expense) {
        return expenseService.update(expenseId, expense);
    }

    @DeleteMapping("/{expense-id}")
    public Mono<Void> delete(@AuthenticationPrincipal DefaultOAuth2AuthenticatedPrincipal principal,
                             @PathVariable("expense-id") String expenseId) {
        return expenseService.delete(expenseId);
    }
}
