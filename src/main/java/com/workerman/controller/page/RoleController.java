package com.workerman.controller.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
import com.workerman.service.RoleAdminService;
import com.workerman.service.RoleService;
import com.workerman.service.WorkermanService;

@Controller
@RequestMapping(value = "/operation/role/*")
public class RoleController extends BaseController{

	@Autowired
	private CodeService codeService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private RoleAdminService roleAdminService;

	@Autowired
	private AdminService adminService;

	/**
	 * @Author : sbahn
	 * @Date	: 2021. 05. 27.
	 * @Method Name : list
	 * @return : String
	 * @note : 권한관리
	 */
	@GetMapping(path="index")
	public String index(
			@RequestParam Map<String, Object> param,
			ModelMap model
			) throws Exception {
		
			model.addAttribute("roleName", roleService.selectRoleName(param));
			model.addAttribute("adminId", roleService.selectNoRole(param));
		return "operation/role/index";
	}

	/**
	 * @Author : sbahn
	 * @Date	: 2021. 05. 27.
	 * @Method Name : list
	 * @return : String
	 * @note : 권한 리스트
	 */
	@PostMapping(path="roleList/ajax")
	public ResponseEntity<Map<String, Object>> roleList(@RequestParam Map<String, Object> param) throws Exception {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		int pqCurpage = Integer.valueOf(  (String) param.getOrDefault("pq_curpage","0") );
		int pqRpp = Integer.valueOf(  (String) param.getOrDefault("pq_rpp","50") );
		if(pqCurpage > 0) {
			int pageFirst = (pqCurpage-1) * pqRpp  ;
			int pageLast = pqRpp ;
			param.put("pageFirst", pageFirst);
			param.put("pageLast", pageLast);
		}
		List<Map<String, Object>> list = roleService.selectAbcRoleList(param);
		int totalRecords = roleService.selectAbcRoleListCnt(param);
		returnMap.put("data", list);
		returnMap.put("curPage", String.valueOf(param.get("pq_curpage")));
		returnMap.put("totalRecords", totalRecords);
		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	
	/**
	 * @Author : sbahn
	 * @Date	: 2021. 05. 27.
	 * @Method Name : list
	 * @return : String
	 * @note : 권한 관리자 리스트
	 */
	@PostMapping(path="roleAdminList/ajax")
	public ResponseEntity<Map<String, Object>> roleAdminList(@RequestParam Map<String, Object> param) throws Exception {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		int pqCurpage = Integer.valueOf(  (String) param.getOrDefault("pq_curpage","0") );
		int pqRpp = Integer.valueOf(  (String) param.getOrDefault("pq_rpp","50") );
		if(pqCurpage > 0) {
			int pageFirst = (pqCurpage-1) * pqRpp  ;
			int pageLast = pqRpp ;
			param.put("pageFirst", pageFirst);
			param.put("pageLast", pageLast);
		}
		List<Map<String, Object>> list = roleAdminService.selectRoleAbcAdminList(param);
		int totalRecords = roleAdminService.selectRoleAbcAdminListCnt(param);
		returnMap.put("data", list);
		returnMap.put("curPage", String.valueOf(param.get("pq_curpage")));
		returnMap.put("totalRecords", totalRecords);
		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}



	/**
	 * @Author : sbahn
	 * @Date	: 2021. 06. 10.
	 * @Method Name : form
	 * @return : String
	 * @note :권한 등록 폼
	 */
	@RequestMapping(path="roleForm")
	public String roleForm(
			@RequestParam Map<String, Object> param,
			ModelMap model
			) throws Exception {
		System.out.println("파람~"+param);
		
		model.addAttribute("data", roleService.selectRole(param));
		model.addAttribute("adminId", roleService.selectNoRole(param));
		//model.put("admin_cd", codeService.selectCodeByGbString("admin_cd")); // 관리자코드
		//model.put("stat_cd", codeService.selectCodeByGbString("stat_cd")); // 상태코드
		//model.put("level_cd", codeService.selectCodeByGbString("level_cd")); // 레벨코드
		//model.put("admin_cate", codeService.selectCodeByGbString("admin_cate")); // 전문분야
		//model.put("department", codeService.selectCodeByGbString("department")); // 부서
		//model.put("position", codeService.selectCodeByGbString("position")); // 지급
		/* case modify */
		if(StringUtils.isNoneBlank((String)param.get("admin_no"))){
			//	Map<String, Object> adminInfo = adminService.selectAdminByAdminNo(param);
			// model.put("adminInfo",adminInfo);
		}

		return "operation/role/role_form";
	}
	
	/**
	 * @Author : hm.kim
	 * @Date	: 2021. 09. 24.
	 * @Method Name : list
	 * @return : String
	 * @note : 권한추가
	 */
	@PostMapping(path="save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String save(
			@RequestParam Map<String, Object> param
			) throws Exception {
		
		param.put("admin_no", getAdminNo());
		if(StringUtils.isNoneBlank((String)param.get("role_no"))){
			roleService.updateRole(param);
		} else {
			roleService.insertRole(param);
		}	
		
		return Constants.EVENT_SAVE_SUCCESS;
	}
	
	/**
	 * @Author : hm.kim
	 * @Date	: 2021. 09. 24.
	 * @Method Name : list
	 * @return : String
	 * @note : 권한추가
	 */
	@PostMapping(path="saveAdminRole/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String saveAdminRole(
			@RequestParam Map<String, Object> param
			) throws Exception {
		
		param.put("create_no", getAdminNo());
		param.put("role_no", param.get("_role_no"));
		param.put("admin_no", param.get("_admin_no"));
		System.out.println("관리자추가"+param);	
		roleService.insertRoleAdmin(param);
		
		return Constants.EVENT_SAVE_SUCCESS;
	}
	
	
	/**
	 * @Author : hm.kim
	 * @Date	: 2021. 09. 24.
	 * @Method Name : list
	 * @return : String
	 * @note : 권한삭제
	 */
	@PostMapping(path="delete")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String delete(
			@RequestBody Map<String, Object> param
			) throws Exception {

		roleService.deleteRole(param);
		return Constants.EVENT_SAVE_SUCCESS;
	}
	
	/**
	 * @Author : hm.kim
	 * @Date	: 2021. 09. 24.
	 * @Method Name : list
	 * @return : String
	 * @note : 관리자 명단삭제
	 */
	@PostMapping(path="deleteAdmin")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String deleteAdminRoll(
			@RequestBody Map<String, Object> param
			) throws Exception {
		System.out.println("어드민 삭제 파람 컨트롤러"+param);
		roleAdminService.deleteRoleAdmin(param);
		return Constants.EVENT_SAVE_SUCCESS;
	}
	
//	@GetMapping(path="/code/brand/delete")
//	public ResponseEntity<Map<String, Object>> delete(
//			@RequestBody Map<String, Object> param,
//			Model model
//			) throws Exception {
//		
//		Map<String,Object> returnMap = new HashMap<String,Object>();
//
//		brandService.deleteBrand(param);
//
//		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
//	}
	
	
	/**
	 * @Author : sbahn
	 * @Date	: 2021. 06. 10.
	 * @Method Name : form
	 * @return : String
	 * @note :권한 등록 폼
	 */
	@RequestMapping(path="roleAdminForm")
	public String roleAdminForm(
			@RequestParam Map<String, Object> param,
			ModelMap model
			) throws Exception {
		
		
		model.addAttribute("roleName", roleService.selectRoleName(param));
		model.addAttribute("adminId", roleService.selectNoRole(param));
		//model.put("admin_cd", codeService.selectCodeByGbString("admin_cd")); // 관리자코드
		//model.put("stat_cd", codeService.selectCodeByGbString("stat_cd")); // 상태코드
		//model.put("level_cd", codeService.selectCodeByGbString("level_cd")); // 레벨코드
		//model.put("admin_cate", codeService.selectCodeByGbString("admin_cate")); // 전문분야
		//model.put("department", codeService.selectCodeByGbString("department")); // 부서
		//model.put("position", codeService.selectCodeByGbString("position")); // 지급
		/* case modify */
		if(StringUtils.isNoneBlank((String)param.get("admin_no"))){
			//	Map<String, Object> adminInfo = adminService.selectAdminByAdminNo(param);
			// model.put("adminInfo",adminInfo);
		}

		return "operation/role/role_admin_form";
	}
}
