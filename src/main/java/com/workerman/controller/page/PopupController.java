package com.workerman.controller.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.workerman.service.AdminScheduleService;
import com.workerman.service.B2bWorkService;
import com.workerman.service.CodeService;
import com.workerman.service.EstimateService;
import com.workerman.service.ExpensesService;
import com.workerman.service.FileService;
import com.workerman.service.MaterialDocService;
import com.workerman.service.PartnerService;
import com.workerman.service.VendorService;
import com.workerman.service.WorkService;
import com.workerman.utils.Utils;

import net.sf.json.JSONArray;

@Controller
public class PopupController extends BaseController{

	@Autowired
	private ExpensesService expensesService;

	@Autowired
	private WorkService workService;

	@Autowired
	private EstimateService estimateService;

	@Autowired
	private CodeService codeService;

	@Autowired
	private B2bWorkService b2bWorkService;

	@Autowired
	private FileService fileService;

	@Autowired
	private PartnerService partnerService;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private MaterialDocService materialDocService;


	@Autowired
	private B2bWorkService b2bWork;


	@Autowired
	private AdminScheduleService adminScheduleService;


	@RequestMapping(path="/popup/estimate/view")// path 주소를 popup이라고 바꾸니까 작동된다...
	public String view(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		System.out.println(param);
		/* return map */
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> price = new HashMap<String,Object>();
		Map<String,Object> currentAuthor = new HashMap<String,Object>();

		/* detail */
		List<Map<String, Object>> datailData = estimateService.selectPopEstimateList(param);

		/* json 형태로 데이터 보내주기 */
		JSONArray jsonArray = new JSONArray();

		model.addAttribute("data",estimateService.selectPopEstimate(param));
		model.addAttribute("price",jsonArray.fromObject(estimateService.selectPopEstimatePrice(param)));

		int estimateVersionNo = 0;
		if(param.getOrDefault("version_no","").equals("")) {
			estimateVersionNo = estimateService.getEstimateVersion(param);
		}else {
			estimateVersionNo  = Integer.parseInt(param.get("version_no").toString() );
		}

		/* get list */
		model.addAttribute("workList", datailData);
		if( !StringUtils.isEmpty( String.valueOf(param.get("version_no")) )) {
			for(Map<String, Object> tmp : datailData) {
				tmp.put("version_no",param.get("version_no"));
				model.addAttribute("estimate_"+tmp.get("work_no"), estimateService.listEstimateDetail(tmp));
			}
		}

		/* 공통코드 */
		model.addAttribute("unit_cd", codeService.selectCodeByGb("unit_cd")); // unit_cd
		model.addAttribute("resource_type", codeService.selectCodeByGb("resource_type")); // estimate_type
		model.addAttribute("estimate_version_no", estimateVersionNo); // estimate_version_no
		/* 최신 요청자... */
		model.addAttribute("currentAuthor",estimateService.selectCurrentAuthor(param));
		model.addAttribute("price_list",estimateService.selectListPrice(param));

		return "pop:/estimate/pop/view";
	}

	@RequestMapping(path="/estimate/pop/detail")// 세부 견적서 controller
	public String detail(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {
		param.put("work_no",4928);
		param.put("version_no",5);

		List<Map<String, Object>> data = estimateService.selectPopEstimateList(param);

		System.out.println(param);
		int estimateVersionNo = 0;
		if(param.getOrDefault("version_no","").equals("")) {
			estimateVersionNo = estimateService.getEstimateVersion(param);
		}else {
			estimateVersionNo  = Integer.parseInt(param.get("version_no").toString() );
		}

		/* get list */
		model.addAttribute("workList", data);
		if( !StringUtils.isEmpty( String.valueOf(param.get("version_no")) )) {
			for(Map<String, Object> tmp : data) {
				tmp.put("version_no",param.get("version_no"));
				model.addAttribute("estimate_"+tmp.get("work_no"), estimateService.listEstimateDetail(tmp) );
			}
		}


		/* 공통코드 */
		model.addAttribute("unit_cd", codeService.selectCodeByGb("unit_cd")); // unit_cd
		model.addAttribute("resource_type", codeService.selectCodeByGb("resource_type")); // estimate_type
		model.addAttribute("estimate_version_no", estimateVersionNo); // estimate_version_no
		model.addAttribute("price",estimateService.selectPopEstimate(param));

		return "/estimate/pop/detail";
	}

	/**
	 * @Author : hm.kim
	 * @Date	: 2021. 08. 09.
	 * @Method Name : form
	 * @return : String
	 * @note : 완료보고서 내부보고용
	 */
	@RequestMapping(path="/popup/report/inter/view")
	public String interReport(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {
		System.out.println("내부 보고서"+param);
		Map<String,Object> data = new HashMap<String,Object>();
		JSONArray jsonArray = new JSONArray();

		model.addAttribute("unit_cd", codeService.selectCodeByGb("unit_cd"));
		//model.addAttribute("data", b2bWorkService.selectWork(param));
		model.addAttribute("data", b2bWorkService.selectB2bWorkReport(param));
		// 공정 일자별 자금 집행 요약
		List<Map<String, Object>> excutePriceDailyList = b2bWorkService.selectDailyWorkList(param);

		for(Map<String, Object> excutePriceData : excutePriceDailyList) {
			String workDate = String.valueOf( excutePriceData.get("st_date_ymd") ).replaceAll("-", "");
			Map<String, Object> expenseParam = new HashMap();
			expenseParam.put("work_no", param.get("work_no"));
			expenseParam.put("report_date", workDate);
			List<Map<String, Object>> expenseList =  b2bWorkService.selectExpenseList(expenseParam);
			double total_price = 0;
			double total_price_01 = 0;
			double total_price_02 = 0;
			double total_price_03 = 0;
			double total_price_04 = 0;
			System.out.println("expenseList"+expenseList);
			for(Map<String, Object> expenseData : expenseList) {
				if( (!String.valueOf(expenseData.get("stat")).equals("99") && !String.valueOf(expenseData.get("exp_category")).equals("자재요청"))
						|| (!String.valueOf(expenseData.get("stat")).equals("06") && String.valueOf(expenseData.get("exp_category")).equals("자재요청")) ) {
					double price = Double.valueOf( String.valueOf(expenseData.get("price")) );
					total_price += price;
					if(expenseData.get("exp_type").equals("01") ) {
						total_price_01 += price;
					}else if(expenseData.get("exp_type").equals("02") ) {
						total_price_02 += price;
					}else if(expenseData.get("exp_type").equals("03") ) {
						total_price_03 += price;
					}else if(expenseData.get("exp_type").equals("04") ) {
						total_price_04 += price;
					}else if(expenseData.get("exp_type").equals("09") ) {
						total_price_03 += price;
					}
				}
			}
			excutePriceData.put("total_price", total_price);
			excutePriceData.put("total_price_01", total_price_01);
			excutePriceData.put("total_price_02", total_price_02);
			excutePriceData.put("total_price_03", total_price_03);
			excutePriceData.put("total_price_04", total_price_04);
		}
		System.out.println("expenseList"+excutePriceDailyList);
		model.addAttribute("excutePriceDailyList", excutePriceDailyList);
		model.addAttribute("estimateData", b2bWorkService.selectEstimateTotalAmount(param));
		model.addAttribute("excutePriceList", b2bWorkService.selectExcutePriceList(param));
		model.addAttribute("payList", jsonArray.fromObject(b2bWorkService.reportInPay(param)));
		model.addAttribute("exp_memo", b2bWorkService.selectReport(param));
		model.addAttribute("exp_price", b2bWorkService.expPriceSum(param));
		model.addAttribute("estimate_total", estimateService.estTotalPrice(param));
		model.addAttribute("totalexcutePrice",  b2bWorkService.totalexcutePrice(param).get("sum_price"));



		return "pop:/work/report/inter/view";
	}
	/**
	 * @Author : hm.kim
	 * @Date	: 2021. 08. 12.
	 * @Method Name : form
	 * @return : String
	 * @note : 완료보고서 고객용
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(path="/popup/report/client/view")
	public String ClientReportView(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {
		Map<String,Object> data = new HashMap<String,Object>();
		JSONArray jsonArray = new JSONArray();
		// 내부 보고용 완료보고서 작성 정보

		model.addAttribute("reportForm", b2bWorkService.reportInForm(param));
		model.addAttribute("data", b2bWorkService.selectWork(param));
		model.addAttribute("est",estimateService.selectPopEstimate(param));
		model.addAttribute("reportImg", jsonArray.fromObject(b2bWorkService.reportClientImg(param)));
		model.addAttribute("clientReport",b2bWorkService.reportClient(param));
		/* model.addAttribute("work", b2bWorkService.selectWork(param)); */
		System.out.println(param);

		return "pop:/work/report/client/view";
	}

	/**
	 * @Author : hm.kim
	 * @Date	: 2021. 08. 09.
	 * @Method Name : form
	 * @return : String
	 * @note : 완료보고서 내부보고용 작성
	 */
	@RequestMapping(path="/popup/report/inter/form")
	public String interReportForm(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {
		Map<String,Object> data = new HashMap<String,Object>();
		// 내부 보고용 완료보고서 작성 정보
		model.addAttribute("reportForm", b2bWorkService.reportInForm(param));
		model.addAttribute("data", b2bWorkService.selectWork(param));


		System.out.println(param);

//		Map<String, Object> fileMap = new HashMap<String, Object>();
//		fileMap.put("img_type", Constants.IMG_TYPE_CUSTOMER_BUSS);
//		fileMap.put("key_no", param.get("customer_no").toString());
//		model.addAttribute("imageList", fileService.listImgInfo(fileMap));

		return "pop:/work/report/inter/form";
	}
	/**
	 * @Author : hm.kim
	 * @Date	: 2021. 08. 09.
	 * @Method Name : form
	 * @return : String
	 * @note : 완료보고서 내부보고용 작성
	 */
	@RequestMapping(path="/popup/report/inter/post")
	public String interReportPost(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {
		Map<String,Object> data = new HashMap<String,Object>();
		Map<String,Object> postData = new HashMap<String,Object>();

		System.out.println(param);

		// 내부 보고용 완료보고서 작성 정보
		model.addAttribute("reportForm", b2bWorkService.reportInForm(param));
		model.addAttribute("data", b2bWorkService.selectWork(param));
		model.addAttribute("postData", b2bWorkService.reportInPost(param));
		System.out.println(param);

		return "pop:/work/report/inter/form";
	}
	/**
	 * @Author : hm.kim
	 * @Date	: 2021. 08. 18.
	 * @Method Name : form
	 * @return : String
	 * @note : 완료보고서 고객전송용 수정
	 */
	@RequestMapping(path="/popup/report/client/post")
	public String clientReportPost(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {
		Map<String,Object> data = new HashMap<String,Object>();
		JSONArray jsonArray = new JSONArray();
		// 내부 보고용 완료보고서 작성 정보

		model.addAttribute("reportForm", b2bWorkService.reportInForm(param));
		model.addAttribute("data", b2bWorkService.selectWork(param));
		model.addAttribute("est",estimateService.selectPopEstimate(param));
		model.addAttribute("reportImg", jsonArray.fromObject(b2bWorkService.reportClientImg(param)));
		model.addAttribute("clientReport",b2bWorkService.reportClient(param));

		List<Map<String, Object>> beforeList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> ingList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> afterList = new ArrayList<Map<String, Object>>();

		for(Map<String, Object> tmp : b2bWorkService.reportPostImg(param)) {
			if(tmp.get("img_type").equals("IMG_TYPE_REPORT_BEFORE")) {
				beforeList.add(tmp);
				model.addAttribute("beforeImg",beforeList);
			} else if (tmp.get("img_type").equals("IMG_TYPE_REPORT_ING")) {
				ingList.add(tmp);
				model.addAttribute("ingImg",ingList);
			} else if (tmp.get("img_type").equals("IMG_TYPE_REPORT_AFTER")) {
				afterList.add(tmp);
				model.addAttribute("afterImg",afterList);
			}
		}

		model.addAttribute("imageList",b2bWorkService.reportPostImg(param));

		return "pop:/work/report/client/form";
	}
	/**
	 * @Author : hm.kim
	 * @Date	: 2021. 08. 13.
	 * @Method Name : form
	 * @return : String
	 * @note : 완료보고서 내부용 수정
	 */
	@RequestMapping(path="/popup/report/client/form")
	public String clientReportForm(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {
		Map<String,Object> data = new HashMap<String,Object>();
		JSONArray jsonArray = new JSONArray();
		// 내부 보고용 완료보고서 작성 정보
		model.addAttribute("reportForm", b2bWorkService.reportInForm(param));
		model.addAttribute("data", b2bWorkService.selectB2bWorkReport(param));
		model.addAttribute("reportImg", jsonArray.fromObject(b2bWorkService.reportClientImg(param)));

		System.out.println(param);

		return "pop:/work/report/client/form";
	}
	/**
	 * @Author : hm.kim
	 * @Date : 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :내부보고서 등록 폼
	 */
	@PostMapping(path = "/reportin/save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String saveInReport(@RequestParam Map<String, Object> param, Model model)
								throws Exception {
		param.put("admin_no", getAdminNo());
		param.put("del_yn", Constants.NOT_DELETED); // 상태
		param.put("create_no", getAdminNo()); // 상태
		param.put("update_no", getAdminNo()); // 상태
		System.out.println("report"+param);
		b2bWorkService.insertReportIn(param);
		System.out.println("paramssss : "+param);
		return Constants.EVENT_SAVE_SUCCESS;
	}
	/**
	 * @Author : hm.kim
	 * @Date : 2021. 08. 17.
	 * @Method Name : form
	 * @return : String
	 * @note :고객용 보고서 등록 폼
	 */
	@PostMapping(path = "/report-client/save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String saveClientReport(@RequestParam Map<String, Object> param,
								@RequestParam("upLoadFile1") MultipartFile[] files1, // upload file,
								@RequestParam("upLoadFile2") MultipartFile[] files2, // upload file,
								@RequestParam("upLoadFile3") MultipartFile[] files3, // upload file,
								Model model)
								throws Exception {

		System.out.println("files : "+ param);

//		Map<String, Object> param = getQueryMap();

		param.put("create_no", getAdminNo()); // 상태
		param.put("update_no", getAdminNo()); // 상태

		param.put("files1", files1); // file
		param.put("files2", files2); // file
		param.put("files3", files3); // file

		b2bWorkService.insertReportClient(param);
		System.out.println("paramssss : "+param);
		return Constants.EVENT_SAVE_SUCCESS;
	}


	/**
	 * @Author : hm.kim
	 * @Date : 2021. 08. 13.
	 * @Method Name : form
	 * @return : String
	 * @note : 내부 보고서 수정
	 */
	@PostMapping(path = "/reportin/post/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String postInReport(@RequestParam Map<String, Object> param, Model model) throws Exception {

		System.out.println(param);

//		Map<String, Object> param = getQueryMap();
		param.put("update_no",getAdminNo());
		System.out.println("파라암"+param);
		b2bWorkService.updateReportIn(param);

		return Constants.EVENT_SAVE_SUCCESS;
	}
	/**
	 * @Author : hm.kim
	 * @Date : 2021. 08. 18.
	 * @Method Name : form
	 * @return : String
	 * @note : 고객전송용 보고서 수정
	 */
	@PostMapping(path = "/report-client/post/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String postClientReport(@RequestParam Map<String, Object> param, Model model,
									@RequestParam("upLoadFile1") MultipartFile[] files1, // upload file,
									@RequestParam("upLoadFile2") MultipartFile[] files2, // upload file,
									@RequestParam("upLoadFile3") MultipartFile[] files3 // upload file,

			) throws Exception {



		// Map<String, Object> param = getQueryMap();

		param.put("files1", files1); // file
		param.put("files2", files2); // file
		param.put("files3", files3); // file

		b2bWorkService.updateReportClient(param);

		return Constants.EVENT_SAVE_SUCCESS;
	}
	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :견적서 등록 폼
	 */
	@RequestMapping(path="/popup/estimate/form")
	public String form(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		List<Map<String, Object>> data = workService.selectWorkParentList(param);

		int estimateVersionNo = 0;
		if(param.getOrDefault("version_no","").equals("")) {
			estimateVersionNo = estimateService.getEstimateVersion(param);
		}else {
			estimateVersionNo  = Integer.parseInt(param.get("version_no").toString() );
		}

		/* get list */
		model.addAttribute("workList", data);
		if( !StringUtils.isEmpty( String.valueOf(param.get("version_no")) )) {
			for(Map<String, Object> tmp : data) {
				tmp.put("version_no",param.get("version_no"));
				model.addAttribute("estimate_"+tmp.get("work_no"), estimateService.listEstimate(tmp) );
			}
		}

		/* 공통코드 */
		model.addAttribute("unit_cd", codeService.selectCodeByGb("unit_cd")); // unit_cd
		model.addAttribute("resource_type", codeService.selectCodeByGb("resource_type")); // estimate_type
		model.addAttribute("estimate_version_no", estimateVersionNo); // estimate_version_no

		return "pop:/estimate/form";
	}


	@RequestMapping(path="/popup/dailyReport/form")
	public String dailyReportForm(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		// 날짜별 비용조회
		// 날짜별 인력투입 (?) - 이 데이터는 어디서 ?
		// 업체 ?
		// 자재투입은 자재현황에서 어떤 상태값?
		// 경비사용현황은 또 어디 .. ?

		// 작업일지 테이블 필요 , 오늘메모, 내일메모 , 작성자 , 사진묶음
		String report_date = String.valueOf( param.get("report_date") ).replaceAll("-", "");
		param.put("report_date", report_date);
		/* return map */
		Map<String, Object> dailyReportDataMap = new HashMap<String, Object>();
		dailyReportDataMap =  b2bWorkService.selectDailyReport(param);
		model.addAttribute("estimateData", b2bWorkService.selectEstimateTotalAmount(param));
		model.addAttribute("dailyReportData",dailyReportDataMap);
		model.addAttribute("dailyReportExpenseList", b2bWorkService.selectExpenseList(param));
		model.addAttribute("dailyReportUsedAmountData", b2bWorkService.selectDailyReportUsedAmount(param));
		/* 공통코드 */
		model.addAttribute("unit_cd", codeService.selectCodeByGb("unit_cd")); // unit_cd
		model.addAttribute("resource_type", codeService.selectCodeByGb("resource_type")); // estimate_type

		/* select file info */
		if( dailyReportDataMap != null && dailyReportDataMap.get("daily_report_no") != null ) {
			Map<String, Object> fileMap = new HashMap<String, Object>();
			fileMap.put("img_type", Constants.IMG_TYPE_DAILY_REPORT);
			fileMap.put("key_no", dailyReportDataMap.get("daily_report_no"));
			model.addAttribute("imageList", fileService.listImgInfo(fileMap));
		}
		return "pop:/work/daily/form";
	}
	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :비용처리 등록 폼
	 */
	@RequestMapping(path="/popup/expenses/form")
	public String expensesForm(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		/* case modify */
		if(StringUtils.isNoneBlank((String)param.get("exp_no"))){
			model.addAttribute("data", expensesService.selectExpenses(param));

			/* select file info */
			Map<String, Object> fileMap = new HashMap<String, Object>();
			fileMap.put("img_type", Constants.IMG_TYPE_EXPENSES);
			fileMap.put("key_no", param.get("exp_no").toString());
			model.addAttribute("imageList", fileService.listImgInfo(fileMap));
		}

		/* 공통코드 */
		model.addAttribute("exp_type", codeService.selectCodeByGb("exp_type")); // 비용분류
		model.addAttribute("exp_unit_cd", codeService.selectCodeByGb("exp_unit_cd")); // 비용단위

		/* 거래처 */
		model.addAttribute("vendor", vendorService.listVendor(param));

		/* 파트너 정보조회 */
		model.addAttribute("partner_list", partnerService.listPartner(null)); // partner list

		model.addAttribute("bank_code", codeService.selectCodeByGb("vacct_bank_code")); // 은행코드

		List<Map<String, Object>> workDayList = b2bWorkService.workDayList(param);
		model.addAttribute("workDayList", Utils.objectToJson(workDayList));

		// 작업일 리스트.
//		b2bWork.selectDailyReport(param);

		return "pop:/account/expenses/pop/form";
	}


	/**
	 * @Author :
	 * @Date	: 2021. 07. 243
	 * @Method Name : form
	 * @return : String
	 * @note :일정 등록 폼
	 */
	@RequestMapping(path="/popup/schedule/form")
	public String scheduleForm(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		/* 공통코드 */
		model.addAttribute("holiday", codeService.selectCodeByGb("p_scd_type"));
		model.addAttribute("schedule", adminScheduleService.getSchedule(param));
		return "pop:/schedule/form";
	}


	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :자재요청 등록 폼
	 */
	@RequestMapping(path="/popup/material/form")
	public String materialForm(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		/* case modify */
		if(StringUtils.isNoneBlank((String)param.get("material_doc_no"))){
			model.addAttribute("data", materialDocService.selectMaterialDoc(param));
		}

		/* 공통코드 */
		List<Map<String, Object>> workDayList = b2bWorkService.workDayList(param);
		model.addAttribute("receive_type", codeService.selectCodeByGb("receive_type")); // receive_type
		model.addAttribute("doc_stat", codeService.selectCodeByGb("doc_stat")); // doc_stat
		model.addAttribute("unit_cd", codeService.selectCodeByGb("unit_cd")); // unit_cd
		model.addAttribute("workDayList", Utils.objectToJson(workDayList));

		return "pop:/material/doc/pop/form";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :자재요청 등록 폼
	 */
	@RequestMapping(path="/popup/material/view")
	public String materialView(
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
		model.addAttribute("receive_type", codeService.selectCodeByGb("receive_type")); // doc_stat

		return "pop:/material/doc/pop/view";
	}

	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 05. 27.
	 * @Method Name : list
	 * @return : String
	 * @note : 코드목록 조회
	 */
	@RequestMapping(path="/popup/common/img")
	public String list(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		System.out.println("img_type:" + param.get("img_type"));
		System.out.println("key_no:" + param.get("key_no"));
		System.out.println("image_name:" + param.get("image_name"));

		model.addAttribute("imageList", fileService.listImgInfo(param));
		model.addAttribute("image_name", param.get("image_name"));

		return "pop:/comm/image_pop";
	}

	/**
	 * @Author : seonbeom.ahn
	 * @Date	: 2021. 09. 28.
	 * @Method Name : list
	 * @return : String
	 * @note : 작업 취소 팝업
	 */
	@RequestMapping(path="/popup/cancel/form")
	public String cancelWork(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {
		model.addAttribute("reject_code_list", codeService.selectCodeByGb("est_reject_cd")); // est_reject_cd
		return "pop:/work/cancel";
	}
}
