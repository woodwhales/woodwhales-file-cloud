package org.woodwhales.cloud.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.woodwhales.cloud.controller.request.ListRequestParam;
import org.woodwhales.cloud.dto.FileModel;
import org.woodwhales.cloud.ftp.FtpService;
import org.woodwhales.cloud.util.JsonTool;
import org.woodwhales.cloud.vo.FileVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IndexController {
	
	@Autowired
	private FtpService ftpService;
	
	@Value("${version}")
	private String version;

	@GetMapping({"", "/", "/index"})
	public String index(Model model, ListRequestParam listRequestParam, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
		String currentPath = StringUtils.defaultIfBlank(listRequestParam.getPath(), "/");
		
		if(listRequestParam.isToParent()) {
			currentPath = StringUtils.substringBeforeLast(currentPath, "/");
			currentPath = StringUtils.defaultIfBlank(currentPath, "/");
			redirectAttributes.addAttribute("path", currentPath);
			return "redirect:/index";
		}
		
		List<FileModel> files = ftpService.lsFiles(currentPath, listRequestParam.isToParent());
		log.info("invoke ftpService to list files, path = {}, result = {}", currentPath, JsonTool.toJsonString(files));

		model.addAttribute("currentPath", currentPath);
		model.addAttribute("version", version);
		
		if(CollectionUtils.isNotEmpty(files)) {
			model.addAttribute("files", files.stream().map(FileVO::convert).collect(Collectors.toList()));
		} else {
			model.addAttribute("files", Collections.emptyList());
		}
		return "index";
	}
	
	@ResponseBody
	@GetMapping("/about")
	public void about(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendRedirect("https://woodwhales.cn/");
	}
	
}
