package jpa.jpabank.repository.transfer.simplequery;

import jpa.jpabank.domain.TransferStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransferSimpleQueryDto {
    private Long transferId;
    private String name;
    private LocalDateTime transferDate;
    private TransferStatus transferStatus;

    public TransferSimpleQueryDto(Long transferId, String name, LocalDateTime transferDate, TransferStatus transferStatus) {
        this.transferId = transferId;
        this.name = name;
        this.transferDate = transferDate;
        this.transferStatus = transferStatus;
    }
}