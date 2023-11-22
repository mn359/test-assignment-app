package com.example.testassignmentapp.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financial_transaction")
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDate date;

    private String description;

    @Column(name = "transaction_value", precision = 50, scale = 10)
    private BigDecimal value;

    public Transaction(Long id, LocalDate date, String description, BigDecimal value) {
        this.date = date;
        this.description = description;
        this.value = value;
    }

    public Transaction(LocalDate date, String description, BigDecimal value) {
        this(null, date, description, value);
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getValue() {
        return value;
    }
}
