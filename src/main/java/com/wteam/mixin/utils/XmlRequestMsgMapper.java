package com.wteam.mixin.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;

import com.wteam.mixin.model.wexin.RequestMessage;

import java.util.Set;

public class XmlRequestMsgMapper {
	
	public static RequestMessage map(Class<RequestMessage> clz, Map<String,String> map) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException{
		
		RequestMessage obj = clz.newInstance();
		Set<Entry<String, String>> entrySet = map.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String fieldName = entry.getKey();
			String value = entry.getValue();
			String pre = fieldName.substring(0,1).toUpperCase();
			String post = fieldName.substring(1, fieldName.length());
			String methodName = "set"+pre+post;
			Class<?> type = clz.getDeclaredField(fieldName).getType();
			String typeName = type.getSimpleName();
			switch (typeName) {
				case "long":
					clz.getMethod(methodName, type).invoke(obj, Long.parseLong(value));
					break;
				case "short":
					clz.getMethod(methodName, type).invoke(obj, Short.parseShort(value));
					break;
				case "int":
					clz.getMethod(methodName, type).invoke(obj, Integer.parseInt(value));
					break;
				case "char":
					clz.getMethod(methodName, type).invoke(obj, value.charAt(0));
					break;
				case "float":
					clz.getMethod(methodName, type).invoke(obj, Float.parseFloat(value));
					break;
				case "double":
					clz.getMethod(methodName, type).invoke(obj, Double.parseDouble(value));
					break;
				case "boolean":
					clz.getMethod(methodName, type).invoke(obj, Boolean.parseBoolean(value));
					break;
				case "String":
					clz.getMethod(methodName, type).invoke(obj, value);
					break;
				default:
					break;
			}
		}
		return obj;
	}
	
	/**
	 * 向上回溯查找该类父类中对应字段为fieldName的属性，若查找不到返回NoSuchFieldException
	 * @param clz
	 * @param fieldName
	 * @return
	 * @throws NoSuchFieldException
	 */
	private static Field getField(Class clz, String fieldName) throws NoSuchFieldException{
		if(clz == null){
			throw new NoSuchFieldException(fieldName);
		}
		Field field = null;
		try {
			field = clz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			field = getField(clz.getSuperclass(), fieldName);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return field;
	}
}
