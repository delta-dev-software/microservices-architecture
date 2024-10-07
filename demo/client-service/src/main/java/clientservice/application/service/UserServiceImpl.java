package clientservice.application.service;



import clientservice.application.port.in.UserService;
import clientservice.application.port.out.UserRepository;
import clientservice.domain.model.User;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    //@Autowired
    private RestTemplate restTemplate=new RestTemplate();
   // @Autowired
    private  WebClient webClient=WebClient.builder().baseUrl("http://localhost:8080/product-service").build();;

    public String getProductWelcomeMessage() {
        String url = "http://localhost:8080/product-service/api/products/welcome";
        return restTemplate.getForObject(url, String.class);
    }



    public Mono<String> getProductWelcomeMessageReactive() {
        return webClient.get()
                .uri("/api/products/welcome")
                .retrieve()
                .bodyToMono(String.class);
    }

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        welcome();
        System.out.println(getProductWelcomeMessageReactive().block());
        System.out.println(getProductWelcomeMessage());
    }

    @Override
    public CompletableFuture<User> createUser(String name, String email) {
        User user = new User(name, email);
        return CompletableFuture.supplyAsync(() -> userRepository.save(user));
    }

    @Override
    public CompletableFuture<User> getUser(Long id) {
        return CompletableFuture.supplyAsync(() -> userRepository.findById(id).orElse(null));
    }

    private final Tracer tracer = GlobalOpenTelemetry.getTracer("my-service");

    public void welcome() {
        Span span = tracer.spanBuilder("myMethod").startSpan();
        try {
            System.out.println("hello world");
        } finally {
            span.end();  // Always end the span
        }
    }
}
