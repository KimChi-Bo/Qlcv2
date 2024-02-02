package com.example.qlycv.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.qlycv.entity.Kpi;
import com.example.qlycv.entity.KpiUser;
import com.example.qlycv.model.response.KpiUserDTO;
import com.example.qlycv.model.response.UserKPIDTO;
import com.example.qlycv.repository.KpiRepo;
import com.example.qlycv.repository.KpiUserRepo;
import com.example.qlycv.service.KpiService;
import com.example.qlycv.service.KpiUserService;

import groovy.lang.Tuple;

@Service
class KpiUserServiceImpl implements KpiUserService {
    @Autowired
    private KpiUserRepo kpiUserRepo;

    @Override
    public List<KpiUser> findByUserId(Integer userId) {
        return kpiUserRepo.findAllByUserId(userId);
    }

    @Override
    public List<UserKPIDTO> findKpiUserByUserId(Integer userId) {
      return kpiUserRepo.findKpiUserByUserId(userId); 
     }
    
}
