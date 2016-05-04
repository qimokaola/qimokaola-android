package com.example.robin.papers.demo.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by 凌子文 on 15/7/22.
 * Content 操作SD卡工具
 */
public class SDCardUtils {

    private SDCardUtils()
    {
        /** cannot be instantiated **/
        throw new UnsupportedOperationException("不能实例化");
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable()
    {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    private static void checkPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * 获取App文件目录
     *
     * @return
     */
    public static String getAppPath() {
        String appPath = getSDCardPath() + "PapersApp" + File.separator;
        checkPath(appPath);
        return appPath;
    }

    /**
     * 获取下载目录
     *
     * @return
     */
    public static String getDownloadPath() {
        String downloadPath = getAppPath() + "donwload" + File.separator;
        checkPath(downloadPath);
        return downloadPath;
    }

    /**
     * 获取缓存目录
     *
     * @return
     */
    public static String getCachePath() {
        String cachePath = getAppPath() + "cache" + File.separator;
        checkPath(cachePath);
        return cachePath;
    }

    /**
     * 获取数据库文件目录
     *
     * @return
     */
    public static String getDBPath() {
        String dbPath = getAppPath() + "database" + File.separator;
        checkPath(dbPath);
        return dbPath;
    }

    public static String getADImagePath() {
        String adImagePath = getAppPath() + "ad" + File.separator;
        checkPath(adImagePath);
        return adImagePath;
    }

    public static String getADImage() {
        return getADImagePath() + "ad.png";
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize()
    {
        if (isSDCardEnable())
        {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath)
    {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath()))
        {
            filePath = getSDCardPath();
        } else
        {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath()
    {
        return Environment.getRootDirectory().getAbsolutePath();
    }
}
