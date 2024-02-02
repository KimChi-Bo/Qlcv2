package com.example.qlycv.controlers;

import com.example.qlycv.constant.Constant;
import com.example.qlycv.entity.Field;
import com.example.qlycv.entity.Role;
import com.example.qlycv.entity.Staff;
import com.example.qlycv.entity.User;
import com.example.qlycv.form.field.AddFieldForm;
import com.example.qlycv.service.JwtService;
import com.example.qlycv.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.qlycv.constant.Message;
import com.example.qlycv.form.VP002Form;
import com.example.qlycv.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/register")
public class VP002Controller {

    @Autowired
    JwtService jwtService;
	
	@Autowired
	private UserService userService;

    @Autowired
    private StaffService staffService;
	
    @RequestMapping(method = RequestMethod.GET)
    public String index(@ModelAttribute("VP002Form") VP002Form form, Model model) {
        int id = jwtService.getUserAuthLog().getIdStaff();
        List<User> userList = userService.getListUser();
        List<Staff> listStaff = staffService.getListStaff();
        List<Role> listRole = userService.getRoleName();
        model.addAttribute("listRole", listRole);
        model.addAttribute("listStaff", listStaff);
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        model.addAttribute("userList", userList);
        model.addAttribute("staffName", userService.getStaffbyId(id).getName());
        return getViewName();
    }
    
    @RequestMapping(method = RequestMethod.POST, path= "/add")
    public String add(@ModelAttribute("VP002Form") VP002Form form, Model model) {
    	
    	if(!userService.registerAccount(form)) {
    		model.addAttribute(Message.ERR_MES, Message.VP002_ADD_FAIL);
    		return getViewName();
    	}
        List<User> userList = userService.getListUser();
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        model.addAttribute("userList", userList);
    	model.addAttribute(Message.ERR_MES, Message.VP002_ADD_OK);
        return getViewName();
    }

    @RequestMapping(method = RequestMethod.GET, path= "/{id}")
    public String removeUser(@PathVariable int id, @ModelAttribute("VP002Form") VP002Form form, Model model) {
        userService.removeUser(id);
        List<User> userList = userService.getListUser();
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        model.addAttribute("userList", userList);
        return getViewName();
    }
    protected String getViewName() {
        return "/VP002";
    }

}
