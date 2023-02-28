package net.youssfi.analyticsservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.youssfi.analyticsservice.entities.AccountAnalytics;
import net.youssfi.analyticsservice.queries.GetAccountAnalyticsByAccountId;
import net.youssfi.analyticsservice.queries.GetAllAccountAnalytics;
import net.youssfi.analyticsservice.repo.AccountAnalyticsRepo;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.sid.accountserviceaxon.commonapi.events.AccountCreatedEvent;
import org.sid.accountserviceaxon.commonapi.events.AccountCreditedEvent;
import org.sid.accountserviceaxon.commonapi.events.AccountDebitedEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AccountAnalyticsEventHandler {
    private AccountAnalyticsRepo accountAnalyticsRepo;
    private QueryUpdateEmitter queryUpdateEmitter;

    @EventHandler
    public void on(AccountCreatedEvent event){
        log.info("AccountCreatedEvent received");
        AccountAnalytics accountAnalytics=AccountAnalytics.builder()
                .accountId(event.getId())
                .status(event.getStatus().toString())
                .balance(event.getBalance())
                .totalDebit(0)
                .totalCredit(0)
                .numberDebitOperations(0)
                .numberCreditOperations(0)
                .build();
        accountAnalyticsRepo.save(accountAnalytics);
    }
    @EventHandler
    public void on(AccountCreditedEvent event){
        log.info("AccountCreatedEvent received");
        AccountAnalytics accountAnalytics=accountAnalyticsRepo.findByAccountId(event.getId());
        accountAnalytics.setTotalCredit(accountAnalytics.getTotalCredit()+event.getAmount());
        accountAnalytics.setNumberCreditOperations(accountAnalytics.getNumberCreditOperations()+1);
        accountAnalytics.setBalance(accountAnalytics.getBalance()+event.getAmount());
        accountAnalyticsRepo.save(accountAnalytics);
        queryUpdateEmitter.emit(GetAccountAnalyticsByAccountId.class,(query)->query.getAccountId().equals(event.getId()),accountAnalytics);
    }
    @EventHandler
    public void on(AccountDebitedEvent event){
        log.info("AccountDebitedEvent received");
        AccountAnalytics accountAnalytics=accountAnalyticsRepo.findByAccountId(event.getId());
        accountAnalytics.setTotalDebit(accountAnalytics.getTotalDebit()+event.getAmount());
        accountAnalytics.setNumberDebitOperations(accountAnalytics.getNumberDebitOperations()+1);
        accountAnalytics.setBalance(accountAnalytics.getBalance()-event.getAmount());
        accountAnalyticsRepo.save(accountAnalytics);
        queryUpdateEmitter.emit(GetAccountAnalyticsByAccountId.class,(query)->query.getAccountId().equals(event.getId()),accountAnalytics);
    }
    @QueryHandler
    public List<AccountAnalytics> on(GetAllAccountAnalytics query){
        return accountAnalyticsRepo.findAll();
    }
    @QueryHandler
    public AccountAnalytics on(GetAccountAnalyticsByAccountId query){
        return accountAnalyticsRepo.findByAccountId(query.getAccountId());
    }
}
