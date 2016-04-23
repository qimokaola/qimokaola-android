package com.example.robin.papers.demo.util;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 16/4/23.
 *
 * 历年卷文件工具
 */
public class PaperFileUtils {

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

    public static String typeWithFileName(String fileName) {

        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1, fileName.length());
    }

}
