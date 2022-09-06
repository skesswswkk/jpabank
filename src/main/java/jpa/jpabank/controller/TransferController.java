package jpa.jpabank.controller;

import jpa.jpabank.domain.Friend;
import jpa.jpabank.domain.Transfer;
import jpa.jpabank.domain.Account;
import jpa.jpabank.repository.TransferSearch;
import jpa.jpabank.service.AccountService;
import jpa.jpabank.service.FriendService;
import jpa.jpabank.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@EnableScheduling
public class TransferController {

    private final TransferService transferService;
    private final FriendService friendService;
    private final AccountService accountService;

    @GetMapping(value = "/transfer")
    public String createForm(Model model) {
        List<Friend> friends = friendService.findFriends();
        List<Account> accounts = accountService.findAccounts();
        model.addAttribute("friends", friends);
        model.addAttribute("accounts", accounts);
        return "transfer/transferForm";
    }

    @PostMapping(value = "/transfer")
    public String transfer(@RequestParam("friendId") Long friendId,
                        @RequestParam("accountId") Long accountId,
                        @RequestParam("transferMoney") int transferMoney) {
        transferService.transfer(friendId, accountId, transferMoney);
        return "redirect:/transfers";
    }

//    @Scheduled(cron = "0/1 * * * * *")
//    public void autoCancel(){
//        List<Transfer> all = transferService.findAll();
//        for(Transfer transfer : all){
//            LocalDateTime now =  LocalDateTime.now();
//
//            LocalDateTime localDateTime = transfer.getTransferDate().plusSeconds(5);//23
//            LocalDateTime cancelTime = transfer.getTransferDate().plusSeconds(10);//24
//
//            if(now.isAfter(localDateTime) && now.isBefore(cancelTime) && transfer.getStatus().equals(TRANSFER)) {
//                //카카오톤 메시지발송 API 사용
//                System.out.println(transfer.getId() + "간편이체를 받아주세요:D");
////                transferService.update(transfer.getId(), ALARM);
//            }
//
//            if(now.isAfter(cancelTime) && transfer.getStatus().equals(TRANSFER))  {
//                System.out.println(transfer.getId() + "간편이체가 자동 취소되었습니다:D");
//                transferService.cancelTransfer(transfer.getId());
//                transferService.update(transfer.getId(), CANCEL);
//            }
//        }
//    }

    @GetMapping(value = "/transfers")
    public String transferList(@ModelAttribute("transferSearch") TransferSearch transferSearch, Model model) {
        List<Transfer> transfers = transferService.findTransfers(transferSearch);
        model.addAttribute("transfers", transfers);

        return "transfer/transferList";
    }

    @PostMapping(value = "/transfers/{transferId}/receive")
    public String receiveTransfer(@PathVariable("transferId") Long transferId, Model model) {
        String result = transferService.receive(transferId);
        model.addAttribute("result", result);
        return "redirect:/transfers";
    }
}
