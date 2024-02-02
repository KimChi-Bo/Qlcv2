package com.example.qlycv.service;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.qlycv.entity.Planweek;
import com.example.qlycv.entity.Role;
import com.example.qlycv.model.request.DayReport;
import com.example.qlycv.model.request.Plan;
import com.example.qlycv.model.request.WeekPlan;
import com.example.qlycv.model.response.PlanWeekDTO;
import com.example.qlycv.model.response.ReportDTO;
import com.example.qlycv.repository.PlanweekRepo;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;

public interface ReportService {
    ReportDTO findByPlanId(Integer planId ) throws JsonProcessingException ;
    Integer save(ReportDTO obj, Integer userId) throws ParseException;
}
