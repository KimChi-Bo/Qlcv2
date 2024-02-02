package com.example.qlycv.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.qlycv.entity.Field;
import com.example.qlycv.entity.KpiUser;
import com.example.qlycv.form.kpi.AddForm;
import com.example.qlycv.repository.FieldRepo;
import com.example.qlycv.repository.KpiUserRepo;
import com.example.qlycv.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.qlycv.entity.Kpi;
import com.example.qlycv.repository.KpiRepo;
import com.example.qlycv.service.KpiService;

@Service
class KpiServiceImpl implements KpiService {
    @Autowired
    private KpiRepo kpiRepo;
    @Autowired
    private FieldRepo fieldRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private KpiUserRepo kpiUserRepo;

    @Override
    public List<Kpi> findByUserName(String userName) {
        // TODO Auto-generated method stub
        return kpiRepo.findByUserName(userName);
    }

    public boolean addKPI(AddForm form) {
        Kpi kpi = new Kpi();
        int idField = form.getId_field();
        String content = form.getContent();
        Float weight = form.getWeight();
        int month = form.getMonth();
        int year = form.getYear();

        kpi.setIdField(idField);
        kpi.setContent(content);
        kpi.setWeight(weight);
        kpi.setMonth(month);
        kpi.setYear(year);

        kpiRepo.save(kpi);
        return true;
    }

    @Override
    public List<Field> getListField(){
        List<Field> listField = fieldRepo.getListField();
        return listField;
    }
    public boolean addKPI(AddForm form, int id) {
        Kpi kpi = new Kpi();
        int idField = form.getId_field();
        String content = form.getContent();
        Float weight = form.getWeight();
        int month = form.getMonth();
        int year = form.getYear();

        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND));

        Date updatedDate = calendar.getTime();

        kpi.setCreateAt(updatedDate);
        kpi.setIdField(idField);
        kpi.setContent(content);
        kpi.setWeight(weight);
        kpi.setMonth(month);
        kpi.setYear(year);
        kpi.setIdGroup(userService.getStaffbyUserId(id).getIdGroup());
        kpiRepo.save(kpi);
        return true;
    }

    public List<Kpi> getListKpi() {


        return kpiRepo.findAll();
    }


    public List<Kpi> getListKpiByUsername(String userName) {
        return kpiRepo.findByUserName(userName);
    }

    public KpiUser findByUserAndKpi(int idUser, int idKpi) {
        return kpiRepo.findByIdUserAndIdKpi(idUser, idKpi);
    }

    public void updateWeight(KpiUser kpiUser) {
        kpiUserRepo.save(kpiUser);
    }

    public KpiUser save(KpiUser kpiUser) {
        return kpiUserRepo.save(kpiUser);
    }
    
}
