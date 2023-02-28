package org.sid.accountserviceaxon.query.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class GetAccountBalanceStream {
    private String accountId;
}
