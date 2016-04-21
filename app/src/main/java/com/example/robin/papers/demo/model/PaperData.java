package com.example.robin.papers.demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 16/4/20.
 */
public class PaperData implements Parcelable {


    private String path;
    private String base;
    /**
     * name : 农村社会学复习.doc
     * size : 48.0
     */

    private List<Files> files;

    private List<Folders> folders;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public List<Files> getFiles() {
        return files;
    }

    public void setFiles(List<Files> files) {
        this.files = files;
    }

    public List<Folders> getFolders() {
        return folders;
    }

    public void setFolders(List<Folders> folders) {
        this.folders = folders;
    }

    public static class Files implements Parcelable {
        private String name;
        private String size;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.size);
        }

        public Files() {
        }

        protected Files(Parcel in) {
            this.name = in.readString();
            this.size = in.readString();
        }

        public static final Creator<Files> CREATOR = new Creator<Files>() {
            @Override
            public Files createFromParcel(Parcel source) {
                return new Files(source);
            }

            @Override
            public Files[] newArray(int size) {
                return new Files[size];
            }
        };
    }

    public static class Folders implements Parcelable {
        private String name;

        private Child child;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Child getChild() {
            return child;
        }

        public void setChild(Child child) {
            this.child = child;
        }

        public static class Child implements Parcelable {
            private String path;
            private List<Files> files;

            private List<Folders> folders;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public List<Files> getFiles() {
                return files;
            }

            public void setFiles(List<Files> files) {
                this.files = files;
            }

            public List<Folders> getFolders() {
                return folders;
            }

            public void setFolders(List<Folders> folders) {
                this.folders = folders;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.path);
                dest.writeTypedList(files);
                dest.writeTypedList(folders);
            }

            public Child() {
            }

            protected Child(Parcel in) {
                this.path = in.readString();
                this.files = in.createTypedArrayList(Files.CREATOR);
                this.folders = in.createTypedArrayList(Folders.CREATOR);
            }

            public static final Creator<Child> CREATOR = new Creator<Child>() {
                @Override
                public Child createFromParcel(Parcel source) {
                    return new Child(source);
                }

                @Override
                public Child[] newArray(int size) {
                    return new Child[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeParcelable(this.child, flags);
        }

        public Folders() {
        }

        protected Folders(Parcel in) {
            this.name = in.readString();
            this.child = in.readParcelable(Child.class.getClassLoader());
        }

        public static final Creator<Folders> CREATOR = new Creator<Folders>() {
            @Override
            public Folders createFromParcel(Parcel source) {
                return new Folders(source);
            }

            @Override
            public Folders[] newArray(int size) {
                return new Folders[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.base);
        dest.writeList(this.files);
        dest.writeList(this.folders);
    }

    public PaperData() {
    }

    protected PaperData(Parcel in) {
        this.path = in.readString();
        this.base = in.readString();
        this.files = new ArrayList<Files>();
        in.readList(this.files, Files.class.getClassLoader());
        this.folders = new ArrayList<Folders>();
        in.readList(this.folders, Folders.class.getClassLoader());
    }

    public static final Parcelable.Creator<PaperData> CREATOR = new Parcelable.Creator<PaperData>() {
        @Override
        public PaperData createFromParcel(Parcel source) {
            return new PaperData(source);
        }

        @Override
        public PaperData[] newArray(int size) {
            return new PaperData[size];
        }
    };
}
