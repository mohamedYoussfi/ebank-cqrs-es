package net.youssfi.analyticsservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountAnalytics {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String accountId;
    private String status;
    private double balance;
    private double totalDebit;
    private double totalCredit;
    private int numberDebitOperations;
    private int numberCreditOperations;
}
