package com.al.agency.controllers;

import com.al.agency.configs.jwt.JwtUtils;
import com.al.agency.dto.JwtResponse;
import com.al.agency.dto.LoginRequest;
import com.al.agency.dto.ObjectDto;
import com.al.agency.dto.UserDetailsImpl;
import com.al.agency.entities.Object;
import com.al.agency.entities.Photo;
import com.al.agency.services.ObjectService;
import com.al.agency.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
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
    private UserService userService;
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

    @GetMapping("/objects")
    public List<ObjectDto> allObject(@RequestParam(value = "countRoom",required = false) String countRoom,
                                     @RequestParam(value = "maxPrice",required = false) String maxPrice,
                                     @RequestParam(value = "minPrice",required = false) String minPrice,
                                     @RequestParam(value = "typeObject",required = false) String typeObject,
                                     @RequestParam(value = "typeMove",required = false) String typeMove){

        Specification<Object> filter = objectService.createSpecificationForObjects(
                countRoom, maxPrice, minPrice, typeObject, typeMove
        );

        return objectService.getForApiObjects(filter);
    }
    @GetMapping("/objects/{id}")
    public ObjectDto getObject(@PathVariable Long id){
        return objectService.getObjectDtoById(id);
    }

    @GetMapping("/objects/{id}/photos")
    public List<Photo> getObjectPhoto(@PathVariable Long id){
        return objectService.getObjectById(id).getPhotos();
    }

    @GetMapping("/like")
    public List<ObjectDto> getLikeObject(Principal principal){
        String username = principal.getName();
        return userService.getLikeObjectDtoByUsername(username);
    }

    @GetMapping("/like/add/{id_object}")
    public ResponseEntity<String> addLikeObject(@PathVariable Long id_object,
                                                Principal principal){
        String username = principal.getName();
        if (userService.addLikeObject(id_object,username))
            return ResponseEntity.status(HttpStatus.OK).body("Запись добавленна");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не найден объект");
    }
}
