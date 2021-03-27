package me.mircea.cc.backend.repository;

import me.mircea.cc.backend.model.Transaction;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface TransactionRepository extends ReactiveCrudRepository<Transaction, UUID> {
    @Query("select t.* " +
            "from transaction_history t " +
            "order by t.created_at desc ")
    Flux<Transaction> findAll();
}
