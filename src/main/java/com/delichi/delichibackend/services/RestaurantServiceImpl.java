package com.delichi.delichibackend.services;

import com.delichi.delichibackend.controllers.dtos.request.CreateRestaurantRequest;
import com.delichi.delichibackend.controllers.dtos.request.UpdateRestaurantRequest;
import com.delichi.delichibackend.controllers.dtos.responses.*;
import com.delichi.delichibackend.entities.Comment;
import com.delichi.delichibackend.entities.Image;
import com.delichi.delichibackend.entities.Reservation;
import com.delichi.delichibackend.entities.Restaurant;
import com.delichi.delichibackend.entities.exceptions.NotFoundException;
import com.delichi.delichibackend.repositories.IRestaurantRepository;
import com.delichi.delichibackend.services.interfaces.ICeoService;
import com.delichi.delichibackend.services.interfaces.IRestaurantService;
import com.delichi.delichibackend.services.interfaces.IUserService;
import com.delichi.delichibackend.services.interfaces.IZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements IRestaurantService {

    @Autowired
    private IRestaurantRepository repository;

    @Autowired
    private ICeoService ceoService;

    @Autowired
    private IZoneService zoneService;

    @Autowired
    private IUserService userService;

    @Override
    public BaseResponse get(Long id) {
        return BaseResponse.builder()
                .data(fromRestaurantToGetRestaurantResponse(findAndEnsureExist(id)))
                .message("Restaurant Got Correctly")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse list(){
        return BaseResponse.builder()
                .data(restaurantResponseList())
                .message("List All Restaurants")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse listAllRestaurantsByName(String name) {
        return BaseResponse.builder()
                .data(getRestaurantResponse(name))
                .message("Restaurant list by her name")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse create(CreateRestaurantRequest request, Long ceoId, Long zoneId){
        return BaseResponse.builder()
                .data(fromRestaurantToCreateRestaurantResponse(repository.save(from(request, ceoId, zoneId))))
                .message("Restaurant Created Correctly")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse update(UpdateRestaurantRequest request, Long id, Long zoneId) {
        return BaseResponse.builder()
                .data(fromRestaurantToUpdateRestaurantResponse(repository.save(from(request, id, zoneId))))
                .message("Update Restaurant Correctly")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse delete(Long id) {
        repository.delete(findAndEnsureExist(id));
        return BaseResponse.builder()
                .message("Restaurant Deleted Correctly")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public Restaurant findAndEnsureExist(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    private GetRestaurantResponse fromRestaurantToGetRestaurantResponse(Restaurant restaurant){
        return GetRestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .phoneNumber(restaurant.getPhoneNumber())
                .address(restaurant.getAddress())
                .schedule(restaurant.getSchedule())
                .menu(restaurant.getMenu())
                .topScore(restaurant.getTopScore())
                .reviews(restaurant.getReviews())
//                .tableNumber(restaurant.getTableNumber())
//                .tableCapacity(restaurant.getTableCapacity())
                .zone(zoneService.from(restaurant.getZone()))
                .ceo(ceoService.fromCeoToCeoResponse(restaurant.getCeo()))
                .kitchen(restaurant.getKitchen())
                .comments(getCommentResponseList(restaurant.getId()))
//                .reservations(  getReservationResponseList(restaurant.getId()))
//                .images(getImageResponsesList(restaurant.getId()))
                .build();
    }

    @Override
    public List<GetImageResponse> getImageResponsesList(Long id){
        return getImageList(id)
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }

    private GetImageResponse from(Image image){
        return GetImageResponse.builder()
                .id(image.getId())
                .fileUrl(image.getFileUrl())
                .imageType(image.getImageType())
                .build();
    }

    private List<Image> getImageList(Long id){
        return findAndEnsureExist(id)
                .getImages();
    }

    private List<GetCommentResponse> getCommentResponseList(Long id){
        return getCommentList(id)
                .stream()
                .map(this::fromCommentToGetCommentResponse)
                .collect(Collectors.toList());
    }

    private GetCommentResponse fromCommentToGetCommentResponse(Comment comment){
        return GetCommentResponse.builder()
                .id(comment.getId())
                .date(comment.getDate())
                .score(comment.getScore())
                .content(comment.getContent())
                .user(userService.fromUserToUserResponse(comment.getUser()))
                .restaurant(fromRestaurantToRestaurantResponse(comment.getRestaurant())).build();
    }

    private List<Comment> getCommentList(Long id){
        return findAndEnsureExist(id)
                .getComments();
    }

    private List<GetReservationResponse> getReservationResponseList(Long id){
        return getReservationList(id)
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }

    private GetReservationResponse from(Reservation reservation){
        return GetReservationResponse.builder()
                .id(reservation.getId())
                .date(reservation.getDate())
                .people(reservation.getPeople())
                .hour(reservation.getHour())
                .status(reservation.getStatus())
                .user(userService.fromUserToUserResponse(reservation.getUser()))
                .restaurant(fromRestaurantToRestaurantResponse(reservation.getRestaurant())).build();
    }

    private List<Reservation> getReservationList(Long id){
        return findAndEnsureExist(id)
                .getReservations();
    }

    private List<Restaurant> restaurantList(){
        return repository.findAllRestaurants().orElseThrow(NotFoundException::new);
    }

    private List<RestaurantResponse> restaurantResponseList(){
        return restaurantList()
                .stream()
                .map(this::fromRestaurantToRestaurantResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantResponse fromRestaurantToRestaurantResponse(Restaurant restaurant){
        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .topScore(restaurant.getTopScore())
                .reviews(restaurant.getReviews())
                .nameZone(restaurant.getZone().getName())
                .kitchen(restaurant.getKitchen())
//                .image(getImageResponsesList(restaurant.getId()))
                .build();
    }

    private List<Restaurant> getRestaurantListByName(String name){
        return repository.findAllByName(name).orElseThrow(NotFoundException::new);
    }

    private List<RestaurantResponse> getRestaurantResponse(String name){
        return getRestaurantListByName(name)
                .stream()
                .map(this::fromRestaurantToRestaurantResponse)
                .collect(Collectors.toList());
    }

    private Restaurant from(CreateRestaurantRequest request, Long ceoId, Long zoneId){
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setPhoneNumber(request.getPhoneNumber());
        restaurant.setAddress(request.getAddress());
        restaurant.setSchedule(request.getSchedule());
        restaurant.setMenu(request.getMenu());
        restaurant.setZone(zoneService.findAndEnsureExist(zoneId));
        restaurant.setKitchen(request.getKitchen());
        restaurant.setCeo(ceoService.findAndEnsureExist(ceoId));
        restaurant.setTableNumber(request.getTableNumber());
        restaurant.setTableCapacity(request.getTableCapacity());
        return restaurant;
    }

    private CreateRestaurantResponse fromRestaurantToCreateRestaurantResponse(Restaurant restaurant){
        return CreateRestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .phoneNumber(restaurant.getPhoneNumber())
                .address(restaurant.getAddress())
                .schedule(restaurant.getSchedule())
                .menu(restaurant.getMenu())
                .zone(restaurant.getZone().getName())
                .kitchen(restaurant.getKitchen())
                .tableNumber(restaurant.getTableNumber())
                .tableCapacity(restaurant.getTableCapacity()).build();
    }



    private Restaurant from(UpdateRestaurantRequest request, Long id, Long zoneId){
        Restaurant restaurant = findAndEnsureExist(id);
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setKitchen(request.getKitchen());
        restaurant.setPhoneNumber(request.getPhoneNumber());
        restaurant.setSchedule(request.getSchedule());
        restaurant.setZone(zoneService.findAndEnsureExist(zoneId));
        restaurant.setDescription(request.getDescription());
        restaurant.setMenu(restaurant.getMenu());
        restaurant.setTableCapacity(request.getTableCapacity());
        restaurant.setTableNumber(request.getTableNumber());
        return restaurant;
    }

    private UpdateRestaurantResponse fromRestaurantToUpdateRestaurantResponse(Restaurant restaurant){
        return UpdateRestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .phoneNumber(restaurant.getPhoneNumber())
                .address(restaurant.getAddress())
                .schedule(restaurant.getSchedule())
                .menu(restaurant.getMenu())
                .zone(restaurant.getZone().getName())
                .kitchen(restaurant.getKitchen())
                .tableNumber(restaurant.getTableNumber())
                .tableCapacity(restaurant.getTableCapacity()).build();
    }

    @Override
    public BaseResponse listAllImagesByRestaurantId(Long restaurantId) {
        return BaseResponse.builder()
                .data(getImageResponsesList(restaurantId))
                .message("Images by restaurant ID")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

}
