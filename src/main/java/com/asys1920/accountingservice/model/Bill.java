package com.asys1920.accountingservice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Bill{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    Double value;
    Long userId;
    Long referenceBill;
    Date creationDate;
    Date paymentDeadlineDate;
    Boolean isPayed;
    Boolean isCanceled;

    public Bill cancel(){
        this.setIsCanceled(true);
        return this;
    }
}
