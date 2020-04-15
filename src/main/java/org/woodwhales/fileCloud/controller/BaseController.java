package org.woodwhales.fileCloud.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ModelAttribute;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseController {

	protected String userName;
	
	@ModelAttribute
	public void setUp(HttpServletRequest request, HttpServletResponse response) {
		log.info("execute setUp...");
		
	}
}
