package com.example.agency.unit.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.agency.services.AWSS3ServiceImp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

        List<String> newName = awss3ServiceImp.uploadFile(new MultipartFile[]{multipartFile});

        Mockito.verify(amazonS3,
                Mockito.times(1)).putObject(Mockito.any());
        Assert.assertNotNull(newName);
    }
}
