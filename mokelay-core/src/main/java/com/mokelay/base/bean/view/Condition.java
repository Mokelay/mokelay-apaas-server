package com.mokelay.base.bean.view;

import com.mokelay.base.util.BooleanUtil;
import com.mokelay.base.util.DataUtil;
import com.mokelay.base.util.StringUtil;
import com.mokelay.base.bean.constant.CG;
import com.mokelay.base.bean.constant.CT;
import com.mokelay.base.bean.constant.DT;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 条件
 * <p>
 * Created by CarlChen on 2016/11/12.
 */
public class Condition {

    //条件的字段名
    private String conditionFieldName;
    //条件的值
    private String conditionFieldValue;
    //条件类型  默认是等于
    private String ct = CT.EQ.getName();
    //数据类型
    private String dt= DT.String.getType();

    public String getConditionFieldName() {
        return conditionFieldName;
    }


    public String getConditionFieldValue() {
        return conditionFieldValue;
    }

    public String getCt() {
        return ct;
    }

    public void setConditionFieldName(String conditionFieldName) {
        this.conditionFieldName = conditionFieldName;
    }

    public void setConditionFieldValue(String conditionFieldValue) {
        this.conditionFieldValue = conditionFieldValue;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public List<Condition> getConditions() {
        return null;
    }

    public CG getCg() {
        return null;
    }


    /**
     * 组件condition条件sql 带?  需要配合下面的buildConditionSqlPs 方法一起使用
     *
     * @param sql
     * @param condition
     */
    public static void buildConditionSql(StringBuilder sql, Condition condition) {
        buildConditionSql(sql, condition, null);
    }

    /**
     * 组件condition条件sql 带?  需要配合下面的buildConditionSqlPs 方法一起使用  有别名的情况
     *
     * @param sql
     * @param condition
     */
    public static void buildConditionSql(StringBuilder sql, Condition condition, String tableAlias) {
        List<Condition> conditions = condition.getConditions();
        if (conditions != null) {
            //muliCondition
            sql.append(" ( ");
            for (int i = 0; i < conditions.size(); i++) {
                Condition _condition = conditions.get(i);
                buildConditionSql(sql, _condition, tableAlias);
                if (i < conditions.size() - 1) {
                    sql.append(condition.getCg().equals(CG.AND) ? " AND " : " OR ");
                }
            }
            sql.append(" ) ");
        } else {
            //Condition
            if (StringUtil.isValid(tableAlias)) {
                sql.append(" ").append(tableAlias).append(".");
            }
            if (CT.IN.getName().equalsIgnoreCase(condition.getCt()) || CT.NOTIN.getName().equalsIgnoreCase(condition.getCt())) {
                sql.append("`").append(condition.getConditionFieldName()).append("` ").append(CT.getSqlType(condition.getCt())).append(" (");
                //这里不过滤condition.getConditionFieldValue() 的值，在设置？的时候过滤
                if (StringUtil.isInvalid(condition.getConditionFieldValue())) {
                    sql.append(" ? ");//后面拼接sql的时候会加上一个不存在的数
                } else {
                    String[] vals = condition.getConditionFieldValue().split(",");
                    for (int i = 0; i < vals.length; i++) {
                        if (i != 0) {
                            sql.append(",");
                        }
                        sql.append(" ? ");
                    }
                }
                sql.append(") ");
            } else {
                sql.append("`").append(condition.getConditionFieldName()).append("` ").append(CT.getSqlType(condition.getCt())).append(" ");
                if (CT.isNeedFieldValue(condition.getCt())) {
                    sql.append(" ? ");
                }
            }
        }
    }

    /**
     * 设置ps参数
     *
     * @param index     针对ps的 开始索引,一般从1开始，如果前面配置的参数，就不为1
     * @param ps
     * @param condition
     */
    public static int buildConditionSqlPs(int index, PreparedStatement ps, Condition condition) throws SQLException {
        System.out.println("index=" + index);
        List<Condition> conditions = condition.getConditions();
        int _index = index;
        if (conditions != null) {
            //muliCondition
            for (int i = 0; i < conditions.size(); i++) {
                Condition _condition = conditions.get(i);
                _index = buildConditionSqlPs(_index, ps, _condition);
            }
        } else {
            //Condition
            if (CT.isNeedFieldValue(condition.getCt())) {
                if (CT.LeftLIKE.getName().equals(condition.getCt())) {
                    ps.setString(_index++, "%" + condition.getConditionFieldValue());
                } else if (CT.RightLIKE.getName().equals(condition.getCt())) {
                    ps.setString(_index++, condition.getConditionFieldValue() + "%");
                } else if (CT.LIKE.getName().equals(condition.getCt())) {
                    ps.setString(_index++, "%" + condition.getConditionFieldValue() + "%");
                } else if (CT.NOTLIKE.getName().equals(condition.getCt())) {
                    ps.setString(_index++, "%" + condition.getConditionFieldValue() + "%");
                } else if (CT.IN.getName().equalsIgnoreCase(condition.getCt()) || CT.NOTIN.getName().equalsIgnoreCase(condition.getCt())) {
                    if (StringUtil.isInvalid(condition.getConditionFieldValue())) {
                        ps.setString(_index++, "9999999999");//设置一个不存在的值
                    } else {
                        String[] vals = condition.getConditionFieldValue().split(",");
                        for (int i = 0; i < vals.length; i++) {
                            ps.setString(_index++, vals[i]);
                        }
                    }
                } else {
                    if ("''".equals(condition.getConditionFieldValue())) {
                        ps.setString(_index++, "");
                    } else {
                        if(DT.INT.getType().equalsIgnoreCase(condition.getDt())){
                            ps.setInt(_index++, DataUtil.getInt(condition.getConditionFieldValue(), 0));
                        } else if(DT.Double.getType().equalsIgnoreCase(condition.getDt())){
                            ps.setDouble(_index++, DataUtil.getDouble(condition.getConditionFieldValue(), 0));
                        } else {
                            ps.setString(_index++, condition.getConditionFieldValue());
                        }
                    }
                }
            }
        }
        return _index;
    }

    /**
     * 构建Criteria查询条件
     *
     * @param c
     * @param junction
     * @param condition
     */
    public static void buildCriteriaCondition(Class beanClass, Criteria c, Junction junction, Condition condition) {
        if (condition != null) {

            List<Condition> conditions = condition.getConditions();
            if (conditions != null) {
                Junction junc = null;
                if (CG.AND.equals(condition.getCg())) {
                    junc = Restrictions.conjunction();
                } else {
                    junc = Restrictions.disjunction();
                }
                //muliCondition
                for (int i = 0; i < conditions.size(); i++) {
                    Condition _condition = conditions.get(i);
                    buildCriteriaCondition(beanClass, c, junc, _condition);
                }
                if (junction == null) {
                    c.add(junc);
                } else {
                    junction.add(junc);
                }
            } else {
                //Condition
                if (junction == null) {
                    c.add(_transferJunction(beanClass, condition));
                } else {
                    junction.add(_transferJunction(beanClass, condition));
                }
            }
        }
    }

    private static Criterion _transferJunction(Class beanClass, Condition condition) {
        if (CT.EQ.getName().equals(condition.getCt())) {
            return Restrictions.eq(condition.getConditionFieldName(), _getConditionValue(beanClass, condition));
        } else if (CT.GT.getName().equals(condition.getCt())) {
            return Restrictions.gt(condition.getConditionFieldName(), _getConditionValue(beanClass, condition));
        } else if (CT.GE.getName().equals(condition.getCt())) {
            return Restrictions.ge(condition.getConditionFieldName(), _getConditionValue(beanClass, condition));
        } else if (CT.LT.getName().equals(condition.getCt())) {
            return Restrictions.lt(condition.getConditionFieldName(), _getConditionValue(beanClass, condition));
        } else if (CT.LE.getName().equals(condition.getCt())) {
            return Restrictions.le(condition.getConditionFieldName(), _getConditionValue(beanClass, condition));
        } else if (CT.NEQ.getName().equals(condition.getCt())) {
            return Restrictions.ne(condition.getConditionFieldName(), _getConditionValue(beanClass, condition));
        } else if (CT.IN.getName().equals(condition.getCt())) {
            return Restrictions.in(condition.getConditionFieldName(), condition.getConditionFieldValue().split(","));
        } else if (CT.NOTIN.getName().equals(condition.getCt())) {
            return Restrictions.not(Restrictions.in(condition.getConditionFieldName(), condition.getConditionFieldValue().split(",")));
        } else if (CT.LIKE.getName().equals(condition.getCt())) {
            return Restrictions.like(condition.getConditionFieldName(), "%" + condition.getConditionFieldValue() + "%");
        } else if (CT.NOTLIKE.getName().equals(condition.getCt())) {
            return Restrictions.sqlRestriction(condition.getConditionFieldName() + " NOT LIKE %" + condition.getConditionFieldValue() + "%");
        } else if (CT.LeftLIKE.getName().equals(condition.getCt())) {
            return Restrictions.like(condition.getConditionFieldName(), "%" + condition.getConditionFieldValue());
        } else if (CT.RightLIKE.getName().equals(condition.getCt())) {
            return Restrictions.like(condition.getConditionFieldName(), condition.getConditionFieldValue() + "%");
        } else if (CT.NULL.getName().equals(condition.getCt())) {
            return Restrictions.isNull(condition.getConditionFieldName());
        } else if (CT.ISNOT.getName().equals(condition.getCt())) {
            return Restrictions.isNotNull(condition.getConditionFieldName());
        } else {
            return Restrictions.eq(condition.getConditionFieldName(), _getConditionValue(beanClass, condition));
        }
        //TODO is   和 isnot   没有转换
    }

    private static Object _getConditionValue(Class beanClass, Condition condition) {
        Field[] fs = beanClass.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            String name = f.getName();
            Class type = f.getType();
            if (StringUtil.isValid(condition.getConditionFieldName()) && name.equals(condition.getConditionFieldName())) {
                if (Boolean.class.equals(type) || boolean.class.equals(type)) {
                    return BooleanUtil.toBool(condition.getConditionFieldValue());
                }
                break;
            }
        }
        return condition.getConditionFieldValue();
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }
}
