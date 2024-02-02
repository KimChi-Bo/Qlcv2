package com.example.qlycv.service.impl;

import java.sql.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.example.qlycv.model.request.WeekPlan;
import com.example.qlycv.model.response.PlanWeekDTO;
import com.example.qlycv.model.response.TableResponse;
import com.example.qlycv.model.response.UserPlanWeek;
import com.example.qlycv.repository.PlanweekRepo;
import com.example.qlycv.service.PlanWeekService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@Service
class PlanWeekServiceIml implements PlanWeekService {
    @Autowired
    private PlanweekRepo planweekRepo;

    @Override
    public String searchPlanWeek(int page, int limit, String keyword) throws JsonProcessingException {
        Sort sorted = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(page, limit, sorted);
        Page<Planweek> p = planweekRepo.searchPlanWeek(keyword, pageable);
        Integer total = p.getTotalPages();
        List<Planweek> lPW = p.getContent();
        List<PlanWeekDTO> lPlanWeekDTOs = new ArrayList<>();
        for (Planweek planweek : lPW) {
            PlanWeekDTO item = new PlanWeekDTO();
            item.setPlanContent(planweek.getContent());
            item.setPlanDate(planweek.getStartAt().toString());
            item.setPlanId(planweek.getId());
            item.setPlanStatus(planweek.getStatus() + "");
            lPlanWeekDTOs.add(item);
        }

        TableResponse tableResponse = new TableResponse();
        tableResponse.setTotal(total);
        ObjectMapper mapper = new ObjectMapper();
        tableResponse.setRows(lPlanWeekDTOs);
        return mapper.writeValueAsString(tableResponse);
    }

    @Override
    public Integer save(WeekPlan form, Integer userId, Integer groupId) throws ParseException {
        Planweek planweek = new Planweek();
        planweek.setContent(form.getPlanContent());
        planweek.setHoursComplete(form.getPlanTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        planweek.setCreateAt(date);
        Date convertedPlanDate = dateFormat.parse(form.getPlanDate());
        planweek.setPlaneDate(convertedPlanDate);
        SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date parsedDate = dtFormat.parse(form.getStartDate());
        Timestamp timestamp = new Timestamp(parsedDate.getTime());
        planweek.setStartAt(timestamp);

        planweek.setIdKpiUser(form.getKpiUserId());
        planweek.setIdUser(userId);
        planweek.setIdGroup(groupId);
        return planweekRepo.save(planweek).getId();
    }

    @Override
    public void remove(Integer id, Integer userId) {
        planweekRepo.deleteById(id);
    }

    @Override
    public List<PlanWeekDTO> getByUserId(String startDate, String endDate, Integer userId) {
        List<UserPlanWeek> lPlanweeks = planweekRepo.findAllByUserId(startDate, endDate, userId);
        List<PlanWeekDTO> lPlanWeekDTOs = new ArrayList<>();
        for (UserPlanWeek planweek : lPlanweeks) {
            PlanWeekDTO item = new PlanWeekDTO();

            item.setPlanId(planweek.getId());
            item.setKpiName(planweek.getKpiName());
            item.setPlanContent(planweek.getPlanContent());
            item.setPlanStatus(planweek.getStatus() + "");
            item.setReportContent(planweek.getReportContent());
            item.setApprovedContent(planweek.getApprovedContent());
            item.setFieldName(planweek.getFieldName());
            item.setReportNote(planweek.getReportNote());
            item.setPlanTime(planweek.getPlanTime());
            item.setReportTime(planweek.getReportTime());

            try {
                Format formatter = new SimpleDateFormat("dd/MM/yyyy");
                String s = formatter.format(planweek.getPlanDate());
                item.setPlanDate(s);
            } catch (Exception e) {
                item.setPlanDate("");
            }
            lPlanWeekDTOs.add(item);
        }
        return lPlanWeekDTOs;
    }

    @Override
    public Integer saveReport(DayReport form, Integer userId) {
        // TODO Auto-generated method stub
        Planweek planweek = new Planweek();
        planweek.setContent(form.getPlanContent());
        planweek.setHoursComplete(form.getPlanTime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        planweek.setCreateAt(date);

        // Date convertedPlanDate = dateFormat.parse(form.getPlanDate());
        // planweek.setPlaneDate(convertedPlanDate);

        SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        // Date parsedDate = dtFormat.parse(form.getStartDate());
        // Timestamp timestamp = new Timestamp(parsedDate.getTime());
        // planweek.setStartAt(timestamp);

        // planweek.setIdKpiUser(form.getKpiUserId());
        planweek.setIdUser(userId);
        return planweekRepo.save(planweek).getId();
    }

    @Override
    public List<PlanWeekDTO> getByGroup(String startDate, String endDate, Integer userId, List<Integer> groupId) {

        List<UserPlanWeek> lPlanweeks = planweekRepo.findAllByGroup(startDate, endDate, groupId);
        List<PlanWeekDTO> lPlanWeekDTOs = new ArrayList<>();
        for (UserPlanWeek planweek : lPlanweeks) {
            PlanWeekDTO item = new PlanWeekDTO();

            item.setPlanId(planweek.getId());
            item.setKpiName(planweek.getKpiName());
            item.setPlanContent(planweek.getPlanContent());
            item.setPlanStatus(planweek.getStatus() + "");
            item.setReportContent(planweek.getReportContent());
            item.setApprovedContent(planweek.getApprovedContent());
            item.setFieldName(planweek.getFieldName());
            item.setReportNote(planweek.getReportNote());
            item.setPlanTime(planweek.getPlanTime());
            item.setReportTime(planweek.getReportTime());
            item.setUserName(planweek.getUserName());
            item.setGroupName(planweek.getGroupName());
            item.setStaffName(planweek.getStaffName());

            try {
                Format formatter = new SimpleDateFormat("dd/MM/yyyy");
                String s = formatter.format(planweek.getPlanDate());
                item.setPlanDate(s);
            } catch (Exception e) {
                item.setPlanDate("");
            }
            lPlanWeekDTOs.add(item);
        }
        return lPlanWeekDTOs;
    }

    @Override
    public Integer updateContentApprove(WeekPlan form, Integer userId) {
        Planweek planweek = planweekRepo.getById(form.getPlanId());
        planweek.setContentApp(form.getApprovedContent());
        return planweekRepo.save(planweek).getId();
    }

    @Override
    public Integer saveByStaff(WeekPlan form, int userId, int groupId) throws ParseException {
        // TODO Auto-generated method stub
        Planweek planweek = new Planweek();
        planweek.setContentApp(form.getPlanContent());
        planweek.setContent(form.getPlanContent());
        planweek.setHoursComplete(form.getPlanTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        planweek.setCreateAt(date);
        Date convertedPlanDate = dateFormat.parse(form.getPlanDate());
        planweek.setPlaneDate(convertedPlanDate);
        SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date parsedDate = dtFormat.parse(form.getStartDate());
        Timestamp timestamp = new Timestamp(parsedDate.getTime());
        planweek.setStartAt(timestamp);

        planweek.setIdKpiUser(form.getKpiUserId());
        planweek.setIdUser(userId);
        planweek.setIdGroup(groupId);
        return planweekRepo.save(planweek).getId();
    }

}