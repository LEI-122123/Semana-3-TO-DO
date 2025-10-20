package com.example.jmoney;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JMoneyRepository extends JpaRepository<JMoney, Long> {
    // If you don't need a total row count, Slice is better than Page as it only performs a select query.
    // Page performs both a select and a count query.
    Slice<JMoney> findAllBy( Pageable pageable);
}
