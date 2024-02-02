package com.example.qlycv.controlers;

import com.example.qlycv.constant.Constant;
import com.example.qlycv.constant.Message;
import com.example.qlycv.entity.Field;
import com.example.qlycv.entity.Group;
import com.example.qlycv.form.field.AddFieldForm;
import com.example.qlycv.form.group.AddGroupForm;
import com.example.qlycv.service.FieldService;
import com.example.qlycv.service.GroupService;
import com.example.qlycv.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/addgroup")
public class AddGroupController {
    @Autowired
    JwtService jwtService;
    @Autowired
    GroupService groupService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(@ModelAttribute("AddGroupForm") AddGroupForm form, Model model) {
        List<Group> groupList = groupService.getListGroup();
        model.addAttribute("groupList", groupList);
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        return getViewName();
    }

    @RequestMapping(method = RequestMethod.POST, path= "/add")
    public String add(@ModelAttribute("AddGroupForm") AddGroupForm form, Model model) {
        if(!groupService.addGroup(form)) {
            model.addAttribute(Message.ERR_MES, "Thêm tổ không thành công ");
            return getViewName();
        }
        model.addAttribute(Message.ERR_MES, "Thêm tổ thành công");
        List<Group> groupList = groupService.getListGroup();
        model.addAttribute("groupList", groupList);
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        return getViewName();
    }

    @RequestMapping(method = RequestMethod.GET, path= "/{id}")
    public String removeGroup(@PathVariable int id, @ModelAttribute("AddGroupForm") AddGroupForm form, Model model) {
        groupService.removeGroup(id);
        List<Group> groupList = groupService.getListGroup();
        model.addAttribute("groupList", groupList);
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        return getViewName();
    }

    protected String getViewName() {
        return "group/AddGroup";
    }

}
