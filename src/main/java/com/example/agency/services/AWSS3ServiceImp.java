package com.example.agency.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class AWSS3ServiceImp implements AWSS3Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(AWSS3ServiceImp.class);

    @Autowired
    private AmazonS3 amazonS3;
    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    @Async
    public String uploadFile(MultipartFile multipartFile) {
        LOGGER.info("Файл в процессе загрузки");
        String saveFileName=null;
        try {
            File file = convertMultiPartFile(multipartFile);
            saveFileName = uploadFileToS3Bucket(bucketName, file);
            LOGGER.info("Файл отправлен");
            file.delete();
        }catch (AmazonServiceException e){
            LOGGER.info("Неудалось загрузить файл");
            LOGGER.error(e.getErrorMessage());
        }
        return saveFileName;
    }
    //переводим в изображение
    private File convertMultiPartFile(final MultipartFile multipartFile){
        File outputfile = new File(multipartFile.getOriginalFilename());

        try {
            FileOutputStream outputStream= new FileOutputStream(outputfile);
            BufferedOutputStream stream =
                    new BufferedOutputStream(outputStream);
            stream.write(multipartFile.getBytes());
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            bufferedImage = ImageIO.read(multipartFile.getInputStream());
//            outputfile = new File(multipartFile.getOriginalFilename());
//            ImageIO.write(bufferedImage, multipartFile.getContentType(), outputfile);
//        } catch (IOException e) {
//            LOGGER.error("Ошибка преобразования файла");
//        }
        return outputfile;
    }
    //оздаём уникально имя и сохраняем
    private String uploadFileToS3Bucket(String bucketName,File file){
        final String uniqueFileName = LocalDateTime.now()+"_"+file.getName();
        LOGGER.info("Уникальное имя фото : "+ uniqueFileName);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,uniqueFileName,file);
        amazonS3.putObject(putObjectRequest);
        return uniqueFileName;
    }

}
