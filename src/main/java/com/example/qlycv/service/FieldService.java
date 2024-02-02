package com.example.qlycv.service;

import com.example.qlycv.entity.Field;
import com.example.qlycv.form.field.AddFieldForm;
import com.example.qlycv.repository.FieldRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class FieldService {
    @Autowired
    FieldRepo fieldRepo ;

    public boolean addField(AddFieldForm form) {
        if (ObjectUtils.isEmpty(form.getName())) {
            return false;
        }
        Field field = new Field();
        List<Field> fieldList = fieldRepo.getListField();
        for (Field field1 : fieldList) {
            if(field1.getName().equals(form.getName())){
                return false;
            }
        }
        field.setName(form.getName());
        fieldRepo.save(field);

        return true;
    }

    public List<Field> getListField(){
        List<Field> fieldList = fieldRepo.getListField();
        return fieldList;
    }

    public boolean removeField(int id){
        fieldRepo.deleteById(id);
        return true;
    }

    public List<Field> getAllField() {
        return fieldRepo.findAll();
    }

    public Field findById(int idField) {
        return fieldRepo.findById(idField);
    }

}
