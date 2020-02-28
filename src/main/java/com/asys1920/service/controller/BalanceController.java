package com.asys1920.service.controller;

import com.asys1920.dto.BalanceDTO;
import com.asys1920.dto.BillDTO;
import com.asys1920.mapper.BalanceMapper;
import com.asys1920.model.Balance;
import com.asys1920.service.exceptions.ValidationException;
import com.asys1920.service.service.AccountingService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;


@Api(produces = "application/json")
@RestController
public class BalanceController {

    private static final Logger LOG = LoggerFactory.getLogger(BalanceController.class);
    private static final String BALANCE_PATH = "/balance";
    final AccountingService accountingService;

    public BalanceController(AccountingService accountingService) {
        this.accountingService = accountingService;
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
        LOG.trace(String.format("GET %s initiated", BALANCE_PATH), start, end);
        if (start == null)
            start = Instant.ofEpochMilli(0L);
        if (end == null)
            end = Instant.now();
        if (end.isBefore(start)) {
            throw new ValidationException("start has to be before end when requesting a balance");
        }
        Balance balance = accountingService.getBalance(start, end);
        LOG.trace(String.format("GET %s completed", BALANCE_PATH));
        return new ResponseEntity<>(
                BalanceMapper.INSTANCE.balanceToBalanceDTO(balance),
                HttpStatus.OK);
    }


}
