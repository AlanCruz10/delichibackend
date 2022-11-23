package com.delichi.delichibackend.services.interfaces;

import com.delichi.delichibackend.controllers.dtos.responses.BaseResponse;
import com.delichi.delichibackend.controllers.dtos.responses.GetImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    BaseResponse GetLogoImageByRestaurantId(Long idRestaurant);

    BaseResponse GetBannerImageByRestaurantId(Long idRestaurant);

    BaseResponse listAllImagesByRestaurantId(Long idRestaurant);

    BaseResponse uploadRestaurantImages(MultipartFile multipartFile, Long idCeo, Long idRestaurant);

    BaseResponse uploadRestaurantLogoImage(MultipartFile multipartFile, Long idCeo, Long idRestaurant);

    BaseResponse uploadRestaurantBannerImage(MultipartFile multipartFile, Long idCeo, Long idRestaurant);

    BaseResponse UpdateRestaurantLogo(MultipartFile multipartFile, Long idRestaurant, Long idCeo);

    BaseResponse UpdateRestaurantBanner(MultipartFile multipartFile, Long idRestaurant, Long idCeo);

    BaseResponse DeleteImage(Long idImage);

}