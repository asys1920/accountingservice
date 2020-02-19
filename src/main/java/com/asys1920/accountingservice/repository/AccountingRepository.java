package com.asys1920.accountingservice.repository;


import com.asys1920.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;


public interface AccountingRepository extends JpaRepository<Bill, Long> {
    List<Bill> findAllByAndCreationDateIsBetween(Date start, Date end);
}
