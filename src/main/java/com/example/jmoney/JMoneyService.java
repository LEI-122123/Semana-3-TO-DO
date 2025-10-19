package com.example.jmoney;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class JMoneyService {

    @Autowired
    private JMoneyRepository repo;

    public List<JMoney> list(PageRequest pageRequest) {
        return repo.findAll(pageRequest).getContent();
    }

    public void createJMoney(String type, String description, Double amountValue, LocalDate date) {
        Money money = Money.of(CurrencyUnit.EUR, BigDecimal.valueOf(amountValue));

        // Expenses are stored as negative amounts
        if ("Expense".equalsIgnoreCase(type)) {
            money = money.negated();
        }

        JMoney jMoney = new JMoney();
        jMoney.setType(type);
        jMoney.setDescription(description);
        jMoney.setMoney(money);
        jMoney.setTransactionDate(date);

        repo.save(jMoney);
    }

    public Money calculateBalance() {
        return repo.findAll().stream()
                .map(JMoney::getMoney)
                .reduce(Money.zero(CurrencyUnit.EUR), Money::plus);
    }
}
