package com.workerman.controller.page;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.JsonObject;
import com.workerman.config.Constants;
import com.workerman.controller.BaseController;
import com.workerman.service.FileService;

@Controller
@RequestMapping(value = "/file/*")
public class FileController extends BaseController {
	
	@Autowired
	private FileService fileService;

	/**
	 * 파일업로드
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/upload")
	public @ResponseBody Map<String, Object> upload(@RequestParam Map<String, Object> param, MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		return returnMap;
	}
	
	/*
	 * 파일다운로드
	 * 
	 */
	@RequestMapping(value = "/download")
	public void download(String path, ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		FileInputStream fis = null;

		try {
			
			log.debug("@ path : "+getMessage("project.root.path") + path);
			
			File downloadFile = new File(getMessage("project.root.path") + path);

			// 파일 유무 예외처리
			if (!downloadFile.exists() || !downloadFile.canRead()) {
				PrintWriter out = response.getWriter();
				out.println("<script>alert('File Not Found');history.back();</script>");
				return;
			}

			response.setContentLength((int) downloadFile.length());
			String fileName = null;
			fileName = new String(FilenameUtils.getName(path).getBytes("utf-8"), "iso-8859-1");

			response.setHeader("Content-Disposition", "attachment; filename=\""	+ fileName + "\";");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-type", "application/vnd.android.package-archive");
			OutputStream fout = response.getOutputStream();

			fis = new FileInputStream(downloadFile);
			FileCopyUtils.copy(fis, fout);
			fout.flush();
		} catch (Exception e) {
			e.printStackTrace();
			PrintWriter out = response.getWriter();
			out.println("<script>alert('File Not Found');history.back();</script>");
			return;
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}

	
	/**
	 * 이미지 삭제
	 * @param param
	 * @param modelMap
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteFile(
			@RequestParam Map<String, Object> param, 
			ModelMap modelMap) throws Exception {

	    if(!StringUtils.isBlank((String)param.get("key"))) {
	    	param.put("img_no", (String)param.get("key"));
	    	fileService.deleteImgInfo(param);
	    } else {
	    	throw new Exception(Constants.ERROR_EMPTY_KEY);
	    }
	    
	    JsonObject obj = new JsonObject();
	    obj.addProperty(RET, Constants.EVENT_DELETE_SUCCESS);
	    
	    return obj.toString();
	}

}
