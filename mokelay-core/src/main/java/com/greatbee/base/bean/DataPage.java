package com.greatbee.base.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 翻页数据
 *
 * Created by CarlChen on 2016/11/12.
 */
public class DataPage implements Serializable {
    private int currentPage;
    private List currentRecords;
    private int currentRecordsNum;
    private int totalPages;
    private int totalRecords;
    private int pageSize;
    /**
     * 得到当前请求的页
     *
     * @return  数量
     */
    public int getCurrentPage(){
        return currentPage;
    }

    /**
     * 得到当前请求页的数据对象
     *
     * @return  数量
     */
    public List getCurrentRecords(){
        return currentRecords;
    }

    /**
     * 当前请求页的数据量
     *
     * @return  数量
     */
    public int getCurrentRecordsNum(){
        return currentRecordsNum;
    }

    /**
     * 得到一共多少页
     *
     * @return  数量
     */
    public int getTotalPages(){
        return totalPages;
    }

    /**
     * 得到一共有多少条数据
     *
     * @return  数量
     */
    public int getTotalRecords(){
        return  totalRecords;
    }

    /**
     * 每页有多少条
     * @return  每页条数
     */
    public int getPageSize(){
        return pageSize;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setCurrentRecords(List currentRecords) {
        this.currentRecords = currentRecords;
    }

    public void setCurrentRecordsNum(int currentRecordsNum) {
        this.currentRecordsNum = currentRecordsNum;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
