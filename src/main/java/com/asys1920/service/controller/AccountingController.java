package com.asys1920.service.controller;

import com.asys1920.dto.BalanceDTO;
import com.asys1920.dto.BillDTO;
import com.asys1920.mapper.BalanceMapper;
import com.asys1920.mapper.BillMapper;
import com.asys1920.model.Balance;
import com.asys1920.model.Bill;
import com.asys1920.service.exceptions.ValidationException;
import com.asys1920.service.service.AccountingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AccountingController {

    final
    AccountingService accountingService;

    public AccountingController(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    @ApiOperation(value = "Create a bill", response = BillDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created bill"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
    @PostMapping("/bills")
    public ResponseEntity<BillDTO> createBill(@RequestBody BillDTO billDTO) throws ValidationException {
        Set<ConstraintViolation<BillDTO>> validate = Validation.buildDefaultValidatorFactory().getValidator().validate(billDTO);
        if (!validate.isEmpty()) {
            throw new ValidationException(validate);

        }
        Bill bill = accountingService.createBill(BillMapper.INSTANCE.billDTOtoBill(billDTO));
        return new ResponseEntity<>(
                BillMapper.INSTANCE.billToBillDTO(bill),
                HttpStatus.CREATED);
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
        return new ResponseEntity<>(
                BillMapper.INSTANCE.billToBillDTO(bill),
                HttpStatus.OK);
    }

    @GetMapping("/balance")
    public ResponseEntity<BalanceDTO> getBalance(@RequestParam(required = false) Instant start,
                                                 @RequestParam(required = false) Instant end)
            throws ValidationException {
        if (start == null)
            start = Instant.ofEpochMilli(0L);
        if (end == null)
            end = Instant.now();
        if (end.isBefore(start)) {
            throw new ValidationException("start has to be before end when requesting a balance");
        }
        Balance balance = accountingService.getBalance(start, end);
        return new ResponseEntity<>(
                BalanceMapper.INSTANCE.balanceToBalanceDTO(balance),
                HttpStatus.OK);
    }

    @PatchMapping("/bills/{id}")
    public ResponseEntity<BillDTO> updateBill(@RequestBody BillDTO billDTO) {
        Bill bill = accountingService.updateBill(BillMapper.INSTANCE.billDTOtoBill(billDTO));
        return new ResponseEntity<>(
                BillMapper.INSTANCE.billToBillDTO(bill),
                HttpStatus.OK);
    }

}