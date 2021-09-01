package com.al.agency.unit.service;

import com.al.agency.dto.ObjectDto;
import com.al.agency.entities.Object;
import com.al.agency.entities.User;
import com.al.agency.repositories.ObjectRepository;
import com.al.agency.repositories.UserRepository;
import com.al.agency.services.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserApiServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectRepository objectRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testSuccessAddLikeObject(){
        User user = new User();
        Object object = Mockito.mock(Object.class);

        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(user);
        Mockito.when(objectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(object));

        Boolean response = userService.addLikeObject(1L,"user");

        Assert.assertTrue(response);
        Assert.assertEquals(1,user.getObjects().size());
        Mockito.verify(userRepository,Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void testBadAddLikeObject(){
        User user = new User();

        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(user);

        Boolean response = userService.addLikeObject(1L,"user");

        Assert.assertFalse(response);
        Mockito.verify(userRepository,Mockito.times(0)).save(Mockito.any(User.class));
    }

    @Test
    public void testGetLikeObject(){
        User user = new User();
        user.getObjects().add(ObjectApiServiceTest.createObject());

        Mockito.when(userRepository.findByLogin(Mockito.anyString())).thenReturn(user);

        List<ObjectDto> response = userService.getLikeObjectDtoByUsername("user");

        Assert.assertEquals(1,response.size());
        Assert.assertEquals("Квартира", response.get(0).getTypeObject());
        Assert.assertEquals("Продажа", response.get(0).getTypeMove());
    }
}
