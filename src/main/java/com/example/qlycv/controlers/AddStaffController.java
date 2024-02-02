package com.example.qlycv.controlers;

import com.example.qlycv.constant.Constant;
import com.example.qlycv.constant.Message;
import com.example.qlycv.entity.Group;
import com.example.qlycv.entity.Staff;
import com.example.qlycv.entity.StaffNew;
import com.example.qlycv.form.staff.AddStaffForm;
import com.example.qlycv.service.GroupService;
import com.example.qlycv.service.JwtService;
import com.example.qlycv.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/addstaff")
public class AddStaffController {
    @Autowired
    JwtService jwtService;
    @Autowired
    GroupService groupService;
    @Autowired
    StaffService staffService;



    @RequestMapping(method = RequestMethod.GET)
    public String index(@ModelAttribute("AddStaffForm") AddStaffForm form, Model model) {
        List<StaffNew> staffNewList = new ArrayList<>();
        List<Group> groupList = groupService.getListGroup();
        model.addAttribute("groupList", groupList);
        List<Staff> staffList = staffService.getListStaff();
        for (Staff s: staffList) {
            StaffNew staffNew = new StaffNew();
            staffNew.setId(s.getId());
            staffNew.setName(s.getName());
            staffNew.setNote(s.getNote());
            for ( Group g: groupList) {
                if(s.getIdGroup() == g.getId()) {
                    staffNew.setGroupName(g.getName());
                }
            }
            staffNewList.add(staffNew);
        }

        model.addAttribute("staffNewList", staffNewList);
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        return getViewName();
    }

    @RequestMapping(method = RequestMethod.POST, path= "/add")
    public String add(@ModelAttribute("AddStaffForm") AddStaffForm form, Model model) {
        List<StaffNew> staffNewList = new ArrayList<>();
        List<Group> groupList = groupService.getListGroup();

        if(!staffService.addStaff(form)) {
            model.addAttribute(Message.ERR_MES, "Thêm nhân viên thất bại");
            return getViewName();
        }
        model.addAttribute(Message.ERR_MES, "Thêm nhân viên thành công");
        List<Staff> staffList = staffService.getListStaff();
        for (Staff s: staffList) {
            StaffNew staffNew = new StaffNew();
            staffNew.setId(s.getId());
            staffNew.setName(s.getName());
            staffNew.setNote(s.getNote());
            for ( Group g: groupList) {
                if(s.getIdGroup() == g.getId()) {
                    staffNew.setGroupName(g.getName());
                }
            }
            staffNewList.add(staffNew);
        }
        model.addAttribute("groupList", groupList);
        model.addAttribute("staffNewList", staffNewList);
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        return getViewName();
    }

    @RequestMapping(method = RequestMethod.GET, path= "/{id}")
    public String removeStaff(@PathVariable int id, @ModelAttribute("AddStaffForm") AddStaffForm form, Model model) {
        staffService.removeStaff(id);
        List<StaffNew> staffNewList = new ArrayList<>();
        List<Group> groupList = groupService.getListGroup();
        List<Staff> staffList = staffService.getListStaff();
        for (Staff s: staffList) {
            StaffNew staffNew = new StaffNew();
            staffNew.setId(s.getId());
            staffNew.setName(s.getName());
            staffNew.setNote(s.getNote());
            for ( Group g: groupList) {
                if(s.getIdGroup() == g.getId()){
                    staffNew.setGroupName(g.getName());
                }
            }
            staffNewList.add(staffNew);
        }
        model.addAttribute("groupList", groupList);
        model.addAttribute("staffNewList", staffNewList);
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        return getViewName();
    }

    protected String getViewName() {
        return "staff/AddStaff";
    }
}
