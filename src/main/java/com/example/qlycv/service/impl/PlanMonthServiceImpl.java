package com.example.qlycv.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.qlycv.constant.Constant;
import com.example.qlycv.model.response.PlanMonthRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.qlycv.entity.Planmonth;
import com.example.qlycv.model.request.MonthPlan;
import com.example.qlycv.model.response.PlanMonth;
import com.example.qlycv.model.response.PlanMonthDto;
import com.example.qlycv.repository.PlanmonthRepo;
import com.example.qlycv.service.PlanMonthService;

@Service
public class PlanMonthServiceImpl implements PlanMonthService {

	@Autowired
	PlanmonthRepo planmonthRepo;

	@Override
	public Integer save(MonthPlan monthPlan, Integer userId) {
		Date date = new Date();
		Planmonth planmonth = new Planmonth();
		planmonth.setIdUser(userId);
		planmonth.setIdField(monthPlan.getIdField());
		planmonth.setMonth(monthPlan.getMonth());
		planmonth.setYear(monthPlan.getYear());
		planmonth.setContent(monthPlan.getContent());

		planmonth.setCreateAt(date);
		planmonth.setStatus(Constant.DEL_FLG_OFF);

		return planmonthRepo.save(planmonth).getId();

	}

	@Override
	public List<PlanMonthDto> getByMonthAndYear(Integer month, Integer year, Integer userId, String role) {
		List<PlanMonthDto> result = new ArrayList<PlanMonthDto>();
		List<PlanMonthRes> pms = new ArrayList<PlanMonthRes>();
		if (role.contains(Constant.LD)) {
			pms = planmonthRepo.getPlaneMonthBByMY(month, year);
		} else if (role.contains(Constant.TT)) {
			pms = planmonthRepo.getPlaneMonthCByMY(month, year, userId);
		} else if (role.contains(Constant.NV)) {
			pms = planmonthRepo.getPlaneMonthDByMY(month, year, userId);
		}

		for (PlanMonthRes p : pms) {
			boolean showBtnDelFlg = false;
			if (role.contains(Constant.NV) && userId.toString().equals(p.getUserId())) {
				showBtnDelFlg = true;
			}
			result.add(new PlanMonthDto(p.getPlanId(), p.getNameUser(), p.getField(), p.getContent(), p.getMonthYear(), showBtnDelFlg));
		}
		return result;

	}

	@Override
	public void remove(Integer planId, Integer userId) {
		Planmonth p = planmonthRepo.findByIdAndIdUser(planId, userId);

		p.setStatus(Constant.DEL_FLG_ON);
		p.setDeleteAt(new Date());

		planmonthRepo.save(p);

	}

}
