package com.delichi.delichibackend.controllers.dtos.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateUserRequest {
    @Email
    @NotNull
    private String email;
    @NotNull
    private String name;
    @NotNull
    private String lastName;
    @NotNull
    private Long phoneNumber;
    @NotNull
    private String password;

    public UpdateUserRequest() {
    }

}
