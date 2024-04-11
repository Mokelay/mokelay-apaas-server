package com.mokelay.vendor.lego.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.mokelay.base.bean.Data;
import com.mokelay.base.util.DataUtil;
import com.mokelay.base.util.StringUtil;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.core.lego.util.LegoUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CreateOssStorage
 *
 * 创建oss存储空间 lego
 * 同一用户创建的存储空间总数不能超过30个
 * 存储空间一旦创建成功，名称、所处地域、存储类型不能修改。
 * 单个存储空间的容量不限制。
 * @author xiaobc
 * @date 18/6/21
 */
@Component("listOssFile")
public class ListOssFile extends OssBase{
    private static final Logger logger = Logger.getLogger(ListOssFile.class);

    private static final String Input_Key_Oss_Next_Marker = "nextMarker";//下一页 文件的开始 文件名（ossKey）
    private static final String Input_Key_Oss_PageSize = "pageSize";
    private static final String Input_Key_Oss_KeyPrefix = "keyPrefix";

    private static final String Output_Key_Oss_File_list = "file_list";//文件列表
    private static final String Output_Key_Oss_Next_Marker = "nextMarker";//下一页文件名

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String bucketName = input.getInputValue(Input_Key_Oss_Bucket_Name);
        if(StringUtil.isInvalid(bucketName)){
            throw new LegoException("OSS存储空间名称无效",ERROR_LEGO_OSS_Bucket_Name_Null);
        }
        //分页列举文件
        String page = DataUtil.getString(input.getInputValue(Input_Key_Oss_Next_Marker), null);
        int pageSize = DataUtil.getInt(input.getInputValue(Input_Key_Oss_PageSize), 100);
        if("null".equalsIgnoreCase(page)) {
            //如果传过来的nexMarker为null 字符串，说明到底了
            output.setOutputValue(Output_Key_Oss_File_list,new ArrayList<Data>());
            output.setOutputValue(Output_Key_Oss_Next_Marker,null);
            return ;
        }
        if(StringUtil.isInvalid(page)||"1".equalsIgnoreCase(page)) {
            page = null;
        }
        //拼接下载地址 ,支持模板
        String downloadUrl = input.getInputValue(Input_Key_File_Download_Url);
        Map params = buildTplParams(input);
        String url = LegoUtil.transferInputValue(downloadUrl, params);//附带参数可能需要模板

        if(pageSize <= 0) {
            pageSize = 1;
        }
        //搜索前缀
        String keyPrefix = input.getInputValue(Input_Key_Oss_KeyPrefix);

        OSSClient ossClient = this.createClient(input);

        String nextMarker = page;
        ObjectListing objectListing;
//        do {
            objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).
                    withPrefix(keyPrefix).withMarker(nextMarker).withMaxKeys(pageSize));
            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();

            List<Data> fileList = new ArrayList<Data>();

            for (OSSObjectSummary s : sums) {
                Data data = new Data();
                data.put("fileName",s.getKey());
                data.put("fileUrl",url+s.getKey());
                fileList.add(data);
            }
            nextMarker = objectListing.getNextMarker();
//        } while (objectListing.isTruncated());

        output.setOutputValue(Output_Key_Oss_File_list,fileList);
        output.setOutputValue(Output_Key_Oss_Next_Marker,nextMarker);

        //关闭ossClient
        this.closeClient(ossClient);

    }


}
