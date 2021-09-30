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
import org.springframework.web.bind.annotation.ResponseStatus;

import com.workerman.controller.BaseController;
import com.workerman.service.CodeService;
import com.workerman.service.FileService;
import com.workerman.service.WorkCategoryService;

@Controller
public class WorkCategoryController extends BaseController{
	
	@Autowired
	private WorkCategoryService workCategoryService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired 
	private CodeService codeService;
	
	
	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :카테고리 목록 list ajax
	 */
	@RequestMapping(path="/work/category/list/ajax")
	public ResponseEntity<Map<String, Object>> selectCategoryList(
			@RequestBody Map<String, Object> param) throws Exception {
		
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		/* get list */
		returnMap.put("data", workCategoryService.selectCategoryList(param));
		
		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}
	
}
