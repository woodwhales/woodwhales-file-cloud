package org.woodwhales.cloud.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RespCodeEnum {

	SUCCESS(0, "响应成功"),
	ERROR(-1, "响应失败"),
	;
	
	private Integer code;
	private String msg;
	
}
