package com.delichi.delichibackend.services;

import com.delichi.delichibackend.controllers.dtos.responses.BaseResponse;
import com.delichi.delichibackend.controllers.dtos.responses.GetImageResponse;
import com.delichi.delichibackend.controllers.dtos.responses.GetZoneResponse;
import com.delichi.delichibackend.controllers.dtos.responses.RestaurantResponse;
import com.delichi.delichibackend.entities.*;
import com.delichi.delichibackend.entities.exceptions.NotFoundException;
import com.delichi.delichibackend.repositories.IZoneRepository;
import com.delichi.delichibackend.services.interfaces.IZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ZoneServiceImpl implements IZoneService {

    @Autowired
    private IZoneRepository repository;

    @Override
    public BaseResponse get(Long id) {
        return BaseResponse.builder()
                .data(from(findAndEnsureExist(id)))
                .message("zones get correctly")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse list() {
        return BaseResponse.builder()
                .data(getZoneResponseList())
                .message("zones list correctly")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse listAllRestaurantByZoneId(Long id){
        return BaseResponse.builder()
                .data(getRestaurantResponseListByZoneId(id))
                .message("Restaurant List By Zone Id")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public Zone findAndEnsureExist(Long id){
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public GetZoneResponse from(Zone zone){
        return GetZoneResponse.builder()
                .id(zone.getId())
                .name(zone.getName())
                .build();
    }

    private List<GetZoneResponse> getZoneResponseList(){
        return getZoneList().stream()
                .map(this::from)
                .collect(Collectors.toList());
    }

    private List<Zone> getZoneList(){
        return repository.findAll();
    }

    private List<RestaurantResponse> getRestaurantResponseListByZoneId(Long id){
        return getRestaurantListByZoneId(id)
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }

    private List<Restaurant> getRestaurantListByZoneId(Long id){
        return findAndEnsureExist(id)
                .getRestaurants();
    }

    private RestaurantResponse from(Restaurant restaurant){
        List<Integer> integers = restaurant.getComments().stream().map(Comment::getScore).toList();
        return RestaurantResponse.builder()
                .name(restaurant.getName())
                .id(restaurant.getId())
                .topScore(moda(integers))
//                .image(getImageResponseList(restaurant))
                .build();
    }

    private Float moda(List<Integer> score){
        HashMap<Integer, Integer> mapa = new HashMap<>();
        for (int x = 0; x < score.size(); x++) {
            Integer numero = score.get(x);
            if (mapa.containsKey(numero)) {
                mapa.put(numero, mapa.get(numero) + 1);
            } else {
                mapa.put(numero, 1);
            }
        }
        float moda =0;
        int mayor = 0;
        for (HashMap.Entry<Integer, Integer> entry : mapa.entrySet()) {
            if (entry.getValue() > mayor) {
                mayor = entry.getValue();
                moda = entry.getKey();
            }
        }
        return moda;
    }

    private List<GetImageResponse> getImageResponseList(Restaurant restaurant){
        return restaurant.getImages()
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }

    private GetImageResponse from(Image image){
        return GetImageResponse.builder()
                .fileUrl(image.getFileUrl())
                .imageType(image.getImageType())
                .id(image.getId()).build();
    }

}