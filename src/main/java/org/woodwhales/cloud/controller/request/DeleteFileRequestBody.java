package org.woodwhales.cloud.controller.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 删除文件
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteFileRequestBody {

	@NotBlank(message = "要删除的目录不允许为空")
	private String path;
	
	@NotBlank(message = "要删除的文件名不允许为空")
	private String fileName;
	
	public String getFilePath() {
		return path + "/" + fileName;
	}
	
}
