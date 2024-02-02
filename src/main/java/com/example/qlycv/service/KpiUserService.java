package com.example.qlycv.service;

import java.util.List;

import com.example.qlycv.entity.Kpi;
import com.example.qlycv.entity.KpiUser;
import com.example.qlycv.model.response.UserKPIDTO;

public interface KpiUserService {
    List<KpiUser> findByUserId(Integer userId);
    List<UserKPIDTO> findKpiUserByUserId(Integer userId);
    
}
