package com.example.testassignmentapp.currency;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
public class CurrencyConfig {
    @Bean
    @Profile("!test")
    CommandLineRunner commandLineRunner(CurrencyService service) {
        return args -> {
            //         Set<String> expectedCurrencyCodes = Set.of("EUR", "USD");
//            repository.saveAll(expectedCurrencyCodes.stream()
//                    .map(code -> new Currency(code))
//                    .toList());

            service.getCurrenciesFromCbrAndSave();
        };
    }
}

