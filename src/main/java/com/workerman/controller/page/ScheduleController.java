package com.workerman.controller.page;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.workerman.config.Constants;
import com.workerman.controller.BaseController;
import com.workerman.service.AdminScheduleService;
import com.workerman.service.AdminService;
import com.workerman.service.B2bWorkService;
import com.workerman.service.CodeService;
import com.workerman.service.CompanyService;
import com.workerman.service.CustomerService;
import com.workerman.service.EstimateService;
import com.workerman.utils.Utils;

@Controller
@RequestMapping(value = "/schedule/*")
public class ScheduleController extends BaseController{

	@Autowired
	private B2bWorkService b2bWorkService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private AdminScheduleService adminScheduleService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private EstimateService estimateService;

	@Autowired
	private CodeService codeService;


	/**
	 * @Author : sbahn
	 * @Date	: 2021. 06. 21.
	 * @Method Name : view
	 * @return : String
	 * @note : view
	 */
	@GetMapping(path="view")
	public String view(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		return "schedule/view";
	}

	/**
	 * @Author : sbahn
	 * @Date	: 2021. 06. 10.
	 * @Method Name :
	 * @return : String
	 * @note :일정관리 관리자 조회
	 */
	@PostMapping(path="admin/list/ajax")
	public  ResponseEntity<Map<String, Object>> scheduleAdminList(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = adminService.scheduleAdminList(param);
		returnMap.put("data", list);
		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}
	/**
	 * @Author : sbahn
	 * @Date	: 2021. 06. 10.
	 * @Method Name :
	 * @return : String
	 * @note :일정조회
	 */
	@PostMapping(path="list/ajax")
	public  ResponseEntity<Map<String, Object>> scheduleList(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = adminScheduleService.scheduleList(param);
		returnMap.put("data", list);
		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


	@RequestMapping(value="save")
	@ResponseBody
	public String save(Map<String, Object> map,
			@RequestParam Map<String, Object> param,
			Model model,
			HttpServletRequest request) throws SQLException {

		String result = "99"; // return fail value

		try{
			System.out.println("* param : " + param.toString());

			param.put("update_admin_no", getAdminNo());
			param.put("admin_no", (String)param.get("admin_no"));

			if(StringUtils.isBlank((String)param.get("admin_no")) ||
					StringUtils.isBlank((String)param.get("st_date_ymd"))) {
				throw new Exception("st_date_ymd, st_date_ymd, st_date_ymd is null !!!");
			}

			//param.put("st_date_ymd", StringUtils.remove((String)param.get("st_date_ymd"), "-"));

			adminScheduleService.insertMySchedule(param,0);

			return "00";

		}catch(Exception e){
			return result;
		}
	}
}
