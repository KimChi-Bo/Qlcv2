package com.example.qlycv.controlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.qlycv.auth.CustomUserDetails;
import com.example.qlycv.constant.Constant;
import com.example.qlycv.constant.Message;
import com.example.qlycv.constant.Screen;
import com.example.qlycv.form.VP001Form;
import com.example.qlycv.service.JwtService;
import com.example.qlycv.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class VP001Controller {

	@Autowired
	private UserService userService;

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	JwtService jwtService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(@ModelAttribute("VP001Form") VP001Form form, Model model) {
		return getViewName();
	}

	@RequestMapping(method = RequestMethod.POST, path = "/check")
	public String login(@ModelAttribute("VP001Form") VP001Form form, Model model, HttpSession session) {
		
		if (!userService.checkLogin(form)) {
			model.addAttribute(Message.ERR_MES, Message.VP001_LOGIN_FAIL);
			return getViewName();
		}
		
		CustomUserDetails userDetail = (CustomUserDetails) userService.loadUserByUsername(form.getUserName());

		String jwt = jwtService.generateToken(userDetail);

		session.setAttribute(Constant.JWT, jwt);

		return Constant.REDIRECT + Screen.MAIN;
	}

	protected String getViewName() {
		return "/VP001";
	}
}
