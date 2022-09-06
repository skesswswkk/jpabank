package jpa.jpabank.api;

import jpa.jpabank.domain.Transfer;
import jpa.jpabank.domain.TransferStatus;
import jpa.jpabank.repository.TransferRepository;
import jpa.jpabank.repository.TransferSearch;
import jpa.jpabank.repository.transfer.simplequery.TransferSimpleQueryDto;
import jpa.jpabank.repository.transfer.simplequery.TransferSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne관계 최적화
 * Transfer
 * Transfer -> Friend : ManyToOne
 * Transfer -> Delivery : OneToOne
 *
 */
@RestController
@RequiredArgsConstructor
public class TransferSimpleApiController {

    private final TransferRepository transferRepository;
    private final TransferSimpleQueryRepository transferSimpleQueryRepository;

    @GetMapping("/api/v1/simple-transfers")
    public List<Transfer> transfersV1(){ //문제점 : Entity(Transfer) 외부로 노출
        List<Transfer> all = transferRepository.findAllByString(new TransferSearch());
        for(Transfer transfer : all){
            transfer.getFriend().getName(); //Lazy 강제 초기화
        }
        return all;
    }

    @GetMapping("/api/v2/simple-transfers")
    public List<TransferSimpleDto> transfersV2(){ //v1해결책 : 엔티티를 DTO로 반환
        //문제점 : N + 1 (성능 문제)
        //TRANSFER 2개
        List<Transfer> transfers = transferRepository.findAllByString(new TransferSearch());
        List<TransferSimpleDto> result = transfers.stream()//엔티티 -> DTO로 변환하는 과정
                .map(o -> new TransferSimpleDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3/simple-transfers")
    public List<TransferSimpleDto> transfersV3(){ //v2해결책 : 페치 조인으로 성능 최적화
        List<Transfer> transfers = transferRepository.findAllWithFriend();
        List<TransferSimpleDto> result = transfers.stream()//문제점 : Entity -> DTO 변환 과정 필요
                .map(o -> new TransferSimpleDto(o))
                .collect(Collectors.toList());

        return result; //문제점 : select 시, 페치조인 대상 테이블 컬럼 다 조회
    }

    @GetMapping("/api/v4/simple-transfers")
    public List<TransferSimpleQueryDto> transfersV4(){ //v3해결책 : JPA에서 DTO로 바로 조회
        return transferSimpleQueryRepository.findTransferDtos();
    }

    @Data
    static class TransferSimpleDto {
        private Long transferId;
        private String name;
        private LocalDateTime transferDate;
        private TransferStatus transferStatus;

        public TransferSimpleDto(Transfer o) {
            this.transferId = o.getId();
            this.name = o.getFriend().getName();//Lazy 초기화로 Friend table 터치
            this.transferDate = o.getTransferDate();
            this.transferStatus = o.getTransferStatus();
        }
    }
}
