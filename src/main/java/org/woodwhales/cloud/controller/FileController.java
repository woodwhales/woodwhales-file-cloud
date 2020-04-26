package org.woodwhales.cloud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
import org.woodwhales.cloud.controller.request.DeleteRequestBody;
import org.woodwhales.cloud.controller.request.DetailFileRequestBody;
import org.woodwhales.cloud.controller.request.DownloadRequestParam;
import org.woodwhales.cloud.controller.request.ListRequestParam;
import org.woodwhales.cloud.controller.request.MakeDirRequestBody;
import org.woodwhales.cloud.controller.request.ShareFileRequestBody;
import org.woodwhales.cloud.controller.request.UpdateRequestBody;
import org.woodwhales.cloud.controller.request.UploadRequestBody;
import org.woodwhales.cloud.controller.response.RespBean;
import org.woodwhales.cloud.dto.FileModel;
import org.woodwhales.cloud.ftp.FtpService;
import org.woodwhales.cloud.vo.FileInfoVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/file")
@RestController
public class FileController {
	
	@Autowired
	private FtpService ftpService;
	
	/**
	 * 列表展示
	 * @param listRequestParam
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/lsFiles")
	public RespBean lsFiles(ListRequestParam listRequestParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<FileModel> files = ftpService.lsFiles(StringUtils.defaultIfBlank(listRequestParam.getPath(), "/"), listRequestParam.isToParent());
		return RespBean.ok(files);
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
	public RespBean upload(@RequestParam("file") MultipartFile file, @RequestBody @Validated UploadRequestBody uploadRequestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean result = ftpService.upload(uploadRequestBody.getPath(), file.getOriginalFilename(), file.getInputStream());
		return RespBean.build(result);
	}
	
	/**
	 * 删除目录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/deleteDir")
	public RespBean deleteDir(@RequestBody @Validated DeleteDirRequestBody deleteDirRequestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return RespBean.build(ftpService.deleteDir(deleteDirRequestBody.getPath()));
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
	public RespBean deleteFile(@RequestBody @Validated DeleteFileRequestBody deleteFileRequestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return RespBean.build(ftpService.deleteFile(deleteFileRequestBody.getFilePath()));
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
	public RespBean download(@Validated DownloadRequestParam downloadRequestParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean exist = ftpService.existFile(downloadRequestParam.getFilePath());
		if(!exist) {
			return RespBean.error("要下载的文件不存在");
		}
		response.setHeader("Content-Disposition", "attachment;fileName="+downloadRequestParam.getFileName());
		ftpService.download(downloadRequestParam.getPath(), downloadRequestParam.getFileName(), response.getOutputStream());
		return RespBean.emptyOk();
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
	public RespBean download(@RequestBody @Validated UpdateRequestBody updateRequestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean exist = ftpService.existFile(updateRequestBody.getOldFilePath());
		if(!exist) {
			return RespBean.error("要更新的文件不存在");
		}

		// 不需要更新直接返回
		if(updateRequestBody.isNotNeedUpdate()) {
			log.info("不需要更新");
			return RespBean.emptyOk();
		}
		
		return RespBean.build(ftpService.updateFileName(updateRequestBody.getOldFilePath(), updateRequestBody.getNewFilePath(), updateRequestBody.getNewPath()));
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
	public RespBean mkdir(@RequestBody @Validated MakeDirRequestBody makeDirRequestBody, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String parentDir = StringUtils.defaultIfBlank(makeDirRequestBody.getPath(), "/");
		boolean existFile = ftpService.existFile(parentDir);
		if(!existFile) {
			return RespBean.error("要创建文件夹的路径不存在");
		}
		
		return RespBean.build(ftpService.mkDir(makeDirRequestBody.getFilePath()));
	}
	
	/**
	 * 分享文件
	 * 生成分享码
	 * 分享码包含：当前分享文件code，分享拥有者code，分享起始时间，分享类型（临时/永久）
	 * @param shareFileRequestBody
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/share")
	public RespBean share(@RequestBody @Validated ShareFileRequestBody shareFileRequestBody, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("shareCode", "shareCode-" + StringUtils.replace(shareFileRequestBody.getCode(), "-", ""));
		return RespBean.ok(map);
	}
	
	/**
	 * 删除文件
	 * @param deleteRequestBody
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/delete")
	public RespBean share(@RequestBody @Validated DeleteRequestBody deleteRequestBody, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("fileCode", "code-" + StringUtils.replace(deleteRequestBody.getCode(), "-", ""));
		return RespBean.ok(map);
	}

	/**
	 * 查看文件详情
	 * @param detailFileRequestBody
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/detail")
	public RespBean detail(@RequestBody @Validated DetailFileRequestBody detailFileRequestBody) throws Exception {
		String fileCode = detailFileRequestBody.getCode();
		String path = detailFileRequestBody.getPath();
		String fileName = detailFileRequestBody.getFileName();
		
		boolean existFile = ftpService.existFile(path);
		if(!existFile) {
			return RespBean.error("要查看的文件的父文件目录的路径不存在");
		}
		
		FileModel fileModel = ftpService.getFile(path, fileName);
		
		if(Objects.isNull(fileModel)) {
			return RespBean.error("要查看的文件不存在");
		}
		
		return RespBean.ok(FileInfoVO.convert(fileModel));
	}
}
