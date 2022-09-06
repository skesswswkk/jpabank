package jpa.jpabank;
import jpa.jpabank.domain.*;
import jpa.jpabank.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void dbInit1() {

            Friend friend = createFriend("userA");
            em.persist(friend);
            Account account1 = createAccount("A뱅크", 10000);
            em.persist(account1);

            Account account2 = createAccount("B뱅크", 20000);
            em.persist(account2);

            TransferAccount orderAccount1 = TransferAccount.createTransferAccount(account1, 100);
            TransferAccount orderAccount2 = TransferAccount.createTransferAccount(account2, 200);
            Transfer order = Transfer.createTransfer(friend, orderAccount1, orderAccount2);
            em.persist(order);
        }

        public void dbInit2() {

            Friend friend = createFriend("userB"); em.persist(friend);
            Account account1 = createAccount("C뱅크", 30000);
            em.persist(account1);

            Account account2 = createAccount("D뱅크", 40000);
            em.persist(account2);

            TransferAccount orderAccount1 = TransferAccount.createTransferAccount(account1, 300);
            TransferAccount orderAccount2 = TransferAccount.createTransferAccount(account2, 400);
            Transfer order = Transfer.createTransfer(friend, orderAccount1, orderAccount2);
            em.persist(order);
        }

        private Friend createFriend(String name) {
            Friend friend = new Friend();
            friend.setName(name);
            return friend;
        }

        private Account createAccount(String name, int stockDeposit) {
            Account account = new Account();
            account.setName(name);
            account.setStockDeposit(stockDeposit);
            return account;
        }
    }
}