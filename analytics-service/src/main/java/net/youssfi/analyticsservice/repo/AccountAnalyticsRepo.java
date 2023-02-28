package net.youssfi.analyticsservice.repo;

import net.youssfi.analyticsservice.entities.AccountAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountAnalyticsRepo extends JpaRepository<AccountAnalytics,Long> {
    AccountAnalytics findByAccountId(String accountId);
}
