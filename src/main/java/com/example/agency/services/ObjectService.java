package com.example.agency.services;

import com.example.agency.dto.InputObjectDto;
import com.example.agency.entities.Object;
import com.example.agency.entities.Photo;
import com.example.agency.entities.TypeMove;
import com.example.agency.entities.TypeObject;
import com.example.agency.repositories.ObjectRepository;
import com.example.agency.repositories.PhotoRepository;
import com.example.agency.repositories.TypeMoveRepository;
import com.example.agency.repositories.TypeObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjectService {
    ObjectRepository objectRepository;
    TypeMoveRepository typeMoveRepository;
    TypeObjectRepository typeObjectRepository;
    PhotoRepository photoRepository;

    public List<Object> getAllObject(){
        return objectRepository.findAll();
    }

    public  void createObject(InputObjectDto objectDto,String savePhotoName){
       Object object = new Object(objectDto);

        TypeObject daTypeObject = typeObjectRepository.findByTypeObject(objectDto.getTypeObject());

        object.setTypeObject(daTypeObject);
        Photo  photo= new Photo();
        photo.setURL_photo(savePhotoName);
        object.setTypeMove(typeMoveRepository.findByTypeMove(objectDto.getTypeMove()));
        Object object1 = objectRepository.save(object);
        photo.setObjects(object1);
        photoRepository.save(photo);
    }

    public List<TypeMove> allTypeMove(){
        return typeMoveRepository.findAll();
    }

    public List<TypeObject> allTypeObject(){
        return typeObjectRepository.findAll();
    }

    @Autowired

    public ObjectService(ObjectRepository objectRepository,
                         TypeMoveRepository typeMoveRepository,
                         TypeObjectRepository typeObjectRepository,
                         PhotoRepository photoRepository) {
        this.objectRepository = objectRepository;
        this.typeMoveRepository = typeMoveRepository;
        this.typeObjectRepository = typeObjectRepository;
        this.photoRepository = photoRepository;
    }
}
