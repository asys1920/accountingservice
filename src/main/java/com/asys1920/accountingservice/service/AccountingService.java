package com.asys1920.accountingservice.service;

import com.asys1920.accountingservice.model.Bill;
import com.asys1920.accountingservice.repository.AccountingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AccountingService {

    @Autowired
    private AccountingRepository accountingRepository;

    public Bill createBill(Bill bill){
        return accountingRepository.save(bill);
    }

    public List<Bill> getAllBills() {
        return accountingRepository.findAll();
    }

    public Bill getBill(long id) {
        return accountingRepository.findById(id).get();
    }

    @Transactional
    public Bill setBillCanceled(Long id) throws NoSuchElementException {
        Optional<Bill> possibill = accountingRepository.findById(id);
        if(possibill.isPresent()){
            accountingRepository.setBillCanceled(id);
            return accountingRepository.findById(id).get().cancel();
        }else{
            throw new NoSuchElementException("Bill can't be canceled, because it doesn't exist");
        }
    }
}
