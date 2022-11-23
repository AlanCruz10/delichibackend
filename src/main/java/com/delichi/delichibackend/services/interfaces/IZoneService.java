package com.delichi.delichibackend.services.interfaces;

import com.delichi.delichibackend.controllers.dtos.responses.BaseResponse;
import com.delichi.delichibackend.controllers.dtos.responses.GetZoneResponse;
import com.delichi.delichibackend.entities.Zone;

public interface IZoneService {
    BaseResponse get(Long id);

    BaseResponse list();

    Zone findAndEnsureExist(Long id);

    GetZoneResponse from(Zone zone);

    BaseResponse listAllRestaurantByZoneId(Long id);

}
