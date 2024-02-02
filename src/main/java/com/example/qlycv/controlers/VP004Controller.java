package com.example.qlycv.controlers;

import com.example.qlycv.constant.Constant;
import com.example.qlycv.form.VP002Form;
import com.example.qlycv.form.VP004Form;
import com.example.qlycv.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/edit")
public class VP004Controller {
    @Autowired
    JwtService jwtService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(@ModelAttribute("VP004Form") VP004Form form, Model model) {
        return getViewName();
    }

    protected String getViewName() {
        return "/VP004";
    }

}
