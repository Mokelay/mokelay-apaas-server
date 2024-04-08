package com.greatbee.vendor.lego.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.greatbee.base.util.StringUtil;
import com.greatbee.api.lego.LegoException;
import com.greatbee.core.lego.system.TYPPC;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

/**
 * VendorUtil
 *
 * @author xiaobc
 * @date 18/9/10
 */
public class VendorUtil {

    private static final Logger logger = Logger.getLogger(VendorUtil.class);

    private static final long ERROR_LEGO_NET_RESOURCE_INVALIDATE = 300054L;
    private static final long ERROR_LEGO_NET_RESOURCE_SAVE_ERROR = 300055L;

    /**
     * 获取上传临时目录
     * @return
     */
    public static String getTmpPath() {
        String locaPath = TYPPC.getTYProp("upload.temp.dir");
        if (StringUtil.isInvalid(locaPath)) {
            locaPath = "/";
        }
        File tmpDir = new File(locaPath);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        return locaPath;
    }

    /**
     * 根据网络地址，保存文件到临时目录
     * @param resourceUrl
     * @param fileName
     * @return
     * @throws LegoException
     */
    public static JSONObject getLocalFileFromNetFile(String resourceUrl,String fileName) throws LegoException {
        JSONObject result = new JSONObject();
        try {
            URL url = new URL(resourceUrl);

            URLConnection uc = url.openConnection();
            //如果文件名没有传，直接从header中获取，4是微信获取文件名的header方式
            if(StringUtil.isInvalid(fileName)){
                fileName = uc.getHeaderField(4);
                if(StringUtil.isInvalid(fileName)){
                    throw new LegoException("资源地址无效,无法获取文件名",ERROR_LEGO_NET_RESOURCE_INVALIDATE);
                }
                fileName = URLDecoder.decode(fileName.substring(fileName.indexOf("filename=") + 9), "UTF-8");
                fileName = fileName.replaceAll("\"", "");//去掉文件名前后的引号
            }
            String contentType = uc.getHeaderField("Content-Type");//获取contentType

            logger.info("[NetResourceToLocalFile] fileName="+fileName);
            BufferedInputStream in = new BufferedInputStream(uc.getInputStream());
            //先将微信媒体文件存到本地
            String locaPath = VendorUtil.class.getResource("/").getPath();
            String tmpPath = TYPPC.getTYProp("upload.temp.dir");
            if(StringUtil.isValid(tmpPath)){
                File tmpFile = new File(tmpPath);
                if(!tmpFile.exists()){
                    tmpFile.mkdirs();
                }
                locaPath = tmpPath;
            }

            String filePath = locaPath+fileName;
            logger.info("[NetResourceToLocalFile] filePath="+filePath);
            File file =  new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = null;
            buffer = new byte[2048];
            int length = in.read(buffer);
            while (length != -1) {
                out.write(buffer, 0, length);
                length = in.read(buffer);
            }
            in.close();
            out.close();

            result.put("filePath", filePath);
            result.put("contentType",contentType);

        }  catch (IOException e) {
            e.printStackTrace();
            throw new LegoException("转存网络资源失败",ERROR_LEGO_NET_RESOURCE_SAVE_ERROR);
        }
        return result;
    }

    /**
     * 根据文件后缀获取文件的contentType
     * @param suffix
     * @return
     */
    public static String findContentTypeBySuffix(String suffix){
        String constJson = "{\n" +
                "    \"apk\": \"application/vnd.android.package-archive\",\n" +
                "    \"3gp\": \"video/3gpp\",\n" +
                "    \"ai\": \"application/postscript\",\n" +
                "    \"aif\": \"audio/x-aiff\",\n" +
                "    \"aifc\": \"audio/x-aiff\",\n" +
                "    \"aiff\": \"audio/x-aiff\",\n" +
                "    \"asc\": \"text/plain\",\n" +
                "    \"atom\": \"application/atom+xml\",\n" +
                "    \"au\": \"audio/basic\",\n" +
                "    \"avi\": \"video/x-msvideo\",\n" +
                "    \"bcpio\": \"application/x-bcpio\",\n" +
                "    \"bin\": \"application/octet-stream\",\n" +
                "    \"bmp\": \"image/bmp\",\n" +
                "    \"cdf\": \"application/x-netcdf\",\n" +
                "    \"cgm\": \"image/cgm\",\n" +
                "    \"class\": \"application/octet-stream\",\n" +
                "    \"cpio\": \"application/x-cpio\",\n" +
                "    \"cpt\": \"application/mac-compactpro\",\n" +
                "    \"csh\": \"application/x-csh\",\n" +
                "    \"css\": \"text/css\",\n" +
                "    \"dcr\": \"application/x-director\",\n" +
                "    \"dif\": \"video/x-dv\",\n" +
                "    \"dir\": \"application/x-director\",\n" +
                "    \"djv\": \"image/vnd.djvu\",\n" +
                "    \"djvu\": \"image/vnd.djvu\",\n" +
                "    \"dll\": \"application/octet-stream\",\n" +
                "    \"dmg\": \"application/octet-stream\",\n" +
                "    \"dms\": \"application/octet-stream\",\n" +
                "    \"doc\": \"application/msword\",\n" +
                "    \"dtd\": \"application/xml-dtd\",\n" +
                "    \"dv\": \"video/x-dv\",\n" +
                "    \"dvi\": \"application/x-dvi\",\n" +
                "    \"dxr\": \"application/x-director\",\n" +
                "    \"eps\": \"application/postscript\",\n" +
                "    \"etx\": \"text/x-setext\",\n" +
                "    \"exe\": \"application/octet-stream\",\n" +
                "    \"ez\": \"application/andrew-inset\",\n" +
                "    \"flv\": \"video/x-flv\",\n" +
                "    \"gif\": \"image/gif\",\n" +
                "    \"gram\": \"application/srgs\",\n" +
                "    \"grxml\": \"application/srgs+xml\",\n" +
                "    \"gtar\": \"application/x-gtar\",\n" +
                "    \"gz\": \"application/x-gzip\",\n" +
                "    \"hdf\": \"application/x-hdf\",\n" +
                "    \"hqx\": \"application/mac-binhex40\",\n" +
                "    \"htm\": \"text/html\",\n" +
                "    \"html\": \"text/html\",\n" +
                "    \"ice\": \"x-conference/x-cooltalk\",\n" +
                "    \"ico\": \"image/x-icon\",\n" +
                "    \"ics\": \"text/calendar\",\n" +
                "    \"ief\": \"image/ief\",\n" +
                "    \"ifb\": \"text/calendar\",\n" +
                "    \"iges\": \"model/iges\",\n" +
                "    \"igs\": \"model/iges\",\n" +
                "    \"jnlp\": \"application/x-java-jnlp-file\",\n" +
                "    \"jp2\": \"image/jp2\",\n" +
                "    \"jpe\": \"image/jpeg\",\n" +
                "    \"jpeg\": \"image/jpeg\",\n" +
                "    \"jpg\": \"image/jpeg\",\n" +
                "    \"js\": \"application/x-javascript\",\n" +
                "    \"kar\": \"audio/midi\",\n" +
                "    \"latex\": \"application/x-latex\",\n" +
                "    \"lha\": \"application/octet-stream\",\n" +
                "    \"lzh\": \"application/octet-stream\",\n" +
                "    \"m3u\": \"audio/x-mpegurl\",\n" +
                "    \"m4a\": \"audio/mp4a-latm\",\n" +
                "    \"m4p\": \"audio/mp4a-latm\",\n" +
                "    \"m4u\": \"video/vnd.mpegurl\",\n" +
                "    \"m4v\": \"video/x-m4v\",\n" +
                "    \"mac\": \"image/x-macpaint\",\n" +
                "    \"man\": \"application/x-troff-man\",\n" +
                "    \"mathml\": \"application/mathml+xml\",\n" +
                "    \"me\": \"application/x-troff-me\",\n" +
                "    \"mesh\": \"model/mesh\",\n" +
                "    \"mid\": \"audio/midi\",\n" +
                "    \"midi\": \"audio/midi\",\n" +
                "    \"mif\": \"application/vnd.mif\",\n" +
                "    \"mov\": \"video/quicktime\",\n" +
                "    \"movie\": \"video/x-sgi-movie\",\n" +
                "    \"mp2\": \"audio/mpeg\",\n" +
                "    \"mp3\": \"audio/mpeg\",\n" +
                "    \"mp4\": \"video/mp4\",\n" +
                "    \"mpe\": \"video/mpeg\",\n" +
                "    \"mpeg\": \"video/mpeg\",\n" +
                "    \"mpg\": \"video/mpeg\",\n" +
                "    \"mpga\": \"audio/mpeg\",\n" +
                "    \"ms\": \"application/x-troff-ms\",\n" +
                "    \"msh\": \"model/mesh\",\n" +
                "    \"mxu\": \"video/vnd.mpegurl\",\n" +
                "    \"nc\": \"application/x-netcdf\",\n" +
                "    \"oda\": \"application/oda\",\n" +
                "    \"ogg\": \"application/ogg\",\n" +
                "    \"ogv\": \"video/ogv\",\n" +
                "    \"pbm\": \"image/x-portable-bitmap\",\n" +
                "    \"pct\": \"image/pict\",\n" +
                "    \"pdb\": \"chemical/x-pdb\",\n" +
                "    \"pdf\": \"application/pdf\",\n" +
                "    \"pgm\": \"image/x-portable-graymap\",\n" +
                "    \"pgn\": \"application/x-chess-pgn\",\n" +
                "    \"pic\": \"image/pict\",\n" +
                "    \"pict\": \"image/pict\",\n" +
                "    \"png\": \"image/png\",\n" +
                "    \"pnm\": \"image/x-portable-anymap\",\n" +
                "    \"pnt\": \"image/x-macpaint\",\n" +
                "    \"pntg\": \"image/x-macpaint\",\n" +
                "    \"ppm\": \"image/x-portable-pixmap\",\n" +
                "    \"ppt\": \"application/vnd.ms-powerpoint\",\n" +
                "    \"ps\": \"application/postscript\",\n" +
                "    \"qt\": \"video/quicktime\",\n" +
                "    \"qti\": \"image/x-quicktime\",\n" +
                "    \"qtif\": \"image/x-quicktime\",\n" +
                "    \"ra\": \"audio/x-pn-realaudio\",\n" +
                "    \"ram\": \"audio/x-pn-realaudio\",\n" +
                "    \"ras\": \"image/x-cmu-raster\",\n" +
                "    \"rdf\": \"application/rdf+xml\",\n" +
                "    \"rgb\": \"image/x-rgb\",\n" +
                "    \"rm\": \"application/vnd.rn-realmedia\",\n" +
                "    \"roff\": \"application/x-troff\",\n" +
                "    \"rtf\": \"text/rtf\",\n" +
                "    \"rtx\": \"text/richtext\",\n" +
                "    \"sgm\": \"text/sgml\",\n" +
                "    \"sgml\": \"text/sgml\",\n" +
                "    \"sh\": \"application/x-sh\",\n" +
                "    \"shar\": \"application/x-shar\",\n" +
                "    \"silo\": \"model/mesh\",\n" +
                "    \"sit\": \"application/x-stuffit\",\n" +
                "    \"skd\": \"application/x-koan\",\n" +
                "    \"skm\": \"application/x-koan\",\n" +
                "    \"skp\": \"application/x-koan\",\n" +
                "    \"skt\": \"application/x-koan\",\n" +
                "    \"smi\": \"application/smil\",\n" +
                "    \"smil\": \"application/smil\",\n" +
                "    \"snd\": \"audio/basic\",\n" +
                "    \"so\": \"application/octet-stream\",\n" +
                "    \"spl\": \"application/x-futuresplash\",\n" +
                "    \"src\": \"application/x-wais-source\",\n" +
                "    \"sv4cpio\": \"application/x-sv4cpio\",\n" +
                "    \"sv4crc\": \"application/x-sv4crc\",\n" +
                "    \"svg\": \"image/svg+xml\",\n" +
                "    \"swf\": \"application/x-shockwave-flash\",\n" +
                "    \"t\": \"application/x-troff\",\n" +
                "    \"tar\": \"application/x-tar\",\n" +
                "    \"tcl\": \"application/x-tcl\",\n" +
                "    \"tex\": \"application/x-tex\",\n" +
                "    \"texi\": \"application/x-texinfo\",\n" +
                "    \"texinfo\": \"application/x-texinfo\",\n" +
                "    \"tif\": \"image/tiff\",\n" +
                "    \"tiff\": \"image/tiff\",\n" +
                "    \"tr\": \"application/x-troff\",\n" +
                "    \"tsv\": \"text/tab-separated-values\",\n" +
                "    \"txt\": \"text/plain\",\n" +
                "    \"ustar\": \"application/x-ustar\",\n" +
                "    \"vcd\": \"application/x-cdlink\",\n" +
                "    \"vrml\": \"model/vrml\",\n" +
                "    \"vxml\": \"application/voicexml+xml\",\n" +
                "    \"wav\": \"audio/x-wav\",\n" +
                "    \"wbmp\": \"image/vnd.wap.wbmp\",\n" +
                "    \"wbxml\": \"application/vnd.wap.wbxml\",\n" +
                "    \"webm\": \"video/webm\",\n" +
                "    \"wml\": \"text/vnd.wap.wml\",\n" +
                "    \"wmlc\": \"application/vnd.wap.wmlc\",\n" +
                "    \"wmls\": \"text/vnd.wap.wmlscript\",\n" +
                "    \"wmlsc\": \"application/vnd.wap.wmlscriptc\",\n" +
                "    \"wmv\": \"video/x-ms-wmv\",\n" +
                "    \"wrl\": \"model/vrml\",\n" +
                "    \"xbm\": \"image/x-xbitmap\",\n" +
                "    \"xht\": \"application/xhtml+xml\",\n" +
                "    \"xhtml\": \"application/xhtml+xml\",\n" +
                "    \"xls\": \"application/vnd.ms-excel\",\n" +
                "    \"xml\": \"application/xml\",\n" +
                "    \"xpm\": \"image/x-xpixmap\",\n" +
                "    \"xsl\": \"application/xml\",\n" +
                "    \"xslt\": \"application/xslt+xml\",\n" +
                "    \"xul\": \"application/vnd.mozilla.xul+xml\",\n" +
                "    \"xwd\": \"image/x-xwindowdump\",\n" +
                "    \"xyz\": \"chemical/x-xyz\",\n" +
                "    \"zip\": \"application/zip\"\n" +
                "}";
        JSONObject obj = JSON.parseObject(constJson);
        if(obj.containsKey(suffix)){
            return obj.getString(suffix);
        }else{
            return obj.getString("jpeg");
        }
    }

}
