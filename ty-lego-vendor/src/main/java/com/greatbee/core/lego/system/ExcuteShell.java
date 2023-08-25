package com.greatbee.core.lego.system;

import com.greatbee.base.util.StringUtil;
import com.greatbee.core.ExceptionCode;
import com.greatbee.core.lego.Input;
import com.greatbee.core.lego.Lego;
import com.greatbee.core.lego.LegoException;
import com.greatbee.core.lego.Output;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.InputStreamReader;

/**
 * 执行shell 命令
 *
 * @author xiaobc
 * @date 18/6/21
 */
@Component("excuteShell")
public class ExcuteShell implements ExceptionCode, Lego{
    private static final Logger logger = Logger.getLogger(ExcuteShell.class);
    //执行shell的命令字符串
    private static final String Input_Key_Cmd = "cmd";
    //执行shell命令的工作目录 默认是临时目录
    private static final String Input_Key_dir = "dir";
    //执行结果
    private static final String Output_Key_Shell_Output = "result";

    private static final long Lego_Error_Shell_Cmd_Null = 300090L;

    @Override
    public void execute(Input input, Output output) throws LegoException {
        //lego 处理逻辑
        String cmd = input.getInputValue(Input_Key_Cmd);//获取cmd命令字符串
        String dir = input.getInputValue(Input_Key_dir);
        if(StringUtil.isInvalid(cmd)){
            throw new LegoException("无效的shell命令",Lego_Error_Shell_Cmd_Null);
        }
        if(StringUtil.isInvalid(dir)){
            String tmpPath = TYPPC.getTYProp("upload.temp.dir");
            if(StringUtil.isValid(tmpPath)){
                dir = tmpPath;
            }else{
                dir = "/";
            }
        }
        File tmpDir = new File(dir);
        if(!tmpDir.exists()){
            tmpDir.mkdirs();
        }
        String result = execCmd(cmd,tmpDir);
        output.setOutputValue(Output_Key_Shell_Output,result);
    }

    /**
     * 执行系统命令, 返回执行结果
     * @param cmd 需要执行的命令
     * @param dir 执行命令的子进程的工作目录, null 表示和当前主进程工作目录相同
     */
    private String execCmd(String cmd, File dir) {
        StringBuilder result = new StringBuilder();

        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;

        try {
            String[] commond = {"/bin/sh","-c",cmd};
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(commond, null, dir);

            // 方法阻塞, 等待命令执行完成（成功会返回0）
            process.waitFor();

            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            // 读取输出
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }

        }catch (Exception e){
            logger.error(e);
        }finally {
            closeStream(bufrIn);
            closeStream(bufrError);

            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
        }

        // 返回执行结果
        return result.toString();
    }

    private void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
            // nothing
            }
        }
    }

}
