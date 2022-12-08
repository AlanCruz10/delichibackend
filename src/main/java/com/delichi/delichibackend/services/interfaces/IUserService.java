package com.delichi.delichibackend.services.interfaces;

import com.delichi.delichibackend.controllers.dtos.request.CreateUserRequest;
import com.delichi.delichibackend.controllers.dtos.request.LoginRequest;
import com.delichi.delichibackend.controllers.dtos.request.UpdateUserRequest;
import com.delichi.delichibackend.controllers.dtos.responses.BaseResponse;
import com.delichi.delichibackend.controllers.dtos.responses.UserResponse;
import com.delichi.delichibackend.entities.User;

public interface IUserService {

    BaseResponse login(LoginRequest request);

    BaseResponse get(String email);

    BaseResponse create(CreateUserRequest request);

    BaseResponse update(UpdateUserRequest request, Long id);

    BaseResponse delete (Long id);

    BaseResponse ListReservationsByUserId(Long userId);

    UserResponse fromUserToUserResponse(User user);

    User findAndEnsureExists(Long id);

}
