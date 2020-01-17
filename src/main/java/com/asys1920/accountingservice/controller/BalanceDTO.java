package com.asys1920.accountingservice.controller;

import lombok.Data;

import java.util.Date;

@Data
public class BalanceDTO {
    Long id;
    Double payed;
    Double open;
    Double sum;
    Date start;
    Date end;

}
