package com.transaction.transac.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "account_details")
public class TransactionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "accountBalance")
    private Double accountBalance;

    @Column(name = "accountNumber")
    private Long accountNumber;

    @Column(name = "userId")
    private String userId;

}
