package com.greatbee.core.lego.basic;

import com.greatbee.base.bean.DBException;
import com.greatbee.base.bean.Data;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.util.CollectionUtil;
import com.greatbee.base.util.RandomGUIDUtil;
import com.greatbee.core.bean.constant.DBMT;
import com.greatbee.api.bean.constant.IOFT;
import com.greatbee.core.bean.oi.DS;
import com.greatbee.core.bean.oi.OI;
import com.greatbee.core.bean.server.FileStorage;
import com.greatbee.api.bean.server.InputField;
import com.greatbee.core.bean.view.ConnectorTree;
import com.greatbee.core.db.RelationalDataManager;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import com.greatbee.core.lego.util.LegoUtil;
import com.greatbee.api.util.SpringContextUtil;
import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Export Data
 * <p/>
 * 输入：
 * 1. 导出的字段(多个,ioft = read)
 * 2. 导出的Condition(多个ioft= condition)
 * 3. Input_Key_Order_Field 需要排序的字段
 * 4. Input_Key_Order 排序类别(升|降)
 * 5. Input_Key_Group_Field Group字段
 * <p/>
 * 输出：
 * 1. 导出文件的serializeName
 * 2、导出文件的file_storage
 *
 * <p/>
 * Author :CarlChen
 * Date:17/7/23
 */
@Component("export")
public class Export extends List implements Lego {
    private static final String Input_Key_Export_OI = "export_oi";

    private static final String Output_Key_Export_Serialize_Name = "export_serializ_name";
    private static final String Output_Key_File_Storage = "file_storage";

    private static final String EXPORT_FILE_TYPE = "xlsx";
    private  static final String EXPORT_FILE_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Override
    public void execute(Input input, Output output) throws LegoException {

        try {
            String pathOiAlias = input.getInputValue(Input_Key_Export_OI);
            OI pathOi = tyDriver.getTyCacheService().getOIByAlias(pathOiAlias);
            DS pathDs = tyDriver.getTyCacheService().getDSByAlias(pathOi.getDsAlias());


            // 通过Input_Key_Export_OI 配置的DS+OI获取保存文件路径
            String storeFilePath = pathDs.getConnectionUrl() + pathOi.getResource();
            String serializeFileName = RandomGUIDUtil.getRawGUID() + "."+EXPORT_FILE_TYPE;
            String path = storeFilePath + File.separator + serializeFileName;

            ConnectorTree root = buildConnectorTree(tyDriver, input);
            //全局搜索关键字 处理全局搜索
            buildKeywords(input,root);

            RelationalDataManager dataManager = (RelationalDataManager) SpringContextUtil.getBean(DBMT.Mysql.getType());
            DataList dataList = dataManager.list(root);

            java.util.List<InputField> ifs = input.getInputField(IOFT.Read);

            String sheetName = "Sheet1";// name of sheet
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet(sheetName);

            java.util.List<Data> list = dataList.getList();
            if (CollectionUtil.isValid(list)) {
                //第一行的文本设置
                XSSFRow firstRow = sheet.createRow(0);
                Map<String, Object> firstDataObject = list.get(0);
                int firstColIndex = 0;
                for (Map.Entry<String, Object> entry : firstDataObject.entrySet()) {
                    XSSFCell cell = firstRow.createCell(firstColIndex);
                    for (InputField _if :ifs){
                        if(entry.getKey().equalsIgnoreCase(_if.getAlias())){
                            cell.setCellValue("" + _if.getName());
                            break;
                        }
                    }
                    firstColIndex++;
                }
                //第二行开始数据
                for (int i = 0; i < list.size(); i++) {
                    Data data = list.get(i);
                    XSSFRow row = sheet.createRow(i + 1);//去掉第一行的文本
                    Map<String, Object> dataObject = data;

                    int colIndex = 0;
                    for (Map.Entry<String, Object> entry : dataObject.entrySet()) {
                        XSSFCell cell = row.createCell(colIndex);
                        cell.setCellValue("" + entry.getValue());
                        colIndex++;
                    }
                }
            }
            File file = LegoUtil.buildFile(path);//获取文件对象
            FileOutputStream fileOut = new FileOutputStream(path);

            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
            //上面已经将数据写入到到文件里

            FileStorage fileStorage = new FileStorage();
            fileStorage.setContentType(EXPORT_FILE_CONTENT_TYPE);
            fileStorage.setFileSize(FileUtils.sizeOf(file));
            fileStorage.setOiAlias(pathOiAlias);
            fileStorage.setOriginalName(serializeFileName);
            fileStorage.setSerializeName(serializeFileName);
            tyDriver.getFileStorageManager().add(fileStorage);
            output.setOutputValue(Output_Key_File_Storage, fileStorage);

            output.setOutputValue(Output_Key_Export_Serialize_Name,serializeFileName);
        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(), e, e.getCode());
        } catch (IOException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(), e, ERROR_LEGO_EXPORT_FILE_IO_ERROR);
        }

    }
}
