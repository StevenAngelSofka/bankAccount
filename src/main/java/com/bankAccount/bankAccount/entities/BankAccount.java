package com.bankAccount.bankAccount.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bankAccount")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idAccount;

    @Column(nullable = false, unique = true)
    private String numberAccount;

    @Column(nullable = false)
    private double balance;

    @Column
    private String type;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

}
