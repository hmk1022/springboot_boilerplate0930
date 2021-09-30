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
import com.workerman.service.VendorService;
import com.workerman.utils.StringUtil;
import com.workerman.service.FileService;

@Controller
public class VendorController extends BaseController{

	@Autowired
	private VendorService vendorService;

	@Autowired
	private FileService fileService;

	@Autowired
	private CodeService codeService;

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : list
	 * @return : String
	 * @note :파트너 리스트
	 */
	@GetMapping(path="/code/vendor/list")
	public String list(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		return "code/vendor/list";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :고객사지점
	 */
	@RequestMapping(path="/code/vendor/list/ajax")
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
			returnMap.put("totalRecords", vendorService.listVendorCnt(param));
		}

		/* get list */
		returnMap.put("data", vendorService.listVendor(param));

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :파트너 등록 폼
	 */
	@RequestMapping(path="/code/vendor/form")
	public String form(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		/* case modify */
		if(StringUtils.isNoneBlank((String)param.get("vendor_no"))){
			model.addAttribute("data", vendorService.selectVendor(param));

			/* select file info */
			Map<String, Object> fileMap = new HashMap<String, Object>();	// 거래처 사업자등록증사본
			fileMap.put("img_type", Constants.IMG_TYPE_VENDOR_BUSS);
			fileMap.put("key_no", param.get("vendor_no").toString());
			model.addAttribute("imageList1", fileService.listImgInfo(fileMap));
			fileMap = new HashMap<String, Object>();	// 거래처 통장사본
			fileMap.put("img_type", Constants.IMG_TYPE_VENDOR_ACCT);
			fileMap.put("key_no", param.get("vendor_no").toString());
			model.addAttribute("imageList2", fileService.listImgInfo(fileMap));
		}

		/* 공통코드 */
		model.addAttribute("work_cate", codeService.selectCodeByGb("work_cate")); // work_cate
		model.addAttribute("vacct_bank_code", codeService.selectCodeByGb("vacct_bank_code")); // bank_code

		return "code/vendor/form";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :파트너 등록 폼
	 */
	@PostMapping(path="/code/vendor/save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String save(
			@RequestParam Map<String, Object> param,
			@RequestParam("upLoadFile1") MultipartFile[] files1, // upload file
			//@RequestParam("upLoadFile2") MultipartFile[] files2, // upload file
			Model model
			) throws Exception {

		param.put("del_yn", Constants.NOT_DELETED); // 상태

		param.put("create_no", getAdminNo()); // admin_no
		param.put("update_no", getAdminNo()); // admin_no

		param.put("vendor_hp1", StringUtil.onlyNumStr(param.get("vendor_hp1")));
		param.put("vendor_hp2", StringUtil.onlyNumStr(param.get("vendor_hp2")));

		param.put("acct_fin_yn", StringUtil.nvl((String)param.get("acct_fin_yn"),"N"));

		param.put("files1", files1); // file
		//param.put("files2", files2); // file
		vendorService.saveVendor(param);

		return Constants.EVENT_SAVE_SUCCESS;
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :파트너 삭제
	 */
	@GetMapping(path="/code/vendor/delete")
	public ResponseEntity<Map<String, Object>> delete(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		vendorService.deleteVendor(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :파트너 등록 폼
	 */
	@PostMapping(path="/code/vendor/view")
	public String view(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		if(StringUtils.isBlank((String)param.get("vendor_no"))){
			throw new Exception(Constants.ERROR_EMPTY_KEY);
		}

		model.addAttribute("data", vendorService.selectVendor(param));

		/* select file info */
		Map<String, Object> fileMap = new HashMap<String, Object>(); // 거래처 사업자등록증사본
		fileMap.put("img_type", Constants.IMG_TYPE_VENDOR_BUSS);
		fileMap.put("key_no", param.get("vendor_no").toString());
		model.addAttribute("imageList1", fileService.listImgInfo(fileMap));
//		fileMap = new HashMap<String, Object>(); // 거래처 통장사본
//		fileMap.put("img_type", Constants.IMG_TYPE_VENDOR_ACCT);
//		fileMap.put("key_no", param.get("vendor_no").toString());
//		model.addAttribute("imageList2", fileService.listImgInfo(fileMap));

		return "code/vendor/view";
	}
}
