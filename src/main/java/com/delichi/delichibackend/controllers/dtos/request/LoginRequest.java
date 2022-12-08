package com.delichi.delichibackend.controllers.dtos.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class LoginRequest {
    @Email
    @NonNull
    private String email;
    @NotNull
    private String password;
}
