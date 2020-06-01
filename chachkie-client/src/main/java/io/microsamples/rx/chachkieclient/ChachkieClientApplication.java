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

import java.time.Instant;
import java.util.UUID;

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
    RouterFunction<ServerResponse> routes(WebClient http) {
        return route()
                .GET("/", request -> ok().body(
                        http.get()
                                .uri("/chachkies")
                                .retrieve()
                                .bodyToFlux(Chachkie.class)
                        , Chachkie.class))
                .build();
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