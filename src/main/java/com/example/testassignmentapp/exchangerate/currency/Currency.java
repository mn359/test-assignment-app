package com.example.testassignmentapp.exchangerate.currency;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Currency {
    enum CurrencyEnum {
        EUR, USD;
    }

    @Id
    @GeneratedValue
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(unique=true)
    private CurrencyEnum code;

    public Currency(CurrencyEnum code) {
        this.code = code;
    }

    public Currency() {}

    public CurrencyEnum getCode() {
        return code;
    }
}
