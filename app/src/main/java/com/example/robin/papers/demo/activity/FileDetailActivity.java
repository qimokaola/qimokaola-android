package com.example.robin.papers.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.model.PaperFile;
import com.example.robin.papers.demo.util.DownLoader;
import com.example.robin.papers.demo.util.LogUtils;
import com.example.robin.papers.demo.util.PaperFileUtils;
import com.example.robin.papers.demo.util.SDCardUtils;
import com.example.robin.papers.demo.util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileDetailActivity extends BaseActivity {
    public static final String Tag = "FileDetailActivityTag";


    private PaperFile mFile;
    private boolean isDownload = false;

    private Thread downloadTask;

    @Bind(R.id.back_major_activity)
    ImageView backMajorActivity;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.uploadImg_course)
    TextView uploadImgCourse;
    @Bind(R.id.img_file_icon)
    ImageView imgFileIcon;
    @Bind(R.id.tv_file_name)
    TextView tvFileName;
    @Bind(R.id.btn_download)
    Button btnDownload;
    @Bind(R.id.btn_send)
    Button btnSend;
    @Bind(R.id.pb_progress)
    ProgressBar pbProgress;
    @Bind(R.id.tv_progress)
    TextView tvProgress;
    @Bind(R.id.btn_cancel)
    ImageButton btnCancel;
    @Bind(R.id.tv_file_size)
    TextView tvFileSize;

    @OnClick(R.id.back_major_activity)
    public void goBack() {
        finish();
    }

    @OnClick({R.id.btn_download, R.id.btn_send, R.id.btn_cancel})
    public void btnClicked(View view) {

        switch (view.getId()) {

            case R.id.btn_download:
                LogUtils.d(Tag, "download begins");
                startDownloadProcess();
                break;
            case R.id.btn_send:
                LogUtils.d(Tag, "send file");
                sendToComputer();
                break;
            case R.id.btn_cancel:
                LogUtils.d(Tag, "cancel download");
                cancelDonwload();
                break;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
        ButterKnife.bind(this);

        mFile = getIntent().getParcelableExtra("file");

        tvTitle.setText(mFile.getCourse());
        tvFileName.setText(mFile.getName());
        tvFileSize.setText(mFile.getSize());

        imgFileIcon.setImageResource(PaperFileUtils.parseImageResource(mFile.getType()));

        String type = mFile.getType().toLowerCase();
        if (type.equals("zip") || type.equals("rar") || type.equals("7z")) {
            btnDownload.setEnabled(false);
        } else {
            btnDownload.setText(mFile.isDownload() ? "WPS打开" : "下载至手机");
        }

    }

    private void startDownloadProcess() {
        if (!mFile.isDownload()) {

            //未下载 执行下载进程

            setDownloadViewVisiablity();
            downloadTask = DownLoader.downloadPaperFile(mFile.getUrl(),
                    SDCardUtils.getDownloadPath() + mFile.getName(),
                    new DownLoader.DownloadTaskCallback() {
                        @Override
                        public void onProgress(final int hasWrite, final int totalExpected) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    pbProgress.setProgress((int) ((double) hasWrite / (double) totalExpected * 100));
                                    tvProgress.setText("下载中...(" + PaperFileUtils.sizeWithDouble(hasWrite / 1024.0) + "" +
                                            "/" + mFile.getSize() + ")");

                                }
                            });

                        }

                        @Override
                        public void onSuccess() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LogUtils.d(Tag, "下载完成: " + mFile.getName());
                                    mFile.setDownload(true);
                                    setDownloadViewVisiablity();
                                    downloadTask = null;
                                }
                            });
                        }

                        @Override
                        public void onFailure(Exception e) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    ToastUtils.showShort(FileDetailActivity.this, "下载发生错误");
                                    setDownloadViewVisiablity();
                                }
                            });
                        }

                        @Override
                        public void onInterruption() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pbProgress.setProgress(0);
                                    setDownloadViewVisiablity();
                                }
                            });
                        }
                    });
            downloadTask.start();
        } else {

            //已下载打开文件

        }
    }

    private void sendToComputer() {

    }

    private void cancelDonwload() {

        if (downloadTask != null && downloadTask.isAlive()) {
            downloadTask.interrupt();
            downloadTask = null;
        }
    }

    private void setDownloadViewVisiablity() {
        isDownload = !isDownload;

        btnDownload.setText(mFile.isDownload() ? "WPS打开" : "下载至手机");

        btnDownload.setVisibility(isDownload ? View.INVISIBLE : View.VISIBLE);
        btnSend.setVisibility(isDownload ? View.INVISIBLE : View.VISIBLE);

        tvProgress.setVisibility(isDownload ? View.VISIBLE : View.INVISIBLE);
        pbProgress.setVisibility(isDownload ? View.VISIBLE : View.INVISIBLE);
        btnCancel.setVisibility(isDownload ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
