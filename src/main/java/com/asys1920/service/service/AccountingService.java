package com.asys1920.service.service;

import com.asys1920.model.Balance;
import com.asys1920.model.Bill;
import com.asys1920.model.User;
import com.asys1920.service.adapter.UserServiceAdapter;
import com.asys1920.service.exceptions.ServiceUnavailableException;
import com.asys1920.service.repository.AccountingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class AccountingService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountingService.class);
    private final AccountingRepository accountingRepository;
    private final UserServiceAdapter userServiceAdapter;

    public AccountingService(AccountingRepository accountingRepository, UserServiceAdapter userServiceAdapter) {
        this.accountingRepository = accountingRepository;
        this.userServiceAdapter = userServiceAdapter;
    }


    private boolean userExists(long userId) throws ServiceUnavailableException {
        User user = userServiceAdapter.getUser(userId);
        return user != null;
    }

    public Bill createBill(Bill bill) throws ServiceUnavailableException {
        LOG.trace("SERVICE createBill() initiated");
        return saveBill(bill);
    }

    public List<Bill> getAllBills() {
        LOG.trace("SERVICE getAllBills() initated");
        return accountingRepository.findAll();
    }

    public Bill getBill(long id) {
        LOG.trace(String.format("SERVICE getBill(%d) initiated", id));
        Optional<Bill> optionalBill = accountingRepository.findById(id);
        if (optionalBill.isPresent()) {
            LOG.trace("SERVICE getBalance() completed");
            return optionalBill.get();
        } else {
            throw new NoSuchElementException("No bill with id: " + id + " is known.");
        }
    }

    @Transactional
    public Balance getBalance(Instant start, Instant end) {
        LOG.trace("SERVICE getBalance() initiated");
        List<Bill> bills = accountingRepository.findAllByCreationDateBetween(start, end);
        LOG.trace("SERVICE getBalance() completed");
        return calculateBalance(bills, start, end);
    }

    private Balance calculateBalance(List<Bill> bills, Instant start, Instant end) {
        LOG.trace("SERVICE calculateBalance() initiated");
        Balance balance = new Balance();
        balance.setStart(start);
        balance.setEnd(end);
        Stream<Bill> payedBills = bills.stream().filter(Bill::isPaid);
        Stream<Bill> openBills = bills.stream().filter(b -> !b.isPaid());
        balance.setPaid(payedBills.mapToDouble(Bill::getValue).sum());
        balance.setOpen(openBills.mapToDouble(Bill::getValue).sum());
        balance.setSum(balance.getPaid() + balance.getOpen());
        LOG.trace("SERVICE calculateBalance() completed");
        return balance;
    }

    @Transactional
    public Bill updateBill(Bill bill) throws ServiceUnavailableException {
        LOG.trace("SERVICE updateBill() initiated");
        return saveBill(bill);
    }

    private Bill saveBill(Bill bill) throws ServiceUnavailableException {
        LOG.trace("SERVICE saveBill() initiated");
        if (userExists(bill.getUserId())) {
            return accountingRepository.save(bill);
        } else {
            throw new NoSuchElementException("There is no user with id: " + bill.getUserId() + " known.");
        }
    }
}
