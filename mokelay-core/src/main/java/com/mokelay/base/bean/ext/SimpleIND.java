package com.mokelay.base.bean.ext;

import com.mokelay.base.bean.IND;

/**
 * Simple IND
 *
 * Created by CarlChen on 16/10/11.
 */
public class SimpleIND implements IND {
    //ID
    private Integer id;
    //名称
    private String name;
    //描述
    private String description;

    public Object clone()
    {
        SimpleIND temp = null;
        try {
            temp = (SimpleIND)super.clone();
        }
        catch (CloneNotSupportedException exception) {
            System.err.println("Clone Abort... 原因：" + exception);
        }
        return temp;
    }

    public String toString()
    {
        StringBuffer s = new StringBuffer();
        s.append("id:").append(id).append("\n");
        s.append("name:").append(name).append("\n");
        s.append("description:").append(description).append("\n");
        return s.toString();
    }

    /**
     * 返回实体唯一标识
     *
     * @return 标识
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * 返回对象名称
     *
     * @return 对象名称
     */
    public String getName()
    {
        return name;
    }

    /**
     * 返回描述
     *
     * @return 返回描述
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
