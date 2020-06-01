package me.mircea.cc.backend.service;

import lombok.RequiredArgsConstructor;
import me.mircea.cc.backend.model.User;
import me.mircea.cc.backend.repository.UserRepository;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> merge(DefaultOAuth2AuthenticatedPrincipal principal) {
        String email = principal.getAttribute("email");
        return userRepository.findByEmail(email)
                .defaultIfEmpty(createNewUser(principal))
                .flatMap(userRepository::save);
    }

    private User createNewUser(DefaultOAuth2AuthenticatedPrincipal principal) {
        return User.builder()
                .email(principal.getAttribute("email"))
                .build();
    }
}
