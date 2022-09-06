package jpa.jpabank.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "transfers")
@Getter @Setter
public class Transfer {

    @Id @GeneratedValue
    @Column(name = "transfer_id")
    private Long id; //**

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "friend_id") //FK명
    private Friend friend; //읽기 전용 //**

    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL)
    private List<TransferAccount> transferAccounts = new ArrayList<>(); //**

    private LocalDateTime transferDate; //**

    @Enumerated(EnumType.STRING)
    private TransferStatus transferStatus;

    public void setFriend(Friend friend) {
        this.friend = friend;
        friend.getTransfers().add(this);
    }

    public void addTransferItem(TransferAccount transferAccount) {
        transferAccounts.add(transferAccount);
        transferAccount.setTransfer(this);
    }

    public static Transfer createTransfer(Friend friend, TransferAccount... transferItems){
        Transfer transfer = new Transfer();
        transfer.setFriend(friend);
        for(TransferAccount transferItem : transferItems){
            transfer.addTransferItem(transferItem);
        }
        transfer.setTransferDate(LocalDateTime.now());
        transfer.setTransferStatus(TransferStatus.TRANSFER);
        return transfer;
    }

    public void cancel() {

        this.setTransferStatus(TransferStatus.CANCEL);
        for (TransferAccount transferAccount : transferAccounts) {
            transferAccount.cancel();
        }
    }
}
