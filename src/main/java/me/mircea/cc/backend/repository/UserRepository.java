package me.mircea.cc.backend.repository;

import me.mircea.cc.backend.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
