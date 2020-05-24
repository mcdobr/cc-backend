package me.mircea.cc.backend.service;

import lombok.RequiredArgsConstructor;
import me.mircea.cc.backend.model.User;
import me.mircea.cc.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Flux<User> findAll() {
        return userRepository.findAll();
    }
}
