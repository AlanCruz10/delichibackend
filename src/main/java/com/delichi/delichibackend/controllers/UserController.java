package com.delichi.delichibackend.controllers;

import com.delichi.delichibackend.controllers.dtos.request.CreateUserRequest;
import com.delichi.delichibackend.controllers.dtos.request.LoginRequest;
import com.delichi.delichibackend.controllers.dtos.request.UpdateUserRequest;
import com.delichi.delichibackend.controllers.dtos.responses.BaseResponse;
import com.delichi.delichibackend.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService service;

    @PostMapping("login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody LoginRequest request){
        BaseResponse baseResponse = service.login(request);
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }

    @GetMapping("{email}")
    public ResponseEntity<BaseResponse> get(@Valid @Email @PathVariable String email){
        BaseResponse baseResponse = service.get(email);
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }

    @GetMapping("reservations/{userId}")
    public ResponseEntity<BaseResponse> ListReservationsByUserId(@Valid @PathVariable Long userId){
        BaseResponse baseResponse = service.ListReservationsByUserId(userId);
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }

    @PostMapping
    public ResponseEntity<BaseResponse> create(@Valid @RequestBody CreateUserRequest request){
        BaseResponse baseResponse = service.create(request);
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }

    @PutMapping("{id}")
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody UpdateUserRequest request, @Valid @PathVariable Long id){
        BaseResponse baseResponse = service.update(request, id);
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BaseResponse> delete(@Valid @PathVariable Long id){
        BaseResponse baseResponse = service.delete(id);
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }

}
