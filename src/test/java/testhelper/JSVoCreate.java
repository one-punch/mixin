package testhelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wteam.mixin.utils.FileUtils;



/**
 * 生成vo对应 的js文件。
 * @version 1.0
 * @author benko
 * @time 2016-2-18 10:59:21
 */
public class JSVoCreate {

	/**
	 * 字符串"get"的长度.
	 */
	private static final int LENGTH_OF_STRING_GET = 3;

	private static final String format = "/**\n" +
			" * \n" +
			" */\n" +
			"if (typeof %s == 'undefined') {\n" +
			"	function %s() {\n%s	}\n" +
			"}\n" +
			"%s.prototype.toJson = function() {\n" +
			"	var temp = JSON.stringify(this);\n" +
			"	return temp;\n" +
			"};\n";

	private static final String fieldFormat = "		this.%s = null;\n";


	private String fileName;

	/**
	 * 将vo类解析成js文本.
	 * @param clazz
	 * @return
	 */
	public String[] paserClass(Class<?> clazz) {
		String jsvoString = "";
		String fieldString = "";

		fileName = clazz.getSimpleName();
		if (fileName.substring(fileName.length()-2, fileName.length()).equals("Vo")) {
			fileName = fileName.substring(0, fileName.length() -2);
		}
		System.out.println("JSVoCreate.paserClass()" + fileName);

		String methodName = null;
		String fieldName = null;
		for (Method m : clazz.getDeclaredMethods()) {
			methodName = m.getName();
			if ("get".equals(methodName.substring(0, LENGTH_OF_STRING_GET))) {

				fieldName = methodName.substring(LENGTH_OF_STRING_GET,
						methodName.length());
				char[] fieldNameArr = fieldName.toCharArray();
				fieldNameArr[0] = Character
						.toLowerCase(fieldNameArr[0]);
				fieldName = new String(fieldNameArr);

				fieldString += String.format(fieldFormat, fieldName);
			}
		}
		System.out.println("JSVoCreate.paserClass() fieldString:" + fieldString);

		jsvoString = String.format(format, fileName,fileName,fieldString,fileName);
		System.out.println("JSVoCreate.paserClass() jsvoString:\n" + jsvoString);
		return new String[]{fieldName, jsvoString};
	}

    public static String createRelativePath(final String fileName, final String... paths){
    	// 根目录的相对路径
    	String relativePath = "";
    	for (String path : paths) {
			relativePath += File.separator + path;
		}
    	relativePath += File.separator + fileName;
    	return relativePath;
    }


	/**
	 * 创建js文件
	 * @param clazz
	 */
	public void createFile(String content, String fileName) {
		try {
			String destFileName = System.getProperty("user.dir") +
					createRelativePath(fileName + ".js", "src","main","webapp","common","js");
			FileUtils.createFile(destFileName);
			FileWriter fw = new FileWriter(destFileName);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 创建js文件
	 * @param clazz
	 */
	public void createFile(List<Class<?>> classes){
		String string = "";
		for (Class<?> clazz : classes) {
			String[] arr = paserClass(clazz);
			System.out.println("JSVoCreate.createFile()" + arr[1]);
			string += arr[1];
		}

		createFile(string, "entity");
	}

	/**
	 * 获取包下面的所有类。
	 * @param pckgname
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?>[] getClasses(String pckgname)
			throws ClassNotFoundException {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		// Get a File object for the package
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null)
				throw new ClassNotFoundException("Can't get class loader.");
			String path = pckgname.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null)
				throw new ClassNotFoundException("No resource for " + path);
			directory = new File(resource.getFile());
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " (" + directory
					+ ") does not appear to be a valid package a");
		}
		if (directory.exists()) {
			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				// we are only interested in .class files
				if (files[i].endsWith(".class")) {
					// removes the .class extension
					classes.add(Class.forName(pckgname + '.'
							+ files[i].substring(0, files[i].length() - 6)));
				}
			}
		} else
			throw new ClassNotFoundException(pckgname
					+ " does not appear to be a valid package b");
		Class<?>[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		JSVoCreate create = new JSVoCreate();
		List<Class<?>> list = new ArrayList<Class<?>>();
		System.out.println("JSVoCreate.main(1)");
		list.addAll(Arrays.asList(getClasses("com.wteam.bawanglong.biz.model.vo")));
		create.createFile(list);
		System.out.println("JSVoCreate.main(2)");
	}
}
