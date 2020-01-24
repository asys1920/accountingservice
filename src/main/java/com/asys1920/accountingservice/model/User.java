package com.asys1920.accountingservice.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    String firstName;
    String lastName;
    String userName;
    String emailAddress;
    String expirationDateDriversLicense;
    boolean isActive;
    boolean isBanned;

    String street;
    String zipCode;
    String city;
    String country;

    public String getName(){
        return String.format("%s %s",firstName,lastName);
    }

}
