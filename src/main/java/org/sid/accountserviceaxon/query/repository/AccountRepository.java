package org.sid.accountserviceaxon.query.repository;

import org.sid.accountserviceaxon.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
}
