package clientservice.infrastructure.controllers;


import clientservice.application.port.in.UserService;
import clientservice.domain.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public CompletableFuture<User> createUser(@RequestParam String name, @RequestParam String email) {
        return userService.createUser(name, email);
    }

    @GetMapping("/{id}")
    public CompletableFuture<User> getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
}

