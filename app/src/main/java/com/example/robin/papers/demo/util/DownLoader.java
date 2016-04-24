package com.example.robin.papers.demo.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Robin on 2015/9/29.
 */
public class DownLoader {

    public interface DownloadTaskCallback {
        public void onProgress(int hasWrite, int totalExpected);
        public void onSuccess();
        public void onFailure(Exception e);
        public void onInterruption();
    }


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

    public static Thread downloadPaperFile(final String url, final String destination, final DownloadTaskCallback callback) {
        return new Thread(new Runnable() {
            @Override
            public void run() {

                InputStream is = null;
                FileOutputStream os = null;
                HttpURLConnection connection = null;
                File file = null;
                try {
                    URL downloadURL = new URL(UrlUnicode.encode(url));
                    connection = (HttpURLConnection) downloadURL.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(3000);
                    connection.setDoInput(true);
                    connection.connect();
                    is = connection.getInputStream();
                    file = new File(destination);
                    os = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int len;
                    int totalLen = connection.getContentLength();
                    int progress = 0;

                    while((len = is.read(buffer)) != -1 && ! Thread.currentThread().isInterrupted())  {
                        os.write(buffer, 0, len);
                        progress += len;

                        if (callback instanceof DownloadTaskCallback) {
                            callback.onProgress(progress, totalLen);
                        }
                    }

                    os.flush();

                    if (Thread.currentThread().isInterrupted()) {

                        //若停止下载,则删除存在的下载文件

                        if (file != null && file.exists()) {
                            file.delete();
                        }

                        if (callback instanceof DownloadTaskCallback) {
                            callback.onInterruption();
                        }
                    }

                    if (callback instanceof DownloadTaskCallback && ! Thread.currentThread().isInterrupted()) {
                        callback.onSuccess();
                    }

                }catch (Exception e) {

                    if (callback instanceof DownloadTaskCallback && ! Thread.currentThread().isInterrupted()) {
                        callback.onFailure(e);
                    }

                } finally {

                    try {

                        if (os != null) {
                            os.close();
                        }

                        if (is != null) {
                            is.close();
                        }

                        if (connection != null) {
                            connection.disconnect();
                        }

                    } catch (Exception e) {

                    }
                }
            }
        });
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
