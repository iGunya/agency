package com.example.agency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SingUpRequest {
    private String login;
    private String role;
}
