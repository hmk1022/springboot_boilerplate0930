package com.workerman.controller.page;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.workerman.config.Constants;
import com.workerman.controller.BaseController;
import com.workerman.service.CodeService;
import com.workerman.service.EstimateResourceService;
import com.workerman.service.WorkService;

@Controller
public class EstimateResourceController extends BaseController{


	@Autowired
	private CodeService codeService;

	@Autowired
	private EstimateResourceService estimateResourceService;

	@Autowired
	private WorkService workService;

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : list
	 * @return : String
	 * @note :견적서 리소스 리스트
	 */
//	@GetMapping(path="/estimate/list")
//	public String list(
//			@RequestParam Map<String, Object> param,
//			Model model
//			) throws Exception {
//
//		return "estimate/list";
//	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :고객사지점
	 */
//	@RequestMapping(path="/estimate/list/ajax")
//	public ResponseEntity<Map<String, Object>> listAjax(@RequestParam Map<String, Object> param) throws Exception {
//
//		/* return map */
//		Map<String,Object> returnMap = new HashMap<String,Object>();
//
//		//List<Map<String, Object>> data = estimateResourceService.listEstimateResource(param);
//
//
//		List<Map<String, Object>> data =workService.selectWorkParentList(param);
//
//		for(Map<String, Object> tmp : data) {
//			returnMap.put("tbody"+tmp.get("work_no"), estimateResourceService.listEstimateResource(tmp) );
//		}
//
//		/* get list */
//		returnMap.put("workList", data);
//
//		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
//	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :견적서 리소스 등록 폼
	 */
//	@RequestMapping(path="/estimate/form")
//	public String form(
//			@RequestParam Map<String, Object> param,
//			Model model
//			) throws Exception {
//
//		param.put("work_no", 2859);
//		/* return map */
//		Map<String,Object> returnMap = new HashMap<String,Object>();
//
//		//List<Map<String, Object>> data = estimateResourceService.listEstimateResource(param);
//
//
//		List<Map<String, Object>> data = workService.selectWorkParentList(param);
//
//		for(Map<String, Object> tmp : data) {
//			model.addAttribute("estimate_"+tmp.get("work_no"), estimateResourceService.listEstimateResource(tmp) );
//		}
//
//		/* get list */
//		model.addAttribute("workList", data);
//
//		/* 공통코드 */
//		model.addAttribute("unit_cd", codeService.selectCodeByGb("unit_cd")); // unit_cd
//		model.addAttribute("resource_type", codeService.selectCodeByGb("resource_type")); // estimate_type
//
//		return "estimate/form";
//	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :견적서 리소스 등록 폼
	 */
//	@PostMapping(path="/estimate/save/ajax")
//	@ResponseStatus(HttpStatus.CREATED)
//	@ResponseBody
//	public String save(
//			@RequestBody Map<String, Object> param,
//			Model model
//			) throws Exception {
//
////		Map<String, Object> param = getQueryMap();
//
//		param.put("del_yn", Constants.NOT_DELETED); // 상태
//
//		param.put("create_no", getAdminNo()); // admin_no
//		param.put("update_no", getAdminNo()); // admin_no
//		param.put("admin_no", getAdminNo()); // admin_no
//
//		estimateResourceService.saveEstimateResource(param);
//
//		return Constants.EVENT_SAVE_SUCCESS;
//	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :견적서 리소스 삭제
	 */
	@PostMapping(path="/estimate/resource/delete")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delete(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		param.put("del_yn", Constants.DELETED); // 상태
		param.put("update_no", getAdminNo()); // admin_no

		estimateResourceService.deleteEstimateResource(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


//	/**
//	 * @Author : dw.kwon
//	 * @Date	: 2021. 03. 24.
//	 * @Method Name : form
//	 * @return : String
//	 * @note :견적서 리소스 등록 폼
//	 */
//	@PostMapping(path="/estimate/view")
//	public String view(
//			@RequestParam Map<String, Object> param,
//			Model model
//			) throws Exception {
//
//		if(StringUtils.isBlank((String)param.get("estimate_no"))){
//			throw new Exception(Constants.ERROR_EMPTY_KEY);
//		}
//
//		model.addAttribute("data", estimateResourceService.selectEstimateResource(param));
//
//		/* select file info */
//		Map<String, Object> fileMap = new HashMap<String, Object>();
//		fileMap.put("img_type", Constants.IMG_TYPE_CUSTOMER_BUSS);
//		fileMap.put("key_no", param.get("estimate_no").toString());
//		model.addAttribute("imageList", fileService.listImgInfo(fileMap));
//
//		return "estimate/view";
//	}
}
