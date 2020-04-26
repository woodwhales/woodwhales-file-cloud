package org.woodwhales.cloud.controller.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查看文件（文件或目录）
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailFileRequestBody {

	@NotBlank(message = "要查看的文件编号不允许为空")
	private String code;
	
	@NotBlank(message = "要查看的目录不允许为空")
	private String path;
	
	@NotBlank(message = "要查看的文件名不允许为空")
	private String fileName;
	
}
