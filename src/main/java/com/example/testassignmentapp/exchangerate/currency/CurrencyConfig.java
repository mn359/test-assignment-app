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
    CommandLineRunner commandLineRunner(CurrencyRepository repository) {
        return args -> {
            Set<String> expectedCurrencyCodes =
                    Arrays.stream(Currency.CurrencyEnum.values()).map(Enum::name).collect(Collectors.toSet());

            repository.saveAll(expectedCurrencyCodes.stream()
                    .map(code -> new Currency(Currency.CurrencyEnum.valueOf(code))).toList());
        };
    }
}

