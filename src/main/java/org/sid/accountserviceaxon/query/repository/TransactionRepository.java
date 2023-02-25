package org.sid.accountserviceaxon.query.repository;

import org.sid.accountserviceaxon.query.entities.Account;
import org.sid.accountserviceaxon.query.entities.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<AccountTransaction,Long> {
}
