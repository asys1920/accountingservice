package com.asys1920.service.controller;

import com.asys1920.dto.BalanceDTO;
import com.asys1920.dto.BillDTO;
import com.asys1920.mapper.BalanceMapper;
import com.asys1920.mapper.BillMapper;
import com.asys1920.model.Balance;
import com.asys1920.model.Bill;
import com.asys1920.service.exceptions.ValidationException;
import com.asys1920.service.service.AccountingService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Api(produces = "application/json")
@RestController
public class AccountingController {

    private static final String BILLS_PATH = "/bills";
    private static final String BALANCE_PATH = "/balance";
    final    AccountingService accountingService;

    public AccountingController(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    @ApiOperation(value = "Create a new bill", response = BillDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created bill"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
    @PostMapping(BILLS_PATH)
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

    @ApiOperation(value = "Get all existing bills", response = BillDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched bills"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")
    })
    @GetMapping(BILLS_PATH)
    public ResponseEntity<List<BillDTO>> getAllBill() {
        List<Bill> allBills = accountingService.getAllBills();
        List<BillDTO> allBillDTOs = allBills.stream()
                .map(BillMapper.INSTANCE::billToBillDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(allBillDTOs);
    }

    @ApiOperation(value = "Get a existing bill", response = BillDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched bill"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
    @GetMapping(BILLS_PATH+"/{id}")
    public ResponseEntity<BillDTO> getBill(@PathVariable Long id) {
        Bill bill = accountingService.getBill(id);
        return new ResponseEntity<>(
                BillMapper.INSTANCE.billToBillDTO(bill),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Get the balance for a specific timeframe", response = BillDTO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "a timestamp when the balance should start", dataType = "Instant"),
            @ApiImplicitParam(name = "end", value = "a timestamp when the balance should end", dataType = "Instant")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched balance"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")})
    @GetMapping(BALANCE_PATH)
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

    @ApiOperation(value = "Updates a specific Bill", response = BillDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated bill"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
    @PatchMapping(BILLS_PATH+"/{id}")
    public ResponseEntity<BillDTO> updateBill(@RequestBody BillDTO billDTO) {
        Bill bill = accountingService.updateBill(BillMapper.INSTANCE.billDTOtoBill(billDTO));
        return new ResponseEntity<>(
                BillMapper.INSTANCE.billToBillDTO(bill),
                HttpStatus.OK);
    }

}
