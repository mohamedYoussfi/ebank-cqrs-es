package org.sid.accountserviceaxon.query.services;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.sid.accountserviceaxon.commonapi.enums.TransactionType;
import org.sid.accountserviceaxon.commonapi.events.AccountCreatedEvent;
import org.sid.accountserviceaxon.commonapi.events.AccountCreditedEvent;
import org.sid.accountserviceaxon.commonapi.events.AccountDebitedEvent;
import org.sid.accountserviceaxon.query.dto.AccountWatchEvent;
import org.sid.accountserviceaxon.query.entities.Account;
import org.sid.accountserviceaxon.query.entities.AccountTransaction;
import org.sid.accountserviceaxon.query.queries.GetAccountBalanceStream;
import org.sid.accountserviceaxon.query.queries.GetAccountById;
import org.sid.accountserviceaxon.query.queries.GetAllAccounts;
import org.sid.accountserviceaxon.query.repository.AccountRepository;
import org.sid.accountserviceaxon.query.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class AccountEventHandlerService {
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private QueryUpdateEmitter queryUpdateEmitter;


    public AccountEventHandlerService(AccountRepository accountRepository, TransactionRepository transactionRepository, QueryUpdateEmitter queryUpdateEmitter) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }
    @EventHandler
    public void on(AccountCreatedEvent event, EventMessage<AccountCreatedEvent> eventMessage){
        log.info("**********************");
        log.info("AccountCreatedEvent received");
        Account account=new Account();
        account.setId(event.getId());
        account.setBalance(event.getBalance());
        account.setStatus(event.getStatus());
        account.setCurrency(event.getCurrency());
        account.setCreatedAt(eventMessage.getTimestamp());
        accountRepository.save(account);
    }
    @EventHandler
    public void on(AccountCreditedEvent event, EventMessage<AccountCreatedEvent> eventMessage){
        log.info("**********************");
        log.info("AccountCreditedEvent received");
        Account account=accountRepository.findById(event.getId()).get();
        AccountTransaction accountTransaction=AccountTransaction.builder()
                .account(account)
                .amount(event.getAmount())
                .type(TransactionType.CREDIT)
                .timestamp(eventMessage.getTimestamp())
                .build();
        transactionRepository.save(accountTransaction);
        account.setBalance(account.getBalance()+event.getAmount());
        accountRepository.save(account);
        AccountWatchEvent accountWatchEvent=new AccountWatchEvent(
            accountTransaction.getTimestamp(),account.getId(),account.getBalance(),accountTransaction.getType(),accountTransaction.getAmount()
        );
        queryUpdateEmitter.emit(GetAccountBalanceStream.class,(query)->(query.getAccountId().equals(account.getId())),accountWatchEvent);
    }
    @EventHandler
    public void on(AccountDebitedEvent event, EventMessage<AccountCreatedEvent> eventMessage){
        log.info("**********************");
        log.info("AccountDebitedEvent received");
        Account account=accountRepository.findById(event.getId()).get();
        AccountTransaction accountTransaction=AccountTransaction.builder()
                .account(account)
                .amount(event.getAmount())
                .type(TransactionType.DEBIT)
                .timestamp(eventMessage.getTimestamp())
                .build();
        transactionRepository.save(accountTransaction);
        account.setBalance(account.getBalance()-event.getAmount());
        accountRepository.save(account);
        AccountWatchEvent accountWatchEvent=new AccountWatchEvent(
                accountTransaction.getTimestamp(),account.getId(),account.getBalance(),accountTransaction.getType(),accountTransaction.getAmount()
        );
        queryUpdateEmitter.emit(GetAccountBalanceStream.class,(query)->(query.getAccountId().equals(account.getId())),accountWatchEvent);
    }
    @QueryHandler
    public List<Account> on(GetAllAccounts query){
        return accountRepository.findAll();
    }
    @QueryHandler
    public Account on(GetAccountById query){
        return accountRepository.findById(query.getAccountId()).get();
    }
    @QueryHandler
    public AccountWatchEvent on(GetAccountBalanceStream query){
        Account account = accountRepository.findById(query.getAccountId()).get();
        AccountTransaction accountTransaction=transactionRepository.findTop1ByAccountIdOrderByTimestampDesc(query.getAccountId());
        if(accountTransaction!=null)
        return new AccountWatchEvent(
                accountTransaction.getTimestamp(),
          account.getId(),account.getBalance(),accountTransaction.getType(),accountTransaction.getAmount()
        );
        return null;
    }
}
