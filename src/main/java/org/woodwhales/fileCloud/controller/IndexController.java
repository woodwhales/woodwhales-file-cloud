package org.woodwhales.fileCloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController extends BaseController {

	@GetMapping("/index")
	public String index() {
		return "ok";
	}
}
