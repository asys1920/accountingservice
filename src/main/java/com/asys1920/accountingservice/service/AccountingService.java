package com.asys1920.accountingservice.service;

import com.asys1920.accountingservice.adapter.UserServiceAdapter;
import com.asys1920.accountingservice.model.Balance;
import com.asys1920.accountingservice.model.Bill;
import com.asys1920.accountingservice.model.User;
import com.asys1920.accountingservice.repository.AccountingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
public class AccountingService {

    private final AccountingRepository accountingRepository;
    private final UserServiceAdapter userServiceAdapter;

    public AccountingService(AccountingRepository accountingRepository, UserServiceAdapter userServiceAdapter) {
        this.accountingRepository = accountingRepository;
        this.userServiceAdapter = userServiceAdapter;
    }

    public Bill createBill(Bill bill) {
        User user = userServiceAdapter.getUser(bill.getUserId());
        bill.setStreet(user.getStreet());
        bill.setZipCode(user.getZipCode());
        bill.setName(user.getName());
        bill.setCity(user.getCity());
        bill.setCountry(user.getCountry());
        return accountingRepository.save(bill);
    }

    public List<Bill> getAllBills() {
        return accountingRepository.findAll();
    }

    public Bill getBill(long id) {
        return accountingRepository.findById(id).get();
    }

    @Transactional
    public Balance getBalance() {
        List<Bill> bills = accountingRepository.findAll();
        return getBalance(bills, null, null);
    }

    @Transactional
    public Balance getBalance(Date start, Date end) {
        List<Bill> bills = accountingRepository.findAllByAndCreationDateIsBetween(start, end);
        return getBalance(bills, start, end);
    }

    private Balance getBalance(List<Bill> bills, Date start, Date end) {
        Balance balance = new Balance();
        balance.setStart(start);
        balance.setEnd(end);
        Stream<Bill> payedBills = bills.stream().filter(Bill::getIsPayed);
        Stream<Bill> openBills = bills.stream().filter(b -> !b.getIsPayed());
        balance.setPayed(payedBills.mapToDouble(Bill::getValue).sum());
        balance.setOpen(openBills.mapToDouble(Bill::getValue).sum());
        balance.setSum(balance.getPayed() + balance.getOpen());
        return balance;
    }

    @Transactional
    public Bill updateBill(Bill bill) {
        return accountingRepository.save(bill);
    }
}
