package com.al.agency.unit.service;

import com.al.agency.dto.ObjectDto;
import com.al.agency.entities.Object;
import com.al.agency.entities.TypeMove;
import com.al.agency.entities.TypeObject;
import com.al.agency.repositories.ObjectRepository;
import com.al.agency.repositories.PhotoRepository;
import com.al.agency.repositories.TypeMoveRepository;
import com.al.agency.repositories.TypeObjectRepository;
import com.al.agency.services.ObjectService;
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
public class ObjectApiServiceTest {
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
    public void testAllObjectForApi(){
        List<Object> list = new ArrayList<>();
        Object object = createObject();
        list.add(object);
        Specification<Object> specification = Specification.where(null);

        Mockito.doReturn(list).when(objectRepository).findAll(specification);

        List<ObjectDto> response = objectService.getForApiObjects(specification);
        Assert.assertEquals(1,response.size());
        Assert.assertEquals("Квартира", response.get(0).getTypeObject());
        Assert.assertEquals("Продажа", response.get(0).getTypeMove());
    }

    @Test
    public void testObjectForApiById(){
        Object object = createObject();

        Mockito.doReturn(Optional.of(object)).when(objectRepository).findById(Mockito.anyLong());

        ObjectDto response = objectService.getObjectDtoById(1L);
        Assert.assertEquals("Квартира", response.getTypeObject());
        Assert.assertEquals("Продажа", response.getTypeMove());
    }


    public static Object createObject(){
        TypeMove typeMove  = new TypeMove();
        typeMove.setTypeMove("Продажа");
        TypeObject typeObject = new TypeObject();
        typeObject.setTypeObject("Квартира");

        Object object = new Object();
        object.setAdress("Район Улица Дом");
        object.setSquare("-1/34/5");
        object.setCountRoom(1);
        object.setCountFloor(1);
        object.setPrice(15000000L);
        object.setRealPrice(1300000L);
        object.setDescription("asdas");
        object.setTypeMove(typeMove);
        object.setTypeObject(typeObject);
        return object;
    }
}
