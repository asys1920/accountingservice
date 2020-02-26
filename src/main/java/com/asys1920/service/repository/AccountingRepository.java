package com.asys1920.service.repository;


import com.asys1920.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;


public interface AccountingRepository extends JpaRepository<Bill, Long> {
    //List<Bill> findAllCreationDateIsBetween(Instant start, Instant end);
    List<Bill> findAllByCreationDateBetween(Instant start, Instant end);
}
