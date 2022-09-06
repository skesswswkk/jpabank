package jpa.jpabank.repository.transfer.query;

import jpa.jpabank.domain.TransferStatus;
import lombok.Data;

import java.time.LocalDateTime;
//v6
@Data
public class TransferFlatDto {
    private Long transferId;
    private String name;
    private LocalDateTime transferDate;
    private TransferStatus transferStatus;
    private String accountName;
    private int transferMoney;

    public TransferFlatDto(Long transferId, String name, LocalDateTime transferDate, TransferStatus transferStatus, String accountName, int transferMoney) {
        this.transferId = transferId;
        this.name = name;
        this.transferDate = transferDate;
        this.transferStatus = transferStatus;
        this.accountName = accountName;
        this.transferMoney = transferMoney;
    }
}
