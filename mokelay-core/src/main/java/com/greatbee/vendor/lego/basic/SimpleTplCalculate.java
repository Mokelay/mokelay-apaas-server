package com.greatbee.vendor.lego.basic;

import com.greatbee.base.util.StringUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.util.LegoUtil;
import com.greatbee.vendor.utils.VendorExceptionCode;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SimpleTplCalculate  简单模板计算
 *
 * ${param1} - ${param2}/${param3}
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("simpleTplCalculate")
public class SimpleTplCalculate implements ExceptionCode, Lego{
    private static final Logger logger = Logger.getLogger(SimpleTplCalculate.class);

    private static final String Input_Key_Calculate_Tpl = "tpl";//计算模板

    private static final String Output_Key_Calculate_Result_Data = "data";//计算结果

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String tpl = input.getInputValue(Input_Key_Calculate_Tpl);
        if(StringUtil.isInvalid(tpl)){
            throw new LegoException("请输入有效的计算模板", VendorExceptionCode.Lego_Error_Calculate_Tpl_Null);
        }
        List<InputField> fields = input.getInputFields();

        String expressReg = "\\$\\{.*?\\}";
        Matcher m = Pattern.compile(expressReg).matcher(tpl);
        List<String> params = new ArrayList<>();
        while(m.find()){
            String matchStr = m.group();
            System.out.println("matchStr:" + matchStr);
            //解析模板然后将模板值存到数组中
            String param = findParam(input,fields,matchStr);
            //如果是字符串就外加一层单引号，因为下面表达式求值 需要
            Pattern pattern = Pattern.compile("[\\D]+");
            if(pattern.matcher(param).matches()){
                //字符串
                param = "'"+param+"'";
            }
            params.add(param);
        }
        for(String item :params){
            tpl = tpl.replaceFirst(expressReg, item);
        }
        /**
         * 表达式字符串 计算
         */
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");

        Object result = null;
        try {
            result = engine.eval(tpl);
        } catch (ScriptException e) {
            e.printStackTrace();
            throw new LegoException("表达式模板错误:"+tpl,VendorExceptionCode.Lego_Error_Calculate_Error);
        }
        output.setOutputValue(Output_Key_Calculate_Result_Data,result);
    }


    /**
     * 填充 模板值
     * @param ifs
     * @param param
     * @return
     */
    private String findParam(Input input,List<InputField> ifs,String param) throws LegoException {
        //freemarker 解析模板   param默认是 ${} 这样的语法
        Map<String,Object> params = LegoUtil.buildTPLParams(input.getRequest(),null,null,input);
        Iterator result = ifs.iterator();
        while(result.hasNext()) {
            InputField _if = (InputField)result.next();
            params.put(_if.getFieldName(), _if.getFieldValue());
        }
        return LegoUtil.transferInputValue(param, params);
    }


    //test
    public static void main(String[] args) throws ScriptException {
        String tpl = "${param1} - ${param2}/${param3} + 123}";
        String expressReg = "\\$\\{.*?\\}";
        Matcher m = Pattern.compile(expressReg).matcher(tpl);
        while(m.find()){
            String matchStr = m.group();
            System.out.println("matchStr:"+matchStr);
        }


        /**
         * 表达式字符串 计算
          */
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
		/*
		 * 字符串转算术表达式
		 */
        String str1 = "43*(2 + 1.4)+2*32/(3-2.1)";
        Object result1 = engine.eval(str1);
        System.out.println("结果类型:" + result1.getClass().getName() + ",结果:" + result1);
        //三元表达式
        String str2 = "1==0?321:123";
        Object result2 = engine.eval(str2);
        System.out.println("结果类型:" + result2.getClass().getName() + ",结果:" + result2);
    }

}
