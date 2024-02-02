package com.example.qlycv.controlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.qlycv.constant.Constant;
import com.example.qlycv.form.VP002Form;
import com.example.qlycv.service.JwtService;

@Controller
@RequestMapping("/main")
public class VP003Controller {
	
	@Autowired
	JwtService jwtService;
	
    @RequestMapping(method = RequestMethod.GET)
    public String index(@ModelAttribute("VP002Form") VP002Form form, Model model) {
    	model.addAttribute(Constant.ROLE, jwtService.getUserAuthLog().getRole());
        return getViewName();
    }

	protected String getViewName() {
	    return "VP003";
	}
	
}
