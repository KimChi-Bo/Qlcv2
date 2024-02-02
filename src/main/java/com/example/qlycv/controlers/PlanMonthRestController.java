package com.example.qlycv.controlers;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.qlycv.auth.CustomUserDetails;
import com.example.qlycv.model.request.MonthPlan;
import com.example.qlycv.model.response.PlanMonthDto;
import com.example.qlycv.model.response.ResponseDto;
import com.example.qlycv.service.PlanMonthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/planmonth")
@RequiredArgsConstructor
public class PlanMonthRestController {
	
	@Autowired
	PlanMonthService planMonthService;

    @RequestMapping(method = RequestMethod.POST, path = "/save-month")
    public ResponseDto<Integer> saveWeekPlan(@RequestBody MonthPlan form) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = user.getUser().getId();
        Integer resId = planMonthService.save(form, userId);
        return ResponseDto.success(resId);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/search-all")
    public ResponseDto<List<PlanMonthDto>> searchAll(@RequestParam("month") int month,  @RequestParam("year") int year) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return ResponseDto.success(planMonthService.getByMonthAndYear(month, year, user.getUser().getId(), user.getUser().getRole()));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/remove/{Id}")
    public ResponseDto<String> remove(@PathVariable Integer Id ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = user.getUser().getId();
        planMonthService.remove(Id, userId);
        return ResponseDto.success("OK");
    }


}
