package com.delichi.delichibackend.services.interfaces;

import com.delichi.delichibackend.controllers.dtos.request.CreateRestaurantRequest;
import com.delichi.delichibackend.controllers.dtos.request.UpdateRestaurantRequest;
import com.delichi.delichibackend.controllers.dtos.responses.BaseResponse;
import com.delichi.delichibackend.controllers.dtos.responses.GetImageResponse;
import com.delichi.delichibackend.controllers.dtos.responses.RestaurantResponse;
import com.delichi.delichibackend.entities.Restaurant;

import java.util.List;

public interface IRestaurantService {

    BaseResponse get(Long id);

    BaseResponse create(CreateRestaurantRequest request, Long ceoId, Long zoneId);

    BaseResponse list();

    BaseResponse listAllRestaurantsByName(String name);

    BaseResponse update(UpdateRestaurantRequest request, Long id, Long zoneId);

    BaseResponse delete(Long id);

    BaseResponse listAllImagesByRestaurantId(Long restaurantId);

    Restaurant findAndEnsureExist(Long id);

    RestaurantResponse fromRestaurantToRestaurantResponse(Restaurant restaurant);

    List<GetImageResponse> getImageResponsesList(Long id);

}
