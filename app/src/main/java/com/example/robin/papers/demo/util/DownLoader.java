package com.example.robin.papers.demo.util;

import android.app.ProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Robin on 2015/9/29.
 */
public class DownLoader {
    public static File downloadUpdate(String url, File file, ProgressDialog pd)  throws Exception{
        URL downloadURL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) downloadURL.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(5000);
        connection.setConnectTimeout(3000);
        InputStream is = connection.getInputStream();
        FileOutputStream os = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len;
        int maxLen = connection.getContentLength();
        pd.setMax(maxLen);
        int progress = 0;
        while((len = is.read(buffer)) != -1)  {
            os.write(buffer, 0, len);
            progress += len;
            pd.setProgress(progress);
        }
        os.flush();
        os.close();
        is.close();
        return file;
    }

    /**
     *
     * @param file 需要的下载文件
     * @param path 下载链接
     * @return 下载完的文件引用
     * @throws Exception 调用者处理
     */
    public static File downloadFile(File file, String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        InputStream is = connection.getInputStream();
        FileOutputStream fout = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = is.read(buffer)) != -1)  {
            fout.write(buffer, 0, len);
        }
        fout.flush();
        fout.close();
        is.close();
        return file;
    }
}
