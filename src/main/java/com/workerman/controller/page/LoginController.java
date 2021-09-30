package com.workerman.controller.page;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.workerman.controller.BaseController;
import com.workerman.service.AdminGpsHistService;
import com.workerman.service.AdminService;

//@RestController
//@Api(description = "sample api")
@Controller
public class LoginController extends BaseController{
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private AdminGpsHistService adminGpsHistService;
	
	
	/**
	 * @Author : kwonsco
	 * @Date	: 2021. 03. 24.
	 * @Method Name : main
	 * @return : String
	 * @note : return page main
	 */
	@GetMapping(path="/login")
	public String main(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {
	
		return "/index.html";
	}
		
}
