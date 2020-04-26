package org.woodwhales.cloud.vo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.woodwhales.cloud.dto.FileModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoVO {

	// 文件编号
	private String fileCode; 
	
	// 文件名
	private String name;
	
	// 文件路径
	private String path;
	
	// 文件完整路径
	private String filePath;
	
	// 文件大小，单位：字节
	private String fileSize;
	
	// 修改时间
	private String updateTime;
	
	// 创建者
	private String creator;
	
	public static FileInfoVO convert(FileModel fileModel) {
		FileInfoVO fileInfoVO = FileInfoVO.builder().build();
		BeanUtils.copyProperties(fileModel, fileInfoVO);
		
		if(fileModel.isDirectory()) {
			fileInfoVO.setFileSize("-");
		} else {
			fileInfoVO.setFileSize(fileModel.getFileSize() + "");
		}

		fileInfoVO.setUpdateTime(fileModel.getFormatedUpdateTime());
		fileInfoVO.setFilePath(StringUtils.equals(fileModel.getPath(), "/") ? fileModel.getPath() + fileModel.getName() : fileModel.getPath() + "/" + fileModel.getName());
		fileInfoVO.setCreator("woodwhales");
		return fileInfoVO;
	}
}
