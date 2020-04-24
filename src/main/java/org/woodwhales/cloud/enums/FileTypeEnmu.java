package org.woodwhales.cloud.enums;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileTypeEnmu {

	TEXT((byte) 1, "文本类型"), 
	IMAGE((byte) 2, "图像类型"), 
	VIDEO((byte) 3, "视频类型"), 
	MUSIC((byte) 4, "音乐类型"),
	OTHER((byte) 5, "其他类型");

	private Byte type;
	private String desc;

	public static FileTypeEnmu match(String fileType) {
		if(StringUtils.equalsAnyIgnoreCase(fileType, "txt", "doc", "docx", "wps", "word", "html", "pdf")) {
			return FileTypeEnmu.TEXT;
		}
		
		if(StringUtils.equalsAnyIgnoreCase(fileType, "bmp", "gif", "jpg", "pic", "png", "jepg", "webp", "svg", "ico")) {
			return FileTypeEnmu.IMAGE;
		}
		
		if(StringUtils.equalsAnyIgnoreCase(fileType, "avi", "mov", "qt", "asf", "rm", "navi", "wav", "mp4")) {
			return FileTypeEnmu.VIDEO;
		}
		
		if(StringUtils.equalsAnyIgnoreCase(fileType, "mp3", "wma")) {
			return FileTypeEnmu.MUSIC;
		}
		
		return FileTypeEnmu.OTHER;
	}

}
