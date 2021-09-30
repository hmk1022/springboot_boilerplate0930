package com.workerman.controller.page;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.workerman.service.AdminGpsHistService;
import com.workerman.service.AdminService;
import com.workerman.service.CodeService;
import com.workerman.service.CompanyService;
import com.workerman.service.CustomerService;
import com.workerman.service.WorkermanService;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/operation/workerman/*")
public class WorkermanController extends BaseController{

	@Autowired
	private WorkermanService workermanService;

	@Autowired
	private CodeService codeService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private AdminService adminService;

	/**
	 * @Author : sbahn
	 * @Date	: 2021. 05. 27.
	 * @Method Name : list
	 * @return : String
	 * @note : Workerman 계정관리
	 */
	@GetMapping(path="index")
	public String index(
			@RequestParam Map<String, Object> param,
			ModelMap model
			) throws Exception {


		 model.put("admin_cd", codeService.selectCodeByGbString("admin_cd")); // 관리자코드
		 model.put("stat_cd", codeService.selectCodeByGbString("stat_cd")); // 상태코드
		 model.put("admin_cate", codeService.selectCodeByGbString("admin_cate")); //
		 model.put("department", codeService.selectCodeByGbString("department"));
		 model.put("position", codeService.selectCodeByGbString("position")); //
		 model.put("company", companyService.selectCompanyList()); // 업체

		return "operation/workerman/index";
	}

	/**
	 * @Author : sbahn
	 * @Date	: 2021. 05. 27.
	 * @Method Name : list
	 * @return : String
	 * @note : Workerman 계정관리 리스트
	 */
	@PostMapping(path="list/ajax")
	public ResponseEntity<Map<String, Object>> list(@RequestParam Map<String, Object> param) throws Exception {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		int pqCurpage = Integer.valueOf(  (String) param.getOrDefault("pq_curpage","0") );
		int pqRpp = Integer.valueOf(  (String) param.getOrDefault("pq_rpp","50") );
		if(pqCurpage > 0) {
			int pageFirst = (pqCurpage-1) * pqRpp  ;
			int pageLast = pqRpp ;
			param.put("pageFirst", pageFirst);
			param.put("pageLast", pageLast);
		}
		param.put("admin_type", "02");
		List<Map<String, Object>> list = workermanService.workermanList(param);
		int totalRecords = workermanService.workermanListCnt(param);
		returnMap.put("data", list);
		returnMap.put("curPage", String.valueOf(param.get("pq_curpage")));
		returnMap.put("totalRecords", totalRecords);
		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}



	/**
	 * @Author : sbahn
	 * @Date	: 2021. 06. 10.
	 * @Method Name : form
	 * @return : String
	 * @note :계정 등록 폼
	 */
	@RequestMapping(path="form")
	public String form(
			@RequestParam Map<String, Object> param,
			ModelMap model
			) throws Exception {

		 Map<String, Object> image_map = new HashMap<String,Object>();
		 JSONArray jsonArray = new JSONArray();

		 model.put("admin_cd", codeService.selectCodeByGbString("admin_cd")); // 관리자코드
		 model.put("stat_cd", codeService.selectCodeByGbString("stat_cd")); // 상태코드
		 model.put("admin_cate", codeService.selectCodeByGbString("admin_cate")); //
		 model.put("department", codeService.selectCodeByGbString("department"));
		 model.put("position", codeService.selectCodeByGbString("position")); //
		 model.put("company", companyService.selectCompanyList()); // 업체

		 System.out.println("param5"+param);
		 model.addAttribute("special_cd",jsonArray.fromObject(adminService.selectAdminSpecialByAdminNo(param)));

		/* case modify */

		if(StringUtils.isNoneBlank((String)param.get("admin_no"))){
			 model.addAttribute("data",adminService.selectAdmin(param));
			 //image_map.put("img_url",(String)
			 //adminService.selectAdmin(param).get("profile_url")); // w_img_info에서 가져와야한다

			 //selectAdminImg
			 //model.addAttribute("imageList",b2bWorkService.reportPostImg(param));

			 List<Map<String, Object>> imageList = new ArrayList<Map<String, Object>>();

			 imageList.add(adminService.selectAdminImg(param));
			 model.addAttribute("imageList",imageList);

		}

		return "operation/workerman/form";
	}

	/**
	 * @Author : sbahn
	 * @Date	: 2021. 06. 10.
	 * @Method Name : form
	 * @return : String
	 * @note :계정 등록 폼
	 */
	@PostMapping(path="save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String save(
			@RequestParam Map<String, Object> param,
			@RequestParam("upLoadFile") MultipartFile[] files, // upload file
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		System.out.println("param:" + param);
		param.put("files", files); // file
		workermanService.saveAdmin(param);

		return Constants.EVENT_SAVE_SUCCESS;
	}

	/**
	 * 계정상태갱신
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value="updateAdminStat")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String updateAdminStat(@RequestBody Map<String, Object> param) throws Exception{
		workermanService.updateAdminStat(param);
		return Constants.EVENT_SAVE_SUCCESS;
	}

}
