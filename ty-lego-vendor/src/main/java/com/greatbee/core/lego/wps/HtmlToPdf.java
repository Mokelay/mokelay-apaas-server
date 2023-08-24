
package com.greatbee.core.lego.wps;

import com.greatbee.base.util.StringUtil;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import com.greatbee.core.lego.system.TYPPC;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * html to pdf
 *
 * @author xiaobc
 * @date 2018-08-06
 */
@Component("htmlToPdf")
public class HtmlToPdf implements Lego {

    //需要转换的html代码
    private static final String Input_Key_Html_String = "html_string";//html字符串

    private static final String Input_Key_Pdf_Name = "pdf_name";//导出的pdf名称  含.pdf

    private static final String Output_Key_Pdf_Filestream = "pdf_file_stream";//返回pdf文件流

    @Override
    public void execute(Input input, Output output) throws LegoException {

        String html = input.getInputValue(Input_Key_Html_String);
        String pdfName = input.getInputValue(Input_Key_Pdf_Name);
        try {
            String locaPath = HtmlToPdf.class.getResource("/").getPath();
            String tmpPath = TYPPC.getTYProp("upload.temp.dir");
            if(StringUtil.isValid(tmpPath)){
                File tmpFile = new File(tmpPath);
                if(!tmpFile.getParentFile().exists()){
                    tmpFile.getParentFile().mkdirs();
                }
                locaPath = tmpPath;
            }
            String tmpFilePath = null;
            if(StringUtil.isInvalid(pdfName)){
                tmpFilePath= locaPath+ UUID.randomUUID()+".pdf";
            }else{
                tmpFilePath= locaPath+pdfName;
            }

            /*1、创建document对象*/
            Document doc = new Document(PageSize.A4);
            /*2、创建PdfWriter实例*/
            PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(tmpFilePath));
            /*3、打开文档*/
            doc.open();
            // 标准化HTML代码
            org.jsoup.nodes.Document _doc = Jsoup.parse(html);
            _doc.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);

            String _html = _doc.html();
            //HTML转PDF
            XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, doc, new ByteArrayInputStream(_html.getBytes()), Charset.forName("UTF-8"), new XMLWorkerFontProvider() {
                public Font getFont(final String fontname, final String encoding,
                                    final boolean embedded, final float size, final int style,
                                    final BaseColor color) {
                    BaseFont bf = null;
                    try {
                        bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Font font = new Font(bf, size, style, color);
                    font.setColor(color);
                    return font;
                }
            });
            doc.close();

            //返回临时文件文件流
            output.setOutputValue(Output_Key_Pdf_Filestream, new File(tmpFilePath));

        }catch(Exception e) {
            e.printStackTrace();
        }

    }
}