package jpa.jpabank.service;

import jpa.jpabank.domain.*;
import jpa.jpabank.domain.Account;
import jpa.jpabank.repository.AccountRepository;
import jpa.jpabank.repository.FriendRepository;
import jpa.jpabank.repository.TransferRepository;
import jpa.jpabank.repository.TransferSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static jpa.jpabank.domain.TransferStatus.TRANSFER;
import static jpa.jpabank.domain.TransferStatus.RECEIVE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransferService {

    private final FriendRepository friendRepository;
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    /**
     * 주문
     */
    @Transactional
    public Long transfer(Long friendId, Long accountId, int transferMoney) {

        //엔티티 조회
        Friend friend = friendRepository.findOne(friendId);
        Account account = accountRepository.findOne(accountId);

        //생성
        TransferAccount transferAccount = TransferAccount.createTransferAccount(account, transferMoney);

        //생성
        Transfer transfer = Transfer.createTransfer(friend, transferAccount);

        //저장
         transferRepository.save(transfer);
         return transfer.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelTransfer(Long transferId) {
        //조회
        Transfer transfer = transferRepository.findOne(transferId);
        //취소
        transfer.cancel();
    }

    /**
     * 주문 검색
     */
    public List<Transfer> findTransfers(TransferSearch transferSearch) {
        return transferRepository.findAllByString(transferSearch);
    }

    public List<Transfer> findAll(){
        return transferRepository.findAll();
    }

    /**
     * 수정
     */
    //setStatus 수정
    @Transactional
    public void update(Long id, TransferStatus transferStatus){
        Transfer findTransfer = transferRepository.findOne(id);
        findTransfer.setTransferStatus(transferStatus);
    }

    /**
     * 받기 버튼 클릭
     */
    @Transactional
    public String receive(Long id){
        Transfer findTransfer = transferRepository.findOne(id);
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
}