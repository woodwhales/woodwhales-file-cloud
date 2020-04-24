package org.woodwhales.cloud.controller.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 删除目录
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteDirRequestBody {

	@NotBlank(message = "要删除的目录不允许为空")
	private String path;
	
}
