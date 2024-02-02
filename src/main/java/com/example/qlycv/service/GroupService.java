package com.example.qlycv.service;

import com.example.qlycv.entity.Field;
import com.example.qlycv.entity.Group;
import com.example.qlycv.entity.Staff;
import com.example.qlycv.form.group.AddGroupForm;
import com.example.qlycv.form.staff.AddStaffForm;
import com.example.qlycv.repository.GroupRepo;
import com.example.qlycv.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class GroupService {

    @Autowired
    GroupRepo groupRepo;

    public List<Group> getListGroup(){
        List<Group> groupList = groupRepo.getListGroup();
        return groupList;
    }

    public Group getGroupById(int id){
        Group group = groupRepo.findById(id);
        return group;
    }

    public boolean removeGroup(int id){
        groupRepo.deleteById(id);
        return true;
    }

    public boolean addGroup(AddGroupForm form) {
        if (ObjectUtils.isEmpty(form.getName()) || ObjectUtils.isEmpty(form.getCenterName())) {
            return false;
        }
        Group group = new Group();
        List<Group> groupList = groupRepo.getListGroup();
        for (Group group1: groupList) {
            if(group1.getName().equals(form.getName())){
                return false;
            }
        }
        group.setName(form.getName());
        group.setCenterName(form.getCenterName());
        groupRepo.save(group);

        return true;
    }

    public List<Group> findAll() {
        return groupRepo.findAll();
    }

    public List<Group> findByStaffId(Integer id) {
        // TODO Auto-generated method stub
        return groupRepo.findByStaffId(id);
    }

}
