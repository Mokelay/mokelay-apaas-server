package com.greatbee.base.manager.ext;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.DataPage;
import com.greatbee.base.bean.Identified;
import com.greatbee.base.manager.BasicManager;
import com.greatbee.base.util.StringUtil;
import com.greatbee.base.bean.view.Condition;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.*;
import org.hibernate.internal.CriteriaImpl;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Basic Manager
 * Created by CarlChen on 2016/12/20.
 */
public abstract class AbstractBasicManager<T extends Identified> extends HibernateDaoSupport implements BasicManager {

    protected Class<T> beanClass;

    public AbstractBasicManager() {
    }

    public AbstractBasicManager(String className) throws ClassNotFoundException {
        this((Class<T>) Class.forName(className));
    }

    public AbstractBasicManager(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * Add
     *
     * @param object
     * @return
     */
    @Override
    public int add(Object object) throws DBException {
        List<Object> list = new ArrayList<Object>();
        list.add(object);
        return this.add(list);
    }

    @Override
    public int add(List<Object> bean) throws DBException {
        Session session =this.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            int lastAddId = -1;
            tx = session.beginTransaction();

            for (Object item : bean) {
                session.save(item.getClass().getName(), item);
                lastAddId = (Integer) session.getIdentifier(item);
            }

            tx.commit();
            return lastAddId;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            logger.error(e.toString());
            if (tx != null) {
                tx.rollback();
            }
            throw new DBException("error_bean_add", e, 10001001);
        } finally {
            releaseSession(session);
        }
    }

    @Override
    public void update(Object object) throws DBException {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(object.getClass().getName(), object);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            logger.error(e.toString());
            if (tx != null) {
                tx.rollback();
            }
            throw new DBException("error_bean_update", e, 10002001);
        } finally {
            releaseSession(session);
        }
    }

    @Override
    public void update(int id, String columnName, Object columnValue) throws DBException {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            StringBuilder hql = new StringBuilder();
            hql.append("UPDATE ").append(beanClass.getName()).append(" SET ");
            hql.append(columnName).append(" = ? ");
            hql.append(" WHERE id =").append(id).append(" ");
            Query q = session.createQuery(hql.toString()).setParameter(0, columnValue);
            q.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            logger.error(e.toString());
            if (tx != null) {
                tx.rollback();
            }
            throw new DBException("error_bean_update", e, 10002001);
        } finally {
            releaseSession(session);
        }


    }

    @Override
    public Object read(int id) throws DBException {
        int[] ids = new int[]{id};
        List list = this.read(ids);
        return list.get(0);
    }

    @Override
    public List read(int[] ids) throws DBException {
        List list = new ArrayList();
        Session session = this.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (int i = 0; i < ids.length; i++) {
                Identified object = (Identified) session.load(beanClass.getName(), ids[i]);
                list.add(object);
            }
            tx.commit();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            logger.error(e.toString());
            if (tx != null) {
                tx.rollback();
            }
            throw new DBException("error_bean_get", e, 10003001);
        } finally {
            releaseSession(session);
        }
    }

    @Override
    public List list() throws DBException {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            String order = " ORDER BY id";
            String hql = "FROM " + beanClass.getName() + order;

            List<Identified> list = session.createQuery(hql).list();
            tx.commit();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            logger.error(e.toString());
            if (tx != null) {
                tx.rollback();
            }
            throw new DBException("error_bean_list", e, 10000004);
        } finally {
            releaseSession(session);
        }
    }

    @Override
    public List list(String columnName, Object columnValue) throws DBException {
        return this.list(columnName, columnValue, null, false);
    }

    @Override
    public List list(String columnName, Object columnValue, String orderColumn, boolean asc) throws DBException {

        Session session = this.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Criteria c = session.createCriteria(beanClass);
            if (StringUtil.isValid(columnName)) {
                c.add(Expression.eq(columnName, columnValue));
            }
            if (StringUtil.isValid(orderColumn)) {
                c.addOrder(asc ? Order.asc(orderColumn) : Order.desc(orderColumn));
            }
            List list = c.list();
            tx.commit();
            return list;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DBException("error_bean_list", e, 1000000);
        } finally {
            releaseSession(session);
        }


    }

    @Override
    public List list(String orderColumn, boolean asc) throws DBException {
        return this.list(null, null, orderColumn, asc);
    }

    @Override
    public List list(Condition condition) throws DBException {

        Session session = this.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Criteria c = session.createCriteria(beanClass);
            Condition.buildCriteriaCondition(beanClass,c, null, condition);
            List list = c.list();
            tx.commit();
            return list;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DBException("error_bean_list", e, 1000000);
        } finally {
            releaseSession(session);
        }
    }

    @Override
    public List list(Condition condition,String orderColumn, boolean asc) throws DBException {

        Session session = this.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Criteria c = session.createCriteria(beanClass);
            Condition.buildCriteriaCondition(beanClass,c, null, condition);
            if (StringUtil.isValid(orderColumn)) {
                c.addOrder(asc ? Order.asc(orderColumn) : Order.desc(orderColumn));
            }
            List list = c.list();
            tx.commit();
            return list;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DBException("error_bean_list", e, 1000000);
        } finally {
            releaseSession(session);
        }
    }

    @Override
    public DataPage page(int page, int pageSize) throws DBException {
        return this.page(page, pageSize, null, null, false);
    }

    @Override
    public DataPage page(int page, int pageSize, Condition condition) throws DBException {
        return this.page(page, pageSize, condition, null, false);
    }

    @Override
    public DataPage page(int page, int pageSize, Condition condition, String orderColumn, boolean isAsc) throws DBException {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Criteria c = session.createCriteria(beanClass);
            Condition.buildCriteriaCondition(beanClass,c, null, condition);
            if (StringUtil.isValid(orderColumn)) {
                c.addOrder(isAsc ? Order.asc(orderColumn) : Order.desc(orderColumn));
            }
            DataPage dp = _getDataPageCriteria(page, pageSize, c);
            tx.commit();
            return dp;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new DBException("error_bean_list", e, 1000000);
        } finally {
            releaseSession(session);
        }

    }

    @Override
    public void delete(int id) throws DBException {
        int[] ids = new int[]{id};
        this.delete(ids);
    }

    @Override
    public void delete(int[] operationBeanIds) throws DBException {
        Session session = this.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            for (int i = 0; i < operationBeanIds.length; i++) {
                Identified identified = (Identified) session.load(beanClass, operationBeanIds[i]);
                session.delete(identified);
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            logger.error(e.toString());
            if (tx != null) {
                tx.rollback();
            }
            throw new DBException("error_bean_list", e, 1000000);
        } finally {
            releaseSession(session);
        }
    }

    /**
     * 根据Creteria 获取dataPage
     *
     * @param page
     * @param pageSize
     * @param c
     * @return
     */
    private DataPage _getDataPageCriteria(int page, int pageSize, Criteria c) {
        DataPage dp = new DataPage();
        dp.setCurrentPage(page);
        dp.setPageSize(pageSize);

        CriteriaImpl tmpCri = (CriteriaImpl) c;
        Projection tmpProjection = tmpCri.getProjection();
        int totalRecords = Integer.valueOf(String.valueOf((Long) c.setProjection(Projections.rowCount()).list().get(0)));

        c.setProjection(tmpProjection);
        if (tmpProjection == null) {
            c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        int totalPages = totalRecords / pageSize + ((totalRecords % pageSize == 0) ? 0 : 1);

        c.setFirstResult(pageSize * (page - 1));
        c.setMaxResults(pageSize);

        List list = c.list();
        dp.setCurrentRecords(list);
        dp.setCurrentRecordsNum(list.size());
        dp.setTotalPages(totalPages);
        dp.setTotalRecords(totalRecords);
        return dp;
    }

    /**
     * 关闭session
     * @param session
     */
    protected void releaseSession(Session session){
        if(session!=null){
            SessionFactoryUtils.closeSession(session);
        }
    }

}
