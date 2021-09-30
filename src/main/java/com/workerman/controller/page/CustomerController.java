package com.workerman.controller.page;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.workerman.service.CodeService;
import com.workerman.service.CustomerService;
import com.workerman.service.FileService;

@Controller
public class CustomerController extends BaseController{

	@Autowired
	private CustomerService customerService;

	@Autowired
	private FileService fileService;

	@Autowired
	private CodeService codeService;

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : list
	 * @return : String
	 * @note :고객사 리스트
	 */
	@GetMapping(path="/code/customer/list")
	public String list(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		return "code/customer/list";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :고객사지점
	 */
	@RequestMapping(path="/code/customer/list/ajax")
	public ResponseEntity<Map<String, Object>> listAjax(@RequestParam Map<String, Object> param) throws Exception {

		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		/* pagination */
		if( param.get("pq_rpp") != null) {
			int pageFirst = (Integer.parseInt( String.valueOf(param.get("pq_curpage")))-1) * Integer.parseInt( String.valueOf(param.get("pq_rpp")))  ;
			int pageLast = Integer.parseInt( String.valueOf(param.get("pq_rpp"))) ;
			param.put("pageFirst", pageFirst);
			param.put("pageLast", pageLast);

			returnMap.put("curPage", String.valueOf(param.get("pq_curpage")));
			/* get count */
			returnMap.put("totalRecords", customerService.listCustomerCnt(param));
		}

		/* get list */
		returnMap.put("data", customerService.listCustomer(param));

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :고객사 등록 폼
	 */
	@RequestMapping(path="/code/customer/form")
	public String form(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		/* case modify */
		if(StringUtils.isNoneBlank((String)param.get("customer_no"))){
			model.addAttribute("data", customerService.selectCustomer(param));

			/* select file info */
			Map<String, Object> fileMap = new HashMap<String, Object>();
			fileMap.put("img_type", Constants.IMG_TYPE_CUSTOMER_BUSS);
			fileMap.put("key_no", param.get("customer_no").toString());
			model.addAttribute("imageList", fileService.listImgInfo(fileMap));
		}

		/* 공통코드 */
		model.addAttribute("position", codeService.selectCodeByGb("position")); // work_cate

		return "code/customer/form";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :고객사 등록 폼
	 */
	@PostMapping(path="/code/customer/save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String save(
			@RequestParam Map<String, Object> param,
			@RequestParam("upLoadFile") MultipartFile[] files, // upload file
			Model model
			) throws Exception {

		System.out.println("param:" + param);

		param.put("stat", Constants.STATE_NORMAL); // 상태
		param.put("del_yn", Constants.NOT_DELETED); // 상태

		param.put("create_no", getAdminNo()); // 상태
		param.put("update_no", getAdminNo()); // 상태

		param.put("deal_start_date", StringUtils.remove((String)param.get("deal_start_date"), "-")); // 상태



		param.put("files", files); // file
		customerService.saveCustomer(param);

		return Constants.EVENT_SAVE_SUCCESS;
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :고객사 삭제
	 */
	@GetMapping(path="/code/customer/delete")
	public ResponseEntity<Map<String, Object>> delete(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		customerService.deleteCustomer(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :고객사 등록 폼
	 */
	@PostMapping(path="/code/customer/view")
	public String view(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		if(StringUtils.isBlank((String)param.get("customer_no"))){
			throw new Exception(Constants.ERROR_EMPTY_KEY);
		}

		Map<String, Object> resutMap = customerService.selectCustomer(param);

		model.addAttribute("data", resutMap);

		model.addAttribute("branch", customerService.listCustomerBranch(resutMap));

		/* select file info */
		Map<String, Object> fileMap = new HashMap<String, Object>();
		fileMap.put("img_type", Constants.IMG_TYPE_CUSTOMER_BUSS);
		fileMap.put("key_no", param.get("customer_no").toString());
		model.addAttribute("imageList", fileService.listImgInfo(fileMap));

		return "code/customer/view";
	}
}
