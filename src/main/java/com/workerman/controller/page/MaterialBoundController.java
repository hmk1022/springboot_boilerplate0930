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
import com.workerman.service.AdminService;
import com.workerman.service.CodeService;
import com.workerman.service.FileService;
import com.workerman.service.MaterialBoundService;
import com.workerman.service.VendorService;
import com.workerman.service.WorkService;
import com.workerman.utils.StringUtil;

@Controller
public class MaterialBoundController extends BaseController{

	@Autowired
	private MaterialBoundService materialBoundService;

	@Autowired
	private FileService fileService;

	@Autowired
	private CodeService codeService;

	@Autowired
	private WorkService workService;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private AdminService adminService;

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : list
	 * @return : String
	 * @note :전표 리스트
	 */
	@GetMapping(path="/material/bound/list")
	public String list(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		return "material/bound/list";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :전표
	 */
	@RequestMapping(path="/material/bound/list/ajax")
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
			returnMap.put("totalRecords", materialBoundService.listMaterialBoundCnt(param));
		}

		/* get list */
		returnMap.put("data", materialBoundService.listMaterialBound(param));

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :전표 등록 폼
	 */
	@RequestMapping(path="/material/bound/in/form")
	public String formIn(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		/* case modify */
		if(StringUtils.isNoneBlank((String)param.get("material_bound_no"))){
			model.addAttribute("data", materialBoundService.selectMaterialBound(param));

			/* select file info */
			Map<String, Object> fileMap = new HashMap<String, Object>();	// 거래처 사업자등록증사본
			fileMap.put("img_type", Constants.IMG_TYPE_MATERIAL_PCS);
			fileMap.put("key_no", param.get("material_bound_no").toString());
			model.addAttribute("imageList", fileService.listImgInfo(fileMap));
		}

		/* 공통코드 */
		model.addAttribute("bound_locate", codeService.selectCodeByGb("bound_locate")); // bound_locate
		model.addAttribute("income_type", codeService.selectCodeByGb("income_type")); // income_type
		model.addAttribute("receive_type", codeService.selectCodeByGb("receive_type")); // income_type
		model.addAttribute("unit_cd", codeService.selectCodeByGb("unit_cd")); // unit_cd

		/* 거래처 */
		model.addAttribute("vendor", vendorService.listVendor(param));

		return "material/bound/in";
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :전표 등록 폼
	 */
	@RequestMapping(path="/material/bound/out/form")
	public String formOut(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		/* case modify */
		if(StringUtils.isNoneBlank((String)param.get("material_bound_no"))){
			model.addAttribute("data", materialBoundService.selectMaterialBound(param));

			/* select file info */
			Map<String, Object> fileMap = new HashMap<String, Object>();	// 거래처 사업자등록증사본
			fileMap.put("img_type", Constants.IMG_TYPE_MATERIAL_PCS);
			fileMap.put("key_no", param.get("material_bound_no").toString());
			model.addAttribute("imageList", fileService.listImgInfo(fileMap));
		}

		/* 공통코드 */
		model.addAttribute("bound_locate", codeService.selectCodeByGb("bound_locate")); // bound_locate
		model.addAttribute("out_type", codeService.selectCodeByGb("out_type")); // out_type
		model.addAttribute("receive_type", codeService.selectCodeByGb("receive_type")); // income_type
		model.addAttribute("unit_cd", codeService.selectCodeByGb("unit_cd")); // unit_cd

		Map<String, Object> adminMap = new HashMap<String, Object>();
		adminMap.put("admin_type", "02");
		model.addAttribute("workerman_list", adminService.adminList(adminMap)); // income_type

		/* 거래처 */
		model.addAttribute("vendor", vendorService.listVendor(param));

		return "material/bound/out";
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :전표 등록 폼
	 */
	@PostMapping(path="/material/bound/save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public Map<String, Object> save(
			@RequestParam Map<String, Object> param,
			@RequestParam("upLoadFile") MultipartFile[] files, // upload file
			Model model
			) throws Exception {

		param.put("del_yn", Constants.NOT_DELETED); // 상태

		param.put("bound_stat", "01"); // 등록

		param.put("create_no", getAdminNo()); // admin_no
		param.put("update_no", getAdminNo()); // update_no

		param.put("bound_date", StringUtil.remove((String)param.get("bound_date"), "-"));

		param.put("out_admin_no", StringUtil.toNull(param.get("out_admin_no")));
		param.put("work_no", StringUtil.toNull(param.get("work_no")));

		param.put("vendor_no", StringUtil.toNull(param.get("vendor_no")));

		materialBoundService.saveMaterialBound(param);

		param.get("material_bound_no").toString();

		return param;
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :전표 삭제
	 */
	@GetMapping(path="/material/bound/delete")
	public ResponseEntity<Map<String, Object>> delete(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		materialBoundService.deleteMaterialBound(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :전표 등록 폼
	 */
	@PostMapping(path="/material/bound/view")
	public String view(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		if(StringUtils.isBlank((String)param.get("material_bound_no"))){
			throw new Exception(Constants.ERROR_EMPTY_KEY);
		}

		model.addAttribute("data", materialBoundService.selectMaterialBound(param));

		/* select file info */
		Map<String, Object> fileMap = new HashMap<String, Object>();
		fileMap.put("img_type", Constants.IMG_TYPE_MATERIAL_PCS);
		fileMap.put("key_no", param.get("material_bound_no").toString());
		model.addAttribute("imageList", fileService.listImgInfo(fileMap));

		model.addAttribute("doc_stat", codeService.selectCodeByGb("doc_stat")); // doc_stat

		return "material/bound/view";
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :자재요청 상태 변경
	 */
	@PostMapping(path="/material/bound/stat/save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> saveDocStat(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {


		param.put("create_no", getAdminNo()); // admin_no
		param.put("update_no", getAdminNo()); // update_no

		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put(MESSAGES_KEY, materialBoundService.updateMaterialBoundStat(param));


		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


}
