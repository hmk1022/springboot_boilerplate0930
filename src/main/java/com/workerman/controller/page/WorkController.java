package com.workerman.controller.page;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.workerman.controller.BaseController;
import com.workerman.service.CodeService;
import com.workerman.service.FileService;
import com.workerman.service.WorkService;

@Controller
public class WorkController extends BaseController{

	@Autowired
	private WorkService workService;

	@Autowired
	private FileService fileService;

	@Autowired
	private CodeService codeService;


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :B2B/B2C 작업목록 list ajax
	 */
	@RequestMapping(path="/work/list/ajax")
	public ResponseEntity<Map<String, Object>> listAjax(@RequestBody Map<String, Object> param) throws Exception {

		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		/* get list */
		returnMap.put("data", workService.selectWorkList(param));

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :B2B/B2C 작업자목록 list ajax
	 */
	@RequestMapping(path="/work/admin/list/ajax")
	public ResponseEntity<Map<String, Object>> listAdminAjax(@RequestBody Map<String, Object> param) throws Exception {

		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		/* 관련어드민목록 */
		returnMap.put("data", workService.selectWorkAdminList(param));

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}
	
	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :B2B/B2C 작업목록 list ajax
	 */
	@RequestMapping(path="/work/list/auto/ajax")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> listAutoAjax(@RequestParam Map<String, Object> param) throws Exception {

		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		/* get list */
		returnMap.put("data", workService.selectAutoWorkList(param));

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}
}
