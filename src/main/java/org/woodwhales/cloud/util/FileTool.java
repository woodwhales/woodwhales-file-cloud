package org.woodwhales.cloud.util;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.woodwhales.cloud.dto.FileModel;
import org.woodwhales.cloud.enums.FileTypeEnmu;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;

public class FileTool {
	
	private final static String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
	private final static String fileSizeFormat = "#,##0.#";
	private final static String emptySize = "-";

	private FileTool() {}
	
	/**
	 * 文件字节大小转换，带单位
	 * 如果没有大小，则返回 "-"
	 * 
	 * @param size
	 * @return
	 */
	public static String readableFileSize(long size) {
        if (size <= 0) {
        	return emptySize;
        }

        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        
        return NumberUtil.decimalFormat(fileSizeFormat, size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
	
	/**
	 * 获取文件后缀名
	 * @param fileName
	 * @return 1:文本类型   2:图像类型  3:视频类型  4:音乐类型  5:其他类型
	 */
	public static String getFileType(String fileName) {
		return StringUtils.substringAfterLast(fileName, ".");
	}
	
	/**
	 * 格式化文件的修改时间
	 * @param date
	 * @return
	 */
	public static String formatFileUpdateTime(FTPFile ftpFile) {
		Objects.requireNonNull(ftpFile, "ftpFile 对象不允许为空");
		return DateUtil.format(ftpFile.getTimestamp().getTime(), DatePattern.NORM_DATETIME_PATTERN);
	}
	
	/**
	 * 根据文件类型获取对应的ionicon图标名称
	 * 图标官网地址：https://ionicons.com/
	 * @param fileModel
	 * @return
	 */
	public static String getIoniconName(FileModel fileModel) {
		
		boolean isDirectory = fileModel.isDirectory();
		if(isDirectory) {
			return "folder-outline";
		}
		
		FileTypeEnmu fileType = fileModel.getFileType();
		
		if(fileType == FileTypeEnmu.IMAGE) {
			return "image-outline";
		}
		
		if(fileType == FileTypeEnmu.VIDEO) {
			return "videocam-outline";
		}
		
		if(fileType == FileTypeEnmu.TEXT) {
			return "document-text-outline";
		}
		
		
		if(fileType == FileTypeEnmu.MUSIC) {
			return "musical-notes-outline";
		}
		
		return "document-outline";
	}
}
