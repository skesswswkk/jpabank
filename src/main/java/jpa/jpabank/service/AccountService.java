package jpa.jpabank.service;

import jpa.jpabank.domain.Account;
import jpa.jpabank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional //DB커넥션을 가져오는 시점
    public void saveAccount(Account account){
        accountRepository.save(account);
    }

    @Transactional
    public Account updateAccount(Long accountId, String name, int stockDeposit){
        Account findAccount = accountRepository.findOne(accountId);
        findAccount.setName(name);
        findAccount.setStockDeposit(stockDeposit);
        return findAccount;
    }

    public Account findOne(Long id){
        return accountRepository.findOne(id);
    }

    public List<Account> findAccounts(){
        return accountRepository.findAll();
    }

}
