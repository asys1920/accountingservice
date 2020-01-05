package com.asys1920.accountingservice.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Date;

@Data
public class BillDTO {
    Long id;
    @NotNull(message = "Bill must have a value")
    Double value;
    @NotNull(message = "Bill must belong to a user")
    Long userId;
    @NotNull(message = "Bill must have a creationDate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Date creationDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Date paymentDeadlineDate;
    Boolean isPayed = false;
    Boolean isCanceled = false;
}
