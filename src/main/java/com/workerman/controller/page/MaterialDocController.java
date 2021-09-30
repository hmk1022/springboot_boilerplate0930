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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.workerman.config.Constants;
import com.workerman.controller.BaseController;
import com.workerman.service.CodeService;
import com.workerman.service.FileService;
import com.workerman.service.MaterialCategoryService;
import com.workerman.service.MaterialDocService;
import com.workerman.service.MaterialPcsService;
import com.workerman.service.MaterialService;
import com.workerman.service.VendorService;
import com.workerman.service.WorkService;
import com.workerman.utils.StringUtil;

@Controller
public class MaterialDocController extends BaseController{

	@Autowired
	private MaterialDocService materialDocService;

	@Autowired
	private FileService fileService;

	@Autowired
	private CodeService codeService;

	@Autowired
	private WorkService workService;

	@Autowired
	private MaterialPcsService materialPcsService;

	@Autowired
	private MaterialService materialService;

	@Autowired
	private MaterialCategoryService materialCategoryService;

	@Autowired
	private VendorService vendorService;

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : list
	 * @return : String
	 * @note :자재구매 리스트
	 */
	@GetMapping(path="/material/doc/list")
	public String list(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		model.addAttribute("receive_type", codeService.selectCodeByGb("receive_type")); // receive_type
		model.addAttribute("doc_stat", codeService.selectCodeByGb("doc_stat")); // doc_stat

		return "material/doc/list";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :자재구매
	 */
	@RequestMapping(path="/material/doc/list/ajax")
	public ResponseEntity<Map<String, Object>> listAjax(@RequestParam Map<String, Object> param) throws Exception {

		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		/* search */
		param.put("s_start_dt", StringUtil.remove((String)param.get("s_start_dt"), "-"));
		param.put("s_end_dt", StringUtil.remove((String)param.get("s_end_dt"), "-"));

		/* pagination */
		if( param.get("pq_rpp") != null) {
			int pageFirst = (Integer.parseInt( String.valueOf(param.get("pq_curpage")))-1) * Integer.parseInt( String.valueOf(param.get("pq_rpp")))  ;
			int pageLast = Integer.parseInt( String.valueOf(param.get("pq_rpp"))) ;
			param.put("pageFirst", pageFirst);
			param.put("pageLast", pageLast);

			returnMap.put("curPage", String.valueOf(param.get("pq_curpage")));
			/* get count */
			returnMap.put("totalRecords", materialDocService.listMaterialDocCnt(param));
		}

		/* get list */
		returnMap.put("data", materialDocService.listMaterialDoc(param));

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :자재구매 등록 폼
	 */
	@RequestMapping(path="/material/doc/form")
	public String form(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		/* case modify */
		if(StringUtils.isNoneBlank((String)param.get("material_doc_no"))){
			model.addAttribute("data", materialDocService.selectMaterialDoc(param));
		}

		/* 공통코드 */
		model.addAttribute("receive_type", codeService.selectCodeByGb("receive_type")); // receive_type
		model.addAttribute("doc_stat", codeService.selectCodeByGb("doc_stat")); // doc_stat
		model.addAttribute("unit_cd", codeService.selectCodeByGb("unit_cd")); // unit_cd

		return "material/doc/form";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :자재구매 등록 폼
	 */
	@PostMapping(path="/material/doc/save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> save(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {

		param.put("del_yn", Constants.NOT_DELETED); // 상태

		param.put("create_no", getAdminNo()); // admin_no
		param.put("update_no", getAdminNo()); // update_no
		param.put("doc_admin_no", getAdminNo()); // doc_admin_no
		param.put("doc_stat", "01"); // doc_stat

		param.put("use_date", StringUtil.remove((String)param.get("use_date"), "-"));
		param.put("material_cnt", StringUtil.remove((String)param.get("material_cnt"), ","));

		param.put("req_usage", Constants.MATERIAL_DOC_REQ_USAGE);

		Map<String, Object> returnMap = materialDocService.saveMaterialDoc(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :자재구매 삭제
	 */
	@GetMapping(path="/material/doc/delete")
	public ResponseEntity<Map<String, Object>> delete(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		materialDocService.deleteMaterialDoc(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :자재구매 등록 폼
	 */
	@PostMapping(path="/material/doc/view")
	public String view(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		if(StringUtils.isBlank((String)param.get("material_doc_no"))){
			throw new Exception(Constants.ERROR_EMPTY_KEY);
		}

		model.addAttribute("data", materialDocService.selectMaterialDoc(param));

		/* select file info */
		Map<String, Object> fileMap = new HashMap<String, Object>();
		fileMap.put("img_type", Constants.IMG_TYPE_CUSTOMER_BUSS);
		fileMap.put("key_no", param.get("material_doc_no").toString());
		model.addAttribute("imageList", fileService.listImgInfo(fileMap));

		model.addAttribute("doc_stat", codeService.selectCodeByGb("doc_stat")); // doc_stat

		return "material/doc/view";
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :자재요청 상태 변경
	 */
	@PostMapping(path="/material/doc/stat/save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> saveDocStat(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {

		param.put("del_yn", Constants.NOT_DELETED); // 상태

		param.put("create_no", getAdminNo()); // admin_no
		param.put("update_no", getAdminNo()); // update_no
		param.put("confirm_admin_no", getAdminNo()); // confirm_admin_no

		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(MESSAGES_KEY, materialDocService.updateDocStat(param));
		returnMap.put("confirm_admin_no", getAdminNo());
		returnMap.put("confirm_admin_name", getAdminName());

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}




	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : list
	 * @return : String
	 * @note :자재요청 리스트 구매정보/신규자재 수정폼.
	 */
	@GetMapping(path="/material/doc/bound/form/{_gubun}")
	public String boundMaterial(
			@RequestParam Map<String, Object> param,
			@PathVariable String _gubun,
			Model model
			) throws Exception {

		String _page = "material/form";
		if(StringUtils.equals("reg", _gubun)) {

			Map<String, Object> cateMap = new HashMap<String, Object>();
			cateMap.put("category_level", "1");
			model.addAttribute("cate1", materialCategoryService.selectCategoryList(cateMap));

			// 자재정보
			model.addAttribute("unit_cd", codeService.selectCodeByGb("unit_cd")); // unit_cd
			model.addAttribute("data", materialService.selectMaterial(param));
			_page = "material/doc/form_material";

		}
		else if(StringUtils.equals("pcs", _gubun)) {

			model.addAttribute("data", materialPcsService.selectMaterialPcs(param));
			/* 공통코드 */
			model.addAttribute("pcs_reason", codeService.selectCodeByGb("pcs_reason")); // pcs_type
			/* 거래처 */
			model.addAttribute("vendor", vendorService.listVendor(param));
//			_page = "material/pcs/form_doc";
			_page = "material/doc/form_pcs";
		}

		return _page;
	}


}
