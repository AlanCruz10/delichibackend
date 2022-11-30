package com.delichi.delichibackend.controllers;

import com.delichi.delichibackend.controllers.dtos.request.CreateReservationRequest;
import com.delichi.delichibackend.controllers.dtos.request.UpdateReservationRequest;
import com.delichi.delichibackend.controllers.dtos.responses.BaseResponse;
import com.delichi.delichibackend.services.interfaces.IReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("reservation")
public class ReservationController {
    @Autowired
    private IReservationService service;

    @GetMapping("{id}")
    public ResponseEntity<BaseResponse> get(@Valid @PathVariable Long id){
        BaseResponse baseResponse = service.get(id);
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }

    @PostMapping("user/{userId}/restaurant/{restaurantId}")
    public ResponseEntity<BaseResponse> create(@Valid @RequestBody CreateReservationRequest request,
                                               @Valid @PathVariable Long userId,
                                               @Valid @PathVariable Long restaurantId){
        BaseResponse baseResponse = service.create(request, userId, restaurantId);
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }

    @PostMapping("{id}" )
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody UpdateReservationRequest request,
                                               @Valid @PathVariable Long id){
        BaseResponse baseResponse = service.update(request, id);
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id){
        BaseResponse baseResponse = service.delete(id);
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }

}
