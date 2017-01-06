package test.hibernate.generate;


import java.util.ArrayList;
import java.util.List;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.define.IPersistentObject;

/**
 * po生成器
 * @author benko
 * @version 2016年6月13日
 * @see MockGenerate
 * @since
 */
public abstract class MockGenerate<T extends IPersistentObject> {

    /** 保存数据 DAO */
    protected IBaseDao baseDao;

    /** po 数据列表 */
    protected List<T> list;

    /**
     * 是否已经保存到数据库
     */
    protected boolean[] saveSigns;

    public MockGenerate(IBaseDao baseDao) {
        this.baseDao = baseDao;
        list = new ArrayList<>();
        setList(list);
        saveSigns = new boolean[list.size()];
    }

    public T generate(int index) {
        return generate(index, false);
    }

    public T generate(int index, boolean isSave) {
        if (isSave) save(list.get(index), index);
        return list.get(index);
    }

    public List<T> generate(int start, int end) {
        return generate(start, end, false);
    }

    public List<T> generate(int start, int end, boolean isSave) {
        List<T> subList = list.subList(start, end);
        if (isSave) save(subList, start);
        return subList;
    }

    public List<T> generate() {
        return generate(false);
    }

    public List<T> generate(boolean isSave) {
        if (isSave) save(list, 0);
        return list;
    }

    private void save(T t, int index) {
        if (!saveSigns[index]) {
            saveSigns[index] = true;
            baseDao.save(t);
        }
    }

    private void save(List<T> list, int start) {
        for (T t : list) {
            save(t, start++ );
        }
    }

    protected abstract void setList(List<T> list);
}
