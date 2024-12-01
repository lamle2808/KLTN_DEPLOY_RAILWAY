package com.example.kltn.dataBean;

import lombok.*;

import java.util.Date;

import com.example.kltn.entity.Account;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDataBean {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Date dateOfBirth;
    private int sex;
    private String phone;
    private String address;
    private Account account;
    private String customerType;
    private String token;
}
