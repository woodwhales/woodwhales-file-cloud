package org.woodwhales.cloud.controller.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 删除文件（文件或目录）
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteRequestBody {

	@NotBlank(message = "要删除的文件编号不允许为空")
	private String code;
	
}
