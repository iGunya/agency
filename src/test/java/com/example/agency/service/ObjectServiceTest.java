package com.example.agency.service;

import com.example.agency.dto.InputObjectDto;
import com.example.agency.entities.Object;
import com.example.agency.entities.Photo;
import com.example.agency.repositories.*;
import com.example.agency.services.ObjectService;
import com.example.agency.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ObjectServiceTest {
    @InjectMocks
    private ObjectService objectService;
    @Mock
    private TypeMoveRepository typeMoveRepository;
    @Mock
    private TypeObjectRepository typeObjectRepository;
    @Mock
    private PhotoRepository photoRepository;
    @Mock
    private ObjectRepository objectRepository;

    @Test
    public void testSaveObject(){
        InputObjectDto object = new InputObjectDto();
        object.setAdress("Где-то");
        object.setSquare("-1/60/15/18/5");
        object.setCountFloor(1);
        object.setCountRoom(2);
        object.setPrice("500000");
        object.setRealPrice("480000");
        object.setTypeObject("Квартира");
        object.setTypeMove("Продажа");

        Object saveObgect =new Object(object);

        String NAME_PHOTO="5872472.jpg";
        Photo savePhoto = new Photo();
        savePhoto.setURL_photo(NAME_PHOTO);

        objectService.createObject(object,NAME_PHOTO);

        Mockito.verify(typeObjectRepository,
                Mockito.times(1)).findByTypeObject(object.getTypeObject());
        Mockito.verify(typeMoveRepository,
                Mockito.times(1)).findByTypeMove(object.getTypeMove());
        Mockito.verify(objectRepository,
                Mockito.times(1)).save(saveObgect);
        Mockito.verify(photoRepository,
                Mockito.times(1)).save(savePhoto);
    }
}
