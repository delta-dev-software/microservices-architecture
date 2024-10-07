package clientservice.application.port.in;



import clientservice.domain.model.User;

import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<User> createUser(String name, String email);
    CompletableFuture<User> getUser(Long id);
}

