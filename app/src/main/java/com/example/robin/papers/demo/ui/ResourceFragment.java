package com.example.robin.papers.demo.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.example.robin.papers.demo.activity.FileFolderActivity;
import com.example.robin.papers.demo.model.PaperData;
import com.example.robin.papers.demo.util.LogUtils;
import com.example.robin.papers.demo.util.OkHttpClientManager;
import com.example.robin.papers.demo.util.ToastUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.okhttp.Request;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResourceFragment extends Fragment {

    //为方便将Fragment在Tag中改为Activity,方便LogCat的过滤
    private static final String Tag = "ResourceActivityTag";

    //文件总数据
    private PaperData mData;

    //数据适配器
    private AcademyAdapter mAdapter;

    private Handler mHandler;


    /**
     * Butter Knife 用法详见  http://jakewharton.github.io/butterknife/
     */

    @Bind(R.id.uploadImage_Academy)
    ImageView uploadImgAcademy;
    @Bind(R.id.pull_refresh_list_view)
    PullToRefreshListView pullRefreshListView;

    @OnClick(R.id.uploadImage_Academy)
    public void clickUploadImage() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resource2, container, false);
        ButterKnife.bind(this, view);

        //设置数据适配器
        mAdapter = new AcademyAdapter();
        pullRefreshListView.setAdapter(mAdapter);

        //设置下拉刷新模式 只允许下拉刷新 上拉加载未需要
        pullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        //设置item点击点击监听器
        pullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FileFolderActivity.class);
                PaperData.Folders folder = mData.getFolders().get(position);
                intent.putExtra("folder", folder);
                FileFolderActivity.BasePath = mData.getBase();
                startActivity(intent);
            }
        });

        //设置刷新监听器
        pullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                loadPaperData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        if (mData == null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullRefreshListView.setRefreshing();
                }
            }, 100);
        }

        return view;
    }


    //加载文件数据
    private void loadPaperData() {

        OkHttpClientManager.getAsyn(getResources().getString(R.string.data_url),
                new OkHttpClientManager.ResultCallback<PaperData>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtils.showShort(getActivity(), "获取数据失败,请确认网络连接正常");
                    }

                    @Override
                    public void onResponse(PaperData response) {

                        if (response != null) {

                            if (mData == null || ! mData.equals(response)) {

                                LogUtils.d(Tag, "加载新数据");

                                mData = response;
                                mAdapter.notifyDataSetChanged();
                            } else {
                                LogUtils.d(Tag, "无最新数据,使用原本数据");
                            }

                        }

                        if (pullRefreshListView != null) {
                            pullRefreshListView.onRefreshComplete();
                        }

                    }
                });

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
       ButterKnife.unbind(this);
    }

    private class AcademyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mData == null) {
                return 0;
            }
            return mData.getFolders().size();
        }

        @Override
        public Object getItem(int position) {
            return mData.getFolders().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {

                convertView = View.inflate(getActivity(), R.layout.lv_item_academy, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder)convertView.getTag();

            }

            PaperData.Folders folder = mData.getFolders().get(position);
            holder.tvAcademyName.setText(folder.getName());

            return convertView;
        }
    }

    static class ViewHolder {
        @Bind(R.id.tv_academy_name)
        TextView tvAcademyName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
