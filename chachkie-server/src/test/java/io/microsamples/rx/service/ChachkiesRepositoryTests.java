package io.microsamples.rx.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ChachkiesRepositoryTests {

    private ChachkieRepository chachkieRepository;

    @BeforeEach
    void setUp(){
        chachkieRepository = new ChachkieRepository();
    }


    @Test
    void shouldGetSomeChachkies(){
        final Flux<Chachkie> chachkies = chachkieRepository.findAll();

        StepVerifier.create(chachkies)
                .expectNextMatches(ch -> ch.getLatitude() > 0)
                .expectNextCount(12)
                .expectComplete()
                .verify();
    }

}
