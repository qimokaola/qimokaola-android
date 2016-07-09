package com.example.robin.papers.util;

/**
 * Created by Administrator on 16/5/4.
 */
public class DispatchQueue {

    public static void  async(Runnable runnable) {

        new Thread(runnable).start();

    }

}
