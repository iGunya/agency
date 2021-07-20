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
        if(objectDto.getIdObject()!=null){
            Object object = objectRepository.getById(objectDto.getIdObject());
            object.setObjectDto(objectDto);
            object.setTypeObject(typeObjectRepository.findByTypeObject(objectDto.getTypeObject()));
            object.setTypeMove(typeMoveRepository.findByTypeMove(objectDto.getTypeMove()));

            Photo photo = new Photo();
            photo.setURL_photo(savePhotoName);
            object.getPhotos().add(photo);

            objectRepository.save(object);
            return;
        }

        Object object = new Object();
        object.setObjectDto(objectDto);
        object.setTypeObject(typeObjectRepository.findByTypeObject(objectDto.getTypeObject()));
        object.setTypeMove(typeMoveRepository.findByTypeMove(objectDto.getTypeMove()));

        Photo photo= new Photo();
        photo.setURL_photo(savePhotoName);
        object.getPhotos().add(photo);

        objectRepository.save(object);
    }

    public void delete(Long id){
        objectRepository.deleteById(id);
    }

    public Object getObjectById(Long id){
        return objectRepository.getById(id);
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
