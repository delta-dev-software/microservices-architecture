package com.example.userservice.controller;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.dto.UserRegistrationRequest;
import com.example.userservice.service.JwtTokenService;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private  UserService userService;

    @Autowired
    JwtTokenService jwtTokenService;

    @GetMapping("/.well-known/jwks.json")
    public ResponseEntity<String> getJWKS() throws Exception {
        ClassPathResource resource = new ClassPathResource(".well-known/jwks.json");
        String jwksJson = new String(Files.readAllBytes(Paths.get(resource.getURI())));
        return ResponseEntity.ok(jwksJson);
    }



    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        UserDTO user = userService.registerUser(request.getName(), request.getEmail(), request.getPassword());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") String id) {
        try {
            UUID userId = UUID.fromString(id);
            return userService.findById(userId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        // Perform authentication (e.g., validate username and password)
        // If valid, generate and return a JWT
        System.out.println(jwtTokenService.generateToken(loginRequest.getUsername()));
        return jwtTokenService.generateToken(loginRequest.getUsername());
    }
}
