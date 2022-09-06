package jpa.jpabank.repository.transfer.query;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

//v5, v6
@Data
public class TransferAccountQueryDto {

     @JsonIgnore
     private Long transferId;
     private String accountName; //출금은행
     private int transferMoney; //출금금액(이체금액)

    public TransferAccountQueryDto(Long transferId, String accountName, int transferMoney) {
        this.transferId = transferId;
        this.accountName = accountName;
        this.transferMoney = transferMoney;
    }
}