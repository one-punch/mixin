package com.wteam.mixin.pagination;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class Pagination extends SimplePage implements Paginable {

    public Pagination() {}

    public Pagination(int pageNo, int pageSize, int totalCount) {
        super(pageNo, pageSize, totalCount);
    }

    public Pagination(int pageNo, int pageSize, int totalCount, List<?> list) {
        super(pageNo, pageSize, totalCount);
        this.list = list;
    }

    public int getFirstResult() {
        return (pageNo - 1) * pageSize;
    }

    private List<?> list;
    
    private Map<String, Object> msg;

    public List<?> getList() {
        return list;
    }

    public void setList(@SuppressWarnings("rawtypes") List list) {
        this.list = list;
    }
    
    public Map<String, Object> getMsg() {
        return msg;
    }

    public void setMsg(Map<String, Object> msg) {
        this.msg = msg;
    }

    public void putMsg(String key, Object value) {
        if (msg == null) {
            msg = new HashMap<>();
        }
        msg.put(key, value);
    }
    
    /**
     * 处理list后并重新设置到分页中
     * @param bock
     * @return
     */
    public Pagination handle(Function<List<?>, List<?>> bock) {
        this.setList(bock.apply(this.getList()));
        return this;
    }

    @Override
    public String toString() {
        return "Pagination [getPageNo()=" + getPageNo() + ", getPageSize()=" + getPageSize()
               + ", getTotalCount()=" + getTotalCount() + ", getTotalPage()=" + getTotalPage()
               + ", isFirstPage()=" + isFirstPage() + ", isLastPage()=" + isLastPage()
               + ", getNextPage()=" + getNextPage() + ", getPrePage()=" + getPrePage() + "]";
    }
    
    
}