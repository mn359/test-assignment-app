package com.example.testassignmentapp.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TransactionReportRequest(
        @NotNull(message = "Please provide a from dateTime")
        @DateTimeFormat(pattern ="yyyy-MM-dd")
        LocalDate from,

        @NotNull(message = "Please provide a to dateTime")
        @DateTimeFormat(pattern ="yyyy-MM-dd")
        LocalDate to,

        @NotBlank(message = "Please provide a currency code")
        String currency) {
}
