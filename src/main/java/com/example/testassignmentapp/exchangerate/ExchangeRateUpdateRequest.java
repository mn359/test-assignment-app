package com.example.testassignmentapp.exchangerate;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

record ExchangeRateUpdateRequest(
        @NotNull(message = "Please provide a date")
        @DateTimeFormat(pattern ="yyyy-MM-dd")
        LocalDate date
) { }
