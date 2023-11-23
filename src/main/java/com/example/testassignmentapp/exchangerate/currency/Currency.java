package com.example.testassignmentapp.exchangerate.currency;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Currency {
    @Id
    @GeneratedValue
    private int id;

    @Column(unique=true)
    private String code;

    @Column(unique=true)
    private String internalCbrCode;

    public Currency(String code, String internalCbrCode) {
        this.code = code;
        this.internalCbrCode = internalCbrCode;
    }

    public Currency() {}

    public String getCode() {
        return code;
    }

    public String getInternalCbrCode() {
        return internalCbrCode;
    }
}
