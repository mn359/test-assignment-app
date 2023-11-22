package com.example.testassignmentapp.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDTO(
    @NotNull(message = "Please provide a date")
    @DateTimeFormat(pattern ="yyyy-MM-dd")
    LocalDate date,

    @NotBlank(message = "Description must not be blank")
    String description,

    @NotNull(message = "Please provide a value")
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    BigDecimal value
) { }
