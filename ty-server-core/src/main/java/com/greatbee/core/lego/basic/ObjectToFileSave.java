package com.greatbee.core.lego.basic;

import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.DataUtil;
import com.greatbee.base.util.RandomGUIDUtil;
import com.greatbee.core.bean.oi.DS;
import com.greatbee.core.bean.oi.OI;
import com.greatbee.core.bean.server.FileStorage;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.util.LegoUtil;
import com.greatbee.core.lego.util.StorageUtil;
import com.greatbee.core.manager.TYDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Object对象保存成文件
 */
@Component("object_to_file_save")
public class ObjectToFileSave extends FileSave implements Lego {
    @Autowired
    private TYDriver tyDriver;

    //需要保存的Object
    public static final String Input_Key_Object = "object";
    //文件名
    public static final String Input_Key_File_Name = "file_name";
    //是否批量保存，如果是批量保存，则object必须是DataList
    public static final String Input_Key_Batch = "batch";

    @Override
    public void execute(Input input, Output output) throws LegoException {
        String oiAlias = input.getApiLego().getOiAlias();

        try {
            OI oi = tyDriver.getTyCacheService().getOIByAlias(oiAlias);
            DS ds = tyDriver.getTyCacheService().getDSByAlias(oi.getDsAlias());

            Object obj = input.getInputObjectValue(Input_Key_Object);
            String fileName = input.getInputValue(Input_Key_File_Name);
            boolean batch = DataUtil.getBoolean(input.getInputValue(Input_Key_Batch), false);

            File targetFile = null;

            if (obj != null) {
                FileStorage fileStorage = new FileStorage();

                if (batch && obj instanceof DataList) {
                    java.util.List list = ((DataList) obj).getList();
                    if (CollectionUtil.isValid(list)) {
                        java.util.List<File> files = new ArrayList<File>();
                        for (Object _obj : list) {
                            //编译生成新的fileName
                            Map<String, Object> params = LegoUtil.buildTPLParams(input.getRequest(), input.getInputFields(), null, input);
                            params.put("object", _obj);
                            String _fileName = LegoUtil.transferInputValue(fileName, params);

                            File file = LegoUtil.buildFile(ds.getConnectionUrl() + oi.getResource() + File.separator + _fileName);
                            writeFile(file, JSONObject.toJSONString(_obj));

                            files.add(file);
                        }

                        //生成ZIP文件，真实的fileName
                        String _fileName = RandomGUIDUtil.getRawGUID() + ".zip";
                        targetFile = LegoUtil.buildFile(ds.getConnectionUrl() + oi.getResource() + File.separator + _fileName);
                        zipFiles(targetFile, files);
                    }
                } else {
                    //如果没有设置fileName，则生成随机数作为FileName
                    String _fileName = RandomGUIDUtil.getRawGUID() + ".txt";
                    targetFile = LegoUtil.buildFile(ds.getConnectionUrl() + oi.getResource() + File.separator + _fileName);
                    writeFile(targetFile, JSONObject.toJSONString(obj));

                    fileStorage.setContentType("text/plain");
                    fileStorage.setOriginalName(_fileName);
                    fileStorage.setSerializeName(_fileName);
                    fileStorage.setFileType("txt");
                }

                fileStorage.setFileSize(targetFile.length());
                fileStorage.setOiAlias(oiAlias);
                fileStorage.setUploadType("server");

                StorageUtil.addFileStorage(fileStorage, output, tyDriver);
            }
        } catch (DBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * String写入文件
     *
     * @param file
     * @param content
     * @throws IOException
     */
    private void writeFile(File file, String content) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), true));
        bw.write(content);
        bw.close();
    }

    /**
     * 将多个文件打包成zip文件
     *
     * @param zipfile
     * @param files
     */
    private void zipFiles(File zipfile, java.util.List<File> files) throws IOException {
        byte[] buf = new byte[1024];
        // Create the ZIP file
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipfile));
            // Compress the files
            for (File file : files) {
                FileInputStream in = new FileInputStream(file);
                // Add ZIP entry to output stream.
                out.putNextEntry(new ZipEntry(file.getName()));
                // Transfer bytes from the file to the ZIP file
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                // Complete the entry
                out.closeEntry();
                in.close();
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }

    }
}