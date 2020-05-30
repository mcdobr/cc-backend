package me.mircea.cc.backend.controller;

import lombok.RequiredArgsConstructor;
import me.mircea.cc.backend.model.User;
import me.mircea.cc.backend.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/hello")
    public String hello() {
        return "Hello there!";
    }

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<User> findAll() {
        return userService.findAll().delayElements(Duration.ofSeconds(5));
    }
}