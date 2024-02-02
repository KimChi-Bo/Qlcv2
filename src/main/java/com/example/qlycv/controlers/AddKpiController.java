package com.example.qlycv.controlers;

import com.example.qlycv.constant.Constant;
import com.example.qlycv.constant.Message;
import com.example.qlycv.entity.*;
import com.example.qlycv.form.VP002Form;
import com.example.qlycv.form.kpi.AddForm;
import com.example.qlycv.service.FieldService;
import com.example.qlycv.service.JwtService;
import com.example.qlycv.service.KpiService;
import com.example.qlycv.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/addkpi")
public class AddKpiController {
    @Autowired
    JwtService jwtService;
    @Autowired
    public KpiService kpiService;
    @Autowired
    FieldService fieldService;
    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(@ModelAttribute("AddForm") AddForm form, Model model) {
        List<Kpi> listKpi = kpiService.getListKpi();
        Map<Integer, String> titleField = new HashMap<>();
        for (Kpi kpi : listKpi) {
            titleField.put(kpi.getId(), fieldService.findById(kpi.getIdField()).getName());
        }
        model.addAttribute("titleField", titleField);
        model.addAttribute("listKpi", listKpi);
        model.addAttribute("field", fieldService.getAllField());
        List<Field> listField = kpiService.getListField();
        model.addAttribute("listField",listField);
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        return getViewName();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/details")
    public String details(@ModelAttribute("AddForm") AddForm form, Model model, HttpServletRequest httpRequest) {
        String username = jwtService.getUserNameFromJWT((String) httpRequest.getSession().getAttribute(Constant.JWT));
        List<Staff> listStaff = userService.getListUserStaffByUsername(username);
        model.addAttribute("listStaff", listStaff);

        List<Kpi> listKpi = kpiService.getListKpiByUsername(username);
        List<KpiUserDetails> listkpiUserDetails = new ArrayList<>();
        if (!listKpi.isEmpty()) {
            int i = 1;
            for (Kpi kpi : listKpi) {
                KpiUserDetails kpiUserDetails = new KpiUserDetails();
                kpiUserDetails.setStt(i++);
                kpiUserDetails.setKpi(kpi.getContent());
                kpiUserDetails.setWeight(kpi.getWeight());
                List<KpiUser> listKpiUser = new ArrayList<>();
                for (Staff staff : listStaff) {
                    KpiUser kpiUser = new KpiUser();
                    kpiUser.setIdKpi(kpi.getId());
                    kpiUser.setIdUser(userService.getUserIdByStaffId(staff.getId()));
                    if (userService.getKpiUserByStaffIdAndIdKpi(userService.getUserIdByStaffId(staff.getId()), kpi.getId()) != null) {
                        kpiUser.setWeight(userService.getKpiUserByStaffIdAndIdKpi(userService.getUserIdByStaffId(staff.getId()), kpi.getId()).getWeight());
                    } else {
                        kpiUser.setWeight(0f);
                    }
                    listKpiUser.add(kpiUser);
                }
                kpiUserDetails.setStaffWeigh(listKpiUser);
                listkpiUserDetails.add(kpiUserDetails);
            }
        }
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        model.addAttribute("listKpiUserDetails", listkpiUserDetails);
        return "kpi/kpi-details";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/add-kpi")
    public String addKPi(@ModelAttribute("AddForm") AddForm form, Model model, HttpServletRequest httpRequest) {
        model.addAttribute("field", fieldService.getAllField());
        int idGroup = userService.getUserIdByUsername(jwtService.getUserNameFromJWT((String) httpRequest.getSession().getAttribute(Constant.JWT)));

        if (!kpiService.addKPI(form, idGroup)) {
            model.addAttribute(Message.ERR_MES, Message.ERR_MES_ADD);
            return getViewName();
        }

        List<Kpi> listKpi = kpiService.getListKpi();
        Map<Integer, String> titleField = new HashMap<>();
        for (Kpi kpi : listKpi) {
            titleField.put(kpi.getId(), fieldService.findById(kpi.getIdField()).getName());
        }
        model.addAttribute("titleField", titleField);
        model.addAttribute(Message.ERR_MES, Message.OK_MES_ADD);
        model.addAttribute("listKpi", listKpi);
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        return getViewName();
    }

    @RequestMapping(value = "/save-kpi-details", method = {RequestMethod.GET, RequestMethod.POST})
    public String saveKpiDetails(@RequestBody List<Map<String, Object>> data) {
        for (Map<String, Object> x : data) {
            KpiUser kpiUser;
            if (kpiService.findByUserAndKpi(Integer.parseInt(x.get("id").toString()), Integer.parseInt(x.get("extraId").toString())) != null) {
                kpiUser = kpiService.findByUserAndKpi(Integer.parseInt(x.get("id").toString()), Integer.parseInt(x.get("extraId").toString()));
                kpiUser.setWeight(Float.parseFloat(x.get("value").toString()));
                kpiService.updateWeight(kpiUser);
            } else {
                kpiUser = new KpiUser();
                kpiUser.setIdUser(Integer.parseInt(x.get("id").toString()));
                kpiUser.setIdKpi(Integer.parseInt(x.get("extraId").toString()));
                kpiUser.setWeight(Float.parseFloat(x.get("value").toString()));
                kpiUser.setHoursComplete(0);
                kpiService.save(kpiUser);
            }
        }

        return "kpi/kpi-details";
    }

    protected String getViewName() {
        return "kpi/addkpi";
    }
}
