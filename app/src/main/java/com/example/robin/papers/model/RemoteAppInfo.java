package com.example.robin.papers.model;

/**
 * Created by Administrator on 16/5/4.
 */
public class RemoteAppInfo {

    /**
     * display : true
     * adversion : 1
     * appversion : 3.2.0
     */

    private boolean display;
    private int adversion;
    private String appversion;

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public int getAdversion() {
        return adversion;
    }

    public void setAdversion(int adversion) {
        this.adversion = adversion;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }
}
