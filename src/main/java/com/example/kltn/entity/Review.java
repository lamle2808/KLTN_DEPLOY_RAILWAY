package com.example.kltn.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review implements Serializable {
    @Id
    private Long id;
    private int rating;
    private String comment;
    private Date createDate;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
