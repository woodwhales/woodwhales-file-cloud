package org.woodwhales.cloud.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.woodwhales.cloud.controller.request.DeleteDirRequestBody;
import org.woodwhales.cloud.controller.request.DeleteFileRequestBody;
import org.woodwhales.cloud.controller.request.DownloadRequestParam;
import org.woodwhales.cloud.controller.request.ListRequestParam;
import org.woodwhales.cloud.controller.request.MakeDirRequestBody;
import org.woodwhales.cloud.controller.request.UpdateRequestBody;
import org.woodwhales.cloud.controller.request.UploadRequestBody;
import org.woodwhales.cloud.exception.RequestParamException;
import org.woodwhales.cloud.ftp.FtpService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/file")
@RestController
public class FileController {
	
	@Autowired
	private FtpService ftpService;
	
	@GetMapping("/lsFiles")
	public Object lsFiles(ListRequestParam listRequestParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return ftpService.lsFiles(StringUtils.defaultIfBlank(listRequestParam.getPath(), "/"), listRequestParam.isToParent());
	}
	
	/**
	 * 上传文件
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file, @RequestBody @Validated UploadRequestBody uploadRequestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return ftpService.upload(uploadRequestBody.getPath(), file.getOriginalFilename(), file.getInputStream()) + "";
	}
	
	/**
	 * 删除目录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/deleteDir")
	public String deleteDir(@RequestBody @Validated DeleteDirRequestBody deleteDirRequestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return ftpService.deleteDir(deleteDirRequestBody.getPath())+"";
	}
	
	/**
	 * 删除文件
	 * @param fileName
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/deleteFile")
	public String deleteFile(@RequestBody @Validated DeleteFileRequestBody deleteFileRequestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return ftpService.deleteFile(deleteFileRequestBody.getFilePath())+"";
	}
	
	/**
	 * 下载文件
	 * @param fileName
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/download")
	public String download(@Validated DownloadRequestParam downloadRequestParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean exist = ftpService.existFile(downloadRequestParam.getFilePath());
		if(!exist) {
			return "false";
		}
		response.setHeader("Content-Disposition", "attachment;fileName="+downloadRequestParam.getFileName());
		ftpService.download(downloadRequestParam.getPath(), downloadRequestParam.getFileName(), response.getOutputStream());
		return "true";
	}
	
	/**
	 * 更新文件
	 * @param oldFileName
	 * @param newFileName
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/update")
	public String download(@RequestBody @Validated UpdateRequestBody updateRequestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean exist = ftpService.existFile(updateRequestBody.getOldFilePath());
		if(!exist) {
			throw RequestParamException.build("要更新的文件不存在");
		}

		// 不需要更新直接返回
		if(updateRequestBody.isNotNeedUpdate()) {
			log.info("不需要更新");
			return "true";
		}
		
		boolean updateFileName = ftpService.updateFileName(updateRequestBody.getOldFilePath(), updateRequestBody.getNewFilePath(), updateRequestBody.getNewPath());
		if(updateFileName) {
			return "true";
		}
		return "false";
	}
	
	/**
	 * 创建文件夹
	 * @param makeDirRequestBody
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/mkdir")
	public String mkdir(@RequestBody @Validated MakeDirRequestBody makeDirRequestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String parentDir = StringUtils.defaultIfBlank(makeDirRequestBody.getPath(), "/");
		boolean existFile = ftpService.existFile(parentDir);
		if(!existFile) {
			throw RequestParamException.build("要创建文件夹的路径不存在");
		}
		
		boolean mkDir = ftpService.mkDir(makeDirRequestBody.getFilePath());
		if(mkDir) {
			return "true";
		}
		return "false";
	}

}
