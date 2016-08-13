package com.example.robin.papers.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.robin.papers.R;
import com.example.robin.papers.activity.FileDetailActivity;
import com.example.robin.papers.db.DownloadDB;
import com.example.robin.papers.model.DownloadedFile;
import com.example.robin.papers.model.PaperFile;
import com.example.robin.papers.util.LogUtils;
import com.example.robin.papers.util.PaperFileUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

//主页面4个tab之一: "已下载"页面
public class DownloadedFragment extends Fragment {

    private static final String Tag = "DownloadedActivityTag";

    private DownloadDB downloadDB;
    private DownloadedAdapter mAdapter;
    private List<DownloadedFile> mFiles;

    @Bind(R.id.lv_downloaded)
    ListView lvDownloaded;
    @Bind(R.id.iv_none_file)
    ImageView ivNoneFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        downloadDB = DownloadDB.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloaded, container, false);
        ButterKnife.bind(this, view);

        checkDownloaded();

        mFiles = downloadDB.getDownloadedFiles();

        mAdapter = new DownloadedAdapter();
        lvDownloaded.setAdapter(mAdapter);
        lvDownloaded.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), FileDetailActivity.class);
                intent.putExtra("file", mFiles.get(position).getFile());
                startActivityForResult(intent, 0);
            }
        });

        return view;
    }

    private void checkDownloaded() {
        boolean isEmpty = downloadDB.isEmpty();
        ivNoneFile.setVisibility(isEmpty ? View.VISIBLE : View.INVISIBLE);
        lvDownloaded.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LogUtils.d(Tag, "on result");

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        checkDownloaded();

        mFiles = downloadDB.getDownloadedFiles();

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class DownloadedAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public DownloadedAdapter() {
            mInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public int getCount() {
            return mFiles.size();
        }

        @Override
        public Object getItem(int position) {
            return mFiles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            DownloadedFile downloadedFile = mFiles.get(position);
            PaperFile file = downloadedFile.getFile();
            ViewHolder holder = null;

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.lv_item_downloaded, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder)convertView.getTag();

            }

            holder.imgFileType.setImageResource(PaperFileUtils.parseImageResource(file.getType()));
            holder.tvFileName.setText(file.getName());
            holder.tvCourse.setText(file.getCourse());
            holder.tvDownloadTime.setText(downloadedFile.getDownloadTime());

            return convertView;
        }

    }

    static class ViewHolder {
        @Bind(R.id.img_file_type)
        ImageView imgFileType;
        @Bind(R.id.tv_file_name)
        TextView tvFileName;
        @Bind(R.id.tv_download_time)
        TextView tvDownloadTime;
        @Bind(R.id.tv_course)
        TextView tvCourse;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
