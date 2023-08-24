package com.greatbee.core.bean.constant;

/**
 * Condition Type
 * Created by CarlChen on 2016/11/12.
 */
public enum CT {
    GE(">=", "ge"),
    GT(">", "gt"),
    LE("<=", "le"),
    LT("<", "lt"),
    NEQ("!=", "neq"),
    EQ("=", "eq"),
    NOTIN("NOT IN", "notIn"),
    IN("IN", "In"),
    LeftLIKE("LIKE", "leftLike"),
    RightLIKE("LIKE", "rightLike"),
    LIKE("LIKE", "like"),
    NOTLIKE("NOT LIKE","notLike"),
    ISNOT("IS NOT", "isnot"),
    NotNull("IS NOT NULL", "NotNull"),
    NULL("IS NULL", "Null"),
    IS("IS", "is"),
    Multi("Multi", "Multi"),//混合类型，需要解析条件的value值
    Custom("Custom","Custom");//自定义类型，条件由前端传过来

    private String sqlType;
    private String name;

    private CT(String sqlType, String name) {
        this.sqlType = sqlType;
        this.name = name;
    }

    public String getSqlType() {
        return sqlType;
    }

    public String getName() {
        return name;
    }


    public static String getSqlType(String name){
        for (CT ct:CT.values()){
            if(ct.getName().equals(name)){
                return ct.getSqlType();
            }
        }
        return null;
    }

    /**
     * 根据名称  判断 是否需要条件值
     * @return
     */
    public static boolean isNeedFieldValue(String ctName){
        if(CT.NULL.getName().equals(ctName) || CT.NotNull.getName().equals(ctName)){
            //不需要字段值
            return false;
        }else{
            return true;
        }
    }
}
