package me.mircea.cc.backend.repository;

import me.mircea.cc.backend.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
    Mono<User> findByEmail(String email);
}
