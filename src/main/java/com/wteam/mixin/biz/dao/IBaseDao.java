package com.wteam.mixin.biz.dao;

import java.io.Serializable;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;


public interface IBaseDao {

    /**
     * 功能：保存po
     * @param po
     * @return
     */
	<T> Serializable save(T po); 
    

    /**
     * 功能：查找po
     * 前提：有id属性
     * @param poClass
     * @param id
     * @return
     */
    <T> T find(Class<T> poClass,long id); 
    
    /**
     * 功能：删除po
     * 前提：有id属性
     * @param po
     */
    <T> void delete(T po); 

    /**
     * 功能：更新po的属性，
     * 前提：有id属性
     * @param po
     * @return
     */
	<T> void update(T po); 

    /**
     * 功能：更新或保存po的属性，
     * 前提：有id属性
     * @param po
     * @return
     */
	<T> void saveOrUpdate(T po);
	
	/**
	 * 根据属性查找列表
	 * @param property 属性
	 * @param value 属性的值
	 * @param cls 要查找 的Po类
	 * @return
	 */
	<T> List<T> findByProperty(String property, Object value,Class<T> cls);
	
	/**
	 * 根据属性查找一个po
	 * @param property
	 * @param value
	 * @param cls
	 * @return
	 */
	<T> T findUniqueByProperty(String property, Object value,Class<T> cls);
	
	/**
	 * 功能：hql查询
	 * 前提：完整的hql查询语句
	 * @param hql
	 * @return
	 */
	<T>  List<T> find(String hql);
	
	/**
	 * 功能：带占位符的hql查询,参数为集合
	 * 前提：带占位符的hql语句，并传入所需参数集合
	 * 
	 * @param hql
	 * @param param：填充占位符参数
	 * @return
	 */
	<T> List<T> find(String hql, Object[] param);
	
	/**
	 * 功能：帶占位符的hql查询，参数为list
	 * 前提：带占位符的hql语句，并传入所需参数list集合
	 * @param hql
	 * @param param
	 * @return
	 */
	<T> List<T> find(String hql, List<Object> param);
	
	/**
	 * 功能：查询集合（带分页），hql参数存储在集合对象中
	 * 
	 * @param hql
	 * 			
	 * @param param		
	 * @param page
	 * 			查询第几页
	 * @param rows
	 * 			每页显示多少条数据
	 * @return
	 */
	<T> List<T> find(String hql, Object[] param, Integer page, Integer rows);
	
	/**
	 * 功能：查询集合（带分页），hql参数存储在List中
	 * @param hql
	 * @param param
	 * 			查询第几页
	 * @param page
	 * 			每页显示多少条数据
	 * @param rows
	 * @return
	 */
	<T> List<T> find(String hql, List<Object> param, Integer page, Integer rows);
	
	/**
	 * 获取一个对象，hql查询语句获得
	 * @param hql
	 * @param param
	 * @return
	 */
	<T> T get(String hql, Object[] param);
	
	/**
	 * 获取一个对象
	 * @param hql
	 * @param param
	 * @return
	 */
	<T> T get(String hql, List<Object> param);
	
	/**
	 * hql查询获取唯一个确定对象
	 * @param hql
	 * @return
	 */
	<T> T getOnly(String hql);
	
	/**
	 * hql查询获取唯一个确定对象
	 * @param hql
	 * @param param
	 * @return
	 */
	<T> T getOnly(String hql, List<?> param);
	
	/**
	 * HQL删除或更新语句
	 * @param hql
	 * @return
	 */
	Integer executeHql(String hql);
	
	/**
	 * HQL删除获取更新语句
	 * @param hql
	 * @param param
	 * @return
	 */
	Integer executeHql(String hql, List<Object> param);


	void flush();
	
}

