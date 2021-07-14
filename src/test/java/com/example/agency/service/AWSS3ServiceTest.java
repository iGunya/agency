package com.example.agency.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.agency.services.AWSS3ServiceImp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class AWSS3ServiceTest {
    @InjectMocks
    private AWSS3ServiceImp awss3ServiceImp;
    @Mock
    private AmazonS3 amazonS3;


    @Test
    public void testUploadFileAWS() throws IOException {
        MultipartFile multipartFile = Mockito.mock(MockMultipartFile.class);

        Mockito.when(multipartFile.getBytes()).thenReturn("".getBytes());
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("test");

        String newName = awss3ServiceImp.uploadFile(multipartFile);

        Mockito.verify(amazonS3,
                Mockito.times(1)).putObject(Mockito.any());
        Assert.assertNotNull(newName);
    }
}
