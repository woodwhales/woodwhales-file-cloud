package org.woodwhales.cloud.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "ftp")
public class FileCloudConfig {

	// ftp服务器IP地址
	private String host;
	
	// ftp 服务器端口号，默认21
	private Integer port = 21;
	
	// ftp账号用户名
	private String user;
	
	// ftp账号密码
	private String password;
}
