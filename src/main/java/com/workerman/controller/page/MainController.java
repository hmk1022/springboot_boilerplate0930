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
public class MainController extends BaseController{

	@Autowired
	private AdminService adminService;

	/**
	 * @Author : kwonsco
	 * @Date	: 2021. 03. 24.
	 * @Method Name : main
	 * @return : String
	 * @note : return page main
	 */
	@GetMapping(path="/main")
	public String main(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {
		
		return "view:/comm/main";
	}

	@GetMapping(path="/left_menu")
	public String leftMenu(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {
		
		param.put("admin_no",param.get("admin_no"));
		model.addAttribute("menuTree", adminService.selectMenuTree(param));
		model.addAttribute("profile", adminService.selectAdminProfile(param));

		return "comm/left_menu";
	}
}
