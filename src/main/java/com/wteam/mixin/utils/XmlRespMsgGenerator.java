package com.wteam.mixin.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.wteam.mixin.converter.StringConverter;

public class XmlRespMsgGenerator {
	
//	public static <T> String generate(Class<T> clz, T obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException{
//		
//		Field[] fields = clz.getDeclaredFields();
//		StringBuffer builder = new StringBuffer();
//		builder.append("<xml>\n");
//		for (Field field : fields) {
//			String fieldName = field.getName();
//			String pre = fieldName.substring(0,1).toUpperCase();
//			String post = fieldName.substring(1, fieldName.length());
//			String methodName = "get"+pre+post;
//			Object invoke = clz.getDeclaredMethod(methodName, new Class[]{}).invoke(obj, new Object[]{});
//			if(invoke != null){
//				builder.append("<"+fieldName+">"+invoke+"</"+fieldName+">\n");
//			}
//		}
//		builder.append("</xml>");
//		return builder.toString();
//	}
	
	public static <T> String generate(T obj){
		
		XStream xs = new XStream(new XppDriver(new XmlFriendlyReplacer()));
		xs.registerConverter(new StringConverter()); 
		xs.alias("xml", obj.getClass());
		
		return xs.toXML(obj).replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("__", "_").replaceAll("&quot;", "\"");
	}
}
