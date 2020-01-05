package com.asys1920.accountingservice.controller;

import com.asys1920.accountingservice.adapter.BillMapper;
import com.asys1920.accountingservice.exceptions.ValidationException;
import com.asys1920.accountingservice.model.Bill;
import com.asys1920.accountingservice.service.AccountingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AccountingController {

    @Autowired
    AccountingService accountingService;

    @PostMapping("/bills")
    public ResponseEntity<BillDTO> createBill(@Valid @RequestBody BillDTO billDTO) throws ValidationException {
        Set<ConstraintViolation<BillDTO>> validate = Validation.buildDefaultValidatorFactory().getValidator().validate(billDTO);
        if (!validate.isEmpty()) {
            throw new ValidationException(validate);

        }
        Bill billy = BillMapper.INSTANCE.billDTOtoBill(billDTO);
        Bill bill = accountingService.createBill(billy);
        BillDTO responseDTO = BillMapper.INSTANCE.billToBillDTO(bill);
        return new ResponseEntity<BillDTO>(responseDTO, HttpStatus.CREATED);
    }


    @GetMapping("/bills")
    public ResponseEntity<List<BillDTO>> getAllBill() {
        List<Bill> allBills = accountingService.getAllBills();
        List<BillDTO> allBillDTOs = allBills.stream()
                .map(BillMapper.INSTANCE::billToBillDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(allBillDTOs);
    }

    @GetMapping("/bills/{id}")
    public ResponseEntity<BillDTO> getBill(@PathVariable Long id) {
        Bill bill = accountingService.getBill(id);

        return new ResponseEntity<BillDTO>(
                BillMapper.INSTANCE.billToBillDTO(bill),
                HttpStatus.OK);
    }

    @PatchMapping("/bills/cancel/{id}")
    public ResponseEntity<BillDTO> cancelBill(@PathVariable Long id) throws Exception {
        Bill bill = accountingService.setBillCanceled(id);
        return new ResponseEntity<BillDTO>(
                BillMapper.INSTANCE.billToBillDTO(bill),
                HttpStatus.OK);
    }

}
