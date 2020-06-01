package me.mircea.cc.backend.repository;

import me.mircea.cc.backend.model.Expense;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ExpenseRepository extends ReactiveMongoRepository<Expense, String> {
    Flux<Expense> findAllByEmail(String email);
}
