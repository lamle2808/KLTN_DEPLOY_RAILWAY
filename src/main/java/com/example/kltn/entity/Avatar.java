package com.example.kltn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "avatars")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Avatar extends Image {

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    // Thêm các thuộc tính hoặc mối quan hệ cụ thể cho Avatar nếu cần
    // Ví dụ: private String additionalAttribute;
}
