package com.workerman.controller.page;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.workerman.controller.BaseController;
import com.workerman.service.ExpensesConfirmService;
import com.workerman.utils.StringUtil;

@Controller
public class ExpensesConfirmController extends BaseController{
	
	@Autowired 
	private ExpensesConfirmService expensesConfirmService;
	
	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :비용처리 확인
	 */
	@PostMapping(path="/account/expenses/confirm/save/ajax")
	public ResponseEntity<Map<String, Object>> reject(
			@RequestBody Map<String, Object> param,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();

		param.put("create_no", getAdminNo()); // 상태
		param.put("update_no", getAdminNo()); // 상태
		param.put("confirm_admin_no", getAdminNo()); // 상태

		param.put("pay_next_date", StringUtil.remove((String)param.get("pay_next_date"), "-")); //
		param.put("pay_price", StringUtil.remove((String)param.get("pay_price"), ",")); //
		
		expensesConfirmService.saveExpensesConfirm(param);
		
		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}
	
}
