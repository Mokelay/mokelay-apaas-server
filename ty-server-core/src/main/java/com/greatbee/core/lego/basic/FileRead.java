package com.greatbee.core.lego.basic;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.bean.oi.DS;
import com.greatbee.core.bean.oi.OI;
import com.greatbee.core.bean.server.FileStorage;
import com.greatbee.core.bean.view.FileStream;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import com.greatbee.core.manager.TYDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * File Donwload
 * <p/>
 * 输入：
 * 1. 保存的文件名 Input_Key_File_Serialize_Name 序列化的文件名
 * 输出
 * 1. OutputStream，然后通过DS+OI来保存成文件 (Input_Key_File_Stream)
 * Author :CarlChen
 * Date:17/7/28
 */
@Component("fileRead")
public class FileRead implements Lego, ExceptionCode {
    private static final String Input_Key_File_Serialize_Name = "file_serialize_name";

    private static final String Output_Key_File_Stream = "file_stream";
    //下载文件名
    private static final String Input_Key_File_Name = "file_name";

    @Autowired
    private TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {

        String oiAlias = input.getApiLego().getOiAlias();
        String fileSerializeName = input.getInputValue(Input_Key_File_Serialize_Name);
        String fileName = input.getInputValue(Input_Key_File_Name);

        try {

            String fileType = fileSerializeName.split("\\.")[fileSerializeName.split("\\.").length-1];

            OI oi = tyDriver.getTyCacheService().getOIByAlias(oiAlias);
            DS ds = tyDriver.getTyCacheService().getDSByAlias(oi.getDsAlias());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
            String now = sdf.format(new Date());

            FileStorage fileStorage = tyDriver.getFileStorageManager().getFileStorage(fileSerializeName);

            FileStream fileStream = new FileStream(new File(ds.getConnectionUrl() + oi.getResource() + File.separator + fileStorage.getSerializeName()));
            fileStream.setContentType(fileStorage.getContentType());
            fileStream.setFileName(StringUtil.isValid(fileName)?(fileName+now+"."+fileType):fileStorage.getOriginalName());

            output.setOutputValue(Output_Key_File_Stream, fileStream);
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(), e, e.getCode());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new LegoException("文件找不到:" + fileSerializeName, ERROR_LEGO_FILE_Not_Found);
        }


    }
}
