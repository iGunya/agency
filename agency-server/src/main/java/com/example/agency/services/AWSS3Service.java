package com.example.agency.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AWSS3Service {
    List<String> uploadFile(MultipartFile[] multipartFile);
}
