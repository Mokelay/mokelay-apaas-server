package com.mokelay.core.lego.basic;

import com.alibaba.fastjson.JSON;
import com.mokelay.api.bean.constant.IOFT;
import com.mokelay.base.bean.DBException;
import com.mokelay.base.bean.Data;
import com.mokelay.base.bean.constant.CT;
import com.mokelay.base.bean.constant.DT;
import com.mokelay.base.util.CollectionUtil;
import com.mokelay.base.util.DataUtil;
import com.mokelay.base.util.StringUtil;
import com.mokelay.db.ExceptionCode;
import com.mokelay.core.bean.constant.*;
import com.mokelay.db.bean.oi.DS;
import com.mokelay.db.bean.oi.Field;
import com.mokelay.db.bean.oi.OI;
import com.mokelay.api.bean.server.APILego;
import com.mokelay.core.bean.server.FileStorage;
import com.mokelay.api.bean.server.InputField;
import com.mokelay.base.bean.view.Condition;
import com.mokelay.db.bean.view.ConnectorTree;
import com.mokelay.db.bean.view.MultiCondition;
import com.mokelay.db.database.RelationalDataManager;
import com.mokelay.api.lego.Input;
import com.mokelay.api.lego.Lego;
import com.mokelay.api.lego.LegoException;
import com.mokelay.api.lego.Output;
import com.mokelay.core.lego.basic.ext.BaseRead;
import com.mokelay.core.lego.util.LegoUtil;
import com.mokelay.core.manager.TYDriver;
import com.mokelay.api.util.SpringContextUtil;
import com.mokelay.db.bean.constant.DBMT;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Import Data
 * <p/>
 * 输入：
 * 1. 文件名 Input_Key_File_Serialize_Name
 * 2. 需要导入的字段(多个,ioft=create，不跨表)
 * 3. Input_Key_Max_Import_Num 最大导入数据量
 * 输出：
 * 1. 导入总数
 * 2. 导入成功数量Output_Key_Import_Success
 * 3. 导入失败数量Output_Key_Import_Fail
 * Author :CarlChen
 * Date:17/7/23
 */
@Component("import")
public class Import extends BaseRead implements Lego, ExceptionCode {

    private static Logger logger = Logger.getLogger(Import.class);

    public static final long ERROR_LEGO_IMPORT_Interrupt = 300000;

    private static final String Removal_Type_Add = "add";//新增
    private static final String Removal_Type_Update = "update";//更新
    private static final String Removal_Type_Interrupt = "interrupt";//中断
    private static final String Removal_Type_Skip = "skip";//跳过


    private static final String Input_Key_File_Serialize_Name = "file_serialize_name";
    private static final String Input_Key_Max_Import_Num = "max_import_num";
//    private static final String Input_Key_IMPORT_FIELDS = "import_fields";

    private static final String Input_Key_Start_Data_Line = "start_data_line";//从第几行开始导入，默认第二行，第一行是标题
    private static final String Input_Key_Removal_Field_Names = "removalFieldNames";//去重判别字段，多个逗号隔开
    //去重类型 字段重复跳过(skip)，字段重复更新(update)，字段重复中断(interrupt),正常新增(add)   默认 正常新增
    private static final String Input_Key_Removal_Type="removalType";

    private static final String Output_Key_Import_Total = "import_total";
    private static final String Output_Key_Import_Success = "import_success";
    private static final String Output_Key_Import_Fail = "import_fail";

    @Autowired
    private TYDriver tyDriver;

    @Override
    public void execute(Input input, Output output) throws LegoException {
        String oiAlias = input.getApiLego().getOiAlias();
        try {
            OI oi = tyDriver.getTyCacheService().getOIByAlias(oiAlias);
//            DS ds = tyDriver.getDSManager().getDSByAlias(oi.getDsAlias());
            List<Field> oiFields = tyDriver.getTyCacheService().getFields(oiAlias);

            int importTotal;
            int importSuccess = 0;
            int importFail = 0;

//            int startDataLine = 2;//第二行开始
            String fileSerializeName = input.getInputValue(Input_Key_File_Serialize_Name);
            int maxImportNum = DataUtil.getInt(input.getInputField(Input_Key_Max_Import_Num), 1000);//默认最大导入数是1000条
            String startDataLineStr = input.getInputField(Input_Key_Start_Data_Line).fieldValueToString();

            String removalFieldNames = DataUtil.getString(input.getInputValue(Input_Key_Removal_Field_Names),"");//去重字段
            String removelType = input.getInputValue(Input_Key_Removal_Type);
            if(StringUtil.isInvalid(removelType)){
                removelType = Removal_Type_Add;//默认是新增
            }

            //通过开始行数来判断是走新逻辑还是走老逻辑
            boolean oldLogic = false;
            if(StringUtil.isInvalid(startDataLineStr) && !"0".equalsIgnoreCase(startDataLineStr)){
                //如果没有设置开始行数 走老逻辑
                oldLogic = true;
            }
            int startDataLine = DataUtil.getInt(startDataLineStr, 2);//默认从第二行开始
            if(startDataLine<=0){
                startDataLine=2;//从第二行开始
            }
            if (!fileSerializeName.endsWith("xls") && !fileSerializeName.endsWith("xlsx")) {
                throw new LegoException(fileSerializeName + "不是excel文件", ERROR_LEGO_IMPORT_NOT_XLSX);
            }
            FileStorage fileStorage = tyDriver.getFileStorageManager().getFileStorage(fileSerializeName);
            if (fileStorage == null) {
                throw new LegoException("文件目录没有找到指定文件", ERROR_LEGO_IMPORT_NO_FILE);
            }
            String pathOiAlias = fileStorage.getOiAlias();
            OI pathOi = tyDriver.getTyCacheService().getOIByAlias(pathOiAlias);
            DS pathDs = tyDriver.getTyCacheService().getDSByAlias(pathOi.getDsAlias());
            String path = pathDs.getConnectionUrl() + pathOi.getResource() + File.separator + fileSerializeName;
            List<List<Object>> lists = readExcel(path,startDataLine);
            if (lists.size() > maxImportNum) {
                throw new LegoException("导入记录数超过最大限制数", ERROR_LEGO_IMPORT_OVER_MAXRECORD);
            }
            importTotal = lists.size();
            RelationalDataManager dataManager = (RelationalDataManager) SpringContextUtil.getBean(DBMT.Mysql.getType());

            List<InputField> ifs = input.getInputField(IOFT.Create);
            int rowNum = 0;
            for (List<Object> row : lists) {
                rowNum++;
                try {
                    List<Field> saveFields = new ArrayList<Field>();
                    if(oldLogic){
                        for (int i = 0; i < row.size(); i++) {
                            Field f = this.buildField(ifs.get(i).getFieldName(),DataUtil.getString(row.get(i), ""),oiFields);
                            saveFields.add(f);
                        }
                        if(ifs.size()> row.size()){
                            //说明有其他字段不是从excel获取的数据，比如创建时间等
                            for(int j=row.size();j<ifs.size();j++){
                                Field f = this.buildField(ifs.get(j).getFieldName(),DataUtil.getString(ifs.get(j).getFieldValue(), ""),oiFields);
                                saveFields.add(f);
                            }
                        }
                    }else{
                        //新逻辑，用field value   ${col1} 来表示读取表格第几列
                        String expressReg = "\\$\\{.*?\\}";
                        Pattern p = Pattern.compile(expressReg);
                        //构建tpl的参数
                        Map<String,Object> params = buildTplParam(row);
                        for(InputField _if:ifs){
                            boolean newLogicFlag = p.matcher(_if.fieldValueToString()).matches();
                            if(FVT.Constant.getType().equalsIgnoreCase(_if.getFvt()) && newLogicFlag){
                                //是常量，并且value值是  ${xxx}格式
                                String val = LegoUtil.transferInputValue(_if.fieldValueToString(), params);
                                Field f = this.buildField(_if.getFieldName(), DataUtil.getString(val, ""), oiFields);
                                saveFields.add(f);
                            }else{
                                //如果不是常量 和${} 结构，就直接存到数据库
                                Field f = this.buildField(_if.getFieldName(),DataUtil.getString(_if.getFieldValue(), ""),oiFields);
                                saveFields.add(f);
                            }
                        }
                    }

//                    dataManager.create(oi, saveFields);
                    int dbFlag = dbOption(input,oi,saveFields,removalFieldNames,removelType,dataManager);
                    if(dbFlag!=4 && dbFlag!=3){
                        importSuccess++;
                    }else if(dbFlag==3){
                        logger.error("第"+rowNum+"行数据已存在,data:"+ JSON.toJSONString(row));
                        throw new LegoException("第"+rowNum+"行数据已存在",ERROR_LEGO_IMPORT_Interrupt);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    importFail++;
                    continue;
                }
            }
            output.setOutputValue(Output_Key_Import_Total, importTotal);
            output.setOutputValue(Output_Key_Import_Success, importSuccess);
            output.setOutputValue(Output_Key_Import_Fail, importFail);

        } catch (DBException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(), e, e.getCode());
        } catch (IOException e) {
            e.printStackTrace();
            throw new LegoException(e.getMessage(), e, ERROR_LEGO_IMPORT_FILE_IO_ERROR);
        }
    }

    /**
     *
     * 根据去重类型，判断是跳过、新增、更新、中断 操作
     * 添加  return 1
     * 更新  return 2
     * 中断  return 3
     * 跳过  return 4
     *
     * @param oi 保存模型
     * @param saveFields 保存字段
     * @param removalFieldNames  去重字段
     * @param removelType  去重类型
     * @param dataManager
     * @return
     */
    private int dbOption(Input input,OI oi,List<Field> saveFields,String removalFieldNames,String removelType,RelationalDataManager dataManager) throws DBException, LegoException {
        int result = 1;
        List<InputField> conditionIfs = null;
        boolean exist = false;//是否已经存在数据了，默认不存在
        //判断是否已经存在
        if(StringUtil.isValid(removalFieldNames)){
            //只有有去重判断字段 才会有去重的逻辑
            String[] removalFields = removalFieldNames.split(",");
            if(!Removal_Type_Add.equalsIgnoreCase(removelType)){
                //=======查询查询模型中是否有数据 start =======
                APILego queryApiLego = new APILego();
                queryApiLego.setOiAlias(oi.getAlias());
                queryApiLego.setLegoAlias("read");
                List<InputField> inputFields = new ArrayList<>();
                InputField idIf = new InputField();
                idIf.setAlias(removalFields[0]);//查询字段设置第一个字段
                idIf.setFieldName(removalFields[0]);
                idIf.setCt(CT.EQ.getName());
                idIf.setIft(IOFT.Read.getType());
                idIf.setDt(DT.String.getType());
                inputFields.add(idIf);
                //添加条件
                conditionIfs = buildConditionInputField(removalFields,saveFields);
                inputFields.addAll(conditionIfs);
                //条件查询
                Input queryInput = new Input(input.getRequest(),input.getResponse());
                queryInput.setApiLego(queryApiLego);
                queryInput.setInputFields(inputFields);
                ConnectorTree root = buildConnectorTree(tyDriver, queryInput);
                Data data = dataManager.read(root);
                //=======查询查询模型中是否有数据   end =======
                if(CollectionUtil.isValid(data)){
                    //如果data 有数据，说明有重复数据
                    exist = true;
                }else{
                    //没有重复，全部走新增的逻辑
                    removelType = Removal_Type_Add;
                }
            }
        }else{
            removelType = Removal_Type_Add;
        }

        //对应处理逻辑
        switch(removelType){
            case Removal_Type_Add:
                //新增，正常逻辑
                dataManager.create(oi, saveFields);
                result= 1;
                break;
            case Removal_Type_Update:
                //更新 逻辑
                String[] conditionFields = removalFieldNames.split(",");
                MultiCondition condition = new MultiCondition();
                for(int i=0;i<conditionFields.length;i++){
                    String conditionName = conditionFields[i];
                    Condition c = new Condition();
                    c.setConditionFieldName(conditionName);
                    c.setDt(DT.String.getType());
                    c.setCt(CT.EQ.getName());
                    for(int j=0;j<saveFields.size();j++){
                        Field f = saveFields.get(j);
                        if(conditionName.equalsIgnoreCase(f.getFieldName())){
                            //是条件字段
                            c.setConditionFieldValue(f.getFieldValue());
                            break;
                        }
                    }
                    condition.addCondition(c);
                }
                dataManager.update(oi, buildUpdateField(conditionFields,saveFields), condition);
                result = 2;
                break;
            case Removal_Type_Interrupt:
                //中断逻辑
                result = 3;
                break;
            case Removal_Type_Skip:
                //跳过逻辑
                result = 4;
                break;
        }
        return result;
    }

    /**
     * 构造更新字段
     * @param removalFields
     * @param saveFields
     * @return
     */
    private List<Field> buildUpdateField(String[] removalFields,List<Field> saveFields){
        List<Field> result = new ArrayList<>();
        Iterator<Field> fieldIterator = saveFields.iterator();
        while(fieldIterator.hasNext()){
            Field f = fieldIterator.next();
            boolean delete = false;
            for(int i=0;i<removalFields.length;i++) {
                String fieldName = removalFields[i];
                if(fieldName.equalsIgnoreCase(f.getFieldName())){
                    //如果是条件字段，删除
                    delete = true;
                    break;
                }
            }
            if(!delete){
                result.add(f);
            }
        }
       return result;
    }


    /**
     * 构建条件输入字段
     * @param removalFields  字段名数组
     * @return
     */
    private List<InputField> buildConditionInputField(String[] removalFields,List<Field> saveFields){
        List<InputField> result = new ArrayList<>();
        for(int i=0;i<removalFields.length;i++){
            String fieldName = removalFields[i];
            String fieldValue = "";
            if(StringUtil.isInvalid(fieldName)){
                continue;
            }
            for(int j=0;j<saveFields.size();j++){
                Field field = saveFields.get(j);
                if(fieldName.equals(field.getFieldName())){
                    fieldValue = field.getFieldValue();
                    break;
                }
            }
            //条件字段
            InputField conditionIf = new InputField();
            conditionIf.setAlias(fieldName);
            conditionIf.setFieldName(fieldName);
            conditionIf.setFieldValue(fieldValue);
            conditionIf.setCt(CT.EQ.getName());
            conditionIf.setIft(IOFT.Condition.getType());
            conditionIf.setDt(DT.String.getType());
            result.add(conditionIf);
        }
        return result;
    }


    /**
     * 构建tpl的参数
     * @param row
     * @return
     */
    private Map<String,Object> buildTplParam(List<Object> row){
        Map<String,Object> map = new HashMap<>();
        for(int i=1;i<=row.size();i++){
            map.put("col"+i,DataUtil.getString(row.get(i-1),""));
        }
        return map;
    }

    /**
     * 生成field
     * @param fieldName
     * @param fieldValue
     * @param oiFields
     * @return
     */
    private Field buildField(String fieldName,String fieldValue,List<Field> oiFields){
        Field f = new Field();
        for(Field _f:oiFields){
            if(fieldName.equalsIgnoreCase(_f.getFieldName())){
                f.setFieldName(fieldName);
                f.setFieldValue(fieldValue);
                f.setDt(_f.getDt());
                f.setFieldLength(_f.getFieldLength());
                break;
            }
        }
        return f;
    }


    /**
     * 读取excel文件
     *
     * @param path
     * @param startLine  开始行数，默认从第二行开始数据导入   第一行是标题
     * @return
     * @throws IOException
     */
    private List<List<Object>> readExcel(String path,int startLine) throws IOException {

        List<List<Object>> list = new ArrayList<List<Object>>();
        FileInputStream stream = new FileInputStream(path);
        Workbook workbook = new HSSFWorkbook();
        if (path.endsWith("xls")) {
            workbook = new HSSFWorkbook(stream);
        } else if (path.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(stream);
        }
        Sheet sheet = workbook.getSheetAt(0);
        Object value = null;
        Row row = null;
        Cell cell = null;

        //检查第一行 title  有多少列
        int maxColLen = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum();
        int startCol = sheet.getRow(sheet.getFirstRowNum()).getFirstCellNum();

        //去掉第一行
        for (int i = sheet.getFirstRowNum()+(startLine-1); i < sheet
                .getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            List<Object> linked = new ArrayList<Object>();

//            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
            for (int j = startCol; j < maxColLen; j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    linked.add("");
                    continue;
                }
                DecimalFormat df = new DecimalFormat("0");
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                DecimalFormat nf = new DecimalFormat("0.00");
                switch (cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_STRING:

                        value = cell.getStringCellValue();
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                            value = df.format(cell.getNumericCellValue());

                        } else if ("General".equals(cell.getCellStyle()
                                .getDataFormatString())) {
                            value = nf.format(cell.getNumericCellValue());
                        } else {
                            value = sdf.format(HSSFDateUtil.getJavaDate(cell
                                    .getNumericCellValue()));
                        }
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        value = cell.getBooleanCellValue();
                        break;
                    case HSSFCell.CELL_TYPE_BLANK:
//                        value = "-";
                        value = "";
                        break;
                    default:
                        value = cell.toString();

                }
                if (value.equals("")) {
//                    value = "-";
                    value = "";
                }
                if (null == value) {
                    value="";
//                    continue;
                }
                linked.add(value);
            }
            list.add(linked);
        }
        return list;

    }


}
