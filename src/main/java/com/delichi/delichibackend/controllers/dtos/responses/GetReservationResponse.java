package com.delichi.delichibackend.controllers.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetReservationResponse {
    private Long id;
    private String date;
    private String hour;
    private Integer people;
    private String status;
    private RestaurantResponse restaurant;
    private UserResponse user;
}
