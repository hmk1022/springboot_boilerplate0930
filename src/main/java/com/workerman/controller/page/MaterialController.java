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
import com.workerman.service.FileService;
import com.workerman.service.MaterialBoundInoutService;
import com.workerman.service.MaterialCategoryService;
import com.workerman.service.MaterialDocService;
import com.workerman.service.MaterialService;
import com.workerman.utils.StringUtil;

@Controller
public class MaterialController extends BaseController{

	@Autowired
	private MaterialService materialService;

	@Autowired
	private FileService fileService;

	@Autowired
	private CodeService codeService;

	@Autowired
	private MaterialBoundInoutService materialBoundInoutService;

	@Autowired
	private MaterialDocService materialDocService;

	@Autowired
	private MaterialCategoryService materialCategoryService;

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : list
	 * @return : String
	 * @note :물류센터 리스트
	 */
	@GetMapping(path="/material/list")
	public String list(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		return "material/list";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :물류센터
	 */
	@RequestMapping(path="/material/list/ajax")
	@ResponseBody
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
			returnMap.put("totalRecords", materialService.listMaterialCnt(param));
		}

		/* get list */
		returnMap.put("data", materialService.listMaterial(param));

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :물류센터
	 */
	@RequestMapping(path="/material/select/list/ajax")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> listMaterial(@RequestBody Map<String, Object> param) throws Exception {

		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();
		/* get list */
		returnMap.put("data", materialService.listMaterial(param));

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :물류센터 등록 폼
	 */
	@RequestMapping(path="/material/form")
	public String form(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		Map<String, Object> cateMap = new HashMap<String, Object>();
		cateMap.put("category_level", "1");
		model.addAttribute("cate1", materialCategoryService.selectCategoryList(cateMap));

		/* case modify */
		if(StringUtils.isNoneBlank((String)param.get("material_no"))){

			Map<String, Object> data = materialService.selectMaterial(param);
			model.addAttribute("data", data);

			/* select file info */
			Map<String, Object> fileMap = new HashMap<String, Object>();	// 이미지
			fileMap.put("img_type", Constants.IMG_TYPE_MATERIAL);
			fileMap.put("key_no", param.get("material_no").toString());
			model.addAttribute("imageList", fileService.listImgInfo(fileMap));

			String _category_id = (String)data.get("category_id");

			cateMap = new HashMap<String, Object>();
			cateMap.put("category_level", "2");
			param.put("p_category_id", StringUtil.substring(_category_id, 0, 5));
			model.addAttribute("cate2", materialCategoryService.selectCategoryList(cateMap));
			cateMap = new HashMap<String, Object>();
			cateMap.put("category_level", "3");
			param.put("p_category_id", StringUtil.substring(_category_id, 0, 8));
			model.addAttribute("cate3", materialCategoryService.selectCategoryList(cateMap));
		}

		/* 공통코드 */
		model.addAttribute("unit_cd", codeService.selectCodeByGb("unit_cd")); // unit_cd

		return "material/form";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :물류센터 등록 폼
	 */
	@PostMapping(path="/material/save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String save(
			@RequestParam Map<String, Object> param,
			@RequestParam("upLoadFile") MultipartFile[] files, // upload file
			Model model
			) throws Exception {

		param.put("del_yn", Constants.NOT_DELETED); // 상태

		param.put("member_open_yn", StringUtil.ifNull((String)param.get("member_open_yn"),"N")); // 상태

		param.put("create_no", getAdminNo()); // admin_no
		param.put("update_no", getAdminNo()); // admin_no

		/* to null */
		param.put("workerman_price", StringUtil.toNull(param.get("workerman_price")));
		param.put("multi_price", StringUtil.toNull(param.get("multi_price")));
		param.put("confirm_admin_no", StringUtil.toNull(param.get("confirm_admin_no")));
		param.put("confirm_date", StringUtil.toNull(param.get("confirm_date")));

		param.put("workerman_price", StringUtil.toNull(param.get("workerman_price")));
		param.put("sale_price", StringUtil.toNull(param.get("sale_price")));
		param.put("purchased_price", StringUtil.toNull(param.get("purchased_price")));
		param.put("multi_price", StringUtil.toNull(param.get("multi_price")));

		param.put("workerman_price", StringUtil.onlyNumStr(param.get("workerman_price")));
		param.put("sale_price", StringUtil.onlyNumStr(param.get("sale_price")));
		param.put("purchased_price", StringUtil.onlyNumStr(param.get("purchased_price")));
		param.put("multi_price", StringUtil.onlyNumStr(param.get("multi_price")));

		param.put("brand_no", StringUtil.toNull(param.get("brand_no")));
		param.put("category_no", StringUtil.toNull(param.get("category_no")));
		
		param.put("files", files); // file
		materialService.saveMaterial(param);

		return Constants.EVENT_SAVE_SUCCESS;
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :물류센터 삭제
	 */
	@GetMapping(path="/material/delete")
	public ResponseEntity<Map<String, Object>> delete(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		materialService.deleteMaterial(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :물류센터 등록 폼
	 */
	@PostMapping(path="/material/view")
	public String view(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		if(StringUtils.isBlank((String)param.get("material_no"))){
			throw new Exception(Constants.ERROR_EMPTY_KEY);
		}

		model.addAttribute("data", materialService.selectMaterial(param));

		/* select file info */
		Map<String, Object> fileMap = new HashMap<String, Object>(); //
		fileMap.put("img_type", Constants.IMG_TYPE_MATERIAL);
		fileMap.put("key_no", param.get("material_no").toString());
		model.addAttribute("imageList", fileService.listImgInfo(fileMap));

		/* 재고현황 */
		model.addAttribute("inoutList", materialBoundInoutService.listMaterialBoundInout(param));

		/* 연결된시공 */
		//model.addAttribute("docList", materialDocService.listMaterialDoc(param));


		return "material/view";
	}
}
