package com.example.robin.papers.demo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.db.DownloadDB;
import com.example.robin.papers.demo.model.PaperFile;
import com.example.robin.papers.demo.util.DownLoader;
import com.example.robin.papers.demo.util.LogUtils;
import com.example.robin.papers.demo.util.PaperFileUtils;
import com.example.robin.papers.demo.util.SDCardUtils;
import com.example.robin.papers.demo.util.ToastUtils;
import com.example.robin.papers.demo.util.UrlUnicode;

import java.io.File;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileDetailActivity extends BaseActivity {
    public static final String Tag = "FileDetailActivityTag";

    private DownloadDB downloadDB;

    private PaperFile mFile;
    private String fileName = null;
    private boolean isDownloading = false;

    private Thread downloadTask;

    @Bind(R.id.iv_exit)
    ImageView ivExit;
    @Bind(R.id.tv_delete)
    TextView tvDelete;
    @Bind(R.id.tv_title)
    TextView tvTitle;
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

    @OnClick({R.id.iv_exit, R.id.tv_delete})
    public void barItemClicked(View view) {
        switch (view.getId()) {
            case  R.id.iv_exit:
                finish();
                break;
            case  R.id.tv_delete:
                deleteDownloadedFile();
                break;
        }
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

        downloadDB = DownloadDB.getInstance(getApplicationContext());

        mFile = getIntent().getParcelableExtra("file");

        tvTitle.setText(mFile.getCourse());
        tvFileName.setText(mFile.getName());
        tvFileSize.setText(mFile.getSize());

        imgFileIcon.setImageResource(PaperFileUtils.parseImageResource(mFile.getType()));

        String type = mFile.getType().toLowerCase();
        if (Arrays.asList(new String[]{"zip", "rar", "7z"}).contains(type.toLowerCase())) {
            btnDownload.setEnabled(false);
        } else {
            btnDownload.setText(mFile.isDownload() ? "WPS打开" : "下载至手机");
            tvDelete.setVisibility(mFile.isDownload() ? View.VISIBLE : View.INVISIBLE);
        }

    }

    private void deleteDownloadedFile() {
        new AlertDialog.Builder(this)
                .setMessage("删除该文件?")
                .setCancelable(true)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        File file = new File(SDCardUtils.getDownloadPath() + downloadDB.getFileName(mFile.getUrl()));
                        if (file.exists()) {
                            file.delete();
                            LogUtils.d(Tag, "删除文件: " + file.getName());
                        }
                        downloadDB.removeDownloadInfo(mFile.getUrl());
                        LogUtils.d(Tag, "清除数据库信息: " + mFile.getUrl());

                        mFile.setDownload(false);

                        refreshDownloadState();

                        setResult(RESULT_OK);
                    }
                }).create().show();
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
                        public void onSuccess(final String successName) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    setResult(RESULT_OK);

                                    LogUtils.d(Tag, "下载完成: " + successName);
                                    fileName = successName;
                                    mFile.setName(successName);
                                    mFile.setDownload(true);

                                    downloadDB.addDownloadInfo(mFile);

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


            Uri uri = Uri.fromFile(new File(SDCardUtils.getDownloadPath() + downloadDB.getFileName(mFile.getUrl())));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);

        }
    }

    private void sendToComputer() {


        try {

            //发送paperurl到我的电脑;
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.setPackage("com.tencent.mobileqq");
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
            intent.putExtra(Intent.EXTRA_TEXT, mFile.isDownload() ? downloadDB.getFileName(mFile.getUrl()) : mFile.getName() + ": " + UrlUnicode.encode(mFile.getUrl()));
            intent.putExtra(Intent.EXTRA_TITLE, "发至电脑");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, "选择\"发送到我的电脑\""));
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"你没有安装QQ",Toast.LENGTH_LONG).show();
        }

    }

    private void cancelDonwload() {

        if (downloadTask != null && downloadTask.isAlive()) {
            downloadTask.interrupt();
            downloadTask = null;
        }
    }

    private void setDownloadViewVisiablity() {
        isDownloading = !isDownloading;

        refreshDownloadState();

        btnDownload.setVisibility(isDownloading ? View.INVISIBLE : View.VISIBLE);
        btnSend.setVisibility(isDownloading ? View.INVISIBLE : View.VISIBLE);

        tvProgress.setVisibility(isDownloading ? View.VISIBLE : View.INVISIBLE);
        pbProgress.setVisibility(isDownloading ? View.VISIBLE : View.INVISIBLE);
        btnCancel.setVisibility(isDownloading ? View.VISIBLE : View.INVISIBLE);
    }

    private void refreshDownloadState() {
        tvDelete.setVisibility(mFile.isDownload() ? View.VISIBLE : View.INVISIBLE);
        btnDownload.setText(mFile.isDownload() ? "打开文件" : "下载至手机");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
