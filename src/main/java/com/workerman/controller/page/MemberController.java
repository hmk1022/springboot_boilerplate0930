package com.workerman.controller.page;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workerman.controller.BaseController;
import com.workerman.service.AdminService;
import com.workerman.service.MemberDeviceService;
import com.workerman.service.MemberService;
import com.workerman.utils.StringUtil;
import com.workerman.vo.MemberInfoReqVo;
import com.workerman.vo.MemberLoginReqVo;
import com.workerman.vo.MemberLoginResVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@RestController
@Api(description = "회원정보관련API")
@RequestMapping(path = "/member")
public class MemberController extends BaseController{

	@Autowired
	private MemberService memberService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private MemberDeviceService memberDeviceService;

	@PostMapping(path="/login")
	public ResponseEntity<MemberLoginResVo> login(
			@ApiParam(value = "로그인" )@Valid @RequestBody MemberLoginReqVo memberLoginReqVo, HttpServletRequest request) throws Exception {
		MemberLoginResVo memberLoginResVo = memberService.login(memberLoginReqVo);
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("admin_no",memberLoginResVo.getResult_data().getMember_no() );
//		request.getSession().setAttribute("menuTree", adminService.selectMenuTree(param));
		return new ResponseEntity<MemberLoginResVo>(memberLoginResVo, HttpStatus.OK);
	}

	@PostMapping(path="/info/ajax")
	public ResponseEntity<Map<String, Object>> selectMemberInfo(
			@ApiParam(value = "회원정보조회" )@Valid @RequestBody MemberInfoReqVo memberInfoReqVo, HttpServletRequest request) throws Exception {
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("admin_no",request.getAttribute("admin_no").toString() );
//		request.getSession().setAttribute("menuTree", adminService.selectMenuTree(param));
		return new ResponseEntity<Map<String, Object>>(memberService.selectMemberInfo(memberInfoReqVo, request), HttpStatus.OK);
	}
}
