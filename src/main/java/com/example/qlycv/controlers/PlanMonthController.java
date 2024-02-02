package com.example.qlycv.controlers;

import java.time.LocalDate;

import com.example.qlycv.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.qlycv.service.FieldService;
import com.example.qlycv.service.JwtService;
import com.example.qlycv.service.UserService;

@Controller
@RequestMapping("/planmonth")
public class PlanMonthController {

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @Autowired
    FieldService fieldService;
    
    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {
        int id = jwtService.getUserAuthLog().getIdStaff();
        
        model.addAttribute("staffName", userService.getStaffbyId(id).getName());
        model.addAttribute("years", LocalDate.now().getYear());
        model.addAttribute("fields", fieldService.getListField());
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        
        return "planmonth/index";
    }


}
