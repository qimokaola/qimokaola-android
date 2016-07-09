package com.example.robin.papers.model;

/**
 * Created by Administrator on 16/5/3.
 */
public class DownloadedFile {

    private PaperFile file;
    private String downloadTime;

    public PaperFile getFile() {
        return file;
    }

    public void setFile(PaperFile file) {
        this.file = file;
    }

    public String getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(String downloadTime) {
        this.downloadTime = downloadTime;
    }
}
