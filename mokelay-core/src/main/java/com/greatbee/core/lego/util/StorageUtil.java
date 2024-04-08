package com.greatbee.core.lego.util;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.StringUtil;
import com.greatbee.core.bean.server.FileStorage;
import com.greatbee.api.lego.Output;
import com.greatbee.core.manager.TYDriver;

/**
 * Storage Util
 *
 * @author CarlChen
 */
public class StorageUtil {
    public static final String Output_Key_File_Serialize_Name = "file_serialize_name";
    public static final String Output_Key_File_Original_Name = "file_original_name";
    public static final String Output_Key_File_Size = "file_size";
    public static final String Output_Key_File_Type = "file_type";
    public static final String Output_Key_File_Content_Type = "file_content_type";
    public static final String Output_Key_File_Storage = "file_storage";
    //文件url
    protected static final String Output_Key_File_Url = "file_url";

    /**
     * 对FileStorage的输出处理
     *
     * @param fileStorage
     * @param output
     * @param tyDriver
     * @throws DBException
     */
    public static void addFileStorage(FileStorage fileStorage, Output output, TYDriver tyDriver) throws DBException {
        output.setOutputValue(Output_Key_File_Original_Name, fileStorage.getOriginalName());
        output.setOutputValue(Output_Key_File_Serialize_Name, fileStorage.getSerializeName());
        output.setOutputValue(Output_Key_File_Size, fileStorage.getFileSize());
        output.setOutputValue(Output_Key_File_Type, fileStorage.getFileType());
        output.setOutputValue(Output_Key_File_Content_Type, fileStorage.getContentType());

        if (StringUtil.isValid(fileStorage.getFileUrl())) {
            output.setOutputValue(Output_Key_File_Url, fileStorage.getFileUrl());
        }

        tyDriver.getFileStorageManager().add(fileStorage);
        output.setOutputValue(Output_Key_File_Storage, fileStorage);
    }
}