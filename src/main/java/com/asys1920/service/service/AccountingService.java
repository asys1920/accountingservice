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

    /**
     * Checks if a user exists in the user service for the given userId
     *
     * @param userId userid to get checked
     * @return if the user exists
     * @throws ServiceUnavailableException gets thrown when the user service is not available
     */
    private boolean userExists(long userId) throws ServiceUnavailableException {
        User user = userServiceAdapter.getUser(userId);
        return user != null;
    }

    /**
     * Saves a bill to the repository
     *
     * @param bill the bill that gets saved
     * @return the created bill
     * @throws ServiceUnavailableException gets thrown when the user service is not available
     */
    public Bill createBill(Bill bill) throws ServiceUnavailableException {
        LOG.trace("SERVICE createBill() initiated");
        return saveBill(bill);
    }

    /**
     * Retrieves all bills from the repository
     *
     * @return a list with all bills
     */
    public List<Bill> getAllBills() {
        LOG.trace("SERVICE getAllBills() initated");
        return accountingRepository.findAll();
    }

    /**
     * Retrieves a bill for the given id
     *
     * @param id the id associated with the bill
     * @return the bill with the given id
     * @throws NoSuchElementException gets thrown when there is no bill with the given id
     */
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

    /**
     * Creates a balance over all bills in the submitted timeframe.
     *
     * @param start the start of the timeframe
     * @param end   the end of the timeframe
     * @return the balance over the timeframe
     */
    @Transactional
    public Balance getBalance(Instant start, Instant end) {
        LOG.trace("SERVICE getBalance() initiated");
        List<Bill> bills = accountingRepository.findAllByCreationDateBetween(start, end);
        LOG.trace("SERVICE getBalance() completed");
        Balance balance = calculateBalance(bills);
        balance.setStart(start);
        balance.setEnd(end);
        return balance;
    }

    /**
     * Calculates the balance over the given list of bills
     *
     * @param bills a list with all bills that are used for calculation
     * @return a balance with data from all submitted bills
     */
    private Balance calculateBalance(List<Bill> bills) {
        LOG.trace("SERVICE calculateBalance() initiated");
        Balance balance = new Balance();
        Stream<Bill> payedBills = bills.stream().filter(Bill::isPaid);
        Stream<Bill> openBills = bills.stream().filter(b -> !b.isPaid());
        balance.setPaid(payedBills.mapToDouble(Bill::getValue).sum());
        balance.setOpen(openBills.mapToDouble(Bill::getValue).sum());
        balance.setSum(balance.getPaid() + balance.getOpen());
        LOG.trace("SERVICE calculateBalance() completed");
        return balance;
    }

    /**
     * Updates a bill in the repository
     *
     * @param bill the bill that gets updated
     * @return the updated bill
     */
    @Transactional
    public Bill updateBill(Bill bill) {
        LOG.trace("SERVICE updateBill() initiated");
        Optional<Bill> oldBill = accountingRepository.findById(bill.getId());
        if (oldBill.isPresent()) {
            return accountingRepository.save(bill);
        } else {
            throw new NoSuchElementException(String.format("There is no bill known with the id: %d", bill.getId()));
        }
    }

    /**
     * Saves a bill to the repository. Connection to the user service needs to be available
     * @param bill the bill that gets saved
     * @return the saved bill
     * @throws ServiceUnavailableException gets thrown when the user service is not available
     */
    private Bill saveBill(Bill bill) throws ServiceUnavailableException {
        LOG.trace("SERVICE saveBill() initiated");
        if (userExists(bill.getUserId())) {
            return accountingRepository.save(bill);
        } else {
            throw new NoSuchElementException("There is no user with id: " + bill.getUserId() + " known.");
        }
    }
}
