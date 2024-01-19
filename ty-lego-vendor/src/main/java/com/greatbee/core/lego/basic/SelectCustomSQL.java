package com.greatbee.core.lego.basic;

import com.alibaba.fastjson.JSON;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.Data;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.bean.DataPage;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.constant.DST;
import com.greatbee.core.bean.constant.IOFT;
import com.greatbee.core.bean.oi.DS;
import com.greatbee.core.bean.server.InputField;
import com.greatbee.core.bean.server.OutputField;
import com.greatbee.core.db.base.BaseTYJDBCTemplate;
import com.greatbee.core.handler.DataHandler;
import com.greatbee.core.handler.QueryHandler;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import com.greatbee.core.lego.util.LegoUtil;
import com.greatbee.core.manager.TYDriver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SelectCustomSQL
 *
 * 自定义查询SQL lego
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("selectCustomSQL")
public class SelectCustomSQL extends BaseTYJDBCTemplate implements ExceptionCode, Lego{
    private static final Logger logger = Logger.getLogger(SelectCustomSQL.class);

    private static final long ERROR_LEGO_DS_ALIAS_IS_NULL= 300017L;
    private static final long ERROR_LEGO_SQL_TPL_IS_NULL= 300018L;
    private static final long ERROR_LEGO_DS_NOT_SUPPORT = 300019L;
    private static final long ERROR_LEGO_Count_SQL_TPL_IS_NULL = 300020L;

    @Autowired
    private TYDriver tyDriver;

    private static final String Input_Key_SQL_TPL = "sql_tpl";
    private static final String Input_Key_Count_SQL_TPL = "count_sql_tpl";//查询总记录条数sql 模板，针对分页数据
    private static final String Input_Key_Return_Type = "return_type";//返回数据类型，默认为list,可选  data/list/page
    private static final String Input_Key_DS_Alias = "ds_alias";//dsAlias 需要操作哪个ds

    private static final String Output_Key_Data = "data";//返回的数据，可以是Data/DataList/PageData

    private static final String Return_Type_Data = "data";
    private static final String Return_Type_List = "list";
    private static final String Return_Type_Page = "page";



    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String sqlTpl = input.getInputValue(Input_Key_SQL_TPL);
        String countSqlTpl = input.getInputValue(Input_Key_Count_SQL_TPL);
        String returnType = DataUtil.getString(input.getInputValue(Input_Key_Return_Type), Return_Type_List);
        String dsAlias = input.getInputValue(Input_Key_DS_Alias);

        if(StringUtil.isInvalid(returnType)){
            returnType = Return_Type_List;
        }
        if(StringUtil.isInvalid(dsAlias)){
            //如果dsAlias无效
            throw new LegoException("请配置ds",ERROR_LEGO_DS_ALIAS_IS_NULL);
        }
        if(StringUtil.isInvalid(sqlTpl)){
            throw new LegoException("请配置SQL模板",ERROR_LEGO_SQL_TPL_IS_NULL);
        }
        DS ds= null;
        try {
            //获取ds详情
            ds = tyDriver.getTyCacheService().getDSByAlias(dsAlias);
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(),e.getCode());
        }
        java.util.List<InputField> ifs = input.getInputFields();
        Map<String,Object> params = LegoUtil.buildTPLParams(input.getRequest(),null,null,input);
        Iterator result = ifs.iterator();
        while(result.hasNext()) {
            InputField _if = (InputField)result.next();
            params.put(_if.getFieldName(), _if.getFieldValue());
        }
        if(DST.Mysql.getType().equalsIgnoreCase(ds.getDst())){
            //如果是mysql 查询处理
            this.mysqlQueryHandle(dsAlias,sqlTpl,countSqlTpl,returnType,params,input,output);
        }else{
            throw new LegoException("暂不支持mysql以外的数据源",ERROR_LEGO_DS_NOT_SUPPORT);
        }
    }


    /**
     * 组装返回数据
     * @param dsAlias
     * @param sqlTpl
     * @param countSqlTpl
     * @param returnType
     * @param tplParams
     * @param input
     * @param output
     * @throws LegoException
     */
    private void mysqlQueryHandle(String dsAlias,String sqlTpl,String countSqlTpl,String returnType,Map tplParams,Input input, Output output) throws LegoException{
        Object responseData = null;//返回给客户端的数据

        ArrayList result = (ArrayList) _mysqlDBHandle(false,dsAlias,sqlTpl,tplParams);
        DataList dataList = new DataList(result);
        if(Return_Type_Data.equalsIgnoreCase(returnType)){
            responseData = CollectionUtil.isValid(result)?result.get(0):new Data();
        }else if(Return_Type_List.equalsIgnoreCase(returnType)){
            responseData = dataList;
        }else if(Return_Type_Page.equalsIgnoreCase(returnType)){
            int page = DataUtil.getInt(input.getInputValue("page"), 1);
            int pageSize = DataUtil.getInt(input.getInputValue("pageSize"), 10);
            if(page <= 0) {
                page = 1;
            }
            if(pageSize <= 0) {
                pageSize = 1;
            }
            if(StringUtil.isInvalid(countSqlTpl)){
                throw new LegoException("请配置总数SQL模板",ERROR_LEGO_Count_SQL_TPL_IS_NULL);
            }
            int count = (int) _mysqlDBHandle(true,dsAlias,countSqlTpl,tplParams);
            DataPage dataPage = new DataPage();
            dataPage.setCurrentPage(page);
            dataPage.setCurrentRecords(result);
            dataPage.setCurrentRecordsNum(result.size());
            dataPage.setPageSize(pageSize);
            dataPage.setTotalPages(count % pageSize > 0?count / pageSize + 1:count / pageSize);
            dataPage.setTotalRecords(count);
            responseData = dataPage;
        }
        //加上序号字段
        LegoUtil.buildListIndex(responseData);
        output.setOutputValue(Output_Key_Data,responseData);

        //拉取所有的常用(common)输出配置
        List outputFields = output.getOutputField(IOFT.Read);
        if(CollectionUtil.isValid(outputFields)) {
            Iterator ofs = outputFields.iterator();
            while(ofs.hasNext()) {
                OutputField outputField = (OutputField)ofs.next();
                ArrayList datas = new ArrayList();
                for(int i = 0; i < result.size(); ++i) {
                    Data data = (Data)result.get(i);
                    Object outputFieldValue = data.get(outputField.getFieldName());
                    if(outputFieldValue != null) {
                        datas.add(outputFieldValue);
                    }
                }
                if(returnType==Return_Type_Data){
                    //如果返回的是一个对象，则单个字段返回的不再是数组，而是对象
                    outputField.setFieldValue(datas.get(0));
                }else{
                    outputField.setFieldValue(datas);
                }
            }
        }
    }

    /**
     * 处理数据库查询的公共方法  mysql查询处理    为了防止sql注入，这里不能通过tpl 来拼接sql
     * @param isCount
     * @param dsAlias
     * @param sqlTpl
     * @param tplParams
     * @return
     * @throws LegoException
     */
    private Object _mysqlDBHandle(boolean isCount,String dsAlias,String sqlTpl,Map tplParams) throws LegoException{
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = this.getConnection(dsAlias);
            ps = new QueryHandler(){
                @Override
                public PreparedStatement execute(Connection connection, PreparedStatement preparedStatement) throws SQLException, DBException {
//                    String regular = "%?<#([A-Za-z]+).*?>.*?</#\\1>%?|%?\\$\\{.*?\\}%?";
                    String expressReg = "%?<#([A-Za-z]+).*?>.*?</#\\1>%?";
                    Matcher expressMatch = Pattern.compile(expressReg).matcher(sqlTpl);
                    List<String> tmpParams = new ArrayList<String>();
                    String sqlTmpTpl = sqlTpl.replaceAll(expressReg, "__T__");//中间转换一下
                    while(expressMatch.find()){
                        tmpParams.add(expressMatch.group());
                    }

                    String regular = "%?\\$\\{.*?\\}%?";//如果是带<#的标签  说明是复杂的脚本，为了功能更强大<# 标签不做prepareStatement处理
                    Pattern pet = Pattern.compile(regular);
                    Matcher match = pet.matcher(sqlTmpTpl);

                    List<Object> params = new ArrayList<Object>();
                    while(match.find()){
                        String matchStr = match.group();
                        try {
                            //针对like 语句优化
                            boolean leftLike = matchStr.startsWith("%");
                            boolean rightLike = matchStr.endsWith("%");
                            String transferStr = LegoUtil.transferInputValue(matchStr, tplParams);
                            if(matchStr.endsWith("?number}")){
                                params.add(Integer.valueOf(transferStr));
                            }else{
                                params.add((leftLike?"%":"")+transferStr+(rightLike?"%":""));
                            }
                        } catch (LegoException e) {
                            e.printStackTrace();
                            throw new DBException(e.getMessage(),e.getCode());
                        }
                    }
                    String psSqlTpl = sqlTmpTpl.replaceAll(regular,"?");
                    try {
                        //还原 sqlTmpTpl
                        if(CollectionUtil.isValid(tmpParams)){
                            for(String item:tmpParams){
                                //replaceFirst 这个方法 两个参数都是正则表达式
                                psSqlTpl = psSqlTpl.replaceFirst("__T__", _makeQueryStringAllRegExp(item));
                            }
                        }
                        //可能有不需要换成?的模板
                        psSqlTpl = LegoUtil.transferInputValue(psSqlTpl, tplParams);
                    } catch (LegoException e) {
                        e.printStackTrace();
                        throw new DBException(e.getMessage(),e.getCode());
                    }
                    preparedStatement = connection.prepareStatement(psSqlTpl);
                    try{
                        logger.info("[SelectCustomSQL] psSQL="+psSqlTpl);
                        logger.info("[SelectCustomSQL] params="+ JSON.toJSONString(params));
                    }catch(Exception ee){}
                    for(int i=0;i<params.size();i++){
                        preparedStatement.setObject(i+1,params.get(i));
                    }
                    return preparedStatement;
                }
            }.execute(conn, ps);
            rs = ps.executeQuery();

            ArrayList result = new ArrayList();
            if(isCount){
                //计算总数
                int recordCount = 0;
                while(rs.next()) {
                    recordCount = rs.getInt(1);
                }
                return recordCount;
            }else{
                while(rs.next()) {
                    Data data = new Data();
                    new DataHandler(){
                        @Override
                        public void execute(ResultSet resultSet, Data data) throws SQLException {
                            ResultSetMetaData md = resultSet.getMetaData(); //获得结果集结构信息,元数据
                            int columnCount = md.getColumnCount();   //获得列数
                            for (int i = 1; i <= columnCount; i++) {
                                Object o = resultSet.getObject(i);
                                String __key = md.getColumnLabel(i);
                                if(StringUtil.isInvalid(__key)){
                                    __key = md.getColumnName(i);
                                }
                                data.put(__key, o==null?"":o);
                            }
                        }
                    }.execute(rs, data);
                    result.add(data);
                }
                return result;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(), 200002L);
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(), e.getCode());
        } finally {
            try {
                this.releaseResultSet(rs);
                this.releasePreparedStatement(ps);
                this.releaseConnection(conn);
            } catch (DBException e) {
                e.printStackTrace();
                throw new LegoException(e.getMessage(), e.getCode());
            }
        }

    }


    /**
     * 转义正则特殊字符 （$()*+.[]?\^{}
     * \\需要第一个替换，否则replace方法替换时会有逻辑bug
     */
    private String _makeQueryStringAllRegExp(String str) {
        if (StringUtil.isInvalid(str)){
            return str;
        }
        return str.replace("\\", "\\\\").replace("*", "\\*")
                .replace("+", "\\+").replace("|", "\\|")
                .replace("{", "\\{").replace("}", "\\}")
                .replace("(", "\\(").replace(")", "\\)")
                .replace("^", "\\^").replace("$", "\\$")
                .replace("[", "\\[").replace("]", "\\]")
                .replace("?", "\\?").replace(",", "\\,")
                .replace(".", "\\.").replace("&", "\\&");
    }


//    //正则测试
//    public static void main(String []args){
//        String regular = "%?<#([A-Za-z]+).*?>.*?</#\\1>%?|%?\\$\\{.*?\\}%?";
//        String sqlTpl = "%<#if (request.city_code)?? >'88888'<#else>${(request.city_code=='')?string('999999',request.city_code)}</#if>% where a='1' and ${request.name} or <#if aa=='bb'>${bbb}</#if>";
//        Pattern pet = Pattern.compile(regular);
//        Matcher match = pet.matcher(sqlTpl);
//        int countAll = match.groupCount();
//        System.out.println("count="+countAll);
//        while(match.find()){
//            String matchStr = match.group();
//            System.out.println("matchStr="+matchStr);
//        }
//    }


}
