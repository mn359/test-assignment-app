package com.example.testassignmentapp.exchangerate.currency;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class CurrencyConfig {
    @Bean
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

