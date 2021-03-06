package com.xinleju.platform.uitls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class FileUtils {
	
	private static Logger log = Logger.getLogger(FileUtils.class);

	public static List<String> readLines(String fileName) {
		InputStream is = FileUtils.class.getResourceAsStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		List<String> lines = new ArrayList<String>();
		try {
			String line = reader.readLine();
			while(line != null) {
				lines.add(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}
		log.info("读取配置文件" + fileName + "内容 ：" + lines);
		return lines;
	}
}
