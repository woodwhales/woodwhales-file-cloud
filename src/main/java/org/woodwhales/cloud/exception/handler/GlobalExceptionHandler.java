package org.woodwhales.cloud.exception.handler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.woodwhales.cloud.exception.RequestParamException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = {Exception.class, Error.class})
	public ModelAndView exception(HttpServletRequest request, HttpServletResponse response, Exception exception) {
		log.error("happen exception, {}, errorMsg = {}", exception, exception.getMessage());
		return newModelAndView(exception);
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public Object methodArgumentNotValidException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException exception) {
		log.error("happen exception, {}", exception);
		
		BindingResult bindingResult = exception.getBindingResult();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		return newModelAndView(fieldErrors.get(0).getDefaultMessage());
	}
	
	@ExceptionHandler(value = RequestParamException.class)
	public Object requestParamException(HttpServletRequest request, HttpServletResponse response, RequestParamException exception) {
		log.error("happen exception, {}", exception);
		return newModelAndView(exception);
	}
	
	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	public Object requestParamException(HttpServletRequest request, HttpServletResponse response, HttpRequestMethodNotSupportedException exception) {
		log.error("happen exception, {}", exception);
		return newModelAndView("请求方法不支持");
	}
	
	private ModelAndView newModelAndView(String errorMsg) {
		ModelAndView modelAndView = new ModelAndView("/error/500.html");
		modelAndView.addObject("errorMsg", errorMsg);
		return modelAndView;
	}
	
	private ModelAndView newModelAndView(Exception exception) {
		return newModelAndView(exception.getMessage());
	}
}
