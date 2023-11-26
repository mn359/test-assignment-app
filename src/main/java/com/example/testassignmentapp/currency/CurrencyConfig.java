package com.example.testassignmentapp.currency;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.stream.Stream;

@Configuration
public class CurrencyConfig {
    @Bean
    @Profile("!test")
    CommandLineRunner commandLineRunner(CurrencyRepository repository) {
        return args -> {
            repository.saveAll(Stream.of("EUR", "USD")
                    .map(code -> new Currency(code, code))
                    .toList());

            //service.getCurrenciesFromCbrAndSave();
        };
    }
}

