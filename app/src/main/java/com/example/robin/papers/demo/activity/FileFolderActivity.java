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
import com.example.robin.papers.demo.db.DownloadDB;
import com.example.robin.papers.demo.model.PaperData;
import com.example.robin.papers.demo.model.PaperFile;
import com.example.robin.papers.demo.util.PaperFileUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

public class FileFolderActivity extends BaseActivity {

    public static String BasePath;

    private static final String Tag = "FileFolderActivityTag";

    private static final int GO_TO_DETAIL = 0;

    private List<PaperData.Files> mFiles;
    private List<PaperData.Folders> mFolders;
    private String mTitle;
    private String mPath;

    private DownloadDB downloadDB;

    private FileFolderAdapter mAdapter;

    @Bind(R.id.back_major_activity)
    ImageView backMajorActivity;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.uploadImg_course)
    TextView uploadImgCourse;
    @Bind(R.id.lv_file_folder)
    ListView lvFileFolder;
    @Bind(R.id.ptr_frame)
    PtrFrameLayout ptrFrame;

    @OnClick(R.id.back_major_activity)
    public void goBack() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_file);
        ButterKnife.bind(this);

        downloadDB = DownloadDB.getInstance(getApplicationContext());

        PaperData.Folders folder = getIntent().getParcelableExtra("folder");
        mTitle = folder.getName();
        mPath = folder.getChild().getPath();
        mFiles = folder.getChild().getFiles();
        mFolders = folder.getChild().getFolders();

        tvTitle.setText(mTitle);

        mAdapter = new FileFolderAdapter(this);
        lvFileFolder.setAdapter(mAdapter);
        lvFileFolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    paperFile.setDownload(downloadDB.isDownloaded(paperFile.getUrl()));

                    Intent intent = new Intent(FileFolderActivity.this, FileDetailActivity.class);
                    intent.putExtra("file", paperFile);
                    startActivityForResult(intent, GO_TO_DETAIL);
                }
            }
        });

        final StoreHouseHeader header = new StoreHouseHeader(this);
        header.setPadding(0,  PtrLocalDisplay.dp2px(15), 0, 0);
        header.setTextColor(R.color.black);
        header.initWithString("Papers");
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }
            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frame.refreshComplete();
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case GO_TO_DETAIL:

                mAdapter.notifyDataSetChanged();

                break;
        }

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
                    fileViewHolder = (FileViewHolder) convertView.getTag();
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
                fileViewHolder.tvFileDownloadTag.setVisibility(downloadDB.isDownloaded(BasePath + mPath + file.getName()) ? View.VISIBLE : View.INVISIBLE);
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
