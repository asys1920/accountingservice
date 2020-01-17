package com.asys1920.accountingservice.model;

import lombok.Data;

import javax.persistence.*;
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
    @Temporal(TemporalType.DATE)
    Date creationDate;
    @Temporal(TemporalType.DATE)
    Date paymentDeadlineDate;
    Boolean isPayed;
    Boolean isCanceled;

    public Bill cancel(){
        this.setIsCanceled(true);
        return this;
    }
}
