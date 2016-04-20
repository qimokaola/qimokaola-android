package com.example.robin.papers.demo.model;

import java.util.List;

/**
 * Created by Administrator on 16/4/20.
 */
public class PaperData {


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

    public static class Files {
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
    }

    public static class Folders {
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

        public static class Child {
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
        }
    }
}
