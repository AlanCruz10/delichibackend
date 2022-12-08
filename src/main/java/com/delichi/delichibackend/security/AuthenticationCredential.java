package com.delichi.delichibackend.security;

import lombok.Data;

@Data
public class AuthenticationCredential {
    private String email;
    private String password;
}
