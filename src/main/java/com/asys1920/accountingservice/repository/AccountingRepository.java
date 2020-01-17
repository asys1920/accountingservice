package com.asys1920.accountingservice.repository;


import com.asys1920.accountingservice.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface AccountingRepository extends JpaRepository<Bill, Long> {

    @Modifying
    @Query("update Bill b set b.isCanceled = true where b.id = :id")
    void setBillCanceled(@Param("id") Long id);

    @Query("select b from Bill b where b.creationDate <= :creationDateStart and b.creationDate >= :creationDateEnd")
    List<Bill> findAllWithCreationDate(
            @Param("creationDateStart") Date creationDateStart,
            @Param("creationDateStart") Date creationDateEnd);
}
