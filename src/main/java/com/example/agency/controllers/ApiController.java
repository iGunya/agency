package com.example.agency.controllers;

import com.example.agency.configs.jwt.JwtUtils;
import com.example.agency.dto.InputObjectDto;
import com.example.agency.dto.JwtResponse;
import com.example.agency.dto.LoginRequest;
import com.example.agency.dto.UserDetailsImpl;
import com.example.agency.entities.Object;
import com.example.agency.repositories.ObjectRepository;
import com.example.agency.services.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ApiController {
    @Autowired
    private ObjectService objectService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/auth/signin")
    public ResponseEntity<?> authUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                userDetails.getId(),
                jwt,
                userDetails.getUsername(),
                roles.get(0)));
    }

    @GetMapping
    public List<InputObjectDto> allObject(){
        Specification<Object> filter = Specification.where(null);

        return objectService.getApiObject(filter);
    }
    @GetMapping("/{id}")
    public void getObgect(){

    }
    @GetMapping("/{id}/photos")
    public void getObgectPhoto(){

    }
    @GetMapping("/like/{id_user}}")
    public void getLikeObgect(){

    }
}
