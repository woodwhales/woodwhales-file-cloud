package org.woodwhales.cloud;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.woodwhales.cloud.util.FileTools;

public class FileToolsTest {

	@Test
	public void test() {
		String fileType = FileTools.getFileType("aa.txt.ss");
		assertEquals("ss", fileType);
		
		System.out.println(StringUtils.substringBeforeLast("/css", "/"));
	}
}
