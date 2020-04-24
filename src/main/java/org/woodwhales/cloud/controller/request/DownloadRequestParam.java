package org.woodwhales.cloud.controller.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 下载文件
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadRequestParam {

	@NotBlank(message = "要下载的目录不允许为空")
	private String path;
	
	@NotBlank(message = "要下载的文件名不允许为空")
	private String fileName;
	
	public String getFilePath() {
		return path + "/" + fileName;
	}

}
