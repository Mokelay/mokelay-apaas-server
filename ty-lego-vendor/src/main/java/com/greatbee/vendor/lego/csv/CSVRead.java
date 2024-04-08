
package com.greatbee.vendor.lego.csv;

import com.csvreader.CsvReader;
import com.greatbee.base.bean.DataList;
import com.greatbee.base.util.DataUtil;
import com.greatbee.core.bean.view.FileStream;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV Read
 *
 * @author CarlChen
 * @date 2018-07-28
 */
@Component("csvRead")
public class CSVRead implements Lego {
    private static final String Input_Key_CSV_FileStream = "csv_filestream";
    private static final String Input_Key_CSV_Header_Row_Num = "header_row_num";

    private static final String Output_Key_Headers = "headers";
    private static final String Output_Key_Data_List = "data_list";

//    public static void main(String args[]) throws LegoException {
//        CSVRead r = new CSVRead();
//        r.execute(null, null);
//    }

    @Override
    public void execute(Input input, Output output) throws LegoException {
        int headerRowNum = DataUtil.getInt(input.getInputObjectValue(Input_Key_CSV_Header_Row_Num), 1);

        Object fs = input.getInputObjectValue(Input_Key_CSV_FileStream);

        if (fs != null) {
            try {
                InputStream is = null;
                if(fs instanceof FileStream){
                    is = ((FileStream) fs).getInputStream();
                }else if(fs instanceof MultipartFile){
                    is = ((MultipartFile) fs).getInputStream();
                }
                CsvReader reader = new CsvReader(is, ',', Charset.forName("UTF-8"));
//                CsvReader reader = new CsvReader(new FileInputStream(new File("/Users/CarlChen/project/TY/io/demo.csv")),',',Charset.forName("UTF-8"));

                List<String[]> headers = new ArrayList<String[]>();
                //读取表头数据
                while (headerRowNum > 0) {
                    reader.readHeaders();
                    headers.add(reader.getHeaders());
                    headerRowNum--;
                }

                // 逐行读入除表头的数据
                List<String[]> datas = new ArrayList<String[]>();
                while (reader.readRecord()) {
//                    System.out.println(reader.getRawRecord());
                    datas.add(reader.getValues());
                }

                reader.close();

                output.setOutputValue(Output_Key_Headers, new DataList(headers));
                output.setOutputValue(Output_Key_Data_List, new DataList(datas));
            } catch (IOException e) {
                e.printStackTrace();
                throw new LegoException(e.getMessage(), e, 1);
            }
        }
    }
}