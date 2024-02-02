package com.example.qlycv.controlers;

import com.example.qlycv.auth.CustomUserDetails;
import com.example.qlycv.constant.Constant;
import com.example.qlycv.constant.Message;
import com.example.qlycv.entity.Group;
import com.example.qlycv.entity.Staff;
import com.example.qlycv.entity.User;
import com.example.qlycv.form.VP002Form;
import com.example.qlycv.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.qlycv.service.GroupService;
import com.example.qlycv.service.JwtService;

@Controller
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @Autowired
    GroupService groupService;

    @RequestMapping(method = RequestMethod.GET, path = "/index")
    public String index(Model model) {
        int id = jwtService.getUserAuthLog().getIdStaff();
        model.addAttribute("staffName", userService.getStaffbyId(id).getName());
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        return "plan/index";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/approve")
    public String list(Model model) throws JsonProcessingException {

        User user = jwtService.getUserAuthLog();
        if (!user.getRole().contains("B") && !user.getRole().contains("C")) {
            model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
            return "/403";
        }

        int id = user.getIdStaff();
        Integer userId = user.getId();
        Staff staff = userService.getStaffbyId(id);
        model.addAttribute("staffName", staff.getName());

        List<Group> lstGroups = new ArrayList<>();
        List<Staff> lstStaffs = new ArrayList<>();
        if (user.getRole().contains("B")) {
            lstGroups = groupService.findAll();
        } else {
            lstGroups = groupService.findByStaffId(id);
            lstStaffs = userService.getStaffsByGroupId(staff.getIdGroup());
        }

        ObjectMapper mapper = new ObjectMapper();
        model.addAttribute("groups", mapper.writeValueAsString(lstGroups));
        model.addAttribute("role", user.getRole());
        model.addAttribute("lstUsers", mapper.writeValueAsString(lstStaffs));
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        return "plan/approve";
    }

}
