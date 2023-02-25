package org.sid.accountserviceaxon.query.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.accountserviceaxon.commonapi.enums.TransactionType;

import javax.persistence.*;
import java.util.Date;
@Entity
@Data @NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date timestamp;
    private double amount;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @ManyToOne
    private Account account;
}
