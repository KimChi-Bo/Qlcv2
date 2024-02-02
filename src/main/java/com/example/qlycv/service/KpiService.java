package com.example.qlycv.service;

import java.util.List;

import com.example.qlycv.entity.Field;
import com.example.qlycv.entity.Kpi;
import com.example.qlycv.entity.KpiUser;
import com.example.qlycv.form.kpi.AddForm;

public interface KpiService {
    List<Kpi> findByUserName(String userName);

    boolean addKPI(AddForm add);

    List<Field> getListField();
    boolean addKPI(AddForm add, int id);

    List<Kpi> getListKpi();

    List<Kpi> getListKpiByUsername(String userName);

    KpiUser findByUserAndKpi(int idUser, int idKpi);

    void updateWeight(KpiUser kpiUser);

    KpiUser save(KpiUser kpiUser);
    
}
