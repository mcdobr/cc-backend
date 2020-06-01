package me.mircea.cc.backend.controller;

import lombok.RequiredArgsConstructor;
import me.mircea.cc.backend.model.User;
import me.mircea.cc.backend.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import java.time.Duration;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<User> findAll() {
        return userService.findAll().delayElements(Duration.ofSeconds(5));
    }

    @PutMapping(value = "/{user-id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<User> merge(@PathVariable("user-id") @NotBlank String id, @AuthenticationPrincipal DefaultOAuth2AuthenticatedPrincipal principal) {
        return userService.merge(principal);
    }
}
