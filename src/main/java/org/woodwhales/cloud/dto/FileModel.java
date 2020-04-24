package org.woodwhales.cloud.dto;

import java.util.Date;
import java.util.Objects;

import org.apache.commons.net.ftp.FTPFile;
import org.woodwhales.cloud.enums.FileTypeEnmu;
import org.woodwhales.cloud.util.FileTools;

import cn.hutool.core.lang.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileModel {
	
	// 文件编号
	private String fileCode;

	// 文件名
	private String name;
	
	// 文件路径
	private String path;
	
	// 文件类型
	private FileTypeEnmu fileType;
	
	// 文件名后缀
	private String type;
	
	// 文件大小，单位：字节
	private Long fileSize;
	
	// 格式化的文件大小
	private String formatedFileSize;
	
	// 是否为文件夹
	private boolean directory;
	
	// 是否为文件
	private boolean file;
	
	// 修改时间
	private Date updateTime;
	
	// 格式化后的修改时间
	private String formatedUpdateTime;
	
	public static FileModel convert(FTPFile ftpFile, String path) {
		Objects.requireNonNull(path, "ftpFile 对象不允许为空");
		
		return FileModel.builder()
						 .fileCode(UUID.randomUUID().toString())
						 .name(ftpFile.getName())
						 .path(path)
						 .type(FileTools.getFileType(ftpFile.getName()))
						 .fileType(FileTypeEnmu.match(FileTools.getFileType(ftpFile.getName())))
						 .fileSize(ftpFile.getSize())
						 .formatedFileSize(FileTools.readableFileSize(ftpFile.getSize()))
						 .file(ftpFile.isFile())
						 .directory(ftpFile.isDirectory())
						 .updateTime(ftpFile.getTimestamp().getTime())
						 .formatedUpdateTime(FileTools.formatFileUpdateTime(ftpFile))
						 .build();
	}
}
