package com.example.robin.papers.demo.util;

import com.example.robin.papers.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Administrator on 16/4/23.
 *
 * 历年卷文件工具
 */
public class PaperFileUtils {
    private static Map<String, Integer> types = new HashMap<String, Integer>(){
        {
            //word
            put("wps", R.drawable.document_type_word);
            put("doc", R.drawable.document_type_word);
            put("docx", R.drawable.document_type_word);

            //ppt
            put("dps", R.drawable.document_type_ppt);
            put("ppt", R.drawable.document_type_ppt);
            put("pptx", R.drawable.document_type_ppt);

            //xls
            put("xls", R.drawable.document_type_xls);
            put("xlt", R.drawable.document_type_xls);
            put("et", R.drawable.document_type_xls);

            //txt
            put("txt", R.drawable.document_type_txt);
            put("rtf", R.drawable.document_type_txt);

            //压缩包
            put("zip", R.drawable.document_type_zip);
            put("rar", R.drawable.document_type_zip);
            put("7z", R.drawable.document_type_zip);

            //pdf
            put("pdf", R.drawable.document_type_pdf);

            //image
            put("png", R.drawable.document_type_img);
            put("jpg", R.drawable.document_type_img);

            //unknow
            put("unknow", R.drawable.document_type_unknow);
        }
    };

    /**
     *
     * @param size 文件大小 单位 kb
     * @return 转换后的字符串
     */
    public static String sizeWithDouble(double size) {

        DecimalFormat format = new DecimalFormat("#.##");

        String result = "";

        if (size < 1024) {
            result = format.format(size) +"KB";
        } else if (size < 1024 * 1024) {
            result = format.format(size / 1024.0) + "MB";
        } else if (size < 1024 * 1024 * 1024) {
            return format.format(size / 1024.0 / 1024.0) + "GB";
        }

        return result;
    }

    /**
     *
     * @param fileName 文件名
     * @return 文件拓展名
     */
    public static String typeWithFileName(String fileName) {

        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1, fileName.length());
    }

    /**
     * 文件后缀获得相应资源文件id
     *
     * @param type
     * @return
     */
    public static int parseImageResource(String type) {
        if (types.containsKey(type.toLowerCase())) {
            return (int)types.get(type.toLowerCase());
        } else {
            return (int)types.get("unknow");
        }
    }

}
