package com.wteam.mixin.biz.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.wteam.mixin.biz.dao.IBaseDao;



/**
 * 对所有PO都有效的基本增、删、查、改；必须先知道PO的id值
 * @author benko
 */
@SuppressWarnings("unchecked")
@Repository("baseDao")
public class BaseDaoImpl implements IBaseDao{


    /**
     * 使用@Autowired注解将sessionFactory注入到CollegeDaoImpl中
     */
    @Autowired
    private SessionFactory sessionFactory;


   	private Session getSession(){
  		return sessionFactory.openSession();
    }

	@Override
	public <T> Serializable save(T po) {
		return getSession().save(po);
	}

	@Override
	public <T> T find(Class<T> poClass, long id) {
		return (T) getSession().get(poClass, id);
	}

	@Override
	public <T> void delete(T po) {
		getSession().delete(po);
	}

	@Override
	public <T> void update(T po) {
		 getSession().update(po);
	}

	@Override
	public <T> void saveOrUpdate(T po) {
		getSession().saveOrUpdate(po);
	}

	@Override
	public <T> List<T> findByProperty(String property, Object value, Class<T> cls) {
        Assert.hasText(property);
        return createCriteria(cls,Restrictions.eq(property, value)).list();
	}

	@Override
	public <T> T findUniqueByProperty(String property, Object value, Class<T> cls) {
        Assert.hasText(property);
        Assert.notNull(value);
        return (T) createCriteria(cls,Restrictions.eq(property, value)).uniqueResult();
	}

	/**
	 * Create a criteria to filter.
	 *
	 * @param criterions
	 *            <p>
	 * @return AN criteria object.
	 */
	@SuppressWarnings("rawtypes")
	public Criteria createCriteria(Class cls, Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(cls);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * 功能：hql查询
	 * 前提：完整的hql查询语句
	 * @param hql
	 * @return
	 */
	@Override
	public <T> List<T> find(String hql) {
		 return getSession().createQuery(hql).list();
	}

	/**
	 * 功能：带占位符的hql查询,参数为集合
	 * 前提：带占位符的hql语句，并传入所需参数集合
	 *
	 * @param hql
	 * @param param：填充占位符参数
	 * @return
	 */
	@Override
	public <T> List<T> find(String hql, Object[] param) {
		Query q = getSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.list();
	}

	/**
	 * 功能：帶占位符的hql查询，参数为list
	 * 前提：带占位符的hql语句，并传入所需参数list集合
	 * @param hql
	 * @param param
	 * @return
	 */
	@Override
	public <T> List<T> find(String hql, List<Object> param) {
		Query q = getSession().createQuery(hql);
		if (param != null && param.size() > 0) {
			for (int i = 0; i < param.size(); i++) {
				q.setParameter(i, param.get(i));
			}
		}
		return q.list();
	}

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
	@Override
	public <T> List<T> find(String hql, Object[] param, Integer page, Integer rows) {
		if (page == null || page < 1) {
			page = 1;
		}
		if (rows == null || rows < 1) {
			rows = 10;
		}
		Query q = getSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

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
	@Override
	public <T> List<T> find(String hql, List<Object> param, Integer page, Integer rows) {
		if (page == null || page < 1) {
			page = 1;
		}
		if (rows == null || rows < 1) {
			rows = 10;
		}
		Query q = getSession().createQuery(hql);
		if (param != null && param.size() > 0) {
			for (int i = 0; i < param.size(); i++) {
				q.setParameter(i, param.get(i));
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	/**
	 * 获取一个对象，hql查询语句获得
	 * @param hql
	 * @param param
	 * @return
	 */
	@Override
	public <T> T get(String hql, Object[] param) {
		List<T> l = this.find(hql, param);
		if (l != null && l.size() > 0) {
			return l.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 获取一个对象
	 * @param hql
	 * @param param
	 * @return
	 */
	@Override
	public <T> T get(String hql, List<Object> param) {
		List<T> l = this.find(hql, param);
		if (l != null && l.size() > 0) {
			return l.get(0);
		} else {
			return null;
		}
	}

	/**
	 * hql查询获取唯一个确定对象
	 * @param hql
	 * @return
	 */
	@Override
	public <T> T getOnly(String hql) {
		return (T) getSession().createQuery(hql).uniqueResult();
	}

	/**
	 * hql查询获取唯一个确定对象
	 * @param hql
	 * @param param
	 * @return
	 */
	@Override
	public <T> T getOnly(String hql, List<?> param) {
		Query q = getSession().createQuery(hql);
		if (param != null && param.size() > 0) {
			for (int i = 0; i < param.size(); i++) {
				q.setParameter(i, param.get(i));
			}
		}
		return (T) q.uniqueResult();
	}

	/**
	 * HQL删除或更新语句
	 * @param hql
	 * @return
	 */
	@Override
	public Integer executeHql(String hql) {
		return getSession().createQuery(hql).executeUpdate();
	}

	/**
	 * HQL删除获取更新语句
	 * @param hql
	 * @param param
	 * @return
	 */
	@Override
	public Integer executeHql(String hql, List<Object> param) {
		Query q = getSession().createQuery(hql);
		if (param != null && param.size() > 0) {
			for (int i = 0; i < param.size(); i++) {
				q.setParameter(i, param.get(i));
			}
		}
		return q.executeUpdate();
	}

	@Override
	public void flush() {
		getSession().flush();
	}
}
