package com.delichi.delichibackend.services.interfaces;

import com.delichi.delichibackend.controllers.dtos.request.LoginRequest;
import com.delichi.delichibackend.controllers.dtos.responses.BaseResponse;

public interface ILoginService {
    BaseResponse get(LoginRequest request);
}
