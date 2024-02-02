package com.example.qlycv.service;
import java.util.List;

import com.example.qlycv.model.request.MonthPlan;
import com.example.qlycv.model.response.PlanMonthDto;

public interface PlanMonthService {
	Integer save(MonthPlan monthPlan, Integer userId);
	List<PlanMonthDto> getByMonthAndYear(Integer month, Integer year, Integer userId, String role);
	void remove(Integer planId, Integer userId);
}