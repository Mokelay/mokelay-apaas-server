package com.greatbee.core.lego.qiniu;

import com.alibaba.fastjson.JSONObject;
import com.greatbee.db.ExceptionCode;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.util.LegoUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

/**
 * 七牛数据处理的回调lego
 *
 * @author xiaobc
 * @date 18/7/16
 */
@Component("qiniuPersistentNotify")
public class QiniuPersistentNotify implements ExceptionCode, Lego {

    private static final Logger logger = Logger.getLogger(QiniuPersistentNotify.class);

    private static final long ERROR_LEGO_NET_RESOURCE_SAVE_ERROR = 300055L;

    //拼接上传或者查询，返回文件url，方便显示或者下载
    private static final String Input_Key_File_Download_Url = "qiniu_download_url";

    private static final String Output_Key_Net_Resource_File_Url = "net_file_url";//网络资源地址

    private static final String Output_Key_Target_File_Name = "fileName";//目标文件名

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //返回网络资源就好了
        HttpServletRequest request = input.getRequest();
        //构建下载地址
        String downloadUrl = input.getInputValue(Input_Key_File_Download_Url);
        Map params = _buildTplParams(input);
        String url = LegoUtil.transferInputValue(downloadUrl, params);//附带参数可能需要模板

        try {
            request.getInputStream();
            String line="";
            BufferedReader br=new BufferedReader(new InputStreamReader(
                    request.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine())!=null){
                sb.append(line);
            }
            logger.info("===============================end！");
            logger.info(sb.toString());
            //{"id":"z0.5b4de77a38b9f324a5176867","pipeline":"1381550658.lianxiya_queue","code":0,"desc":"The fop was completed successfully","reqid":"O2kAAJyviW5CKUIV","inputBucket":"lianxiya","inputKey":"61c38df905edce0290f655dd5c975be7.mp3","items":[{"cmd":"avthumb/mp3|saveas/bGlhbnhpeWE6NjFjMzhkZjkwNWVkY2UwMjkwZjY1NWRkNWM5NzViZTcubXAz","code":0,"desc":"The fop was completed successfully","hash":"Fhxbj1kWplGB6SXenVqWvrnrCQxz","key":"61c38df905edce0290f655dd5c975be7.mp3","returnOld":0}]}
            JSONObject resJson = JSONObject.parseObject(sb.toString());
            if(resJson.containsKey("inputKey")){
                String fileName = resJson.getString("inputKey");
                output.setOutputValue(Output_Key_Target_File_Name,fileName);
                output.setOutputValue(Output_Key_Net_Resource_File_Url,url+"/"+fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 构建tpl模板需要的请求参数
     * @param input
     * @return
     * @throws LegoException
     */
    private Map _buildTplParams(Input input) throws LegoException {
        java.util.List ifs = input.getInputFields();
        Map<String,Object> params = LegoUtil.buildTPLParams(input.getRequest(),null,null,input);
        Iterator result = ifs.iterator();
        while(result.hasNext()) {
            InputField _if = (InputField)result.next();
            params.put(_if.getFieldName(), _if.getFieldValue());
        }
        return params;
    }

}
