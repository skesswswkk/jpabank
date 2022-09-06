package jpa.jpabank.repository.transfer.query;
import jpa.jpabank.domain.TransferStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.List;

//v4, v5, v6
@Data
@EqualsAndHashCode(of = "transferId")
public class TransferQueryDto {

    private Long transferId;
    private String name;
    private LocalDateTime transferDate;
    private TransferStatus transferStatus;
    private List<TransferAccountQueryDto> transferAccounts;

    public TransferQueryDto(Long transferId, String name, LocalDateTime transferDate, TransferStatus transferStatus) {
        this.transferId = transferId;
        this.name = name;
        this.transferDate = transferDate;
        this.transferStatus = transferStatus;
    }

    public TransferQueryDto(Long transferId, String name, LocalDateTime transferDate, TransferStatus transferStatus, List<TransferAccountQueryDto> transferAccounts) {
        this.transferId = transferId;
        this.name = name;
        this.transferDate = transferDate;
        this.transferStatus = transferStatus;
        this.transferAccounts = transferAccounts;
    }
}
