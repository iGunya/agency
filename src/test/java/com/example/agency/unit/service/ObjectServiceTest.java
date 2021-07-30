package com.example.agency.unit.service;

import com.example.agency.dto.ObjectDto;
import com.example.agency.entities.Object;
import com.example.agency.repositories.*;
import com.example.agency.services.ObjectService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        ObjectDto saveObject = Mockito.mock(ObjectDto.class);

        Object object = Mockito.mock(Object.class);
        List<String> NAME_PHOTO= new ArrayList<>();
        NAME_PHOTO.add("5872472.jpg");
        NAME_PHOTO.add("5872472.jpg");

        Mockito.doReturn(1L).when(saveObject).getIdObject();
        Mockito.doReturn(Optional.of(object)).when(objectRepository).findById(Mockito.anyLong());

        objectService.createObjectAndSavePhotos(saveObject,NAME_PHOTO);

        Mockito.verify(typeObjectRepository,
                Mockito.times(1)).findByTypeObject(saveObject.getTypeObject());
        Mockito.verify(typeMoveRepository,
                Mockito.times(1)).findByTypeMove(saveObject.getTypeMove());
        Mockito.verify(objectRepository,
                Mockito.times(1)).save(object);
    }

    @Test
    public void testSavePhoto(){
        ObjectDto saveObject = Mockito.mock(ObjectDto.class);

        Object object = Mockito.mock(Object.class);
        List<String> NAME_PHOTO= new ArrayList<>();
        NAME_PHOTO.add("5872472.jpg");
        NAME_PHOTO.add("5872472.jpg");

        Mockito.doReturn(1L).when(saveObject).getIdObject();
        Mockito.doReturn(Optional.of(object)).when(objectRepository).findById(Mockito.anyLong());

        objectService.createObjectAndSavePhotos(saveObject,NAME_PHOTO);

        Mockito.verify(object,
                Mockito.times(2)).getPhotos();
    }

    @Test
    public void testCreateFilterSearch(){
        String countRoom = "2";
        String maxPrice = null;
        String minPrice = Integer.toString(1_000_000);
        String typeObject = null;
        String typeMove = null;

        Specification<Object> filter = objectService.createSpecificationForObjects(countRoom,maxPrice, minPrice,typeObject,typeMove);

        Assert.assertNotNull(filter);
    }
}
