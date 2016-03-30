package com.example.robin.papers.demo.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Robin on 2016/2/3.
 */
public class UrlUnicode {
    public static String encode(String url){

        //对路径进行编码 然后替换路径中所有空格 编码之后空格变成“+”而空格的编码表示是“%20” 所以将所有的“+”替换成“%20”就可以了
        try {
            url = URLEncoder.encode(url, "utf-8").replaceAll("\\+", "%20");
            //编码之后的路径中的“/”也变成编码的东西了 所有还有将其替换回来 这样才是完整的路径
            url = url.replaceAll("%3A", ":").replaceAll("%2F", "/");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
