package com.greatbee.base.manager;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.DataPage;
import com.greatbee.base.bean.view.Condition;

import java.util.List;

/**
 * BasicManager
 *
 * Created by CarlChen on 2016/12/20.
 */
public interface BasicManager {
    /**
     * 添加
     *
     * @param object 添加对象
     * @return 唯一值
     */
    public int add(Object object) throws DBException;

    /**
     * 批量添加
     *
     * @param bean 对象
     * @return 添加后唯一值
     * @throws DBException
     */
    public int add(List<Object> bean) throws DBException;

    /**
     * 更新
     *
     * @param object 对象
     */
    public void update(Object object) throws DBException;


    /**
     * 根据字段条件来更新
     *
     * @param id 唯一ID
     * @param columnName 条件列名
     * @param columnValue 列值
     * @throws DBException
     */
    public void update(int id, String columnName, Object columnValue) throws DBException;

    /**
     * 读取
     */
    public Object read(int id) throws DBException;

    /**
     * 读取多个
     *
     * @param ids ID列表
     * @return 数据列表
     * @throws DBException
     */
    public List read(int[] ids) throws DBException;

    /**
     * 列表
     *
     * @return 数据列表
     */
    public List list() throws DBException;

    /**
     * 根据列值来获取列表
     *
     * @param columnName 列名
     * @param columnValue 列值
     * @return 列表
     * @throws DBException
     */
    public List list(String columnName, Object columnValue) throws DBException;

    /**
     * 根据列值来获取列表
     *
     * @param columnName 列名
     * @param columnValue 列值
     * @param orderColumn 排序列
     * @param asc 升序或降序
     * @return 列表数据
     * @throws DBException
     */
    public List list(String columnName, Object columnValue, String orderColumn, boolean asc) throws DBException;

    /**
     * 根据排序来列表
     *
     * @param orderColumn 排序列
     * @param asc 升序或降序
     * @return 列表数据
     * @throws DBException
     */
    public List list(String orderColumn, boolean asc) throws DBException;

    /**
     * 列表搜索
     *
     * @param condition 条件
     * @return 数据
     * @throws DBException
     */
    public List list(Condition condition) throws DBException;

    /**
     * 列表搜索 带排序
     *
     * @param condition 条件
     * @return 数据
     * @throws DBException
     */
    public List list(Condition condition,String orderColumn, boolean asc) throws DBException;

    /**
     * 翻页
     *
     * @param page     页数
     * @param pageSize 每页条数
     * @return 页对象
     */
    public DataPage page(int page, int pageSize) throws DBException;

    /**
     * 搜索翻页
     *
     * @param page 页数
     * @param pageSize 每页条数
     * @param condition 条件
     * @return
     * @throws DBException
     */
    public DataPage page(int page, int pageSize, Condition condition) throws DBException;

    /**
     * 搜索翻页
     *
     * @param page 页数
     * @param pageSize 每页条数
     * @param orderColumn 排序列名
     * @param isAsc 是否升序
     * @return 页对象
     * @throws DBException
     */
    public DataPage page(int page, int pageSize, Condition condition, String orderColumn, boolean isAsc) throws DBException;

    /**
     * 删除
     *
     * @param id id
     */
    public void delete(int id) throws DBException;

    /**
     * 批量删除
     *
     * @param operationBeanIds 操作ID列表
     * @throws DBException
     */
    public void delete(int[] operationBeanIds) throws DBException;
}
