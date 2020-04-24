package org.woodwhales.cloud.vo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.woodwhales.cloud.dto.FileModel;
import org.woodwhales.cloud.enums.FileTypeEnmu;
import org.woodwhales.cloud.util.FileTools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileVO {
	
	// 文件编号
	private String fileCode; 
	
	// 文件名
	private String name;
	
	// 文件路径
	private String path;
	
	// 文件完整路径
	private String filePath;
	
	// 文件类型
	private FileTypeEnmu fileType;
	
	// 文件名后缀
	private String type;
	
	// 文件大小，单位：字节
	private String fileSize;
	
	// 是否为文件夹
	private boolean directory;
	
	// 是否为文件
	private boolean file;
	
	// 修改时间
	private String updateTime;
	
	// 文件图标样式
	// 图标官网地址：https://ionicons.com/
	private String ioniconName;
	
	public static FileVO convert(FileModel fileModel) {
		FileVO fileVO = FileVO.builder().build();
		BeanUtils.copyProperties(fileModel, fileVO);
		fileVO.setFileSize(fileModel.getFormatedFileSize());
		fileVO.setUpdateTime(fileModel.getFormatedUpdateTime());
		fileVO.setIoniconName(FileTools.getIoniconName(fileModel));
		fileVO.setFilePath(StringUtils.equals(fileModel.getPath(), "/") ? fileModel.getPath() + fileModel.getName() : fileModel.getPath() + "/" + fileModel.getName());
		return fileVO;
	}
}
