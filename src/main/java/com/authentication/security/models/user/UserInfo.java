package com.authentication.security.models.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private int id;
    private String fullName;
    private String email;
    private String  gender;
    private String phone;
    private String cin;
    private String address;
}
