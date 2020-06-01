package me.mircea.cc.backend.service;

import lombok.RequiredArgsConstructor;
import me.mircea.cc.backend.model.Income;
import me.mircea.cc.backend.repository.IncomeRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepository incomeRepository;

    public Flux<Income> findAll(DefaultOAuth2AuthenticatedPrincipal principal) {
        return incomeRepository.findAllByEmail(principal.getAttribute("email"));
    }

    public Mono<Income> findById(DefaultOAuth2AuthenticatedPrincipal principal, String incomeId) {
        return incomeRepository.findById(incomeId)
                .switchIfEmpty(Mono.error(new NoSuchElementException()))
                .flatMap(income -> {
                    if (!Objects.equals(principal.getAttribute("email"), income.getEmail())) {
                        return Mono.error(new AccessDeniedException("Access denied"));
                    } else {
                        return Mono.just(income);
                    }
                });
    }

    public Mono<Income> create(Income income) {
        return incomeRepository.save(income);
    }

    public Mono<Income> update(String incomeId, Income income) {
        return incomeRepository.findById(incomeId)
                .switchIfEmpty(Mono.error(new NoSuchElementException()))
                .flatMap(inc -> incomeRepository.save(income));
    }

    public Mono<Void> delete(String incomeId) {
        return incomeRepository.findById(incomeId)
                .switchIfEmpty(Mono.error(new NoSuchElementException()))
                .flatMap(incomeRepository::delete);
    }
}
