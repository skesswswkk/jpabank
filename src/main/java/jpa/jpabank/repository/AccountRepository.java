package jpa.jpabank.repository;

import jpa.jpabank.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final EntityManager em;

    public void save(Account account){
        if(account.getId() == null) em.persist(account);//신규등록
        else em.merge(account);//업데이트
    }

    public Account findOne(Long id){
        return em.find(Account.class, id);
    }

    public List<Account> findAll(){
        return em.createQuery("select a from Account a", Account.class)
                .getResultList();
    }
}
