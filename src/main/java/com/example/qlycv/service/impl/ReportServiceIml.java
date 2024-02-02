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
import com.example.qlycv.entity.Report;
import com.example.qlycv.entity.Role;
import com.example.qlycv.model.request.DayReport;
import com.example.qlycv.model.request.WeekPlan;
import com.example.qlycv.model.response.PlanWeekDTO;
import com.example.qlycv.model.response.ReportDTO;
import com.example.qlycv.model.response.TableResponse;
import com.example.qlycv.model.response.UserPlanWeek;
import com.example.qlycv.repository.PlanweekRepo;
import com.example.qlycv.repository.ReportRepo;
import com.example.qlycv.service.PlanWeekService;
import com.example.qlycv.service.ReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@Service
class ReportServiceIml implements ReportService {
    @Autowired
    private ReportRepo reportRepo;

    @Override
    public ReportDTO findByPlanId(Integer planId) throws JsonProcessingException {
        Report item = reportRepo.findByPlanId(planId);
        if (item == null) {
            return null;
        }else {
            String createDate = "";
            try {
                Format formatter = new SimpleDateFormat("dd/MM/yyyy");
                createDate = formatter.format(item.getCreateAt());
            } catch (Exception e) {
                createDate = "";
            }
            ReportDTO reportDTO = new ReportDTO(item.getId(), item.getContent(),item.getHoursComplete(),item.getIdPlanMonth(), createDate, item.getStatus(), item.getNote());
            return reportDTO;
        }
    }

    @Override
    public Integer save(ReportDTO obj, Integer userId) throws ParseException {
        Report report = new Report();
        report.setContent(obj.getReportContent());
        report.setHoursComplete(obj.getReportTime());
        report.setIdUser(userId);
        report.setIdPlanMonth(obj.getPlanId());
        report.setStatus(obj.getReportStatus());
        report.setNote(obj.getReportReason());


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedPlanDate = dateFormat.parse(obj.getReportDate());
        report.setCreateAt(convertedPlanDate);

        if (obj.getId() != null && obj.getId() > 0) {
            report.setId(obj.getId());
        }
        return reportRepo.save(report).getId();
    }
}