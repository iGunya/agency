package com.example.agency.services;

import com.example.agency.dto.UserDetailsImpl;
import com.example.agency.entities.User;
import com.example.agency.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
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

    public void save(User user) {
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void updateRole(User user){
        userRepository.updateRole(user.getRole(), user.getLogin(), user.getId_user());
    }

    public User findUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);
        if (user == null){
            throw new UsernameNotFoundException("Пльзователь с таким именем не найден");
        }
        return user;
    }

    public List<User> findAllUser(){
        return userRepository.findAll();
    }

    public User findByLogin(String login){
        return userRepository.findByLogin(login);
    }

    @Autowired
    public UserService(UserRepository userRepository,
    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
    }
}
