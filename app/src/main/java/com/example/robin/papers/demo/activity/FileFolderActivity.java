package com.example.robin.papers.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.model.PaperData;
import com.example.robin.papers.demo.model.PaperFile;
import com.example.robin.papers.demo.util.PaperFileUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileFolderActivity extends BaseActivity {

    public static String BasePath;

    private static final String Tag = "FileFolderActivityTag";

    private List<PaperData.Files> mFiles;
    private List<PaperData.Folders> mFolders;
    private String mTitle;
    private String mPath;

    private FileFolderAdapter mAdapter;

    @Bind(R.id.back_major_activity)
    ImageView backMajorActivity;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.uploadImg_course)
    TextView uploadImgCourse;
    @Bind(R.id.pull_refresh_list_view)
    PullToRefreshListView pullRefreshListView;

    @OnClick(R.id.back_major_activity)
    public void goBack() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_file);
        ButterKnife.bind(this);
        PaperData.Folders folder = getIntent().getParcelableExtra("folder");
        mTitle = folder.getName();
        mPath = folder.getChild().getPath();
        mFiles = folder.getChild().getFiles();
        mFolders = folder.getChild().getFolders();

        tvTitle.setText(mTitle);

        mAdapter = new FileFolderAdapter(this);
        pullRefreshListView.setAdapter(mAdapter);
        pullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pullRefreshListView.onRefreshComplete();
                            }
                        });

                    }
                }).start();

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        pullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (isPositionInFolders(position)) {

                    PaperData.Folders folder = mFolders.get(position);
                    Intent intent = new Intent(FileFolderActivity.this, FileFolderActivity.class);
                    intent.putExtra("folder", folder);
                    startActivity(intent);

                } else {

                    PaperData.Files file = mFiles.get(position - mFolders.size());
                    PaperFile paperFile = new PaperFile(file, BasePath + mPath, mTitle);

                    Intent intent = new Intent(FileFolderActivity.this, FileDetailActivity.class);
                    intent.putExtra("file", paperFile);
                    startActivityForResult(intent, 0);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class FileFolderAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public static final int FolderType = 0;
        public static final int FileType = 1;

        public FileFolderAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getItemViewType(int position) {
            return isPositionInFolders(position) ? FolderType : FileType;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public int getCount() {
            return mFiles.size() + mFolders.size();
        }

        @Override
        public Object getItem(int position) {
            if (isPositionInFolders(position)) {
                return mFolders.get(position);
            } else {
                return mFiles.get(position - mFolders.size());
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            int type = getItemViewType(position);

            FileViewHolder fileViewHolder = null;
            FolderViewHolder folderViewHolder = null;

            if (convertView == null) {

                if (type == FolderType) {

                    convertView = mInflater.inflate(R.layout.lv_item_folder, null);
                    folderViewHolder = new FolderViewHolder(convertView);
                    convertView.setTag(folderViewHolder);

                } else {

                    convertView = mInflater.inflate(R.layout.lv_item_file, null);
                    fileViewHolder = new FileViewHolder(convertView);
                    convertView.setTag(fileViewHolder);
                }
            } else {

                if (type == FolderType) {
                    folderViewHolder = (FolderViewHolder) convertView.getTag();
                } else {
                    fileViewHolder = (FileViewHolder)convertView.getTag();
                }

            }

            if (type == FolderType) {

                PaperData.Folders folder = mFolders.get(position);

                folderViewHolder.tvFolderName.setText(folder.getName());

            } else {

                PaperData.Files file = mFiles.get(position - mFolders.size());

                fileViewHolder.tvFileName.setText(file.getName());
                fileViewHolder.tvFileSize.setText(PaperFileUtils.sizeWithDouble(Double.valueOf(file.getSize())));
                fileViewHolder.imgFileType.setImageResource(PaperFileUtils.parseImageResource(PaperFileUtils.typeWithFileName(file.getName())));
            }

            return convertView;
        }
    }

    //判断当期索引是否处于文件夹中
    private boolean isPositionInFolders(int position) {
        return position < mFolders.size() && mFolders.size() != 0;
    }

    static class FolderViewHolder {
        @Bind(R.id.tv_folder_name)
        TextView tvFolderName;

        FolderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class FileViewHolder {
        @Bind(R.id.img_file_type)
        ImageView imgFileType;
        @Bind(R.id.tv_file_name)
        TextView tvFileName;
        @Bind(R.id.tv_file_size)
        TextView tvFileSize;
        @Bind(R.id.tv_file_download_tag)
        TextView tvFileDownloadTag;

        FileViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
