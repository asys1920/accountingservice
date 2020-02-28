package com.asys1920.service.controller;

import com.asys1920.dto.BillDTO;
import com.asys1920.mapper.BillMapper;
import com.asys1920.model.Bill;
import com.asys1920.service.exceptions.ServiceUnavailableException;
import com.asys1920.service.exceptions.ValidationException;
import com.asys1920.service.service.AccountingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Api(produces = "application/json")
@RestController
public class BillController {

    private static final Logger LOG = LoggerFactory.getLogger(BillController.class);
    private static final String BILLS_PATH = "/bills";
    final AccountingService accountingService;

    public BillController(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    @ApiOperation(value = "Create a new bill", response = BillDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created bill"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
    @PostMapping(BILLS_PATH)
    public ResponseEntity<BillDTO> createBill(@RequestBody BillDTO billDTO) throws ValidationException, ServiceUnavailableException {
        LOG.trace(String.format("POST %s initiated", BILLS_PATH));
        Set<ConstraintViolation<BillDTO>> validate = Validation.buildDefaultValidatorFactory().getValidator().validate(billDTO);
        if (!validate.isEmpty()) {
            throw new ValidationException(validate);

        }
        Bill bill = accountingService.createBill(BillMapper.INSTANCE.billDTOtoBill(billDTO));
        LOG.trace(String.format("POST %s completed", BILLS_PATH));
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
        LOG.trace(String.format("GET %s initiated", BILLS_PATH));
        List<Bill> allBills = accountingService.getAllBills();
        List<BillDTO> allBillDTOs = allBills.stream()
                .map(BillMapper.INSTANCE::billToBillDTO)
                .collect(Collectors.toList());
        LOG.trace(String.format("GET %s completed", BILLS_PATH));
        return ResponseEntity.ok().body(allBillDTOs);
    }

    @ApiOperation(value = "Get a existing bill", response = BillDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched bill"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
    @GetMapping(BILLS_PATH + "/{id}")
    public ResponseEntity<BillDTO> getBill(@PathVariable Long id) {
        LOG.trace(String.format("GET %s %d initiated", BILLS_PATH, id));
        Bill bill = accountingService.getBill(id);
        LOG.trace(String.format("GET %s %d completed", BILLS_PATH, id));
        return new ResponseEntity<>(
                BillMapper.INSTANCE.billToBillDTO(bill),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Updates a specific Bill", response = BillDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated bill"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
    @PatchMapping(BILLS_PATH + "/{id}")
    public ResponseEntity<BillDTO> updateBill(@RequestBody BillDTO billDTO) throws ServiceUnavailableException {
        LOG.trace(String.format("PATCH %s initiated", BILLS_PATH));
        Bill bill = accountingService.updateBill(BillMapper.INSTANCE.billDTOtoBill(billDTO));
        LOG.trace(String.format("PATCH %s completed", BILLS_PATH));
        return new ResponseEntity<>(
                BillMapper.INSTANCE.billToBillDTO(bill),
                HttpStatus.OK);
    }

}
