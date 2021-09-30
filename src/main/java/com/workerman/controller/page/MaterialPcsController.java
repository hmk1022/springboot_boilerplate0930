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
import com.workerman.service.MaterialPcsService;
import com.workerman.service.VendorService;
import com.workerman.service.WorkService;
import com.workerman.utils.StringUtil;

@Controller
public class MaterialPcsController extends BaseController{

	@Autowired
	private MaterialPcsService materialPcsService;

	@Autowired
	private FileService fileService;

	@Autowired
	private CodeService codeService;

	@Autowired
	private WorkService workService;

	@Autowired
	private VendorService vendorService;

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : list
	 * @return : String
	 * @note :자재구매 리스트
	 */
	@GetMapping(path="/material/pcs/list")
	public String list(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		return "material/pcs/list";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :자재구매
	 */
	@RequestMapping(path="/material/pcs/list/ajax")
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
			returnMap.put("totalRecords", materialPcsService.listMaterialPcsCnt(param));
		}

		/* get list */
		returnMap.put("data", materialPcsService.listMaterialPcs(param));

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :자재구매 등록 폼
	 */
	@RequestMapping(path="/material/pcs/form")
	public String form(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		/* case modify */
		if(StringUtils.isNoneBlank((String)param.get("material_pcs_no"))){
			model.addAttribute("data", materialPcsService.selectMaterialPcs(param));

			/* select file info */
			Map<String, Object> fileMap = new HashMap<String, Object>();	// 거래처 사업자등록증 사본
			fileMap.put("img_type", Constants.IMG_TYPE_MATERIAL_PCS);
			fileMap.put("key_no", param.get("material_pcs_no").toString());
			model.addAttribute("imageList", fileService.listImgInfo(fileMap));
		}

		/* 공통코드 */
		model.addAttribute("pcs_reason", codeService.selectCodeByGb("pcs_reason")); // pcs_type
		model.addAttribute("unit_cd", codeService.selectCodeByGb("unit_cd")); // unit_cd
		/* 거래처 */
		model.addAttribute("vendor", vendorService.listVendor(param));

		return "material/pcs/form";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :자재구매 등록 폼
	 */
	@PostMapping(path="/material/pcs/save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String save(
			@RequestParam Map<String, Object> param,
			@RequestParam("upLoadFile") MultipartFile[] files, // upload file
			Model model
			) throws Exception {

		param.put("del_yn", Constants.NOT_DELETED); // 상태

		param.put("pcs_stat", "01"); // 등록

		param.put("create_no", getAdminNo()); // admin_no
		param.put("update_no", getAdminNo()); // update_no

		param.put("pay_req_date", StringUtil.remove((String)param.get("pay_req_date"), "-"));
		//param.put("material_cnt", StringUtil.remove((String)param.get("material_cnt"), ","));

		param.put("work_no", StringUtil.toNull(param.get("work_no")));

		param.put("vat_yn", StringUtil.nvl((String)param.get("vat_yn"), "N")); // 등록

		param.put("files", files); // file
		materialPcsService.saveMaterialPcs(param);

		param.get("material_pcs_no").toString();

		return param.get("material_pcs_no").toString();
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :자재구매 삭제
	 */
	@GetMapping(path="/material/pcs/delete")
	public ResponseEntity<Map<String, Object>> delete(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		materialPcsService.deleteMaterialPcs(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :자재구매 등록 폼
	 */
	@PostMapping(path="/material/pcs/view")
	public String view(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		if(StringUtils.isBlank((String)param.get("material_pcs_no"))){
			throw new Exception(Constants.ERROR_EMPTY_KEY);
		}

		model.addAttribute("data", materialPcsService.selectMaterialPcs(param));

		/* select file info */
		Map<String, Object> fileMap = new HashMap<String, Object>();
		fileMap.put("img_type", Constants.IMG_TYPE_MATERIAL_PCS);
		fileMap.put("key_no", param.get("material_pcs_no").toString());
		model.addAttribute("imageList", fileService.listImgInfo(fileMap));

		model.addAttribute("doc_stat", codeService.selectCodeByGb("doc_stat")); // doc_stat

		return "material/pcs/view";
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :자재요청 상태 변경
	 */
	@PostMapping(path="/material/pcs/stat/save/ajax")
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
		returnMap.put(MESSAGES_KEY, materialPcsService.updateMaterialPcsStat(param));


		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


}
