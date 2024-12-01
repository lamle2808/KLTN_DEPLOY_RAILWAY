package com.example.kltn.dataBean;

import com.example.kltn.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private Long accountId;
    private String email;
    private String userName;
    private String phoneNumber;
    private String role;
    private int isVerified;
    private int enable;
    
    public static LoginResponse fromAccount(String token, Account account) {
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setAccountId(account.getId());
        response.setEmail(account.getEmail());
        response.setUserName(account.getUsername());
        response.setPhoneNumber(account.getPhoneNumber());
        response.setRole(account.getRoles().isEmpty() ? null : account.getRoles().iterator().next().getName());
        response.setIsVerified(account.getIsVerified());
        response.setEnable(account.getEnable());
        return response;
    }
} 