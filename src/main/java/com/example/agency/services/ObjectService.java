package com.example.agency.services;

import com.example.agency.dto.ObjectDto;
import com.example.agency.entities.*;
import com.example.agency.entities.Object;
import com.example.agency.repositories.*;
import com.example.agency.repositories.specification.ObjectSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ObjectService {
    ObjectRepository objectRepository;
    TypeMoveRepository typeMoveRepository;
    TypeObjectRepository typeObjectRepository;
    PhotoRepository photoRepository;
    UserRepository userRepository;

    public List<Object> getObjectsWithFilter(Specification<Object> specification){
        return objectRepository.findAll(specification);
    }

    public  void createObjectAndSavePhotos(ObjectDto objectDto, List<String> savePhotoName){
        Object object;

        if(objectDto.getIdObject()!=null){
            object = objectRepository.findById(objectDto.getIdObject()).orElse(null);
        }else {
            object = new Object();
        }

        object.setObjectDto(objectDto);
        object.setTypeObject(typeObjectRepository.findByTypeObject(objectDto.getTypeObject()));
        object.setTypeMove(typeMoveRepository.findByTypeMove(objectDto.getTypeMove()));

        if (savePhotoName != null) {
            for (String namePhoto : savePhotoName) {
                Photo photo = new Photo();
                photo.setURL_photo(namePhoto);
                object.getPhotos().add(photo);
            }
        }

        objectRepository.save(object);
    }

    public Specification<Object> createSpecificationForObjects(String countRoom,
                                                              String maxPrice,
                                                              String minPrice,
                                                              String typeObject,
                                                              String typeMove){
        Specification<Object> filter = Specification.where(null);
        if(countRoom != null && !countRoom.equals(""))
            filter = filter.and(ObjectSpecification.countRoomEqual(Integer.parseInt(countRoom)));
        if(maxPrice != null && !maxPrice.equals(""))
            filter = filter.and(ObjectSpecification.priceGreaterThanOrEqual(maxPrice));
        if(minPrice != null && !minPrice.equals(""))
            filter = filter.and(ObjectSpecification.priceLesserThanOrEqual(minPrice));
        if(typeObject!=null && !typeObject.equals("Любой объект")){
            TypeObject dbTypeObject = typeObjectRepository.findByTypeObject(typeObject);
            filter = filter.and(ObjectSpecification.typeObjectEqual(dbTypeObject));
        }
        if(typeMove!=null && !typeMove.equals("Любой тип")){
            TypeMove dbTypeMove = typeMoveRepository.findByTypeMove(typeMove);
            filter = filter.and(ObjectSpecification.typeMoveEqual(dbTypeMove));
        }
        return filter;
    }

    public List<ObjectDto> getForApiObjects(Specification<Object> specification){
        return objectRepository.findAll(specification)
                .stream().map(e->{
                    ObjectDto temp = new ObjectDto();
                    temp.setObject(e);
                    return temp;
                })
                .collect(Collectors.toList());
    }

    public ObjectDto getObjectDtoById(Long id){
        Object object = objectRepository.findById(id).orElse(null);
        ObjectDto objectDto = new ObjectDto();
        if (object != null) {
            objectDto.setObject(object);
        }
        return objectDto;
    }

    public void deleteObjectById(Long id){
        List<User> users = userRepository.findUserByObjects_IdObject(id);
        Object object = objectRepository.findById(id).orElse(null);
        for (User user:users){
            user.removeObject(object);
        }

//        objectRepository.deleteById(id);
    }

    public Object getObjectById(Long id){
        return objectRepository.findById(id).orElse(null);
    }

    public List<TypeMove> getAllTypeMove(){
        return typeMoveRepository.findAll();
    }

    public List<TypeObject> getAllTypeObject(){
        return typeObjectRepository.findAll();
    }

    @Autowired
    public ObjectService(ObjectRepository objectRepository,
                         TypeMoveRepository typeMoveRepository,
                         TypeObjectRepository typeObjectRepository,
                         PhotoRepository photoRepository,
                         UserRepository userRepository) {
        this.objectRepository = objectRepository;
        this.typeMoveRepository = typeMoveRepository;
        this.typeObjectRepository = typeObjectRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
    }
}
