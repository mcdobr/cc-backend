package me.mircea.cc.backend.controller;

import lombok.RequiredArgsConstructor;
import me.mircea.cc.backend.model.Income;
import me.mircea.cc.backend.service.IncomeService;
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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
public class IncomeController {
    private IncomeService incomeService;

    @GetMapping
    public Flux<Income> findAll(@AuthenticationPrincipal DefaultOAuth2AuthenticatedPrincipal principal) {
        return incomeService.findAll(principal);
    }

    @GetMapping("/{income-id}")
    public Mono<Income> findById(@AuthenticationPrincipal DefaultOAuth2AuthenticatedPrincipal principal,
                                 @PathVariable("income-id") @NotNull @Positive Long incomeId) {
        return incomeService.findById(principal, incomeId);
    }

    @PostMapping
    public Mono<Income> create(@AuthenticationPrincipal DefaultOAuth2AuthenticatedPrincipal principal,
                               @RequestBody Income income) {
        return incomeService.create(principal, income);
    }

    @PutMapping("/{income-id}")
    public Mono<Income> update(@AuthenticationPrincipal DefaultOAuth2AuthenticatedPrincipal principal,
                               @PathVariable("income-id") Long incomeId,
                               @RequestBody Income income) {
        return incomeService.update(incomeId, income);
    }

    @DeleteMapping("/{income-id}")
    public Mono<Void> delete(@AuthenticationPrincipal DefaultOAuth2AuthenticatedPrincipal principal,
                             @PathVariable("income-id") Long incomeId) {
        return incomeService.delete(incomeId);
    }
}
