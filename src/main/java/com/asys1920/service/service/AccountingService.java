package com.asys1920.service.service;

import com.asys1920.model.Balance;
import com.asys1920.model.Bill;
import com.asys1920.model.User;
import com.asys1920.service.adapter.UserServiceAdapter;
import com.asys1920.service.repository.AccountingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class AccountingService {

    private final AccountingRepository accountingRepository;
    private final UserServiceAdapter userServiceAdapter;

    public AccountingService(AccountingRepository accountingRepository, UserServiceAdapter userServiceAdapter) {
        this.accountingRepository = accountingRepository;
        this.userServiceAdapter = userServiceAdapter;
    }


    public boolean userExists(long userId) {
        User user = userServiceAdapter.getUser(userId);
        return user != null;
    }

    public Bill createBill(Bill bill) {
        return saveBill(bill);
    }

    public List<Bill> getAllBills() {
        return accountingRepository.findAll();
    }

    public Bill getBill(long id) {
        Optional<Bill> optionalBill = accountingRepository.findById(id);
        if (optionalBill.isPresent()) {
            return optionalBill.get();
        } else {
            throw new NoSuchElementException("No bill with id: " + id + " is known.");
        }
    }

    @Transactional
    public Balance getBalance() {
        List<Bill> bills = accountingRepository.findAll();
        return getBalance(bills, null, null);
    }

    @Transactional
    public Balance getBalance(Instant start, Instant end) {
        List<Bill> bills = accountingRepository.findAllByCreationDateBetween(start, end);
        return getBalance(bills, start, end);
    }

    private Balance getBalance(List<Bill> bills, Instant start, Instant end) {
        Balance balance = new Balance();
        balance.setStart(start);
        balance.setEnd(end);
        Stream<Bill> payedBills = bills.stream().filter(Bill::isPaid);
        Stream<Bill> openBills = bills.stream().filter(b -> !b.isPaid());
        balance.setPaid(payedBills.mapToDouble(Bill::getValue).sum());
        balance.setOpen(openBills.mapToDouble(Bill::getValue).sum());
        balance.setSum(balance.getPaid() + balance.getOpen());
        return balance;
    }

    @Transactional
    public Bill updateBill(Bill bill) {
        return saveBill(bill);
    }

    private Bill saveBill(Bill bill) {
        if(userExists(bill.getUserId())) {
            return accountingRepository.save(bill);
        }else{
            throw new NoSuchElementException("There is no user with id: "+ bill.getUserId() +" known.");
        }
    }
}
