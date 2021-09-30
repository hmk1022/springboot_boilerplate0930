package com.workerman.controller.page;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.workerman.aws.S3UploadService;
import com.workerman.config.Constants;
import com.workerman.controller.BaseController;
import com.workerman.dao.CommonDao;
import com.workerman.service.AdminGpsHistService;
import com.workerman.service.AdminService;
import com.workerman.service.CodeService;
import com.workerman.service.FileService;
import com.workerman.utils.S3Util;
import com.workerman.utils.Utils;

//@RestController
//@Api(description = "sample api")
@Controller
public class SampleController extends BaseController{
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private AdminGpsHistService adminGpsHistService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired 
	private S3UploadService sSUploadService;

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private CodeService codeService;
	
	@Autowired
	S3Util s3Util;
	
	/**
	 * @Author : kwonsco
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listActivityAdmin
	 * @return : String
	 * @note :워커맨용 알림리스트
	 */
	@PostMapping(path="/sample/sample1")
	public String sample(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {
	
		return "view:/sample/sample1";
	}
	
	
	/**
	 * @Author : kwonsco
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listActivityAdmin
	 * @return : String
	 * @note :워커맨용 알림리스트
	 */
	@GetMapping(path="/sample/sample1")
	public String sampleget(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {
		
		/* return test data */
		model.addAttribute("test", "test");
		
		return "view:/sample/sample2";
	}
	
	/**
	 * @Author : kwonsco
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listActivityAdmin
	 * @return : String
	 * @note :워커맨용 알림리스트
	 */
	@GetMapping(path="/sample/sample3")
	public String sample3(
			@RequestParam Map<String, Object> param,
			Model model
			) throws Exception {
		
		/* return test data */
		model.addAttribute("test", "test");
		
		/* 공통코드 */
		model.addAttribute("work_cate", codeService.selectCodeByGb("work_cate")); // work_cate
		return "view:/sample/sample3";
	}	
	
	/**
	 * 작업수기등록처리
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sample/sample3/upload2/ajax")
	public @ResponseBody Map<String, Object> addProc(
			@RequestParam Map<String, Object> param,
			MultipartHttpServletRequest request) throws Exception {
		
		
		System.out.println("param:" + param.toString());	// param data
		
		Map<String, MultipartFile> fileMap = request.getFileMap();

		Set<String> keySet = fileMap.keySet();

		int cnt = 0;

		for (String key : keySet) {

			MultipartFile multipartFile = fileMap.get(key);

			String ext = Utils.getExtensions(multipartFile.getOriginalFilename());



		}

		
		return new HashMap<String, Object>();
	}
	
	/**
	 * 작업수기등록처리
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sample/sample3/upload3/ajax")
	public @ResponseBody Map<String, Object> ddd(
			@RequestParam Map<String, Object> param,
			MultipartHttpServletRequest request) throws Exception {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("abc", "org");
		
		Map<String, MultipartFile> fileMap = request.getFileMap();

		Set<String> keySet = fileMap.keySet();
		
		for (String key : keySet) {
			
			MultipartFile multipartFile = fileMap.get(key);
			
			String ext = Utils.getExtensions(multipartFile.getOriginalFilename());
			System.out.println("OriginalFilename:" + multipartFile.getOriginalFilename());
			System.out.println("Name:" + multipartFile.getName());
			long time = System.currentTimeMillis(); 
			SimpleDateFormat dayTime = new SimpleDateFormat("yyMMdd");
			String date = dayTime.format(new Date(time));
			String uploadFileName = date+"_"+System.nanoTime()+"."+ext;
			
			
			File file = new File(multipartFile.getOriginalFilename());
			multipartFile.transferTo(file);

		}
		
		
		return ret;
	}
	
	
	/**
	 * 작업수기등록처리
	 * @param param
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sample/sample3/upload/delete/ajax")
	public @ResponseBody Map<String, Object> fadel(
			@RequestParam Map<String, Object> param,
			MultipartHttpServletRequest request) throws Exception {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("abc", "org");
		
		return ret;
	}
	
	/**
	 * @Author : kwonsco
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listActivityAdmin
	 * @return : String
	 * @note :워커맨용 알림리스트
	 */
	@PostMapping(path="/sample/sample3/upload/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String save(
//			@RequestBody Map<String, Object> param,
			@RequestParam Map<String, Object> param,
//			@RequestParam("input-file-1[]")MultipartFile[] file,
			@RequestParam("input-file-1[]") MultipartFile[] uploadfiles,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		
//		if(file !=null) {
//			for(int i =0; i < file.length; i++) {
//				
//				System.out.println("file:" + file[i]);
//			}
//		}
		System.out.println("param:" + param);
		param.put("stat", "00"); // 상태
		param.put("del_yn", "N"); // 상태
		param.put("create_no", "104"); // 상태
		param.put("update_no", "104"); // 상태
		
		
		for (MultipartFile file : uploadfiles) {
			System.out.println("file :" + file);
		}


		
//		customerService.saveCustomerBranch(param);
		
		return "Y";
	}
	
	
	/**
	 * @Author : kwonsco
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listActivityAdmin
	 * @return : String
	 * @note :워커맨용 알림리스트
	 */
	@PostMapping(path="/sample/sample4/upload/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String save2(
//			@RequestBody Map<String, Object> param,
			@RequestParam Map<String, Object> param,
//			@RequestParam("input-file-1[]")MultipartFile[] file,
			MultipartHttpServletRequest request,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		
//		if(file !=null) {
//			for(int i =0; i < file.length; i++) {
//				
//				System.out.println("file:" + file[i]);
//			}
//		}
		System.out.println("param:" + param);
		param.put("stat", "00"); // 상태
		param.put("del_yn", "N"); // 상태
		param.put("create_no", "104"); // 상태
		param.put("update_no", "104"); // 상태
		
		Map<String, MultipartFile> fileMap = request.getFileMap();

		Set<String> keySet = fileMap.keySet();
		
		MultipartFile[] imgArray = (MultipartFile[])param.get("input-file-1");
		
		for(MultipartFile img : imgArray) {
			if(img != null && !img.isEmpty()) {
				
				String fName = img.getOriginalFilename();
				String ext = fName.substring(fName.lastIndexOf(".")+1, fName.length());
				
				String temp_dir = "c:\\temp\\";
				
				File uploadTempFile = new File(temp_dir + img.getOriginalFilename());
				img.transferTo(uploadTempFile);

//				
//				if(request.getHeader("User-Agent").contains("Android") || request.getHeader("User-Agent").contains("iPhone")) {
//
//			    	BufferedImage srcImg = Utils.checkImage(uploadTempFile, Utils.getOrientation(uploadTempFile));
//				    ImageIO.write(srcImg, ext, uploadTempFile);
//			    }
				
				

			}
		}
		
		return "Y";
	}
	
	/**
	 * @Author : kwonsco
	 * @Date	: 2021. 03. 24.
	 * @Method Name : listActivityAdmin
	 * @return : String
	 * @note :워커맨용 알림리스트
	 */
	@PostMapping(path="/sample/sample5/upload/ajax")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String save4(
			@RequestParam Map<String, Object> param,
			@RequestParam("upLoadFile") MultipartFile[] files,
			Model model
			) throws Exception {

		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		System.out.println("param:" + param);
		System.out.println("param:" + files);
		
		/* save file info */
		param.put("files", files);
		param.put("key_no", "99999");
		
		fileService.saveImgInfo( param);
		
		return "Y";
	}

	
	
	
	/**
	 * @Author : dw.kwon
	 * @Date	: 2021. 03. 24.
	 * @Method Name : form
	 * @return : String
	 * @note :고객사 등록 폼
	 */
	@RequestMapping(path="/sample/file/upload/ajax")
	@ResponseBody
	public String save(
			//@RequestPart List<MultipartFile> files,
			@RequestParam Map<String, Object> param,
			HttpServletRequest request,
			MultipartHttpServletRequest files,
			Model model
			) throws Exception {
		/* return map */
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		Map<String, MultipartFile> fileMap = files.getFileMap();
		
		request.getAttribute("upLoadFile");
		param.put("files", fileMap);
		System.out.println("param:" + param);
		param.put("stat", "00"); // 상태
		param.put("del_yn", "N"); // 상태
		param.put("create_no", "104"); // 상태
		param.put("update_no", "104"); // 상태
		fileService.upload(param);
		
		return "1";
	}
	
	
	
	
	
	
	@RequestMapping(value = "/uploadMultiFile", method = RequestMethod.POST)
	public String submit(
			@RequestParam("files") MultipartFile[] files, 
			ModelMap modelMap) {
	    modelMap.addAttribute("files", files);
	    return "fileUploadView";
	}

	
	
//	@PostMapping(path="/gps/upload")
//	public ResponseEntity<ResultVo> login(
//			@ApiParam(value = "관리자위치업로드" )@Valid @RequestBody AdminGpsReqVo adminGpsReqVo) throws Exception {
//		return new ResponseEntity<ResultVo>(adminGpsHistService.insertAdminGpsHist(adminGpsReqVo), HttpStatus.OK);
//	}
	
}
