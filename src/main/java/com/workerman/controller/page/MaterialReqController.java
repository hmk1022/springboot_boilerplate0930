package com.workerman.controller.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.workerman.service.FileService;
import com.workerman.service.MaterialReqService;
import com.workerman.utils.StringUtil;

@Controller
public class MaterialReqController extends BaseController{
	
	@Autowired
	private MaterialReqService materialReqService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired 
	private CodeService materialService;
	
	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : list
	 * @return : String
	 * @note :요청자재 리스트
	 */
	@GetMapping(path="/material/req/list")
	public String list(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {
	
		return "code/materialReq/list";
	}
	
	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :고객사지점
	 */
	@RequestMapping(path="/material/req/list/ajax")
	public ResponseEntity<Map<String, Object>> listAjax(
			@RequestBody Map<String, Object> param) throws Exception {
		
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		/* get list */
		returnMap.put("data", materialReqService.listMaterialReq(param));
		
		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}
	

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :요청자재 등록 폼
	 */
	@PostMapping(path="/material/req/save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String save(
			@RequestParam Map<String, Object> param,
			@RequestParam("upLoadFile") MultipartFile[] files, // upload file
			Model model
			) throws Exception {
		
//		Map<String, Object> param = getQueryMap();
		
//		if(StringUtil.equals((String)param.get("material_no"), "ADD")) {
//			param.put("material_no", null);
//		} 
		
		param.put("req_stat", "01");
		param.put("del_yn", Constants.NOT_DELETED); // del_yn
		
		param.put("create_no", getAdminNo()); // admin_no
		param.put("update_no", getAdminNo()); // admin_no

		param.put("files", files); // files

		param.put("brand_no", StringUtil.toNull(param.get("brand_no")));

		param.put("material_cnt", StringUtil.onlyNumStr(param.get("material_cnt")));
		param.put("purchased_price", StringUtil.onlyNumStr(param.get("purchased_price")));
		param.put("material_doc_no", StringUtil.toNull(param.get("material_doc_no")));
		param.put("category_no", StringUtil.toNull(param.get("category_no")));
		param.put("material_no", StringUtil.toNull(param.get("material_no")));
		
		materialReqService.saveMaterialReq(param);
		
		return Constants.EVENT_SAVE_SUCCESS;
	}
	
	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :요청자재 삭제
	 */
	@PostMapping(path="/material/req/delete/ajax")
	public ResponseEntity<Map<String, Object>> delete(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();
		param.put("update_no", getAdminNo()); // admin_no
		materialReqService.deleteMaterialReq(param);
		
		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :자재요청 자재 상태변경
	 */
	@RequestMapping(path="/material/req/stat/save/ajax")
	public ResponseEntity<Map<String, Object>> udpateReqStat(
			@RequestBody Map<String, Object> param) throws Exception {
		
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();
		param.put("create_no", getAdminNo()); // admin_no
		param.put("update_no", getAdminNo()); // admin_no
		materialReqService.updateMaterialReqStat(param);
		
		// 자재등록일 경우.
		String _req_stat = (String)param.get("req_stat");
		if(StringUtil.equals(Constants.REQ_STAT_REG, _req_stat)) {
			List<Map<String, Object>> materialList = new ArrayList<Map<String, Object>>();
			String[] material_req_no_arr = StringUtil.split((String)param.get("material_req_no_str"), "^");
			for(String material_req_no : material_req_no_arr) {
				/* 요청자재 조회 */
				param.put("material_req_no", material_req_no);
				Map<String, Object> materialMap = materialReqService.selectMaterialReq(param);
				materialList.add(materialMap);
			}
			param.put("materialList", materialList);
		}

		return new ResponseEntity<Map<String, Object>>(param, HttpStatus.OK);
	}
}
