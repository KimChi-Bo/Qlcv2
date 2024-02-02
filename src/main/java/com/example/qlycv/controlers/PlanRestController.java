package com.example.qlycv.controlers;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.hibernate.sql.ast.tree.expression.Star;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.qlycv.auth.CustomUserDetails;
import com.example.qlycv.constant.Constant;
import com.example.qlycv.entity.Group;
import com.example.qlycv.entity.Kpi;
import com.example.qlycv.entity.KpiUser;
import com.example.qlycv.entity.Planweek;
import com.example.qlycv.entity.Staff;
import com.example.qlycv.entity.User;
import com.example.qlycv.form.VP002Form;
import com.example.qlycv.model.request.DayReport;
import com.example.qlycv.model.request.Plan;
import com.example.qlycv.model.request.WeekPlan;
import com.example.qlycv.model.response.UserKPIDTO;
import com.example.qlycv.model.response.KpiUserDTO;
import com.example.qlycv.model.response.PlanWeekDTO;
import com.example.qlycv.model.response.ReportDTO;
import com.example.qlycv.model.response.ResponseDto;
import com.example.qlycv.model.response.TableResponse;
import com.example.qlycv.service.GroupService;
import com.example.qlycv.service.JwtService;
import com.example.qlycv.service.KpiService;
import com.example.qlycv.service.KpiUserService;
import com.example.qlycv.service.PlanWeekService;
import com.example.qlycv.service.ReportService;
import com.example.qlycv.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.data.domain.Page;

import ch.qos.logback.core.model.Model;
import jakarta.persistence.Convert;

@RestController
@RequestMapping("/api/v1/plan")
@RequiredArgsConstructor
public class PlanRestController {
    @Autowired
    KpiUserService kpiUserService;

    @Autowired
    PlanWeekService planWeekService;

    @Autowired
    ReportService reportService;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    @GetMapping("/get")
    public ResponseDto<List<PlanWeekDTO>> getByUserId(@ModelAttribute("Plan") Plan form)
            throws JsonProcessingException {
        Integer userId = jwtService.getUserAuthLog().getId();
        System.out.println("=======================: " + userId);

        String startDate = form.getStartDate();
        String endDate = form.getEndDate();
        return ResponseDto.success(planWeekService.getByUserId(startDate, endDate, userId));
    }

    @GetMapping("/get-by-group")
    public ResponseDto<List<PlanWeekDTO>> getByGroup(@ModelAttribute("Plan") Plan form) throws JsonProcessingException {
        User user = jwtService.getUserAuthLog();
        Integer userId = user.getId();
        Staff staff = userService.getStaffbyId(user.getIdStaff());

        if (!user.getRole().contains("B") && !user.getRole().contains("C")) {
            return ResponseDto.fail("Không có quyền truy cập api", String.class);
        }

        /**
         * -Nếu group null => tìm theo group của cán bộ
         * group not null => tìm theo group
         */
        List<Integer> groupId = new ArrayList<>();

        if (form.getGroup() == null || form.getGroup() == 0) {
            List<Group> lstGroups = new ArrayList<>();
            if (user.getRole().contains("B")) {
                lstGroups = groupService.findAll();
            } else {
                lstGroups = groupService.findByStaffId(user.getIdStaff());
            }
            for (Group group : lstGroups) {
                groupId.add(group.getId());
            }
        } else {
            groupId.add(form.getGroup());
        }

        String startDate = form.getStartDate();
        String endDate = form.getEndDate();
        return ResponseDto.success(planWeekService.getByGroup(startDate, endDate, userId, groupId));
    }

    @GetMapping("/get-paging")
    public String get(@ModelAttribute("Plan") Plan form) throws JsonProcessingException {
        int offset = form.getOffset();
        int limit = form.getLimit();

        if (offset <= 0)
            offset = 0;
        if (limit <= 0)
            limit = 10;

        return planWeekService.searchPlanWeek(offset, 10, null);
    }

    @GetMapping("/get-kpi")
    public ResponseDto<List<UserKPIDTO>> getKPI(@ModelAttribute("Plan") Plan form) {
        Integer userId = jwtService.getUserAuthLog().getId();
        return ResponseDto.success(kpiUserService.findKpiUserByUserId(userId));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/get-kpi-staff/{id}")
    public ResponseDto<List<UserKPIDTO>> getKPIByStaffId(@PathVariable Integer id) {
        User u = userService.getUserByStaffId(id);
        Integer userId = u.getId();
        return ResponseDto.success(kpiUserService.findKpiUserByUserId(userId));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/save-week")
    public ResponseDto<Integer> saveWeekPlan(@RequestBody WeekPlan form) throws ParseException {
        User user = jwtService.getUserAuthLog();
        Integer userId = user.getId();
        Staff staff = userService.getStaffbyId(user.getIdStaff());

        Integer resId = planWeekService.save(form, userId, staff.getIdGroup());
        return ResponseDto.success(resId);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/save-week-by-staff")
    public ResponseDto<Integer> saveWeekPlanByStaff(@RequestBody WeekPlan form) throws ParseException {
        Staff staff = userService.getStaffbyId(form.getStaffId());
        User u = userService.getUserByStaffId(staff.getId());
        Integer resId = planWeekService.saveByStaff(form, u.getId(), staff.getIdGroup());
        return ResponseDto.success(resId);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/save-content-aprove")
    public ResponseDto<Integer> saveContentApprove(@RequestBody WeekPlan form) throws ParseException {
        Integer userId = jwtService.getUserAuthLog().getId();
        Integer resId = planWeekService.updateContentApprove(form, userId);
        return ResponseDto.success(resId);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/remove/{Id}")
    public ResponseDto<String> remove(@PathVariable Integer Id) {
        Integer userId = jwtService.getUserAuthLog().getId();
        planWeekService.remove(Id, userId);
        return ResponseDto.success("OK");
    }

    @RequestMapping(method = RequestMethod.POST, path = "/save-day-report")
    public ResponseDto<Integer> saveWeekPlan(@RequestBody ReportDTO form) throws ParseException {
        Integer userId = jwtService.getUserAuthLog().getId();
        Integer resId = reportService.save(form, userId);
        return ResponseDto.success(resId);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/get-report/{planId}")
    public ResponseDto<ReportDTO> getDayReportByPlanId(@PathVariable Integer planId) throws JsonProcessingException {
        Integer userId = jwtService.getUserAuthLog().getId();
        ReportDTO reportDTO = reportService.findByPlanId(planId);
        return ResponseDto.success(reportDTO);
    }

}
