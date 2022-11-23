package com.delichi.delichibackend.controllers.dtos.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class UpdateRestaurantRequest {
    @NotNull
    private String name;
    @NotNull
    private Long phoneNumber;
    @NotNull
    private String address;
    @NotNull
    private String schedule;
    private String kitchen;
    private String description;
    private String menu;
    @NotNull
    private Integer tableNumber;
    @NotNull
    private Integer tableCapacity;
}
