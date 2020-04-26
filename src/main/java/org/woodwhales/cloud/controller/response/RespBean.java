package org.woodwhales.cloud.controller.response;

import org.apache.commons.lang3.StringUtils;
import org.woodwhales.cloud.common.RespCodeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {

	private Integer code;
    private String msg;
    private Object data;
    
    public static RespBean build() {
        return new RespBean();
    }
    
    public static RespBean build(Boolean result) {
    	if(result) {
    		return emptyOk();
    	}
    	
    	return emptyError();
    }
    
    /**
     * 成功响应
     * @param msg
     * @return
     */
    public static RespBean ok(Object data) {
        return build(RespCodeEnum.SUCCESS, data);
    }
    
    /**
     * 成功响应，data为空字符串
     * @return
     */
    public static RespBean emptyOk() {
        return build(RespCodeEnum.SUCCESS, StringUtils.EMPTY);
    }
    
    /**
     * 失败响应
     * @param respCodeEnum
     * @return
     */
    public static RespBean error(RespCodeEnum respCodeEnum) {
        return build(respCodeEnum, StringUtils.EMPTY);
    }
    
    /**
     * 失败响应
     * @param errorMsg
     * @return
     */
    public static RespBean error(String errorMsg) {
    	RespBean respBean = build();
    	respBean.setCode(RespCodeEnum.ERROR.getCode());
    	respBean.setMsg(errorMsg);
    	respBean.setData(StringUtils.EMPTY);
        return respBean;
    }
    
    /**
     * 失败响应，data为空字符串
     * @param msg
     * @return
     */
    public static RespBean emptyError() {
        return build(RespCodeEnum.ERROR, StringUtils.EMPTY);
    }
    
    private static RespBean build(RespCodeEnum respCodeEnum, Object data) {
    	return new RespBean(respCodeEnum.getCode(), respCodeEnum.getMsg(), data);
    }
    
}
