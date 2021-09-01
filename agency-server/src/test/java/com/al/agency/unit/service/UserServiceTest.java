package com.al.agency.unit.service;

import com.al.agency.entities.User;
import com.al.agency.repositories.UserRepository;
import com.al.agency.services.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testSaveUser(){
        User user = new User();

        userService.saveNewUser(user);

        Assert.assertEquals("ROLE_USER", user.getRole());
        Mockito.verify(userRepository,Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void testLoadUser(){
        String findLogin="user";

        Mockito.when(userRepository.findByLogin(findLogin)).thenReturn(new User(1L,"user","","ROLE_USER"));

        UserDetails securityUser = userService.loadUserByUsername(findLogin);

        Assert.assertEquals("user", securityUser.getUsername());

        String role = securityUser.getAuthorities().toString();
        Assert.assertEquals("[ROLE_USER]", role);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testNullLoadUser(){
        String findNullLogin="user";

        userService.loadUserByUsername(findNullLogin);

        Mockito.when(userRepository.findByLogin(findNullLogin)).thenReturn(null);
    }
}
