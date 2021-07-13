package com.example.agency.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.agency.services.AWSS3ServiceImp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
        String bucketName=null;

        File file =new File("test.jpg");
        FileInputStream stream = new FileInputStream(file);

        MultipartFile multipartFile = new MockMultipartFile("file",
                "test.jpg", String.valueOf(MediaType.IMAGE_PNG),stream);

        String newName = awss3ServiceImp.uploadFile(multipartFile);
        Assert.assertNotNull(newName);
    }
}
