package org.woodwhales.cloud.controller.request;

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新文件
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestBody {

	@NotBlank(message = "要更新的文件目录不允许为空")
	private String oldPath;
	
	private String oldFileName;

	private String newPath;
	
	private String newFileName;
	
	public String getOldFilePath() {
		return oldPath + oldFileName;
	}
	
	public String getNewFilePath() {
		// 新文件路径不写表示，只更新文件名称
		if(StringUtils.isBlank(newPath)) {
			newPath = oldPath;
		}
		
		// 新文件名不写，表示剪切文件到新目录
		if(StringUtils.isBlank(newFileName)) {
			newFileName = oldFileName;
		}
		
		return newPath + newFileName;
	}
	
	/**
	 * 是否不需要更新
	 * @return
	 */
	public boolean isNotNeedUpdate() {
		return StringUtils.equals(getOldFilePath(), getNewFilePath());
	}
}
