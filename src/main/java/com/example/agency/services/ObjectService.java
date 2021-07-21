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
import com.example.agency.repositories.specification.ObjectSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjectService {
    ObjectRepository objectRepository;
    TypeMoveRepository typeMoveRepository;
    TypeObjectRepository typeObjectRepository;
    PhotoRepository photoRepository;

    public List<Object> getObjectWithPaginationAndFilter(Specification<Object> specification){
        return objectRepository.findAll(specification);
    }

    public  void createObject(InputObjectDto objectDto,String savePhotoName){
        Object object;

        if(objectDto.getIdObject()!=null){
            object = objectRepository.findById(objectDto.getIdObject()).get();
        }else {
            object = new Object();
        }

        object.setObjectDto(objectDto);
        object.setTypeObject(typeObjectRepository.findByTypeObject(objectDto.getTypeObject()));
        object.setTypeMove(typeMoveRepository.findByTypeMove(objectDto.getTypeMove()));

        Photo photo= new Photo();
        photo.setURL_photo(savePhotoName);
        object.getPhotos().add(photo);

        objectRepository.save(object);
    }

    public Specification<Object> createSpecificationForObject(Integer countRoom,
                                                              String maxPrice,
                                                              String minPrice,
                                                              String typeObject,
                                                              String typeMove){
        Specification<Object> filter = Specification.where(null);
        if(countRoom != null && !countRoom.equals(0))
            filter = filter.and(ObjectSpecification.countRoomEq(countRoom));
        if(maxPrice != null && !maxPrice.equals(""))
            filter = filter.and(ObjectSpecification.priceGreaterThanOrEq(maxPrice));
        if(minPrice != null && !minPrice.equals(""))
            filter = filter.and(ObjectSpecification.priceLesserThanOrEq(minPrice));
        if(typeObject!=null && !typeObject.equals("Любой объект")){
            TypeObject dbTypeObject = typeObjectRepository.findByTypeObject(typeObject);
            filter = filter.and(ObjectSpecification.typeObjectEq(dbTypeObject));
        }
        if(typeMove!=null && !typeMove.equals("Любой тип")){
            TypeMove dbTypeMove = typeMoveRepository.findByTypeMove(typeMove);
            filter = filter.and(ObjectSpecification.typeMoveEq(dbTypeMove));
        }
        return filter;
    }

    public void delete(Long id){
        objectRepository.deleteById(id);
    }

    public Object getObjectById(Long id){
        return objectRepository.findById(id).get();
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
