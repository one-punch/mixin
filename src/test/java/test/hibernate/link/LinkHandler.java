package test.hibernate.link;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.define.IPersistentObject;


public class LinkHandler {

    static IBaseDao baseDao;

    public static void setBaseDao(IBaseDao baseDao) {
        LinkHandler.baseDao = baseDao;
    }

    public static <T extends IPersistentObject, S extends IPersistentObject> void manyToMany(T t,
                                                                                             S s,
                                                                                             ILinker<T, S> linker) {
        manyToMany(new ArrayList<>(Arrays.asList(t)), new ArrayList<>(Arrays.asList(s)), linker);
    }

    public static <T extends IPersistentObject, S extends IPersistentObject> void manyToMany(List<T> tList,
                                                                                             S s,
                                                                                             ILinker<T, S> linker) {
        manyToMany(tList, new ArrayList<>(Arrays.asList(s)), linker);
    }

    public static <T extends IPersistentObject, S extends IPersistentObject> void manyToMany(T t,
                                                                                             List<S> sList,
                                                                                             ILinker<T, S> linker) {
        manyToMany(new ArrayList<>(Arrays.asList(t)), sList, linker);
    }

    public static <T extends IPersistentObject, S extends IPersistentObject> void manyToMany(List<T> tList,
                                                                                             List<S> sList,
                                                                                             ILinker<T, S> linker) {
        tList.forEach(t -> sList.forEach(s -> {
            baseDao.saveOrUpdate(t);
            baseDao.saveOrUpdate(s);
            linker.link(t, s);
        }));
    }

    public static <T extends IPersistentObject, S extends IPersistentObject> void oneToMany(T t,
                                                                                            S s,
                                                                                            ILinker<T, S> linker) {
        oneToMany(new ArrayList<>(Arrays.asList(t)), new ArrayList<>(Arrays.asList(s)), linker);
    }

    public static <T extends IPersistentObject, S extends IPersistentObject> void oneToMany(List<T> tList,
                                                                                            S s,
                                                                                            ILinker<T, S> linker) {
        oneToMany(tList, new ArrayList<>(Arrays.asList(s)), linker);
    }

    public static <T extends IPersistentObject, S extends IPersistentObject> void oneToMany(T t,
                                                                                            List<S> sList,
                                                                                            ILinker<T, S> linker) {
        oneToMany(new ArrayList<>(Arrays.asList(t)), sList, linker);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <S extends IPersistentObject> void onesToMany(List<IPersistentObject> tList,
                                                                List<S> sList,
                                                                ILinker<IPersistentObject, S>... linkers) {
        if (tList.size() != linkers.length)
            throw new RuntimeException("tList 的数目和 linkers 的数目不一行");
        sList.forEach(s -> {
            for (int i = 0; i < linkers.length; i++ ) {
                IPersistentObject t = tList.get(i);
                ILinker linker = linkers[i];
                baseDao.saveOrUpdate(t);
                linker.link(t, s);
            }
            baseDao.saveOrUpdate(s);
        });
    }

    public static <T extends IPersistentObject, S extends IPersistentObject> void oneToMany(List<T> tList,
                                                                                            List<S> sList,
                                                                                            ILinker<T, S> linker) {
        tList.forEach(t -> sList.forEach(s -> {
            baseDao.saveOrUpdate(t);
            linker.link(t, s);
            baseDao.saveOrUpdate(s);
        }));
    }

    public static <T extends IPersistentObject, S extends IPersistentObject> void oneToOne(T t,
                                                                                           S s,
                                                                                           ILinker<T, S> linker) {
        oneToOne(new ArrayList<>(Arrays.asList(t)), new ArrayList<>(Arrays.asList(s)), linker);
    }

    public static <T extends IPersistentObject, S extends IPersistentObject> void oneToOne(List<T> tList,
                                                                                           List<S> sList,
                                                                                           ILinker<T, S> linker) {
        int end = tList.size() < sList.size() ? tList.size() : sList.size();
        for (int i = 0; i < end; i++ ) {
            linker.link(tList.get(i), sList.get(i));
            baseDao.saveOrUpdate(tList.get(i));
            baseDao.saveOrUpdate(sList.get(i));
        }
    }

}
