package com.wteam.mixin.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class WhereCauseSqlUtils {
	
	/**
	 * 根据实体成员变量赋值情况构造hql查询语句及占位符参数集合
	 * @param poSimpleObjectName 被查询Po对象简单对象名称
	 * @param valueObject 查询条件所在的VO对象
	 * @return result:一个list 0index为where语句， 1index为参数数组
	 */
	public static <T> List<Object> create(String poSimpleObjectName, T valueObject){
		
		List<Object> result = new ArrayList<>(); 
		List<Object> params = new ArrayList<>(); 
		Class<?> objClass = valueObject.getClass();
		String hql = "from "+ poSimpleObjectName;
		Field[] fields = objClass.getDeclaredFields();
		boolean whereCauseSet = false;
		StringBuilder builder = new StringBuilder();
		for (Field field : fields) {
			String fieldName = field.getName();
			String methodName ="get"+ fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1,fieldName.length());
			Object value = null;
			try {
				value = objClass.getMethod(methodName).invoke(valueObject, new Object[]{});
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			if(value==null){
				continue;
			}else{
				String tmp = whereCauseSet?" and "+fieldName+"=?":fieldName+"=?";
				params.add(value);
				whereCauseSet = true;
				builder.append(tmp);
			}
		}
		String whereCauseSql = whereCauseSet?" where "+builder.toString():"";
		hql += whereCauseSql;
		result.add(hql);
		result.add(params);
		
		return result;
	}
}
