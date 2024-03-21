package com.delichi.delichibackend.controllers;

import com.delichi.delichibackend.controllers.dtos.request.CreateRestaurantRequest;
import com.delichi.delichibackend.controllers.dtos.request.UpdateRestaurantRequest;
import com.delichi.delichibackend.controllers.dtos.responses.BaseResponse;
import com.delichi.delichibackend.services.interfaces.IRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("restaurant")
public class RestaurantController {
    @Autowired
    private IRestaurantService service;

    @GetMapping("api/v1/{id}")
    public ResponseEntity<BaseResponse> get(@Valid @PathVariable Long id){
        BaseResponse baseResponse = service.get(id);
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }


    @GetMapping("api/v1/restaurants")
    public ResponseEntity<BaseResponse> list(){
        BaseResponse baseResponse = service.list();
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }

    @GetMapping("restaurants/name/{name}")
    public ResponseEntity<BaseResponse> listAllRestaurantsByName(@Valid @PathVariable String name){
        BaseResponse baseResponse = service.listAllRestaurantsByName(name);
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }

    @PostMapping("ceo/{ceoId}/zone/{zoneId}")
    public ResponseEntity<BaseResponse> create(@Valid @RequestBody CreateRestaurantRequest request,
                                               @Valid @PathVariable Long ceoId,
                                               @Valid @PathVariable Long zoneId){
        BaseResponse baseResponse = service.create(request, ceoId, zoneId);
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }

    @PostMapping("{id}/zone/{zoneId}")
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody UpdateRestaurantRequest request,
                                               @Valid @PathVariable Long id,
                                               @Valid @PathVariable Long zoneId){
        BaseResponse baseResponse = service.update(request, id, zoneId);
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BaseResponse> delete(@Valid @PathVariable Long id){
        BaseResponse baseResponse = service.delete(id);
        return new ResponseEntity<>(baseResponse, baseResponse.getHttpStatus());
    }

}
