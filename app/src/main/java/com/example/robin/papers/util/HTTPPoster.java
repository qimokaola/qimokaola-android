package com.example.robin.papers.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by space on 15/10/25.
 */
public class HTTPPoster {
    private static final int TIMEOUT_IN_MILLIONS = 5000;
    private static final String IN_ENCODING = "utf-8";
    private static final String OUT_ENCODING = "utf-8";
    private static final int TIMEOUT = 5000;
    private static final String LOG_TAG = "HTTPPOSTER";

    /*
    * HttpRequestUtil.postHttpString("http://test/123", "a", "1", "pid", "2", "name", "someone");
    * */
    public static String postHttpString(String path, String... argList) throws Exception {
        String result = "";
        StringBuilder params = new StringBuilder();
        int i;
        for(i=0; i<argList.length/2; i++){
            params.append(URLEncoder.encode(argList[2 * i], OUT_ENCODING))
                    .append("=")
                    .append(URLEncoder.encode(argList[2*i+1], OUT_ENCODING))
                    .append("&");
        }
        params.deleteCharAt(params.length() - 1);
        System.out.println(params.toString());

        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setReadTimeout(TIMEOUT);
        connection.setConnectTimeout(TIMEOUT);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.getOutputStream().write(params.toString().getBytes());
        InputStream inStream=connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, IN_ENCODING));
        String line = "";
        StringBuilder buffer = new StringBuilder();
        while((line = reader.readLine()) != null)  {
            buffer.append(line);
        }
        result = new String(buffer.toString().getBytes(), IN_ENCODING);
        Log.v(LOG_TAG, "postHttpString " + path +" "+ params.toString());
        Log.v(LOG_TAG, "postHttpString ret:" + result);
        return result;
    }
}
