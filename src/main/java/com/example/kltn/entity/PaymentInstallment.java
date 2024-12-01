package com.example.kltn.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_installments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInstallment implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
    private double amount;          // Số tiền phải trả
    private double paidAmount;      // Số tiền đã trả
    private String installmentType; // DEPOSIT (đặt cọc) hoặc FINAL (thanh toán cuối)
    private Date dueDate;          // Hạn thanh toán
    private Date paidDate;         // Ngày đã thanh toán
    private String status;         // PENDING, PAID, OVERDUE
}
