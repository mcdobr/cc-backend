package me.mircea.cc.backend.repository;

import me.mircea.cc.backend.model.Income;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface IncomeRepository extends ReactiveCrudRepository<Income, String> {
    Flux<Income> findAllByEmail(String email);
}
