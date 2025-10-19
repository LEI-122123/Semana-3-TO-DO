package com.example.jmoney;

import jakarta.persistence.*;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
public class JMoney {
    public static final int DESCRIPTION_MAX_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // Expense or Income
    private String description;

    // Persisted as plain values, but used as Money in code
    private BigDecimal amount;
    private String currency;

    private LocalDate transactionDate;
    private Instant creationDate = Instant.now();

    protected JMoney() {}

    public JMoney(String type, String description, Money money, LocalDate date) {
        this.type = type;
        this.description = description;
        this.setMoney(money);
        this.transactionDate = date;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }

    public void setMoney(Money money) {
        this.amount = money.getAmount();
        this.currency = money.getCurrencyUnit().getCode();
    }

    public Money getMoney() {
        if (amount == null) return Money.zero( CurrencyUnit.EUR);
        return Money.of(CurrencyUnit.of(currency), amount);
    }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public Instant getCreationDate() { return creationDate; }
}
