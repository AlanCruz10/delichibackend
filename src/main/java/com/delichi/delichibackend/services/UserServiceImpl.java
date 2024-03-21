package com.delichi.delichibackend.services;

import com.delichi.delichibackend.controllers.dtos.request.*;
import com.delichi.delichibackend.controllers.dtos.responses.*;
import com.delichi.delichibackend.entities.Reservation;
import com.delichi.delichibackend.entities.Restaurant;
import com.delichi.delichibackend.entities.User;
import com.delichi.delichibackend.entities.exceptions.ExistingDataConflictException;
import com.delichi.delichibackend.entities.exceptions.NotFoundException;
import com.delichi.delichibackend.entities.exceptions.NotValidException;
import com.delichi.delichibackend.repositories.IUserRepository;
import com.delichi.delichibackend.security.TokenUtils;
import com.delichi.delichibackend.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository repository;

    @Override
    public BaseResponse login(LoginRequest request) {
        User user = findUserByEmailAndEnsureExists(request.getEmail());
        boolean login = new BCryptPasswordEncoder().matches(request.getPassword(), user.getPassword());
        if(login){
            String token = TokenUtils.createToken(user.getName(), user.getEmail());
            return BaseResponse.builder()
                    .data(fromUserToGetUserResponse(user,token))
                    .message("User Obtained")
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
                .data(fromUserToUserResponse(findUserByEmailAndEnsureExists(email)))
                .message("User Obtained")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse ListReservationsByUserId(Long userId) {
        return BaseResponse.builder()
                .data(getReservationList(userId))
                .message("Reservations List By User")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse create(CreateUserRequest request) {
        return BaseResponse.builder()
                .data(from(repository.save(from(validateEmailAndPhoneNumberExists(request)))))
                .message("User Created Correctly")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse update(UpdateUserRequest request, Long id) {
        return BaseResponse.builder()
                .data(fromUserToUpdateUserResponse(repository.save(validationUpdateDateUser(request, id))))
                .message("User Updated Correctly")
                .httpStatus(HttpStatus.OK)
                .success(Boolean.TRUE)
                .build();
    }

    @Override
    public BaseResponse delete(Long id) {
        repository.delete(findAndEnsureExists(id));
        return BaseResponse.builder()
                .message("User Deleted")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public User findAndEnsureExists(Long id){
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    private User findUserByEmailAndEnsureExists(String email) {
        return repository.findByEmail(email).orElseThrow(NotFoundException::new);
    }

    private GetUserResponse fromUserToGetUserResponse(User user, String token) {
        return GetUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .token("Bearer " + token).build();
    }

    private List<GetReservationResponse> getReservationList(Long userId){
        return findAndEnsureExists(userId)
                .getReservations()
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }

    private GetReservationResponse from(Reservation reservation){
        return GetReservationResponse.builder()
                .date(reservation.getDate())
                .hour(reservation.getHour())
                .status(reservation.getStatus())
                .people(reservation.getPeople())
                .id(reservation.getId())
                .user(fromUserToUserResponse(reservation.getUser()))
                .restaurant(fromRestaurantToRestaurantResponse(reservation.getRestaurant()))
                .build();
    }

    @Override
    public UserResponse fromUserToUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .build();
    }

    private RestaurantResponse fromRestaurantToRestaurantResponse(Restaurant restaurant){
        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .build();
    }

    private User from(CreateUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword( new BCryptPasswordEncoder().encode(request.getPassword()));
        return user;
    }

    private CreateUserResponse from(User user) {
        return CreateUserResponse.builder()
                .phoneNumber(user.getPhoneNumber())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .name(user.getName())
                .id(user.getId()).build();
    }

    private CreateUserRequest validateEmailAndPhoneNumberExists(CreateUserRequest request){
        if (repository.findByEmailOrPhoneNumber(request.getEmail(), request.getPhoneNumber()).isPresent()){
            throw new ExistingDataConflictException();
        }
        return request;
    }

    private UpdateUserRequest validateEmailAndPhoneNumberExists(UpdateUserRequest request){
        if (repository.findByEmailOrPhoneNumber(request.getEmail(), request.getPhoneNumber()).isPresent()){
            throw new ExistingDataConflictException();
        }
        return request;
    }

    private User validationUpdateDateUser(UpdateUserRequest request, Long id){
        User user = findAndEnsureExists(id);
        user.setName(request.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setLastName(request.getLastName());
        user.setId(user.getId());
//        UpdateUserRequest requestValidate = validateEmailAndPhoneNumberExists(request);
//        if(request.getName().length() == 0 || request.getName() == null || Objects.equals(request.getName(), "")) {
//            user.setName(user.getName());
//        }else {
//            user.setName(requestValidate.getName());
//        }
//        if(request.getPhoneNumber() == null || request.getPhoneNumber() == 0) {
//            user.setPhoneNumber(user.getPhoneNumber());
//        }else {
//            user.setPhoneNumber(requestValidate.getPhoneNumber());
//        }
//        if(request.getEmail().length() == 0 || request.getEmail() == null || Objects.equals(request.getEmail(), "")) {
//            user.setEmail(user.getEmail());
//        }else {
//            user.setEmail(requestValidate.getEmail());
//        }
//        if(request.getPassword().length() == 0 || request.getPassword() == null || Objects.equals(request.getPassword(), "")) {
//            user.setPassword(user.getPassword());
//        }else {
//            user.setPassword(new BCryptPasswordEncoder().encode(requestValidate.getPassword()));
//        }
//        if(request.getLastName().length() == 0 || request.getLastName() == null || Objects.equals(request.getLastName(), "")) {
//            user.setLastName(user.getLastName());
//        }else {
//            user.setLastName(requestValidate.getLastName());
//        }
        return user;
    }

    private UpdateUserResponse fromUserToUpdateUserResponse(User user){
        return UpdateUserResponse.builder()
                .lastName(user.getLastName())
                .email(user.getEmail())
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .name(user.getName()).build();
    }

    /*private User from(UpdateUserRequest request, Long id){
        User user = validationUpdateDateUser(request, id);
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(request.getPassword());
        return user;
    }*/

    /*private User validationUpdateDateUser(UpdateUserRequest request, Long id){
        User user = findAndEnsureExists(id);
        if(request.getName().length() == 0 || request.getName() == null || Objects.equals(request.getName(), "")) {
            user.setName(user.getName());
        }else {
            user.setName(request.getName());
        }
        if(request.getPhoneNumber() == null || request.getPhoneNumber() == 0) {
            user.setPhoneNumber(user.getPhoneNumber());
        }else {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if(request.getEmail().length() == 0 || request.getEmail() == null || Objects.equals(request.getEmail(), "")) {
            user.setEmail(user.getEmail());
        }else {
            user.setEmail(request.getEmail());
        }
        if(request.getPassword().length() == 0 || request.getPassword() == null || Objects.equals(request.getPassword(), "")) {
            user.setPassword(user.getPassword());
        }else {
            user.setPassword(request.getPassword());
        }
        if(request.getLastName().length() == 0 || request.getLastName() == null || Objects.equals(request.getLastName(), "")) {
            user.setLastName(user.getLastName());
        }else {
            user.setLastName(request.getLastName());
        }
        return user;
    }*/

    /*public static void main(String[] args) {
        String name = "holacomoestas";
        String encode = new BCryptPasswordEncoder().encode(name);
        System.out.println(encode);
        System.out.println(new BCryptPasswordEncoder().matches(name, encode));
        System.out.println(new BCryptPasswordEncoder().matches(encode, encode));
        String encode2 = new BCryptPasswordEncoder().encode(name);
        System.out.println(encode2);
        System.out.println(new BCryptPasswordEncoder().matches(name, encode2));
        System.out.println(new BCryptPasswordEncoder().matches(encode2, encode2));
    }*/

}
