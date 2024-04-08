package com.greatbee.core.lego.basic;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.util.RandomGUIDUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.db.bean.oi.DS;
import com.greatbee.db.bean.oi.OI;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * File Upload
 * <p/>
 * 输入：
 * 1. InputStream，然后通过DS+OI来保存成文件 (Input_Key_File_Stream)
 * 输出
 * 1. 保存的文件名 Output_Key_File_Serialize_Name 序列化的文件名
 * 2. 原始文件名
 * 3. 文件大小
 * 4. 文件类型
 * Author :CarlChen
 * Date:17/7/28
 */
@Component("file_save")
public class FileSave implements Lego, ExceptionCode {
    @Autowired
    private TYDriver tyDriver;
    private static final String Input_Key_File_Stream = "file_stream";

    @Override
    public void execute(Input input, Output output) throws LegoException {
        String oiAlias = input.getApiLego().getOiAlias();
        try {
            OI oi = tyDriver.getTyCacheService().getOIByAlias(oiAlias);
            DS ds = tyDriver.getTyCacheService().getDSByAlias(oi.getDsAlias());

            Object objectValue = input.getInputObjectValue(Input_Key_File_Stream);
            if (objectValue == null) {
                throw new LegoException("没有需要保存的文件", ERROR_LEGO_FILE_EMPTY);
            }
            if (objectValue instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) objectValue;

                String originalName = file.getOriginalFilename();
                long fileSize = file.getSize();
                String fileType = originalName.split("\\.")[originalName.split("\\.").length - 1];
                String serializeName = RandomGUIDUtil.getRawGUID() + "." + fileType;
                String contentType = file.getContentType();

                File _file = LegoUtil.buildFile(ds.getConnectionUrl() + oi.getResource() + File.separator + serializeName);
                //保存文件
                file.transferTo(_file);

                FileStorage fileStorage = new FileStorage();
                fileStorage.setContentType(contentType);
                fileStorage.setFileSize(fileSize);
                fileStorage.setOiAlias(oiAlias);
                fileStorage.setOriginalName(originalName);
                fileStorage.setSerializeName(serializeName);
                fileStorage.setFileType(fileType);
                fileStorage.setUploadType("server");
                StorageUtil.addFileStorage(fileStorage, output, tyDriver);
            }
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(), e, e.getCode());
        } catch (IOException e) {
            e.printStackTrace();
            throw new LegoException("保存文件失败", e, ERROR_LEGO_FILE_SAVE_FAIL);
        }
    }
}
