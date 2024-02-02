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
import com.example.qlycv.repository.PlanweekRepo;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;

public interface PlanWeekService {
    String searchPlanWeek(int page, int size, String keyword) throws JsonProcessingException ;
    Integer save(WeekPlan Planweek, Integer userId, Integer groupId) throws ParseException;
    void remove(Integer id, Integer userId);
    List<PlanWeekDTO> getByUserId(String startDate, String endDate, Integer userId);
    List<PlanWeekDTO> getByGroup(String startDate, String endDate, Integer userId, List<Integer> groupId);
    Integer saveReport(DayReport form, Integer userId);
    Integer updateContentApprove(WeekPlan form, Integer userId);
    Integer saveByStaff(WeekPlan form, int id, int idGroup) throws ParseException;
}
