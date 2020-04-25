package org.woodwhales.cloud.controller.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分享文件
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareFileRequestBody {

	@NotBlank(message = "要分享的文件编号允许为空")
	private String code;
	
}
