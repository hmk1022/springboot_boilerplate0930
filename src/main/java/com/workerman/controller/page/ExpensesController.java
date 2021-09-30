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
import com.workerman.service.ExpensesService;
import com.workerman.service.FileService;
import com.workerman.service.PartnerService;
import com.workerman.service.VendorService;
import com.workerman.utils.StringUtil;

@Controller
public class ExpensesController extends BaseController{

	@Autowired
	private ExpensesService expensesService;

	@Autowired
	private FileService fileService;

	@Autowired
	private CodeService codeService;

	@Autowired
	private PartnerService partnerService;

	@Autowired
	private VendorService vendorService;

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : list
	 * @return : String
	 * @note :비용처리 리스트
	 */
	@GetMapping(path="/account/expenses/list")
	public String list(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		/* 공통코드 */
		model.addAttribute("exp_reject", codeService.selectCodeByGb("exp_reject")); // 반려사유코드
		model.addAttribute("pay_scope", codeService.selectCodeByGb("pay_scope")); // 결제범위

		model.addAttribute("exp_stat", codeService.selectCodeByGb("exp_stat")); // exp_stat
		model.addAttribute("work_target", codeService.selectCodeByGb("work_target")); // work_target
		model.addAttribute("exp_type", codeService.selectCodeByGb("exp_type")); // 결제수단

		return "account/expenses/list";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :고객사지점
	 */
	@RequestMapping(path="/account/expenses/list/ajax")
	public ResponseEntity<Map<String, Object>> listAjax(@RequestParam Map<String, Object> param) throws Exception {

		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		param.put("start_dt", StringUtils.remove((String)param.get("start_dt"), "-"));
		param.put("end_dt", StringUtils.remove((String)param.get("end_dt"), "-"));

		/* pagination */
		if( param.get("pq_rpp") != null) {
			int pageFirst = (Integer.parseInt( String.valueOf(param.get("pq_curpage")))-1) * Integer.parseInt( String.valueOf(param.get("pq_rpp")))  ;
			int pageLast = Integer.parseInt( String.valueOf(param.get("pq_rpp"))) ;
			param.put("pageFirst", pageFirst);
			param.put("pageLast", pageLast);

			returnMap.put("curPage", String.valueOf(param.get("pq_curpage")));
			/* get count */
			returnMap.put("totalRecords", expensesService.listExpensesCnt(param));
		}

		/* get list */
		returnMap.put("data", expensesService.listExpenses(param));

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :비용처리 등록 폼
	 */
	@RequestMapping(path="/account/expenses/form")
	public String form(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		/* case modify */
		if(StringUtils.isNoneBlank((String)param.get("exp_no"))){
			model.addAttribute("data", expensesService.selectExpenses(param));

			/* select file info */
			Map<String, Object> fileMap = new HashMap<String, Object>();
			fileMap.put("img_type", Constants.IMG_TYPE_EXPENSES);
			fileMap.put("key_no", param.get("exp_no").toString());
			model.addAttribute("imageList", fileService.listImgInfo(fileMap));
		}

		/* 공통코드 */
		model.addAttribute("exp_type", codeService.selectCodeByGb("exp_type")); // 비용분류
		model.addAttribute("exp_unit_cd", codeService.selectCodeByGb("exp_unit_cd")); // 비용단위
		model.addAttribute("bank_code", codeService.selectCodeByGb("vacct_bank_code")); // 은행코드

		/* 거래처 */
		model.addAttribute("vendor", vendorService.listVendor(param));

		/* 파트너 정보조회 */
		model.addAttribute("partner_list", partnerService.listPartner(null)); // partner list

		return "account/expenses/form";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :비용처리 등록 폼
	 */
	@PostMapping(path="/account/expenses/save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String save(
			@RequestParam Map<String, Object> param,
			@RequestParam("upLoadFile") MultipartFile[] files, // upload file
			Model model
			) throws Exception {

		System.out.println("param:" + param);

		param.put("del_yn", Constants.NOT_DELETED); // 상태

		param.put("create_no", getAdminNo()); // 상태
		param.put("update_no", getAdminNo()); // 상태

		param.put("exp_stat", "01"); // 상태

		param.put("st_date", StringUtils.remove((String)param.get("st_date"), "-")); //
		param.put("ed_date", StringUtils.remove((String)param.get("ed_date"), "-")); //
		param.put("pay_req_date", StringUtils.remove((String)param.get("pay_req_date"), "-")); //
		param.put("exp_price", StringUtils.remove((String)param.get("exp_price"), ",")); //
		param.put("exp_count", StringUtils.remove((String)param.get("exp_count"), ",")); //

		param.put("partner_no", StringUtil.toNull(param.get("partner_no"))); //
		param.put("vendor_no", StringUtil.toNull(param.get("vendor_no"))); //

		param.put("files", files); // file
		expensesService.saveExpenses(param);

		return Constants.EVENT_SAVE_SUCCESS;
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :비용처리 삭제
	 */
	@GetMapping(path="/account/expenses/delete")
	public ResponseEntity<Map<String, Object>> delete(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		expensesService.deleteExpenses(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :비용처리 등록 폼
	 */
	@PostMapping(path="/account/expenses/view")
	public String view(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		if(StringUtils.isBlank((String)param.get("exp_no"))){
			throw new Exception(Constants.ERROR_EMPTY_KEY);
		}

		model.addAttribute("data", expensesService.selectExpenses(param));

		/* select file info */
		Map<String, Object> fileMap = new HashMap<String, Object>();
		fileMap.put("img_type", Constants.IMG_TYPE_EXPENSES);
		fileMap.put("key_no", param.get("exp_no").toString());
		model.addAttribute("imageList", fileService.listImgInfo(fileMap));

		return "account/expenses/view";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :비용처리 반려처리
	 */
	@PostMapping(path="/account/expenses/reject/save/ajax")
	public ResponseEntity<Map<String, Object>> reject(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		param.put("create_no", getAdminNo()); // 로그인no
		param.put("update_no", getAdminNo()); // 로그인no
		param.put("confirm_admin_no", getAdminNo()); // 처리자no

		expensesService.updateExpStat(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

}
