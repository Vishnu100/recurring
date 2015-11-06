package com.itranswarp.recurring.account.service;

import com.itranswarp.recurring.account.model.Account;
import com.itranswarp.recurring.base.util.BeanUtil;
import com.itranswarp.recurring.db.Database;
import com.itranswarp.recurring.db.PagedResults;
import org.springframework.beans.BeanUtils;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class AccountService {

    @Inject
    Database database;

    public Account createAccount(Account account) {
        account.setStatus(Account.AccountStatus.ACTIVE);
        database.save(account);

        return account;
    }

    public Account updateAccount(Account newAccount) {
        this.validAccount(newAccount);
        Account account = database.fetch(Account.class, newAccount.getId());
        BeanUtils.copyProperties(newAccount,account, BeanUtil.getNullPropertyNames(newAccount));

        database.update(account);
        return account;
    }

    public Account readAccount(String accountId) {
        Account account = database.fetch(Account.class, accountId);
        return account;
    }

    public PagedResults<Account> readAccounts(Integer pageIndex, Integer itemsPerPage) {
        PagedResults<Account>  accountPageResults = database.from(Account.class).where("status= ?",Account.AccountStatus.ACTIVE).list(pageIndex,itemsPerPage);
        return accountPageResults;
    }

    public Account deleteAccount(String accountId) {
        Account account = database.fetch(Account.class, accountId);
        account.setStatus(Account.AccountStatus.EXPIRED);
        database.update(account);

        return account;
    }

    private void validAccount(Account account) {
        //valid account
    }
}
