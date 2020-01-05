package com.asys1920.accountingservice.adapter;

import com.asys1920.accountingservice.controller.BillDTO;
import com.asys1920.accountingservice.model.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BillMapper {
    BillMapper INSTANCE = Mappers.getMapper(BillMapper.class);

    Bill billDTOtoBill(BillDTO bill);

    BillDTO billToBillDTO(Bill bill);
}
