package jpa.jpabank.api;

import jpa.jpabank.domain.*;
import jpa.jpabank.repository.AccountRepository;
import jpa.jpabank.repository.FriendRepository;
import jpa.jpabank.repository.TransferRepository;
import jpa.jpabank.repository.TransferSearch;
import jpa.jpabank.repository.transfer.query.TransferAccountQueryDto;
import jpa.jpabank.repository.transfer.query.TransferFlatDto;
import jpa.jpabank.repository.transfer.query.TransferQueryDto;
import jpa.jpabank.repository.transfer.query.TransferQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.*;
import static jpa.jpabank.domain.TransferStatus.RECEIVE;
import static jpa.jpabank.domain.TransferStatus.TRANSFER;

@RestController
@RequiredArgsConstructor
public class TransferApiController {

    private final TransferRepository transferRepository;
    private final TransferQueryRepository transferQueryRepository;

    private final FriendRepository friendRepository;
    private final AccountRepository accountRepository;

    @GetMapping("/api/v1/transfers") //v1 : 엔티티 직접 노출
    public List<Transfer> transferV1(){
        List<Transfer> all = transferRepository.findAllByString(new TransferSearch());
        for (Transfer transfer : all) {
            transfer.getFriend().getName(); //Lazy 강제 초기화

            List<TransferAccount> transferAccounts = transfer.getTransferAccounts();
            transferAccounts.stream().forEach(o -> o.getAccount().getName()); //Lazy 강제 초기화
        }
        return all;
    }

    @GetMapping("/api/v2/transfers")//v2 : 엔티티를 DTO로 변환
    public List<TransferDto> transfersV2() {
        List<Transfer> transfers = transferRepository.findAllByString(new TransferSearch());
        List<TransferDto> result = transfers.stream()
                .map(o -> new TransferDto(o))
                .collect(toList());
        return result;
    }

    @GetMapping("/api/v3/transfers")//v3 : 엔티티를 DTO로 변환 - 페치 조인 최적화
    public List<TransferDto> transfersV3() {
        List<Transfer> transfers = transferRepository.findAllWithAccount();
        List<TransferDto> result = transfers.stream()
                .map(o -> new TransferDto(o))
                .collect(toList());
        return result;
    }

    /**
     * V3.1 엔티티를 조회해서 DTO로 변환시, 페이징 고려
     * (1) ToOne 관계 : 우선 모두 페치 조인으로 최적화
     * (2) 컬렉션 관계 : hibernate.default_batch_fetch_size, @BatchSize로 최적화
     */
    @GetMapping("/api/v3.1/transfers")//v3.1 : 엔티티를 DTO로 변환 - 페이징과 한계 돌파
    public List<TransferDto> transfersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {

        List<Transfer> trasfers = transferRepository.findAllWithFriend(offset, limit);
        List<TransferDto> result = trasfers.stream()
                .map(o -> new TransferDto(o))
                .collect(toList());

        return result;
    }

    @GetMapping("/api/v4/transfers")//v4 : JPA에서 DTO로 직접 조회
    public List<TransferQueryDto> transfersV4() {
        return transferQueryRepository.findTransferQueryDtos();
    }

    @GetMapping("/api/v5/transfers")//v5 : JPA에서 DTO로 직접 조회 - 컬렉션 조회 최적화
    public List<TransferQueryDto> transfersV5() {
        return transferQueryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/transfers")//v6 : JPA에서 DTO로 직접 조회 - 플랫 데이터 최적화
    public List<TransferQueryDto> transfersV6() {

        List<TransferFlatDto> flats = transferQueryRepository.findAllByDto_flat();

        return flats.stream()
                .collect(groupingBy(o -> new TransferQueryDto(o.getTransferId(), o.getName(), o.getTransferDate(), o.getTransferStatus()),
                        mapping(o -> new TransferAccountQueryDto(o.getTransferId(), o.getAccountName(), o.getTransferMoney()), toList())
                )).entrySet().stream()
                .map(e -> new TransferQueryDto(e.getKey().getTransferId(), e.getKey().getName(), e.getKey().getTransferDate(), e.getKey().getTransferStatus(), e.getValue()))
                .collect(toList());
    }


    @PostMapping("/api/transfer")
    @Transactional
    public Transfer transfer(@RequestParam("friendId") Long friendId,
                           @RequestParam("accountId") Long accountId,
                           @RequestParam("transferMoney") int transferMoney) {
//        transferService.transfer(friendId, accountId, transferMoney);
//        return "redirect:/transfers";

        //조회
        Friend friend = friendRepository.findOne(friendId);
        Account account = accountRepository.findOne(accountId);

        //생성
        TransferAccount transferAccount = TransferAccount.createTransferAccount(account, transferMoney);

        //생성
        Transfer transfer = Transfer.createTransfer(friend, transferAccount);

        //저장
        transferRepository.save(transfer);
        return transfer;
    }

    @PostMapping(value = "api/transfers/{transferId}/receive")
    @Transactional
    public String receiveTransfer(@PathVariable("transferId") Long transferId) {
//        String result = transferService.receive(transferId);
//        model.addAttribute("result", result);
//        return "redirect:/transfers";
        Transfer findTransfer = transferRepository.findOne(transferId);
        TransferStatus findStatus = findTransfer.getTransferStatus();
        if(findStatus.equals(TRANSFER)) {
            findTransfer.setTransferStatus(RECEIVE);
            return "받기완료";
        }
        else if (findStatus.equals(RECEIVE)) {
            return "이미 입급 처리 완료된 요청입니다.";
        }

        return "Todo";
    }

    @Data
    static class TransferDto {

        private Long transferId;
        private String name;
        private LocalDateTime transferDate;
        private TransferStatus transferStatus;
        private List<TransferAccountDto> transferItems;

        public TransferDto(Transfer transfer) {
            transferId = transfer.getId();
            name = transfer.getFriend().getName();
            transferDate = transfer.getTransferDate();
            transferStatus = transfer.getTransferStatus();
            transferItems = transfer.getTransferAccounts().stream()
                    .map(transferAccount -> new TransferAccountDto(transferAccount))
                    .collect(toList());
        }
    }

    @Data
    static class TransferAccountDto {

        private String accountName;
        private int transferMoney;

        public TransferAccountDto(TransferAccount transferAccount) {
            accountName = transferAccount.getAccount().getName();
            transferMoney = transferAccount.getTransferMoney();
        }
    }
}
