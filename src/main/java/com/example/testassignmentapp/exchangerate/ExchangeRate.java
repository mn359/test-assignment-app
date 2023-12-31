package com.example.testassignmentapp.exchangerate;

import com.example.testassignmentapp.currency.Currency;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class ExchangeRate {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade= CascadeType.MERGE )
    @JoinColumn(name="currency_id", nullable=false)
    private Currency currency;

    private LocalDateTime datetime;

    @Column(precision = 50, scale = 10)
    private BigDecimal rate;

    public ExchangeRate(){

    }

    public ExchangeRate(Currency currency, LocalDateTime datetime, BigDecimal rate) {
        this(null, currency,  datetime, rate);
    }

    public ExchangeRate(Long id, Currency currency, LocalDateTime datetime, BigDecimal rate) {
        this.id = id;
        this.currency = currency;
        this.datetime = datetime;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public int hashCode() {
        return Objects.hash(id, currency, datetime, rate);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExchangeRate that = (ExchangeRate) o;
        return new EqualsBuilder()
                .append(id, that.id)
                .append(currency, that.currency)
                .append(datetime, that.datetime)
                .append(rate, that.rate)
                .isEquals();
    }
}
