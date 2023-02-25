package org.sid.accountserviceaxon.commonapi.events;

import lombok.Getter;
import org.sid.accountserviceaxon.commonapi.enums.AccountStatus;

public class AccountDebitedEvent extends BaseEvent<String> {
    @Getter private String currency;
    @Getter private double amount;
    public AccountDebitedEvent(String id, String currency, double amount) {
        super(id);
        this.currency = currency;
        this.amount = amount;
    }
}
