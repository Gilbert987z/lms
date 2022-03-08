package cn.zjut.lms.util;


import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 导出excel的一些方法,可以直接粘贴使用
 */
public class ExcelUtil {
    /**
     * 商家表导出的默认样式
     * @return
     */
    public static HorizontalCellStyleStrategy defaultTenantStyles(){
        //TODO 默认样式
        //表头样式策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置表头居中对齐
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //表头前景设置淡蓝色
        // headWriteCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setBold(true);
        headWriteFont.setFontName("宋体");
        headWriteFont.setFontHeightInPoints((short) 18);
        headWriteCellStyle.setWriteFont(headWriteFont);

        //内容样式策略策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置背景颜色白色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        // 设置垂直居中为居中对齐
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置左右对齐为靠左对齐
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        // 设置单元格上下左右边框为细边框
        contentWriteCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        contentWriteCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        contentWriteCellStyle.setBorderRight(BorderStyle.MEDIUM);
        contentWriteCellStyle.setBorderTop(BorderStyle.MEDIUM);
        //创建字体对象
        WriteFont contentWriteFont = new WriteFont();
        //内容字体大小
        contentWriteFont.setFontName("宋体");
        contentWriteFont.setFontHeightInPoints((short) 16);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 初始化表格样式
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        return horizontalCellStyleStrategy;
    }

    /**
     * 从服务器上下载相关的代码放到这里
     */
    public static void export(String fileName,  HttpServletResponse response) {
        File excelFile = new File(fileName);
        response.setCharacterEncoding("UTF-8");
        String realFileName = excelFile.getName();
        response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
        response.setContentType("application/octet-stream;charset=UTF-8");
        //加上设置大小下载下来的.xlsx文件打开时才不会报“Excel 已完成文件级验证和修复。此工作簿的某些部分可能已被修复或丢弃”
        response.addHeader("Content-Length", String.valueOf(excelFile.length()));
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(realFileName.trim(), "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(excelFile));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     * 删除excel
     */
    public static void   deleteFile(String fileName){
        File file = new File(fileName);
        // 路径为文件且不为空则进行删除
        if (file.isFile()&& file.exists()) {
            file.delete();
        }
    }

}
