package com.asys1920.accountingservice.adapter;

import com.asys1920.accountingservice.controller.BalanceDTO;
import com.asys1920.accountingservice.model.Balance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BalanceMapper {
    BalanceMapper INSTANCE = Mappers.getMapper(BalanceMapper.class);

    Balance balanceDTOToBalance(BalanceDTO balanceDTO);

    BalanceDTO balanceToBalanceDTO(Balance balance);
}