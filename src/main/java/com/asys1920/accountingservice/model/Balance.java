package com.asys1920.accountingservice.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    Double payed;
    Double open;
    Double sum;
    Date start;
    Date end;


}
