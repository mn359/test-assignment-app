package com.example.testassignmentapp.exchangerate.currency;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Objects;

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

    public int hashCode() {
        return Objects.hash(id, code, internalCbrCode);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Currency that = (Currency) o;
        return new EqualsBuilder()
                .append(id, that.id)
                .append(code, that.code)
                .append(internalCbrCode, that.internalCbrCode)
                .isEquals();
    }
}
