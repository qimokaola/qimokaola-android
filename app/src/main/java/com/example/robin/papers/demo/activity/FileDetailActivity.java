package com.example.robin.papers.demo.activity;

import android.os.Bundle;
import android.os.Environment;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileDetailActivity extends BaseActivity {
    public static final String Tag = "FileDetailActivityTag";

    private PaperFile mFile;
    private boolean isDownload = false;

    private Thread downloadTask;

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

    @OnClick({R.id.btn_download, R.id.btn_send, R.id.btn_cancel})
    public void btnClicked(View view) {

        switch (view.getId()) {

            case R.id.btn_download:
                LogUtils.d(Tag, "download begins");
                startDownload();
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

    private void startDownload() {
        setDownloadViewVisiablity();

        downloadTask = DownLoader.downloadPaperFile(mFile.getUrl(),
                Environment.getExternalStorageDirectory() + "/" + mFile.getName(),
                new DownLoader.DownloadTaskCallback() {
                    @Override
                    public void onProgress(final int hasWrite, final int totalExpected) {

                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {

                               pbProgress.setProgress((int)((double)hasWrite / (double)totalExpected * 100));
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

                                LogUtils.d(Tag, "error");
                            }
                        });
                    }
                });
        downloadTask.start();
    }

    private void sendToComputer() {

    }

    private void cancelDonwload() {

        //TO DO * fix the interrput error

        setDownloadViewVisiablity();
        if (downloadTask != null && downloadTask.isAlive()) {
        }
    }

    private void setDownloadViewVisiablity() {
        isDownload = !isDownload;

        btnDownload.setVisibility(isDownload ? View.INVISIBLE : View.VISIBLE);
        btnSend.setVisibility(isDownload ? View.INVISIBLE : View.VISIBLE);

        tvProgress.setVisibility(isDownload ? View.VISIBLE : View.INVISIBLE);
        pbProgress.setVisibility(isDownload ? View.VISIBLE : View.INVISIBLE);
        btnCancel.setVisibility(isDownload ? View.VISIBLE : View.INVISIBLE);
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

        imgFileIcon.setImageResource(R.drawable.document_type_pdf);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
