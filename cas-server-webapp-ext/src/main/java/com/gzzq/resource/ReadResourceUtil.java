package com.gzzq.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 获取资源文件路径
 * 
 * @author linzl
 *
 */
public class ReadResourceUtil {
	private String path;
	private ClassLoader classLoader;
	private Class<?> clazz;

	/**
	 * 初始ClassLoader根路径资源
	 */
	public ReadResourceUtil() {
		this("", (ClassLoader) null);
	}

	public ReadResourceUtil(String path) {
		this(path, (ClassLoader) null);
	}

	public ReadResourceUtil(String path, ClassLoader classLoader) {
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		this.path = path;
		this.classLoader = ((classLoader != null) ? classLoader : getDefaultClassLoader());
	}

	public ReadResourceUtil(String path, Class<?> clazz) {
		this.path = path;
		this.clazz = clazz;
	}

	public ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			System.out.println("Cannot access thread context ClassLoader - falling back to system class loader" + ex);
		}
		if (cl == null) {
			cl = ReadResourceUtil.class.getClassLoader();
		}
		return cl;
	}

	public URL getURL() throws IOException {
		URL url = null;
		if (this.clazz != null) {
			url = this.clazz.getResource(this.path);
		} else {
			url = this.classLoader.getResource(this.path);
		}
		if (url == null) {
			throw new FileNotFoundException(
					"class path resource [" + this.path + "] cannot be resolved to URL because it does not exist");
		}
		return url;
	}

	public InputStream getInputStream() throws IOException {
		InputStream input = null;
		if (this.clazz != null) {
			input = this.clazz.getResourceAsStream(this.path);
		} else {
			input = this.classLoader.getResourceAsStream(this.path);
		}
		if (input == null) {
			throw new FileNotFoundException(
					"class path resource [" + this.path + "] cannot be resolved to URL because it does not exist");
		}
		return input;
	}

	/**
	 * 获取cls类加载器中 /WebContent/WEB-INF/路径下的资源文件,递归搜索
	 * 
	 * @param name
	 *            资源文件名称
	 * @param cls
	 * @return
	 */
	public URL getWebInfoResouceURL(String name, Class<?> cls) {
		File targetFile = null;
		String classPath = cls.getResource("/").getFile();
		// 先从外围找
		int index = classPath.indexOf("classes");
		if (index > -1) {
			String outClassPath = classPath.substring(0, index);
			targetFile = findFile(new File(outClassPath), name);
		}

		// 找不到，再深入类文件中看是否有匹配的
		if (targetFile == null && index > -1) {
			// 递归搜索文件
			targetFile = findFile(new File(classPath), name);
		}
		try {
			return targetFile.toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private File findFile(File targetFile, String name) {
		File[] files = targetFile.listFiles();

		if (files == null) {
			return null;
		}
		File target = null;
		for (File file : files) {
			if (file.isFile() && file.getName().equalsIgnoreCase(name)) {
				return file;
			} else {
				target = findFile(file, name);
				if (target != null) {
					return target;
				}
			}
		}
		return target;
	}

	public static void main(String[] args) {
		ReadResourceUtil util = new ReadResourceUtil("/com/linzl/cn/property/jdbc.properties");
		// ReadResourceUtil util = new ReadResourceUtil();
		// ReadResourceUtil util = new ReadResourceUtil("src.properties");
		try {
			URL url = util.getURL();
			System.out.println(url.toURI().toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
