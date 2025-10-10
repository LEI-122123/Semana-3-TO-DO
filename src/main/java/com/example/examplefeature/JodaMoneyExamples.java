package com.example.examplefeature;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.RoundingMode;
import java.util.List;

public class JodaMoneyExamples {

    public static void main(String[] args) {
        System.out.println("=== 1. Basic Money Creation ===");
        basicCreation();

        System.out.println("\n=== 2. Arithmetic Operations ===");
        arithmeticOperations();

        System.out.println("\n=== 3. Parsing and Formatting ===");
        parsingAndFormatting();

        System.out.println("\n=== 4. Comparing and Checking Amounts ===");
        comparisonExample();

    }

    // 1️⃣ Basic Money Creation
    private static void basicCreation() {
        Money tenEuros = Money.of(CurrencyUnit.EUR, 10.00);
        System.out.println(tenEuros); // EUR 10.00
    }

    // 2️⃣ Arithmetic Operations
    private static void arithmeticOperations() {
        Money price = Money.of(CurrencyUnit.EUR, 15.50);
        Money tax = Money.of(CurrencyUnit.EUR, 3.10);

        Money total = price.plus(tax);
        Money discount = total.multipliedBy(0.9, RoundingMode.HALF_EVEN);

        System.out.println("Price: " + price);
        System.out.println("Tax: " + tax);
        System.out.println("Total: " + total);
        System.out.println("Discounted (10% off): " + discount);
    }

    // 3️⃣ Parsing and Formatting
    private static void parsingAndFormatting() {
        Money parsed = Money.parse("USD 25.75");
        System.out.println("Parsed: " + parsed); // USD 25.75
    }

    // 4️⃣ Comparing and Checking Amounts
    private static void comparisonExample() {
        Money wallet = Money.of(CurrencyUnit.EUR, 50.00);
        Money price = Money.of(CurrencyUnit.EUR, 30.00);

        if (wallet.isGreaterThan(price)) {
            System.out.println("You can afford it!");
        } else {
            System.out.println("Not enough funds.");
        }
    }


}
