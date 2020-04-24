package org.woodwhales.cloud.controller.request;

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建文件夹
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MakeDirRequestBody {

	private String path;
	
	@NotBlank(message = "要创建的文件夹名称不允许为空")
	private String name;
	
	public String getFilePath() {
		return StringUtils.equals(StringUtils.defaultIfBlank(path, "/"), "/") ? (path + name): (path+"/"+name);
	}
}
