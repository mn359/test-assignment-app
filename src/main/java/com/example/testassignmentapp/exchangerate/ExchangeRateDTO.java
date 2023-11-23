package com.example.testassignmentapp.exchangerate;

import java.time.LocalDateTime;

public record ExchangeRateDTO(
        String currency,
        String rate,
        LocalDateTime dateTime
) { }
