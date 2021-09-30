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
import com.workerman.service.WarehouseService;
import com.workerman.utils.StringUtil;
import com.workerman.service.FileService;

@Controller
public class WarehouseController extends BaseController{

	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private FileService fileService;

	@Autowired
	private CodeService codeService;

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : list
	 * @return : String
	 * @note :물류센터 리스트
	 */
	@GetMapping(path="/material/warehouse/list")
	public String list(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		return "material/warehouse/list";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :물류센터 list ajax
	 */
	@RequestMapping(path="/material/warehouse/list/ajax")
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
			returnMap.put("totalRecords", warehouseService.listWarehouseCnt(param));
		}

		/* get list */
		returnMap.put("data", warehouseService.listWarehouse(param));

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :물류센터 등록 폼
	 */
//	@RequestMapping(path="/material/warehouse/form")
//	public String form(
//			@RequestParam Map<String, Object> param,
//			Model model
//			) throws Exception {
//
//		/* case modify */
//		if(StringUtils.isNoneBlank((String)param.get("warehouse_no"))){
//			model.addAttribute("data", warehouseService.selectWarehouse(param));
//		}
//
//		/* 공통코드 */
//		model.addAttribute("work_cate", codeService.selectCodeByGb("work_cate")); // work_cate
//		model.addAttribute("vacct_bank_code", codeService.selectCodeByGb("vacct_bank_code")); // bank_code
//
//		return "material/warehouse/form";
//	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :물류센터 등록 폼
	 */
//	@PostMapping(path="/material/warehouse/save/ajax")
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
//
//		param.put("id_no_fin_yn", StringUtil.nvl((String)param.get("id_no_fin_yn"),"N"));
//		param.put("acct_fin_yn", StringUtil.nvl((String)param.get("acct_fin_yn"),"N"));
//
//		param.put("warehouse_hp1", StringUtil.onlyNumStr(param.get("warehouse_hp1")));
//		param.put("warehouse_hp2", StringUtil.onlyNumStr(param.get("warehouse_hp2")));
//
//		warehouseService.saveWarehouse(param);
//
//		return Constants.EVENT_SAVE_SUCCESS;
//	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :물류센터 삭제
	 */
//	@GetMapping(path="/material/warehouse/delete")
//	public ResponseEntity<Map<String, Object>> delete(
//			@RequestBody Map<String, Object> param,
//			Model model
//			) throws Exception {
//		/* return map */
//		Map<String,Object> returnMap = new HashMap<String,Object>();
//
//		warehouseService.deleteWarehouse(param);
//
//		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
//	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :물류센터 등록 폼
	 */
//	@PostMapping(path="/material/warehouse/view")
//	public String view(
//			@RequestParam Map<String, Object> param,
//			Model model
//			) throws Exception {
//
//		if(StringUtils.isBlank((String)param.get("warehouse_no"))){
//			throw new Exception(Constants.ERROR_EMPTY_KEY);
//		}
//
//		model.addAttribute("data", warehouseService.selectWarehouse(param));
//
//		/* select file info */
//		Map<String, Object> fileMap = new HashMap<String, Object>();
//		fileMap.put("img_type", Constants.IMG_TYPE_CUSTOMER_BUSS);
//		fileMap.put("key_no", param.get("warehouse_no").toString());
//		model.addAttribute("imageList", fileService.listImgInfo(fileMap));
//
//		return "material/warehouse/view";
//	}
}
