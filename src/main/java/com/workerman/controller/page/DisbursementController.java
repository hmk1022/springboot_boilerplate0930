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
import com.workerman.service.DisbursementService;
import com.workerman.service.FileService;
import com.workerman.service.MemberService;

@Controller
public class DisbursementController extends BaseController{

	@Autowired
	private DisbursementService disbursementService;

	@Autowired
	private FileService fileService;

	@Autowired
	private CodeService codeService;

	@Autowired
	private MemberService memberService;


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : list
	 * @return : String
	 * @note :지출결의 리스트
	 */
	@GetMapping(path="/account/disbursement/list")
	public String list(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		model.addAttribute("disb_reject", codeService.selectCodeByGb("disb_reject")); // disb_reject
		model.addAttribute("disb_usage", codeService.selectCodeByGb("disb_usage")); // disb_type
		return "account/disbursement/list";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :지출결의지점
	 */
	@RequestMapping(path="/account/disbursement/list/ajax")
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
			returnMap.put("totalRecords", disbursementService.listDisbursementCnt(param));
		}

		/* get list */
		returnMap.put("data", disbursementService.listDisbursement(param));

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :지출결의 등록 폼
	 */
	@RequestMapping(path="/account/disbursement/form")
	public String form(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		/* case modify */
//		if(StringUtils.isNoneBlank((String)param.get("disb_no"))){
//			model.addAttribute("data", disbursementService.selectDisbursement(param));
//
//			/* select file info */
//			Map<String, Object> fileMap = new HashMap<String, Object>();
//			fileMap.put("img_type", Constants.IMG_TYPE_DISBURSEMENT);
//			fileMap.put("key_no", param.get("disb_no").toString());
//			model.addAttribute("imageList", fileService.listImgInfo(fileMap));
//		}

		/* 공통코드 */
		model.addAttribute("department", codeService.selectCodeByGb("department")); //
		model.addAttribute("disb_type", codeService.selectCodeByGb("disb_type")); //
		model.addAttribute("disb_usage", codeService.selectCodeByGb("disb_usage")); //

		model.addAttribute("member", memberService.selectMemberInfo(getAdminNo())); //


		return "account/disbursement/form";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :지출결의 등록 폼
	 */
	@PostMapping(path="/account/disbursement/save/ajax")
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
		param.put("admin_no", getAdminNo()); // 상태

		param.put("disb_stat", "01"); // 상태

		param.put("disb_date", StringUtils.remove((String)param.get("disb_date"), "-")); //
		param.put("disb_amt", StringUtils.remove((String)param.get("disb_amt"), ",")); //

		param.put("files", files); // file

		disbursementService.saveDisbursement(param);

		return Constants.EVENT_SAVE_SUCCESS;
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :지출결의 삭제
	 */
	@GetMapping(path="/account/disbursement/delete")
	public ResponseEntity<Map<String, Object>> delete(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		disbursementService.deleteDisbursement(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :지출결의 등록 폼
	 */
	@PostMapping(path="/account/disbursement/view")
	public String view(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		if(StringUtils.isBlank((String)param.get("disb_no"))){
			throw new Exception(Constants.ERROR_EMPTY_KEY);
		}

		model.addAttribute("data", disbursementService.selectDisbursement(param));

		/* select file info */
		Map<String, Object> fileMap = new HashMap<String, Object>();
		fileMap.put("img_type", Constants.IMG_TYPE_DISBURSEMENT);
		fileMap.put("key_no", param.get("disb_no").toString());
		model.addAttribute("imageList", fileService.listImgInfo(fileMap));

		return "account/disbursement/view";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :지출결의 삭제
	 */
	@PostMapping(path="/account/disbursement/stat/save/ajax")
	public ResponseEntity<Map<String, Object>> stat(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();
		param.put("confirm_admin_no", getAdminNo()); // 상태
		disbursementService.updateDisbursement(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

}
