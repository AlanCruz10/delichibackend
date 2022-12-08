package com.delichi.delichibackend.services;

import com.delichi.delichibackend.controllers.dtos.request.CreateCeoRequest;
import com.delichi.delichibackend.controllers.dtos.request.LoginRequest;
import com.delichi.delichibackend.controllers.dtos.request.UpdateCeoRequest;
import com.delichi.delichibackend.controllers.dtos.responses.*;
import com.delichi.delichibackend.entities.Ceo;
import com.delichi.delichibackend.entities.Image;
import com.delichi.delichibackend.entities.Restaurant;
import com.delichi.delichibackend.entities.exceptions.ExistingDataConflictException;
import com.delichi.delichibackend.entities.exceptions.NotFoundException;
import com.delichi.delichibackend.entities.exceptions.NotValidException;
import com.delichi.delichibackend.repositories.ICeoRepository;
import com.delichi.delichibackend.security.TokenUtils;
import com.delichi.delichibackend.services.interfaces.ICeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CeoServiceImpl implements ICeoService {
    @Autowired
    private ICeoRepository repository;

    @Override
    public BaseResponse login(LoginRequest request) {
        Ceo ceo = findCeoByEmail(request.getEmail());
        boolean login = new BCryptPasswordEncoder().matches(request.getPassword(), ceo.getPassword());
        if(login){
            String token = TokenUtils.createToken(ceo.getName(), ceo.getEmail());
            return BaseResponse.builder()
                    .data(fromCeoToGetCeoResponse(ceo,token))
                    .message("Ceo Obtained")
                    .success(Boolean.TRUE)
                    .httpStatus(HttpStatus.OK)
                    .build();
        }else {
            throw new NotValidException();
        }
    }

    @Override
    public BaseResponse get(String email) {
        return BaseResponse.builder()
                .data(fromCeoToCeoResponse(findCeoByEmail(email)))
                .message("Ceo Obtained")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse listAllRestaurantByCeoId(Long id) {
        return BaseResponse.builder()
                .data(getRestaurantResponseList(id))
                .message("Restaurant List By Ceo Id")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse create(CreateCeoRequest request){
        return BaseResponse.builder()
                .data(from(repository.save(from(validateEmailAndPhoneNumberExists(request)))))
                .message("Ceo Created Correctly")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse update(UpdateCeoRequest request, Long id) {
        return BaseResponse.builder()
                .data(fromCeoToUpdateCeoResponse(repository.save(validationUpdateDateCeo(request, id))))
                .message("Ceo Update Correctly")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse delete(Long id) {
        repository.delete(findAndEnsureExist(id));
        return BaseResponse.builder()
                .message("Ceo Deleted Correctly")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public Ceo findAndEnsureExist(Long id){
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Ceo findCeoByEmail(String email) {
        return repository.findCeoByEmail(email).orElseThrow(NotFoundException::new);
    }

    @Override
    public GetCeoResponse fromCeoToGetCeoResponse(Ceo ceo, String token) {
        return GetCeoResponse.builder()
                .id(ceo.getId())
                .name(ceo.getName())
                .firstSurname(ceo.getFirstSurname())
                .secondSurname(ceo.getSecondSurname())
                .email(ceo.getEmail())
                .token("Bearer " + token).build();
    }

    private CreateCeoRequest validateEmailAndPhoneNumberExists(CreateCeoRequest request){
        if (repository.findByEmailOrPhoneNumber(request.getEmail(), request.getPhoneNumber()).isPresent()){
            throw new ExistingDataConflictException();
        }
        return request;
    }

    private UpdateCeoRequest validateEmailAndPhoneNumberExists(UpdateCeoRequest request){
        if (repository.findByEmailOrPhoneNumber(request.getEmail(), request.getPhoneNumber()).isPresent()){
            throw new ExistingDataConflictException();
        }
        return request;
    }

    private List<RestaurantResponse> getRestaurantResponseList(Long id){
        return getRestaurantList(id)
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }

    private List<Restaurant> getRestaurantList(Long id){
        return findAndEnsureExist(id)
                .getRestaurants();
    }

    private RestaurantResponse from(Restaurant restaurant){
        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .nameZone(restaurant.getZone().getName())
                .image(getImageResponseList(restaurant)).build();
    }

    private Ceo from(CreateCeoRequest request){
        Ceo ceo = new Ceo();
        ceo.setName(request.getName());
        ceo.setFirstSurname(request.getFirstSurname());
        ceo.setSecondSurname(request.getSecondSurname());
        ceo.setPhoneNumber(request.getPhoneNumber());
        ceo.setEmail(request.getEmail());
        ceo.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        return ceo;
    }

    private CreateCeoResponse from(Ceo ceo){
        return CreateCeoResponse.builder()
                .id(ceo.getId())
                .name(ceo.getName())
                .firstSurname(ceo.getFirstSurname())
                .secondSurname(ceo.getSecondSurname())
                .email(ceo.getEmail()).build();
    }

    private Ceo validationUpdateDateCeo(UpdateCeoRequest request, Long id){
        Ceo ceo = findAndEnsureExist(id);
        UpdateCeoRequest requestValidate = validateEmailAndPhoneNumberExists(request);
        if(request.getName().length() == 0 || request.getName() == null || Objects.equals(request.getName(), "")) {
            ceo.setName(ceo.getName());
        }else {
            ceo.setName(requestValidate.getName());
        }
        if(request.getPhoneNumber() == null || request.getPhoneNumber() == 0) {
            ceo.setPhoneNumber(ceo.getPhoneNumber());
        }else {
            ceo.setPhoneNumber(requestValidate.getPhoneNumber());
        }
        if(request.getEmail().length() == 0 || request.getEmail() == null || Objects.equals(request.getEmail(), "")) {
            ceo.setEmail(ceo.getEmail());
        }else {
            ceo.setEmail(requestValidate.getEmail());
        }
        if(request.getPassword().length() == 0 || request.getPassword() == null || Objects.equals(request.getPassword(), "")) {
            ceo.setPassword(ceo.getPassword());
        }else {
            ceo.setPassword(requestValidate.getPassword());
        }
        if(request.getFirstSurname().length() == 0 || request.getFirstSurname() == null || Objects.equals(request.getFirstSurname(), "")) {
            ceo.setFirstSurname(ceo.getFirstSurname());
        }else {
            ceo.setFirstSurname(requestValidate.getFirstSurname());
        }
        if(request.getSecondSurname().length() == 0 || request.getSecondSurname() == null || Objects.equals(request.getSecondSurname(), "")) {
            ceo.setSecondSurname(ceo.getSecondSurname());
        }else {
            ceo.setSecondSurname(requestValidate.getSecondSurname());
        }
        return ceo;
    }

    private UpdateCeoResponse fromCeoToUpdateCeoResponse(Ceo ceo){
        return UpdateCeoResponse.builder()
                .id(ceo.getId())
                .name(ceo.getName())
                .firstSurname(ceo.getFirstSurname())
                .secondSurname(ceo.getSecondSurname())
                .email(ceo.getEmail())
                .phoneNumber(ceo.getPhoneNumber()).build();
    }

    @Override
    public CeoResponse fromCeoToCeoResponse(Ceo ceo){
        return CeoResponse.builder()
                .id(ceo.getId())
                .name(ceo.getName())
                .firstSurname(ceo.getFirstSurname())
                .secondSurname(ceo.getSecondSurname())
                .phoneNumber(ceo.getPhoneNumber())
                .email(ceo.getEmail())
                .build();
    }

    public List<GetImageResponse> getImageResponseList(Restaurant restaurant){
        return restaurant
                .getImages()
                .stream()
                .filter(img-> Objects.equals(img.getImageType(), "logo"))
                .map(this::fromImageToGetImageResponse)
                .collect(Collectors.toList());
    }

    private GetImageResponse fromImageToGetImageResponse(Image image){
        return GetImageResponse.builder()
                .id(image.getId())
                .fileUrl(image.getFileUrl())
                .imageType(image.getImageType())
                .build();
    }

}
