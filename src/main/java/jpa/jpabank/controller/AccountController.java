package jpa.jpabank.controller;

import jpa.jpabank.domain.Account;
import jpa.jpabank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/accounts/new")
    public String createForm(Model model){
        model.addAttribute("form", new AccountForm());
        return "accounts/createAccountForm";
    }

    @PostMapping("/accounts/new")
    public String create(AccountForm form){

        Account account = new Account();
        account.setName(form.getName());
        account.setStockDeposit(form.getStockDeposit());

        accountService.saveAccount(account);
        return "redirect:/";
    }

    @GetMapping("/accounts")
    public String list(Model model){
        List<Account> accounts = accountService.findAccounts();
        model.addAttribute("accounts", accounts);
        return "accounts/accountList";
    }

     @GetMapping("accounts/{countId}/edit")
    public String updateAccountForm(@PathVariable("countId") Long accountId, Model model){
         Account account = (Account) accountService.findOne(accountId);

         AccountForm form = new AccountForm();
         form.setId(account.getId());
         form.setStockDeposit(account.getStockDeposit());
         form.setName(account.getName());

         model.addAttribute("form", form);
         return "accounts/updateAccountForm";
     }

    @PostMapping("accounts/{accountId}/edit")
    public String updateAccount(@PathVariable Long accountId, @ModelAttribute("form") AccountForm form){

        accountService.updateAccount(accountId, form.getName(), form.getStockDeposit());

        return "redirect:/accounts";
    }
}
