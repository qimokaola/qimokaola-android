package com.example.robin.papers.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.robin.papers.demo.util.PaperFileUtils;

/**
 * Created by Administrator on 16/4/23.
 */
public class PaperFile implements Parcelable {

    private String name;
    private String url;
    private String type;
    private String size;
    private String course;
    private boolean download;

    public PaperFile(PaperData.Files file, String urlPath, String course) {

        this.name = file.getName();
        this.url = urlPath + this.name;
        this.type = PaperFileUtils.typeWithFileName(this.name);
        this.size = PaperFileUtils.sizeWithDouble(Double.valueOf(file.getSize()));
        this.course = course;
        this.download = Math.random() * 100 < 50.0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.type);
        dest.writeString(this.size);
        dest.writeString(this.course);
        dest.writeByte(download ? (byte) 1 : (byte) 0);
    }

    protected PaperFile(Parcel in) {
        this.name = in.readString();
        this.url = in.readString();
        this.type = in.readString();
        this.size = in.readString();
        this.course = in.readString();
        this.download = in.readByte() != 0;
    }

    public static final Parcelable.Creator<PaperFile> CREATOR = new Parcelable.Creator<PaperFile>() {
        @Override
        public PaperFile createFromParcel(Parcel source) {
            return new PaperFile(source);
        }

        @Override
        public PaperFile[] newArray(int size) {
            return new PaperFile[size];
        }
    };
}
