package me.mircea.cc.backend.repository;

import me.mircea.cc.backend.model.Income;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IncomeRepository extends ReactiveMongoRepository<Income, String> {
    Flux<Income> findAllByEmail(String email);
}
