package com.delichi.delichibackend.controllers.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateReservationResponse {
    private Long id;
    private String date;
    private String hour;
    private Integer people;
    private String status;
    private Integer tableNumber;
    private RestaurantResponse restaurant;
    private UserResponse user;
}
