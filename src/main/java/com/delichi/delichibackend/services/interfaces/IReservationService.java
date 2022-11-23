package com.delichi.delichibackend.services.interfaces;

import com.delichi.delichibackend.controllers.dtos.request.CreateReservationRequest;
import com.delichi.delichibackend.controllers.dtos.request.UpdateReservationRequest;
import com.delichi.delichibackend.controllers.dtos.responses.BaseResponse;
import com.delichi.delichibackend.controllers.dtos.responses.ReservationResponse;
import com.delichi.delichibackend.entities.Reservation;

public interface IReservationService {
    BaseResponse create(CreateReservationRequest request, Long userId, Long restaurantId);

    BaseResponse get(Long id);

    BaseResponse update(UpdateReservationRequest request, Long id);

    BaseResponse delete(Long id);

    ReservationResponse fromReservationToReservationResponse(Reservation reservation);

}
