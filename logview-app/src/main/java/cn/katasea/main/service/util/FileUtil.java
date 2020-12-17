package cn.katasea.main.service.util;

import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 */
public class FileUtil {

    /**
     *  压缩并导出文件
     * @param zipName 压缩为文件名 **.zip
     * @param createFilesPath 需要压缩的文件列表
     * @param response
     * @return
     * @throws IOException
     */
    public static void downloadZip(String zipName, String[] createFilesPath, HttpServletResponse response) {
        byte[] buffer = new byte[1024];
        try {
            File tmpZip = new ClassPathResource("temp").getFile();
            String strZipPath = tmpZip.getPath()+"/"+zipName;
            if (!tmpZip.exists())
                tmpZip.mkdirs();
            File tmpZipFile = new File(strZipPath);
            if (!tmpZipFile.exists())
                tmpZipFile.createNewFile();
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(strZipPath));
            File[] file1 =new File[createFilesPath.length] ;

            for(int i=0;i<createFilesPath.length;i++){
                file1[i]=new File(createFilesPath[i]);
            }
            for (int i = 0; i < file1.length; i++) {
                FileInputStream fis = new FileInputStream(file1[i]);
                out.putNextEntry(new ZipEntry(file1[i].getName()));
                int len;
                // 读入需要下载的文件的内容，打包到zip文件
                while ((len = fis.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.closeEntry();
                fis.close();
            }
            out.close();
            FileUtil.downloadFile(tmpZip.getPath(),zipName,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 以压缩文件导出
     * @param fileName
     * @param filePath
     * @param response
     */
    public static void downloadFile(String filePath, String fileName, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        BufferedInputStream fis = null;
        OutputStream toClient = null;
        try {
            File file=new File(filePath,fileName);
            // 以流的形式下载文件。
            fis = new BufferedInputStream(new FileInputStream(file.getPath()));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // 清空response
            response.reset();
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            toClient.write(buffer);
            toClient.flush();

        }
        catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if(null != fis) {
                    fis.close();
            }
            if(null != toClient) {
                toClient.close();
            }
        }
    }

    /**
     * 单个文件下载
     * @param response
     * @param filePath
     * @param filename
     */
    /**
     * @param resp
     * @param filePath      文件路径
     * @param downloadName 文件下载时名字
     */
    public static void downLoadFile(HttpServletResponse resp, String filePath, String downloadName) {
        String fileName = downloadName;
        try {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File file = new File(filePath);
        resp.reset();
        resp.setContentType("application/octet-stream");
        resp.setCharacterEncoding("utf-8");
        resp.setContentLength((int) file.length());
        resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = resp.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
