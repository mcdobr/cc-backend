package me.mircea.cc.backend.repository;

import me.mircea.cc.backend.model.Expense;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ExpenseRepository extends ReactiveCrudRepository<Expense, Long> {
    Flux<Expense> findAllByEmail(String email);
}
