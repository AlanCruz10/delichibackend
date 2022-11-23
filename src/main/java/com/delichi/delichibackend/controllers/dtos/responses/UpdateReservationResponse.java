package com.delichi.delichibackend.controllers.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateReservationResponse {
    private Long id;
    private String date;
    private String hour;
    private Integer people;
    private RestaurantResponse restaurant;
    private UserResponse user;
}
