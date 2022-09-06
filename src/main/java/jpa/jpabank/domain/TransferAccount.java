package jpa.jpabank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@Table(name = "transfer_account")
public class TransferAccount {

    @Id @GeneratedValue
    @Column(name = "transfer_account_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "transfer_id")
    private Transfer transfer;

    private int transferMoney; //이체금액

    /**
     * 생성메서드
     */
    public static TransferAccount createTransferAccount(Account account, int transferMoney){
        TransferAccount transferAccount = new TransferAccount();
        transferAccount.setAccount(account);
        transferAccount.setTransferMoney(transferMoney);

        account.removeStock(transferMoney);//**

        return transferAccount;
    }

    /**
     * 비즈니스 로직
     */
    public void cancel(){
         getAccount().addStock(transferMoney);//**
    }
}
