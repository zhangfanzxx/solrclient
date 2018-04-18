package com.tidfore.demo.solr;

import java.util.ArrayList;
import java.util.List;

public class SolrBean<T> {
    List<T> list;
    private Long totalSize;
    private Integer size;
    private Integer totalPage;
    private Integer pageSize;

    public List<T> getList() {
        if(list==null){
            return  new ArrayList<>();
        }
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
