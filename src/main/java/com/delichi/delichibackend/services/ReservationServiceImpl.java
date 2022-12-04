package com.delichi.delichibackend.services;

import com.delichi.delichibackend.controllers.dtos.request.CreateReservationRequest;
import com.delichi.delichibackend.controllers.dtos.request.UpdateReservationRequest;
import com.delichi.delichibackend.controllers.dtos.responses.*;
import com.delichi.delichibackend.entities.Reservation;
import com.delichi.delichibackend.entities.Restaurant;
import com.delichi.delichibackend.entities.exceptions.InvalidDataReservation;
import com.delichi.delichibackend.entities.exceptions.NotFoundException;
import com.delichi.delichibackend.repositories.IReservationRepository;
import com.delichi.delichibackend.services.interfaces.IReservationService;
import com.delichi.delichibackend.services.interfaces.IRestaurantService;
import com.delichi.delichibackend.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class ReservationServiceImpl implements IReservationService {

    @Autowired
    private IReservationRepository repository;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRestaurantService restaurantService;

    @Override
    public BaseResponse get(Long id) {
        return BaseResponse.builder()
                .data(fromReservationToGetReservationResponse(findAndEnsureExist(id)))
                .message("Reservation Obtained")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse create(CreateReservationRequest request, Long userId, Long restaurantId) {
        return BaseResponse.builder()
                .data(from(repository.save(validCreateReservation(request, userId, restaurantId))))
                .message("Reservation Created Correctly")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse update(UpdateReservationRequest request, Long id) {
        return BaseResponse.builder()
                .data(fromReservationToUpdateReservationResponse(repository.save(validationUpdateDateReservation(request, id))))
                .message("reservation updated correctly")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    @Override
    public BaseResponse delete(Long id) {
        repository.delete(findAndEnsureExist(id));
        return BaseResponse.builder()
                .message("Reservation Deleted Correctly")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    private Reservation findAndEnsureExist(Long id){
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    private GetReservationResponse fromReservationToGetReservationResponse(Reservation reservation){
        return GetReservationResponse.builder()
                .id(reservation.getId())
                .date(reservation.getDate())
                .people(reservation.getPeople())
                .status(reservation.getStatus())
                .hour(reservation.getHour())
                .user(userService.fromUserToUserResponse(reservation.getUser()))
                .restaurant(restaurantService.fromRestaurantToRestaurantResponse(reservation.getRestaurant())).build();
    }

    private LocalDate getLocalDate(){
        int monthToMonth = LocalDate.now().getMonthValue();
        int yearToYear = LocalDate.now().getYear();
        int dayToDay = LocalDate.now().getDayOfMonth();
        String date = yearToYear+"-"+monthToMonth+"-"+dayToDay;

        if(dayToDay<10){
            DateTimeFormatter dateFormatToDate = DateTimeFormatter.ofPattern("yyyy-MM-d");
            DateTimeFormatter dateFormatToDate2 = DateTimeFormatter.ofPattern("yyyy-MM-d");
            LocalDate datetime = LocalDate.parse(date, dateFormatToDate);
            String format = datetime.format(dateFormatToDate2);
            return LocalDate.parse(format,dateFormatToDate2);
        } else if (monthToMonth<10) {
            DateTimeFormatter dateFormatToDate = DateTimeFormatter.ofPattern("yyyy-M-dd");
            DateTimeFormatter dateFormatToDate2 = DateTimeFormatter.ofPattern("yyyy-M-dd");
            LocalDate datetime = LocalDate.parse(date, dateFormatToDate);
            String format = datetime.format(dateFormatToDate2);
            return LocalDate.parse(format,dateFormatToDate2);

        } else if (monthToMonth>10 && dayToDay>10) {
            DateTimeFormatter dateFormatToDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dateFormatToDate2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate datetime = LocalDate.parse(date, dateFormatToDate);
            String format = datetime.format(dateFormatToDate2);
            return LocalDate.parse(format,dateFormatToDate2);
        }else {
            DateTimeFormatter dateFormatToDate = DateTimeFormatter.ofPattern("yyyy-M-d");
            DateTimeFormatter dateFormatToDate2 = DateTimeFormatter.ofPattern("yyyy-M-d");
            LocalDate datetime = LocalDate.parse(date, dateFormatToDate);
            String format = datetime.format(dateFormatToDate2);
            return LocalDate.parse(format,dateFormatToDate2);
        }
    }

    private LocalDate dateOfReservation(CreateReservationRequest request){
        int year = Integer.parseInt(request.getDate().substring(0, 4));
        int mont = Integer.parseInt(request.getDate().substring(5, 7));
        int day = Integer.parseInt(request.getDate().substring(8, 10));
        String date = year+"-"+mont+"-"+day;
        if(day<10){
            DateTimeFormatter dateFormatToDate = DateTimeFormatter.ofPattern("yyyy-MM-d");
            DateTimeFormatter dateFormatToDate2 = DateTimeFormatter.ofPattern("yyyy-MM-d");
            LocalDate datetime = LocalDate.parse(date, dateFormatToDate);
            String format = datetime.format(dateFormatToDate2);
            return LocalDate.parse(format, dateFormatToDate2);
        } else if (mont<10) {
            DateTimeFormatter dateFormatToDate = DateTimeFormatter.ofPattern("yyyy-M-dd");
            DateTimeFormatter dateFormatToDate2 = DateTimeFormatter.ofPattern("yyyy-M-dd");
            LocalDate datetime = LocalDate.parse(date, dateFormatToDate);
            String format = datetime.format(dateFormatToDate2);
            return LocalDate.parse(format, dateFormatToDate2);

        } else if (mont>10 && day>10) {
            DateTimeFormatter dateFormatToDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dateFormatToDate2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate datetime = LocalDate.parse(date, dateFormatToDate);
            String format = datetime.format(dateFormatToDate2);
            return LocalDate.parse(format, dateFormatToDate2);
        }else {
            DateTimeFormatter dateFormatToDate = DateTimeFormatter.ofPattern("yyyy-M-d");
            DateTimeFormatter dateFormatToDate2 = DateTimeFormatter.ofPattern("yyyy-M-d");
            LocalDate datetime = LocalDate.parse(date, dateFormatToDate);
            String format = datetime.format(dateFormatToDate2);
            return LocalDate.parse(format, dateFormatToDate2);
        }
    }

    private LocalTime getLocalHour(){
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        String hourNow= hour+":"+minute;
        if (hour<10 && minute<10){
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("H:m");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime nowHour = LocalTime.parse(hourNow,FOMATTER2);
            String format = nowHour.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        }else if(hour<10){
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("H:mm");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime nowHour = LocalTime.parse(hourNow,FOMATTER2);
            String format = nowHour.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        } else if (minute<10) {
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("HH:m");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime nowHour = LocalTime.parse(hourNow,FOMATTER2);
            String format = nowHour.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        }else {
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("HH:mm");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime nowHour = LocalTime.parse(hourNow,FOMATTER2);
            String format = nowHour.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        }
    }

    private String scheduleOfDayName(CreateReservationRequest request){
        int year = Integer.parseInt(request.getDate().substring(0, 4));
        int mont = Integer.parseInt(request.getDate().substring(5, 7));
        int day = Integer.parseInt(request.getDate().substring(8, 10));
        String date = year+"-"+mont+"-"+day;
        if(day<10){
            DateTimeFormatter dateFormatToDate = DateTimeFormatter.ofPattern("yyyy-MM-d");
            DateTimeFormatter dateFormatToDate2 = DateTimeFormatter.ofPattern("yyyy-MM-d");
            LocalDate datetime = LocalDate.parse(date, dateFormatToDate);
            String format = datetime.format(dateFormatToDate2);
            LocalDate parse = LocalDate.parse(format, dateFormatToDate2);
            String dateString = DateTimeFormatter.RFC_1123_DATE_TIME.format(parse.atStartOfDay(ZoneId.systemDefault()));
            return dateString.substring(0, 3);
        } else if (mont<10) {
            DateTimeFormatter dateFormatToDate = DateTimeFormatter.ofPattern("yyyy-M-dd");
            DateTimeFormatter dateFormatToDate2 = DateTimeFormatter.ofPattern("yyyy-M-dd");
            LocalDate datetime = LocalDate.parse(date, dateFormatToDate);
            String format = datetime.format(dateFormatToDate2);
            LocalDate parse = LocalDate.parse(format, dateFormatToDate2);
            String dateString = DateTimeFormatter.RFC_1123_DATE_TIME.format(parse.atStartOfDay(ZoneId.systemDefault()));
            return dateString.substring(0, 3);

        } else if (mont>10 && day>10) {
            DateTimeFormatter dateFormatToDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dateFormatToDate2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate datetime = LocalDate.parse(date, dateFormatToDate);
            String format = datetime.format(dateFormatToDate2);
            LocalDate parse = LocalDate.parse(format, dateFormatToDate2);
            String dateString = DateTimeFormatter.RFC_1123_DATE_TIME.format(parse.atStartOfDay(ZoneId.systemDefault()));
            return dateString.substring(0, 3);
        }else {
            DateTimeFormatter dateFormatToDate = DateTimeFormatter.ofPattern("yyyy-M-d");
            DateTimeFormatter dateFormatToDate2 = DateTimeFormatter.ofPattern("yyyy-M-d");
            LocalDate datetime = LocalDate.parse(date, dateFormatToDate);
            String format = datetime.format(dateFormatToDate2);
            LocalDate parse = LocalDate.parse(format, dateFormatToDate2);
            String dateString = DateTimeFormatter.RFC_1123_DATE_TIME.format(parse.atStartOfDay(ZoneId.systemDefault()));
            return dateString.substring(0, 3);
        }
    }

    private Reservation validCreateReservation(CreateReservationRequest request, Long userId, Long restaurantId){
        Restaurant restaurant = restaurantService.findAndEnsureExist(restaurantId);
        String nameOfDay = scheduleOfDayName(request);
        String schedule = scheduleRestaurant(restaurant).get(nameOfDay);
        if(!Objects.equals(schedule, "cerrado-cerrado")){

            LocalTime firstHour = firstHourSchedule(request, restaurant);
            LocalTime secondHour = secondHourSchedule(request, restaurant);
            LocalTime hourReservation = hourReservation(request);
            LocalDate dateReservation = dateOfReservation(request);

            if(dateReservation.isEqual(getLocalDate()) && hourReservation.equals(getLocalHour())
                    || dateReservation.isEqual(getLocalDate()) && hourReservation.equals(firstHour)
                    || dateReservation.isEqual(getLocalDate()) && hourReservation.isAfter(getLocalHour())
                    && hourReservation.isAfter(firstHour) && hourReservation.isBefore(secondHour)){

                if(hourReservation.isBefore(firstHour) && getLocalHour().equals(hourReservation)
                        || hourReservation.isBefore(firstHour) && hourReservation.isAfter(getLocalHour())
                        || hourReservation.isBefore(getLocalHour())
                        || hourReservation.isAfter(getLocalHour()) && hourReservation.isBefore(firstHour)
                        || hourReservation.isAfter(secondHour)){
                    throw new InvalidDataReservation();
                }else {
                    int sumTotalTableNumberOfReservations = restaurant.getReservations()
                            .stream()
                            .filter(reservation -> Objects.equals(reservation.getDate(), request.getDate()) && Objects.equals(reservation.getStatus(), "reserved"))
                            .mapToInt(Reservation::getTableNumber)
                            .sum();

                    int tablesAvailable = restaurant.getTableNumber() - sumTotalTableNumberOfReservations;
                    Integer tableNumberReservation = assignNumberOfTables(request, restaurant);
                    if(tablesAvailable==0 || tablesAvailable<0){
                        throw new InvalidDataReservation();
                    }else if(tableNumberReservation <= tablesAvailable){
                        String status = "reserved";
                        return from(request, userId, restaurantId, status, tableNumberReservation);
                    }else{
                        throw new InvalidDataReservation();
                    }
                }
            }else if(dateReservation.isAfter(getLocalDate()) && hourReservation.equals(firstHour)
                    || dateReservation.isAfter(getLocalDate()) && hourReservation.isAfter(firstHour) && hourReservation.isBefore(secondHour)){

                int sumTotalTableNumberOfReservations = restaurant.getReservations()
                        .stream()
                        .filter(reservation -> Objects.equals(reservation.getDate(), request.getDate()) && Objects.equals(reservation.getStatus(), "reserved"))
                        .mapToInt(Reservation::getTableNumber)
                        .sum();

                int tablesAvailable = restaurant.getTableNumber() - sumTotalTableNumberOfReservations;
                Integer tableNumberReservation = assignNumberOfTables(request, restaurant);
                if(tablesAvailable==0 || tablesAvailable<0){
                    throw new InvalidDataReservation();
                }else if(tableNumberReservation <= tablesAvailable){
                    String status = "reserved";
                    return from(request, userId, restaurantId, status, tableNumberReservation);
                }else{
                    throw new InvalidDataReservation();
                }

            }else{
                throw new InvalidDataReservation();
            }

        }else{
            throw new InvalidDataReservation();
        }
    }

    private LocalTime hourReservation(CreateReservationRequest request){
        String hourReserved = request.getHour().substring(0, 5);
        String hour = hourReserved.substring(0, 2);
        int hourFirs = Integer.parseInt(hour);
        String minute = hourReserved.substring(3, 5);
        int minuteFirs = Integer.parseInt(minute);
        String hourInitial = hourFirs + ":" + minuteFirs;

        if (hourFirs<10 && minuteFirs<10){
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("H:m");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime firstHourTransformed = LocalTime.parse(hourInitial,FOMATTER2);
            String format = firstHourTransformed.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        }else if(hourFirs<10){
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("H:mm");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime firstHourTransformed = LocalTime.parse(hourInitial,FOMATTER2);
            String format = firstHourTransformed.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        } else if (minuteFirs<10) {
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("HH:m");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime firstHourTransformed = LocalTime.parse(hourInitial,FOMATTER2);
            String format = firstHourTransformed.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        }else {
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("HH:mm");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime firstHourTransformed = LocalTime.parse(hourInitial,FOMATTER2);
            String format = firstHourTransformed.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        }
    }

    private LocalTime firstHourSchedule(CreateReservationRequest request, Restaurant restaurant) {
        String nameOfDay = scheduleOfDayName(request);
        String schedule = scheduleRestaurant(restaurant).get(nameOfDay);
        if (Objects.equals(schedule, "close")) {
            throw new InvalidDataReservation();
        }
        String firstHour = schedule.substring(0, 5);
        String hour = firstHour.substring(0, 2);
        int hourFirs = Integer.parseInt(hour);
        String minute = firstHour.substring(3, 5);
        int minuteFirs = Integer.parseInt(minute);
        String hourInitial = hourFirs + ":" + minuteFirs;

        if (hourFirs<10 && minuteFirs<10){
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("H:m");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime firstHourTransformed = LocalTime.parse(hourInitial,FOMATTER2);
            String format = firstHourTransformed.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        }else if(hourFirs<10){
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("H:mm");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime firstHourTransformed = LocalTime.parse(hourInitial,FOMATTER2);
            String format = firstHourTransformed.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        } else if (minuteFirs<10) {
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("HH:m");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime firstHourTransformed = LocalTime.parse(hourInitial,FOMATTER2);
            String format = firstHourTransformed.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        }else {
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("HH:mm");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime firstHourTransformed = LocalTime.parse(hourInitial,FOMATTER2);
            String format = firstHourTransformed.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        }
    }

    private LocalTime secondHourSchedule(CreateReservationRequest request, Restaurant restaurant) {
        String nameOfDay = scheduleOfDayName(request);
        String schedule = scheduleRestaurant(restaurant).get(nameOfDay);
        if (Objects.equals(schedule, "close")) {
            throw new InvalidDataReservation();
        }
        String secondHour = schedule.substring(6, 11);
        String hour = secondHour.substring(0, 2);
        int hourFirs = Integer.parseInt(hour);
        String minute = secondHour.substring(3, 5);
        int minuteFirs = Integer.parseInt(minute);
        String hourFinal = hourFirs + ":" + minuteFirs;

        if (hourFirs<10 && minuteFirs<10){
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("H:m");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime firstHourTransformed = LocalTime.parse(hourFinal,FOMATTER2);
            String format = firstHourTransformed.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        }else if(hourFirs<10){
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("H:mm");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime firstHourTransformed = LocalTime.parse(hourFinal,FOMATTER2);
            String format = firstHourTransformed.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        } else if (minuteFirs<10) {
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("HH:m");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime firstHourTransformed = LocalTime.parse(hourFinal,FOMATTER2);
            String format = firstHourTransformed.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        }else {
            DateTimeFormatter FOMATTER2 = DateTimeFormatter.ofPattern("HH:mm");
            DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime firstHourTransformed = LocalTime.parse(hourFinal,FOMATTER2);
            String format = firstHourTransformed.format(FOMATTER);
            return LocalTime.parse(format,FOMATTER);
        }
    }

    private Integer assignNumberOfTables(CreateReservationRequest request, Restaurant restaurant){
        int i;
        int tableNumber = 0;
        List<Integer> integerList = new ArrayList<>();
        int j = 0;
        for (i=1;i<=restaurant.getTableNumber();i++){
            j = j+restaurant.getTableCapacity();
            integerList.add(j);
        }
        for (i=0;i<integerList.size();i++){
            if(request.getPeople() <= integerList.get(i)) {
                tableNumber=i+1;
                break;
            }
        }
        return tableNumber;
    }

    private Map<String, String> scheduleRestaurant(Restaurant restaurant){
        String[] week = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        String[] scheduleArray = restaurant.getSchedule().split(",");
        Map<String, String> schedule = new HashMap<>();
        for (int i = 0; i < week.length; i++) {
            schedule.put(week[i],scheduleArray[i]);
        }
        return schedule;
    }

    private Reservation from(CreateReservationRequest request, Long userId, Long restaurantId, String status, Integer tableNumber){
        Reservation reservation = new Reservation();
        reservation.setDate(request.getDate());
        reservation.setHour(request.getHour());
        reservation.setStatus(status);
        reservation.setTableNumber(tableNumber);
        reservation.setPeople(request.getPeople());
        reservation.setUser(userService.findAndEnsureExists(userId));
        reservation.setRestaurant(restaurantService.findAndEnsureExist(restaurantId));
        return reservation;
    }

    private CreateReservationResponse from(Reservation reservation){
        return CreateReservationResponse.builder()
                .id(reservation.getId())
                .date(reservation.getDate())
                .hour(reservation.getHour())
                .people(reservation.getPeople())
                .status(reservation.getStatus())
                .tableNumber(reservation.getTableNumber())
                .restaurant(restaurantService.fromRestaurantToRestaurantResponse(reservation.getRestaurant()))
                .user(userService.fromUserToUserResponse(reservation.getUser()))
                .build();
    }

    private Reservation validationUpdateDateReservation(UpdateReservationRequest request, Long id){
        Reservation reservation = findAndEnsureExist(id);
        if(request.getDate().length() == 0 || request.getDate() == null || Objects.equals(request.getDate(), "")) {
            reservation.setDate(reservation.getDate());
        }else {
            reservation.setDate(request.getDate());
        }
        if(request.getHour().length() == 0 || request.getHour() == null || Objects.equals(request.getHour(), "")) {
            reservation.setHour(reservation.getHour());
        }else {
            reservation.setHour(request.getHour());
        }
        if(request.getPeople() == null || request.getPeople() == 0) {
            reservation.setPeople(reservation.getPeople());
        }else {
            reservation.setPeople(request.getPeople());
        }
        return reservation;
    }

    private UpdateReservationResponse fromReservationToUpdateReservationResponse(Reservation reservation){
        return UpdateReservationResponse.builder()
                .id(reservation.getId())
                .date(reservation.getDate())
                .hour(reservation.getHour())
                .people(reservation.getPeople())
                .user(userService.fromUserToUserResponse(reservation.getUser()))
                .restaurant(restaurantService.fromRestaurantToRestaurantResponse(reservation.getRestaurant())).build();
    }

    @Override
    public ReservationResponse fromReservationToReservationResponse(Reservation reservation){
        return ReservationResponse.builder()
                .people(reservation.getPeople())
                .date(reservation.getDate())
                .id(reservation.getId()).build();
    }

}
