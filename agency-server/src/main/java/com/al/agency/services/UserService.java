package com.al.agency.services;

import com.al.agency.dto.ObjectDto;
import com.al.agency.dto.UserDetailsImpl;
import com.al.agency.entities.Object;
import com.al.agency.entities.User;
import com.al.agency.repositories.ObjectRepository;
import com.al.agency.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private ObjectRepository objectRepository;
    private PasswordEncoder passwordEncoder;

    //по имени пользователя вернуть самого пользователя для аунтификации
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);
        if (user == null){
            throw new UsernameNotFoundException("Пльзователь с таким именем не найден");
        }
        UserDetailsImpl principalUser = UserDetailsImpl.build(user);
        return principalUser;
    }

    public void saveNewUser(User user) {
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void updateRoleAndLogin(User user){
        userRepository.updateRoleAndLogin(user.getRole(), user.getLogin(), user.getId_user());
    }

    public User chekUserByUsername(String login) {
        return userRepository.findByLogin(login);
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public boolean addLikeObject(Long id_object, String username){
        User user = userRepository.findByLogin(username);
        Object object = objectRepository.findById(id_object).orElse(null);
        if (object == null){
            return false;
        }
        user.addObject(object);
        userRepository.save(user);
        return true;
    }

    public List<ObjectDto> getLikeObjectDtoByUsername(String username){
//        List<Object> objects = userRepository.findObjectEagerly(username);
//
//        return objects.stream().map(object -> {
//                    ObjectDto objectDto = new ObjectDto();
//                    objectDto.setObject(object);
//                    return objectDto;
//                })
//                .collect(Collectors.toList());

        User user = userRepository.findByLogin(username);
        return user.getObjects().stream().map(object -> {
                    ObjectDto objectDto = new ObjectDto();
                    objectDto.setObject(object);
                    return objectDto;
                })
                .collect(Collectors.toList());
    }

    @Autowired
    public UserService(UserRepository userRepository,
                       ObjectRepository objectRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.objectRepository = objectRepository;
        this.passwordEncoder = passwordEncoder;
    }
}
