package com.itranswarp.recurring.account.rest;

import com.itranswarp.recurring.account.model.Account;
import com.itranswarp.recurring.account.service.AccountService;
import com.itranswarp.recurring.db.Database;
import com.itranswarp.recurring.db.PagedResults;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
public class AccountController {

    final Log log = LogFactory.getLog(getClass());

    @Inject
    AccountService accountService;

    public AccountController() {
        log.info("Init Account Controller...");
    }

    @RequestMapping(value="/api/account/account", method=RequestMethod.POST)
    public Account create(@RequestBody Account account) {
        account = accountService.createAccount(account);
        return account;
    }

    @RequestMapping(value="/api/account/account/{id}", method=RequestMethod.GET)
    public Account read(@PathVariable(value = "id") String id) {
        Account account = accountService.readAccount(id);
        return account;
    }

    @RequestMapping(value="/api/account/account/{id}", method=RequestMethod.PUT)
    public Account update(@PathVariable(value = "id") String id,@RequestBody Account account) {
        account.setId(id);
        account.setStatus(null);
        account = accountService.updateAccount(account);
        return account;
    }

    @RequestMapping(value="/api/account/account/{id}", method=RequestMethod.DELETE)
    public Account delete(@PathVariable(value = "id") String id) {
        Account account = accountService.deleteAccount(id);
        return account;
    }

    @RequestMapping(value="/api/account/accounts", method=RequestMethod.GET)
    public PagedResults<Account> read(@RequestParam Integer pageIndex,@RequestParam Integer itemsPerPage) {
        PagedResults<Account> accountPageResults = accountService.readAccounts(pageIndex,itemsPerPage);
        return accountPageResults;
    }
}
