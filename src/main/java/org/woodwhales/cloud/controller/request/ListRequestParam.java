package org.woodwhales.cloud.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件列表查询
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListRequestParam {
	// 是否需要返回上级目录
	private boolean toParent = false;
	
	// 要访问的路径
	private String path = "/";
	
}
