package com.fetch.receiptprocessor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
//    @Column(name = "receiptitem_id", columnDefinition = "BINARY(16)")
//    UUID id = UUID.randomUUID();
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @Column(columnDefinition="UUID")
//    private UUID id;

    private String retailer;

    private String purchaseDate;

    private String purchaseTime;

    private double total;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "receipt_id")
    List<ReceiptItem> items;

    private int points = 0;
}
