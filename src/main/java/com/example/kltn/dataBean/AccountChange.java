package com.example.kltn.dataBean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountChange {
    private String email;
    private String passwordOld;
    private String passwordNew;
}
