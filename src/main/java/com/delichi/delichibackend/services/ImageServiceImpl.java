package com.delichi.delichibackend.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.delichi.delichibackend.controllers.dtos.responses.BaseResponse;
import com.delichi.delichibackend.controllers.dtos.responses.GetImageResponse;
import com.delichi.delichibackend.entities.Image;
import com.delichi.delichibackend.entities.Restaurant;
import com.delichi.delichibackend.entities.exceptions.ExistingDataConflictException;
import com.delichi.delichibackend.entities.exceptions.InternalServerError;
import com.delichi.delichibackend.entities.exceptions.NotFoundException;
import com.delichi.delichibackend.entities.exceptions.NotValidException;
import com.delichi.delichibackend.repositories.IImageRepository;
import com.delichi.delichibackend.services.interfaces.ICeoService;
import com.delichi.delichibackend.services.interfaces.IImageService;
import com.delichi.delichibackend.services.interfaces.IRestaurantService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements IImageService {

    @Autowired
    private ICeoService ceoService;

    @Autowired
    private IRestaurantService restaurantService;

    @Autowired
    private IImageRepository repository;

    private AmazonS3 s3client;

    private String ENDPOINT_URL = "s3.us-east-2.amazonaws.com";

    private String BUCKET_NAME = "delichi";

    private String ACCESS_KEY = "AKIATH7APQJ7TP5UMR3Z";

    private String SECRET_KEY = "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";

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2)
                .build();
    }

    @Override
    public BaseResponse GetLogoImageByRestaurantId(Long idRestaurant) {
        Image logoImage = repository.GetLogoImageByRestaurantId(idRestaurant)
                .orElseThrow(NotFoundException::new);
        try{
            return BaseResponse.builder()
                    .data(from(logoImage))
                    .message("Logo Of Restaurant")
                    .success(Boolean.TRUE)
                    .httpStatus(HttpStatus.OK)
                    .build();
        }catch (Error e){
            throw new InternalServerError();
        }
    }

    @Override
    public BaseResponse GetBannerImageByRestaurantId(Long idRestaurant) {
        Image bannerImage = repository.GetBannerImageByRestaurantId(idRestaurant)
                .orElseThrow(NotFoundException::new);
        return BaseResponse.builder()
                .data(from(bannerImage))
                .message("Banner Of Restaurant")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse listAllImagesByRestaurantId(Long restaurantId) {
        return BaseResponse.builder()
                .data(getImageResponseList(restaurantId))
                .message("List All Images By Restaurant")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public BaseResponse uploadRestaurantImages(MultipartFile multipartFile, Long ceoId, Long restaurantId) {
        String images = "images";
        String urlDirectionImage = "/images/restaurantImages/";
        if (Objects.equals(images, "images")){
            return uploadImage(multipartFile, ceoId, restaurantId, images, urlDirectionImage);
        }
        throw new ExistingDataConflictException();
    }

    @Override
    public BaseResponse uploadRestaurantLogoImage(MultipartFile multipartFile, Long ceoId, Long restaurantId) {
        String images = "logo";
        String urlDirectionImage = "/images/logo/";
        if(ValidateNumberOfLogos(restaurantId)){
            throw new ExistingDataConflictException();
        }
        return uploadImage(multipartFile, ceoId, restaurantId, images, urlDirectionImage);
    }

    @Override
    public BaseResponse uploadRestaurantBannerImage(MultipartFile multipartFile, Long ceoId, Long restaurantId) {
        String images = "banner";
        String urlDirectionImage = "/images/banner/";
        if(ValidateNumberOfBanners(restaurantId)){
            throw new ExistingDataConflictException();
        }
        return uploadImage(multipartFile, ceoId, restaurantId, images, urlDirectionImage);
    }

    @Override
    public BaseResponse UpdateRestaurantLogo(MultipartFile multipartFile, Long restaurantId, Long ceoId) {
        String images = "logo";
        String urlDirectionImage = "/images/logo/";
        return updateImage(multipartFile, ceoId, restaurantId, images, urlDirectionImage);
    }

    @Override
    public BaseResponse UpdateRestaurantBanner(MultipartFile multipartFile, Long restaurantId, Long ceoId) {
        String images = "banner";
        String urlDirectionImage = "/images/banner/";
        return updateImage(multipartFile, ceoId, restaurantId, images, urlDirectionImage);
    }

    @Override
    public BaseResponse DeleteImage(Long idImage) {
        repository.delete(FindImageAndEnsureExist(idImage));
        return BaseResponse.builder()
                .message("Image Deleted Correctly")
                .success(Boolean.TRUE)
                .httpStatus(HttpStatus.OK).build();
    }

    private GetImageResponse from(Image image){
        return GetImageResponse.builder()
                .id(image.getId())
                .imageType(image.getImageType())
                .fileUrl(image.getFileUrl()).build();
    }

    private List<GetImageResponse> getImageResponseList(Long restaurantId){
        return imageListByRestaurantId(restaurantId)
                .stream()
                .map(this::from)
                .collect(Collectors.toList());
    }

    private List<Image> imageListByRestaurantId(Long restaurantId){
        return restaurantService.findAndEnsureExist(restaurantId).getImages();
    }

    private BaseResponse uploadImage(MultipartFile multipartFile, Long ceoId, Long restaurantId,String images, String urlDirectionImage){
        String urlDirection;
        if(ValidateFileExtension(multipartFile)){
            try {
                urlDirection = "data/businessInformation/ceo/" + ceoService.findAndEnsureExist(ceoId).getEmail()
                        + "/properties/ceoRestaurants/" + replaceRestaurantName(restaurantService.findAndEnsureExist(restaurantId))
                        + urlDirectionImage;
                uploadFileToS3Bucket(filePath(urlDirection, multipartFile), convertMultiPartToFile(multipartFile));
                repository.save(createImageNew(fileURL(urlDirection,multipartFile), restaurantId, multipartFile, ceoId, images));
                convertMultiPartToFile(multipartFile).delete();
                return BaseResponse.builder()
                        .data(fileURL(urlDirection,multipartFile))
                        .message("Image Uploaded Correctly")
                        .success(Boolean.TRUE)
                        .httpStatus(HttpStatus.OK)
                        .build();
            } catch (IOException e) {
                throw new InternalServerError();
            }
        }else {
            throw new NotValidException();
        }
    }

    private BaseResponse updateImage(MultipartFile multipartFile, Long ceoId, Long restaurantId,String images, String urlDirectionImage){
        String urlDirection;
        if(ValidateFileExtension(multipartFile)){
            try {
                urlDirection = "data/businessInformation/ceo/" + ceoService.findAndEnsureExist(ceoId).getEmail()
                        + "/properties/ceoRestaurants/" + replaceRestaurantName(restaurantService.findAndEnsureExist(restaurantId))
                        + urlDirectionImage;
                uploadFileToS3Bucket(filePath(urlDirection, multipartFile), convertMultiPartToFile(multipartFile));
                repository.save(findAndCreateImageNew(fileURL(urlDirection,multipartFile), restaurantId, multipartFile, ceoId, images));
                convertMultiPartToFile(multipartFile).delete();
                return BaseResponse.builder()
                        .data(fileURL(urlDirection,multipartFile))
                        .message("Image Updated Correctly")
                        .success(Boolean.TRUE)
                        .httpStatus(HttpStatus.OK)
                        .build();
            } catch (IOException e) {
                throw new InternalServerError();
            }
        }else {
            throw new NotValidException();
        }
    }

    private Image FindImageAndEnsureExist(Long id){
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    private Boolean ValidateNumberOfLogos(Long idRestaurant){
        return repository.GetLogoImageByRestaurantId(idRestaurant).isPresent();
    }

    private Boolean ValidateNumberOfBanners(Long idRestaurant){
        return repository.GetBannerImageByRestaurantId(idRestaurant).isPresent();
    }

    private Boolean ValidateFileExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return (Objects.equals(extension, "jpeg"))
                || Objects.equals(extension, "jpg")
                || Objects.equals(extension, "png")
                ? Boolean.TRUE : Boolean.FALSE;
    }

    private void uploadFileToS3Bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(BUCKET_NAME, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private String replaceRestaurantName(Restaurant restaurant){
        return restaurant.getName().replace(" ","_");
    }

    private String filePath(String urlDirection, MultipartFile multipartFile){
        return urlDirection + generateFileName(multipartFile);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String fileURL(String urlDirection, MultipartFile multipartFile){
        return "https://" + BUCKET_NAME + "." + ENDPOINT_URL + "/" + filePath(urlDirection, multipartFile);
    }

    private Image createImageNew(String fileUrl, Long restaurantId, MultipartFile multipartFile, Long ceoId, String images){
        Image image = new Image();
        image.setFileUrl(fileUrl);
        image.setRestaurant(restaurantService.findAndEnsureExist(restaurantId));
        image.setName(generateFileName(multipartFile));
        image.setImageType(images);
        image.setCeo(ceoService.findAndEnsureExist(ceoId));
        return image;
    }

    private Image findAndCreateImageNew(String fileUrl, Long restaurantId, MultipartFile multipartFile, Long ceoId, String images){
        Image newImage = repository.GetLogoImageByRestaurantId(restaurantId)
                .orElseThrow(NotFoundException::new);
        if(images.equals("banner")){
            newImage = repository.GetBannerImageByRestaurantId(restaurantId)
                    .orElseThrow(NotFoundException::new);
        }
        newImage.setFileUrl(fileUrl);
        newImage.setRestaurant(restaurantService.findAndEnsureExist(restaurantId));
        newImage.setName(generateFileName(multipartFile));
        newImage.setImageType(images);
        newImage.setCeo(ceoService.findAndEnsureExist(ceoId));
        return newImage;
    }

    private String generateFileName(MultipartFile multiPart) {
        return multiPart.getOriginalFilename().replace(" ", "_");
    }

    public void deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(BUCKET_NAME, fileName));
    }

}