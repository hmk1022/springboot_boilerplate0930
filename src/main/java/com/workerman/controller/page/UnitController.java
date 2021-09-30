package com.workerman.controller.page;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
import com.workerman.service.BrandService;
import com.workerman.service.CodeService;
import com.workerman.service.FileService;
import com.workerman.utils.StringUtil;
import com.workerman.service.UnitService;

@Controller
public class UnitController extends BaseController {


	@Autowired
	private UnitService unitService;

	@Autowired
	private FileService fileService;

	@Autowired
	private CodeService codeService;

	/**
	 * @Author : hm.kim
	 * @Date : 2021. 09. 06.
	 * @Method Name : listAjax
	 * @return : String
	 * @note :브랜드목록 list ajax
	 */

	@RequestMapping(path="/code/unit/list/ajax")
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
			returnMap.put("totalRecords", unitService.listUnitCnt(param));
		}

		/* get list */
		returnMap.put("data", unitService.listUnit(param));

		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	/**
	 * @Author : hm.kim
	 * @Date : 2021. 09. 06.
	 * @Method Name : list
	 * @return : String
	 * @note : 단위 리스트
	 */
	@GetMapping(path = "/code/unit/list")
	public String list(
			@RequestParam Map<String, Object> param,
			Model model) throws Exception {

		return "/code/unit/list";
	}

	/**
	 * @Author : hm.kim
	 * @Date : 2021. 09. 06.
	 * @Method Name : list
	 * @return : String
	 * @note : 단위 상세보기
	 */
	@PostMapping(path="/code/unit/view")
	public String view(
			@RequestParam Map<String, Object> param,
			Model model) throws Exception {

			if(StringUtil.isBlank((String)param.get("code_value"))){
				throw new Exception(Constants.ERROR_EMPTY_KEY);
			}

			model.addAttribute("data",unitService.selectUnit(param));

		return "code/unit/view";
	}
	/**
	 * @Author : hm.kim
	 * @Date	: 2021. 09. 06.
	 * @Method Name : form
	 * @return : String
	 * @note :브랜드 등록 폼
	 */
	@RequestMapping(path="/code/unit/form")
	public String form(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {

		/* case modify */
		if(StringUtil.isNoneBlank((String)param.get("code_value"))){
			model.addAttribute("data", unitService.selectUnit(param));
		}

		return "code/unit/form";
	}

	/**
	 * @Author : hm.kim
	 * @Date	: 2021. 09. 06.
	 * @Method Name : save
	 * @return : String
	 * @note :브랜드 등록 폼
	 */
	@PostMapping(path="/code/unit/save/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String save(
			@RequestParam Map<String, Object> param,
			@RequestParam("upLoadFile") MultipartFile[] files, // upload file
			Model model
			) throws Exception {


		//param.put("create_no", getAdminNo()); // admin_no
		//param.put("update_no", getAdminNo()); // admin_no

		unitService.saveUnit(param);

		return Constants.EVENT_SAVE_SUCCESS;
	}


	/**
	 * 단위 상태갱신
	 * @param param
	 * @return
	 * @throws Exception
	 */
	 @PostMapping(value="/unit/updateUnitStat")
	 @ResponseStatus(HttpStatus.CREATED)
	 @ResponseBody public String updateAdminStat(@RequestBody Map<String, Object>
	 param) throws Exception{
		 System.out.println("단위 상태"+param);
		 unitService.updateUnitStat(param); return
	 Constants.EVENT_SAVE_SUCCESS; }


	// updateBrandStat
}
