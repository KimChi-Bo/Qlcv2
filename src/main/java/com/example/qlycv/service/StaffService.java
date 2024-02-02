package com.example.qlycv.service;

import com.example.qlycv.entity.Staff;
import com.example.qlycv.form.staff.AddStaffForm;
import com.example.qlycv.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class StaffService {
    @Autowired
    StaffRepo staffRepo;

    public List<Staff> getListStaff(){
        List<Staff> userStaff = staffRepo.getListStaff();
        return userStaff;
    }

    public Staff getStaffById(int id){
        Staff staff = staffRepo.findById(id);
        return staff;
    }

    public boolean removeStaff(int id){
        staffRepo.deleteById(id);
        return true;
    }

    public boolean addStaff(AddStaffForm form) {
        if (ObjectUtils.isEmpty(form.getName()) || ObjectUtils.isEmpty(form.getId_group())) {
            return false;
        }
        Staff staff = new Staff();
        List<Staff> staffList = staffRepo.getListStaff();
        for (Staff staff1 : staffList) {
            if(staff1.getName().equals(form.getName())){
                return false;
            }
        }
        staff.setIdGroup(form.getId_group());
        staff.setName(form.getName());
        staff.setNote(form.getNote());
        staffRepo.save(staff);

        return true;
    }

}
