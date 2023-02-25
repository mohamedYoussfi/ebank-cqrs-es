package org.sid.accountserviceaxon.commonapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class CreditAccountRequestDTO {
    private String accountId;
    private String currency;
    private double amount;
}
