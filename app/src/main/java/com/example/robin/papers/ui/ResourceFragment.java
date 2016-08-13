package com.example.robin.papers.ui;


import android.content.Intent;
import android.os.Bundle;
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
import com.example.robin.papers.activity.FileFolderActivity;
import com.example.robin.papers.activity.WebViewActivity;
import com.example.robin.papers.model.PaperData;
import com.example.robin.papers.util.LogUtils;
import com.example.robin.papers.util.OkHttpClientManager;
import com.example.robin.papers.util.ToastUtils;
import com.squareup.okhttp.Request;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

/**
 * A simple {@link Fragment} subclass.
 * 主页面四个tab之一: 资源页面
 */
public class ResourceFragment extends Fragment  {

    //为方便将Fragment在Tag中改为Activity,方便LogCat的过滤
    private static final String Tag = "ResourceActivityTag";

    //文件总数据
    private PaperData mData;

    //数据适配器
    private AcademyAdapter mAdapter;

    private ImageView uploadImg;


    /**
     * Butter Knife 用法详见  http://jakewharton.github.io/butterknife/
     */
    @Bind(R.id.uploadImage_Academy)
    ImageView uploadImageAcademy;
    @Bind(R.id.lv_academy)
    ListView lvAcademy;
    @Bind(R.id.ptr_frame)
    PtrFrameLayout ptrFrame;

    @OnClick(R.id.uploadImage_Academy)
    public void clickUploadImage() {
        // TODO: 16/5/6 在这里添加打开上传网页
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resource, container, false);
        ButterKnife.bind(this, view);


        //上传资源按钮
        uploadImg = (ImageView) view.findViewById(R.id.uploadImage_Academy);
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至上传web
                Intent toWebIntent = new Intent(getActivity(), WebViewActivity.class);
                toWebIntent.putExtra("url", "http://robinchen.mikecrm.com/f.php?t=ZmhFim");
                toWebIntent.putExtra("title", "上传你的资源");
                startActivity(toWebIntent);
            }
        });

        mAdapter = new AcademyAdapter();
        lvAcademy.setAdapter(mAdapter);
        lvAcademy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FileFolderActivity.class);
                PaperData.Folders folder = mData.getFolders().get(position);
                intent.putExtra("folder", folder);
                FileFolderActivity.BasePath = mData.getBase();
                startActivity(intent);
            }
        });

        StoreHouseHeader header = new StoreHouseHeader(getActivity());
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(15));
        header.initWithString("finalexam.cn");
        header.setTextColor(R.color.black);
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                //loadPaperData();

                OkHttpClientManager.getAsyn(getResources().getString(R.string.data_url),
                        new OkHttpClientManager.ResultCallback<PaperData>() {

                            @Override
                            public void onError(Request request, Exception e) {
                                ToastUtils.showShort(getActivity(), "获取数据失败,请确认网络连接正常");

                                frame.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        frame.refreshComplete();
                                    }
                                }, 100);
                            }

                            @Override
                            public void onResponse(final PaperData response) {

                                frame.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (response != null) {

                                            if (mData == null || !mData.equals(response)) {

                                                LogUtils.d(Tag, "加载新数据");

                                                mData = response;
                                                mAdapter.notifyDataSetChanged();
                                            } else {
                                                LogUtils.d(Tag, "无最新数据,使用原本数据");
                                            }

                                        }

                                        if (ptrFrame != null) {
                                            ptrFrame.refreshComplete();
                                        }

                                    }
                                }, 1000);

                            }
                        });

            }
        });
        ptrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
              ptrFrame.autoRefresh();
            }
        }, 100);

        return view;
    }


    //加载文件数据
    private void loadPaperData() {



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

                holder = (ViewHolder) convertView.getTag();

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

