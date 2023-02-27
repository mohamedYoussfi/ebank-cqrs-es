package org.sid.accountserviceaxon.query.dto;

import org.sid.accountserviceaxon.commonapi.enums.TransactionType;

import java.time.Instant;

public record AccountWatchEvent(Instant instant, String accountId, double currentBalance, TransactionType type, double transactionAmount) {
}
