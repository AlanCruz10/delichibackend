package com.delichi.delichibackend.services.interfaces;

import com.delichi.delichibackend.controllers.dtos.request.CreateCeoRequest;
import com.delichi.delichibackend.controllers.dtos.request.LoginRequest;
import com.delichi.delichibackend.controllers.dtos.request.UpdateCeoRequest;
import com.delichi.delichibackend.controllers.dtos.responses.BaseResponse;
import com.delichi.delichibackend.controllers.dtos.responses.CeoResponse;
import com.delichi.delichibackend.controllers.dtos.responses.GetCeoResponse;
import com.delichi.delichibackend.entities.Ceo;

public interface ICeoService {

    BaseResponse login(LoginRequest request);

    BaseResponse get(String email);

    BaseResponse listAllRestaurantByCeoId(Long id);

    BaseResponse create(CreateCeoRequest request);

    BaseResponse update(UpdateCeoRequest request, Long id);

    BaseResponse delete(Long id);

    Ceo findAndEnsureExist(Long id);

    CeoResponse fromCeoToCeoResponse(Ceo ceo);

    GetCeoResponse fromCeoToGetCeoResponse(Ceo ceo, String token);

    Ceo findCeoByEmail(String email);

}
