package com.greatbee.core.lego.basic;

import com.greatbee.base.util.DataUtil;
import com.greatbee.db.ExceptionCode;
import com.greatbee.core.bean.view.FileStream;
import com.greatbee.api.lego.Input;
import com.greatbee.api.lego.Lego;
import com.greatbee.api.lego.LegoException;
import com.greatbee.api.lego.Output;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * 获取图形验证码
 * <p/>
 * Author :CarlChen
 * Date:17/7/20
 */
@Component("getGraphicVC")
public class GetGraphicVC implements Lego, ExceptionCode {

    private static final String Input_Key_System_Font_Num="sysFontNum";//第几个系统字体

    private static final String Output_Key_File_Stream = "file_stream";
    private static final String Output_Key_VC = "vc";

    private static final Font mFont = new Font("Times New Roman", Font.PLAIN, 17);

    @Override
    public void execute(Input input, Output output) throws LegoException {

        int width = 70, height = 18;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();
        Random random = new Random();
        g.setColor(getRandColor(200, 250));
        g.fillRect(1, 1, width - 1, height - 1);
        g.setColor(new Color(102, 102, 102));
        g.drawRect(0, 0, width - 1, height - 1);

        //找到不会乱码的字体
        int fontNum = DataUtil.getInt(input.getInputValue(Input_Key_System_Font_Num),0);
        if(fontNum<0){
            fontNum=0;
        }
        /*
         * 获取服务器上的字体，随机获取服务器上一个字体，进行设置
         */
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontName = ge.getAvailableFontFamilyNames();
        Font font = new Font(fontName[fontNum], Font.PLAIN, 17);
        g.setFont(font);
//        g.setFont(mFont);

        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(height - 1);
            int xl = random.nextInt(6) + 1;
            int yl = random.nextInt(12) + 1;
            g.drawLine(x, y, x + xl, y + yl);
        }

        String sRand = "";
        for (int i = 0; i < 4; i++) {
            int itmp = random.nextInt(10);
            sRand += String.valueOf(itmp);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(String.valueOf(itmp), 15 * i + 10, 16);
        }

        //保存验证码号
        output.setOutputValue(Output_Key_VC, sRand);

        g.dispose();

        java.io.InputStream is = null;
        try {
            java.io.ByteArrayOutputStream _output = new java.io.ByteArrayOutputStream();
            ImageIO.write(image, "JPEG", _output);
            byte[] buff = _output.toByteArray();
            is = new java.io.ByteArrayInputStream(buff);
        } catch (IOException e) {
            e.printStackTrace();
            throw new LegoException("生成图片输入流错误", ERROR_LEGO_VALUE_CREATE_VC_ERROR);
        }
        FileStream fs = new FileStream(is);
        fs.setContentType("image/jpeg");
        fs.setFileName("vc.jpg");
        output.setOutputValue(Output_Key_File_Stream,fs);
    }

    /**
     * Get Rand Color
     *
     * @param fc
     * @param bc
     * @return
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}
