package io.microsamples.rx.chachkieclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
@Slf4j
public class ChachkieClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChachkieClientApplication.class, args);
    }

    @Bean
    WebClient webClient(WebClient.Builder builder
			, @Value("${chachkies.server.url:http://localhost:8080}") String  chachkiesServerUrl) {
        log.info("Server requests will be forwarded to {}", chachkiesServerUrl);
        return builder.baseUrl(chachkiesServerUrl).build();
    }

    @Bean
    Function<String, Flux<Chachkie>> chachkies(WebClient http) {
        return value ->  http.get()
                .uri("/chachkies")
                .retrieve()
                .bodyToFlux(Chachkie.class);
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Chachkie {
    private UUID id;
    private double latitude;
    private double longitude;
    private Instant when;
}