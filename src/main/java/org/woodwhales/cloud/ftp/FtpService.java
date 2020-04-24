package org.woodwhales.cloud.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.stereotype.Service;
import org.woodwhales.cloud.dto.FileModel;
import org.woodwhales.cloud.exception.RequestParamException;

import cn.hutool.extra.ftp.Ftp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FtpService {
	
	/**
	 * 初始化ftp客户端
	 * @return {@link Ftp}
	 */
	private Ftp init() {
		return new Ftp("10.99.0.148", 21, "woodwhales-ftp", "Admin,123");
	}
	
	/**
	 * 关闭ftp客户端
	 * @param ftp
	 */
	private void close(Ftp ftp) {
		if(Objects.nonNull(ftp)) {
			try {
				ftp.close();
			} catch (IOException e) {
				log.error("close ftp process is failed, {}", e);
			}
		}
	}
	
	/**
	 * 遍历当前目录中的所有文件（文件和文件夹）
	 * @param path
	 * @return {@link FileModel}
	 */
	public List<FileModel> lsFiles(String path, boolean isToParent) {
		Ftp ftp = init();
		
		try {
			if(StringUtils.equals(path, "/")) {
				return lsFiles(path, ftp);
			}
			
			boolean exist = ftp.exist(path);
			if(!exist) {
				throw RequestParamException.build("要访问的路径不存在");
			}
			
			if(isToParent) {
				path = StringUtils.substringBeforeLast(path, "/");
			}
		
			return lsFiles(path, ftp);
		} finally {
			close(ftp);
		}
	}
	
	private List<FileModel> lsFiles(String path, Ftp ftp) {
		FTPFile[] ftpFiles = ftp.lsFiles(path);
		
		if(ftpFiles.length == 0) {
			return Collections.emptyList();
		}
		
		return Arrays.asList(ftpFiles).stream().map(ftpFile -> FileModel.convert(ftpFile, path)).collect(Collectors.toList());
	}
	
	/**
	 * 删除文件夹
	 * @param dirPath
	 * @return
	 * @throws Exception
	 */
	public boolean deleteDir(String dirPath) throws Exception {
		Ftp ftp = init();
		try {
			boolean delDirResult = init().delDir(dirPath);
			return delDirResult;
		} finally {
			close(ftp);
		}
	}
	
	/**
	 * 删除文件
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public boolean deleteFile(String path) throws Exception {
		Ftp ftp = init();
		try {
			boolean delDirResult = ftp.delFile(path);
			return delDirResult;
		} finally {
			close(ftp);
		}
	}
	
	/**
	 * 上传文件
	 * @param path
	 * @param fileName
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public boolean upload(String path, String fileName, InputStream inputStream) throws Exception {
		Ftp ftp = init();
		try {
			boolean uploadResult = ftp.upload(path, fileName, inputStream);
			return uploadResult;
		} finally {
			close(ftp);
		}
	}
	
	/**
	 * 判断文件是否存在
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public boolean existFile(String filePath) throws Exception {
		Ftp ftp = init();
		try {
			return ftp.existFile(filePath);
		} finally {
			close(ftp);
		}
	}
	
	private boolean existFile(String filePath, Ftp ftp) throws Exception {
		return ftp.existFile(filePath);
	}
	
	/**
	 * 下载文件
	 * @param path
	 * @param fileName
	 * @param out
	 * @throws Exception
	 */
	public void download(String path, String fileName, OutputStream out) throws Exception {
		Ftp ftp = init();
		try {
			ftp.download(path, fileName, out);
		} finally {
			close(ftp);
		}
	}
	
	
	/**
	 * 更新文件
	 * @param oldFilePath
	 * @param newFilePath
	 * @return
	 * @throws Exception
	 */
	public boolean updateFileName(String oldFilePath, String newFilePath, String newPath) throws Exception {
		Ftp ftp = init();
		try {
			if(!existFile(oldFilePath, ftp)) {
				return false;
			}

			FTPClient client = ftp.getClient();
			mkDir(newPath);
			boolean rename = client.rename(oldFilePath, newFilePath);

			if(rename) {
				return true;
			} 
			
			return false;
		} finally {
			close(ftp);
		}
	}
	
	/**
	 * 创建文件夹
	 * @param dirPath
	 * @return
	 */
	public boolean mkDir(String dirPath) {
		Ftp ftp = init();
		try {
			if(ftp.exist(dirPath)) {
				return true;
			}
			
			return ftp.mkdir(dirPath);
		} finally {
			close(ftp);
		}
	}

}
