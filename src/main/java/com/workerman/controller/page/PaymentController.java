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
import com.workerman.service.PartnerService;
import com.workerman.service.PaymentService;
import com.workerman.utils.StringUtil;

@Controller
public class PaymentController extends BaseController{

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private FileService fileService;

	@Autowired
	private CodeService codeService;

	@Autowired
	private PartnerService partnerService;

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : list
	 * @return : String
	 * @note :매출관리 리스트
	 */
	@GetMapping(path="/account/payment/list")
	public String list(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		model.addAttribute("pay_bill_type", codeService.selectCodeByGb("pay_bill_type")); // 지출증빙방식
		model.addAttribute("pay_type", codeService.selectCodeByGb("pay_abc_type")); // 결제수단
		model.addAttribute("work_target", codeService.selectCodeByGb("work_target")); // work_target

		return "account/payment/list";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :고객사지점
	 */
	@RequestMapping(path="/account/payment/list/ajax")
	public ResponseEntity<Map<String, Object>> listAjax(@RequestParam Map<String, Object> param) throws Exception {

		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		param.put("start_dt", StringUtils.remove((String)param.get("start_dt"), "-"));
		param.put("end_dt", StringUtils.remove((String)param.get("end_dt"), "-"));

		/* pagination */
		if( param.get("pq_rpp") != null) {
			int pageFirst = (Integer.parseInt( String.valueOf(param.get("pq_curpage")))-1) * Integer.parseInt( String.valueOf(param.get("pq_rpp")))  ;
			int pageLast = Integer.parseInt( String.valueOf(param.get("pq_rpp"))) ;
			param.put("pageFirst", pageFirst);
			param.put("pageLast", pageLast);

			returnMap.put("curPage", String.valueOf(param.get("pq_curpage")));
			/* get count */
			returnMap.put("totalRecords", paymentService.listPaymentCnt(param));
		}

		/* get list */
		returnMap.put("data", paymentService.listPayment(param));

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :매출관리 등록 폼
	 */
	@RequestMapping(path="/account/payment/form")
	public String form(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		/* case modify */
		if(StringUtils.isNoneBlank((String)param.get("pay_no"))){
			model.addAttribute("data", paymentService.selectPayment(param));

			/* select file info */
			Map<String, Object> fileMap = new HashMap<String, Object>();
			fileMap.put("img_type", Constants.IMG_TYPE_PAYMENT);
			fileMap.put("key_no", param.get("pay_no").toString());
			model.addAttribute("imageList", fileService.listImgInfo(fileMap));
		}

		/* 공통코드 */
		model.addAttribute("amount_type", codeService.selectCodeByGb("amount_type")); // 지불방식
		model.addAttribute("pay_type", codeService.selectCodeByGb("pay_type")); // 결제방식
		model.addAttribute("stat", codeService.selectCodeByGb("pay_stat")); // 결제상태


		return "account/payment/form";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :매출관리 등록 폼
	 */
	@PostMapping(path="/account/payment/save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String save(
			@RequestParam Map<String, Object> param,
			@RequestParam("upLoadFile") MultipartFile[] files, // upload file
			Model model
			) throws Exception {

		System.out.println("param:" + param);

		param.put("del_yn", Constants.NOT_DELETED); // 상태

		param.put("create_no", getAdminNo()); // 상태
		param.put("update_no", getAdminNo()); // 상태

		param.put("st_date", StringUtils.remove((String)param.get("st_date"), "-")); //
		param.put("ed_date", StringUtils.remove((String)param.get("ed_date"), "-")); //
		param.put("exp_price", StringUtils.remove((String)param.get("exp_price"), ",")); //
		param.put("exp_count", StringUtils.remove((String)param.get("exp_count"), ",")); //


		param.put("files", files); // file
		paymentService.savePayment(param);

		return Constants.EVENT_SAVE_SUCCESS;
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :매출관리 삭제
	 */
	@GetMapping(path="/account/payment/delete")
	public ResponseEntity<Map<String, Object>> delete(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		paymentService.deletePayment(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :매출관리 등록 폼
	 */
	@PostMapping(path="/account/payment/view")
	public String view(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		if(StringUtils.isBlank((String)param.get("pay_no"))){
			throw new Exception(Constants.ERROR_EMPTY_KEY);
		}

		model.addAttribute("data", paymentService.selectPayment(param));

		/* select file info */
		Map<String, Object> fileMap = new HashMap<String, Object>();
		fileMap.put("img_type", Constants.IMG_TYPE_PAYMENT);
		fileMap.put("key_no", param.get("pay_no").toString());
		model.addAttribute("imageList", fileService.listImgInfo(fileMap));

		return "account/payment/view";
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :매출관리 삭제
	 */
	@PostMapping(path="/account/payment/approval/save/ajax")
	public ResponseEntity<Map<String, Object>> approval(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		param.put("create_no", getAdminNo()); // login id
		param.put("update_no", getAdminNo()); // login id

		param.put("pay_bill_yymmdd", StringUtil.onlyNumStr(param.get("pay_bill_yymmdd"))); //
		param.put("pay_bill_comp_number", StringUtil.onlyNumStr(param.get("pay_bill_comp_number"))); //

		paymentService.updatePaymentApproval(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :매출관리 삭제
	 */
	@PostMapping(path="/account/payment/complete/save/ajax")
	public ResponseEntity<Map<String, Object>> complete(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		param.put("create_no", getAdminNo()); // login id
		param.put("update_no", getAdminNo()); // login id

		param.put("paid_date", StringUtil.onlyNumStr(param.get("paid_date"))); //
		//param.put("approval_no", StringUtil.onlyNumStr(param.get("pay_bill_comp_number"))); //

		paymentService.updatePaymentComplete(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :매출관리 내용보기
	 */
	@PostMapping(path="/account/payment/view/select/ajax")
	public ResponseEntity<Map<String, Object>> select(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();
		return new ResponseEntity<Map<String, Object>>(paymentService.selectPayment(param), HttpStatus.OK);
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :매출관리 삭제
	 */
	@PostMapping(path="/account/payment/info/save/ajax")
	public ResponseEntity<Map<String, Object>> modiInfo(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		param.put("create_no", getAdminNo()); // login id
		param.put("update_no", getAdminNo()); // login id

		param.put("total_pay_price", StringUtil.onlyNumStr(param.get("total_pay_price"))); //
		param.put("paid_date", StringUtil.onlyNumStr(param.get("paid_date"))); //
		param.put("pay_bill_yymmdd", StringUtil.onlyNumStr(param.get("pay_bill_yymmdd"))); //

		paymentService.updatePaymentInfo(param);

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}
}
