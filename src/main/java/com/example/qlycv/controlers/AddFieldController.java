package com.example.qlycv.controlers;

import com.example.qlycv.constant.Constant;
import com.example.qlycv.constant.Message;
import com.example.qlycv.entity.Field;
import com.example.qlycv.form.field.AddFieldForm;
import com.example.qlycv.service.FieldService;
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
@RequestMapping("/addfield")
public class AddFieldController {
    @Autowired
    JwtService jwtService;
    @Autowired
    FieldService fieldService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(@ModelAttribute("AddFieldForm") AddFieldForm form, Model model) {
        List<Field> fieldList = fieldService.getListField();
        model.addAttribute("fieldList", fieldList);
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        return getViewName();
    }

    @RequestMapping(method = RequestMethod.POST, path= "/add")
    public String add(@ModelAttribute("AddFieldForm") AddFieldForm form, Model model) {

        if(!fieldService.addField(form)) {
            model.addAttribute(Message.ERR_MES, Message.ERR_MES_ADD);
            return getViewName();
        }
        model.addAttribute(Message.ERR_MES, Message.OK_MES_ADD);
        List<Field> fieldList = fieldService.getListField();
        model.addAttribute("fieldList", fieldList);
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        return getViewName();
    }

    @RequestMapping(method = RequestMethod.GET, path= "/{id}")
    public String removeField(@PathVariable int id, @ModelAttribute("AddFieldForm") AddFieldForm form, Model model) {
        fieldService.removeField(id);
        List<Field> fieldList = fieldService.getListField();
        model.addAttribute("fieldList", fieldList);
        model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        return getViewName();
    }

    protected String getViewName() {
        return "field/AddField";
    }

}
