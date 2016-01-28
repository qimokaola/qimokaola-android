package com.example.robin.papers.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.adapter.MyGridAdapter;
import com.example.robin.papers.demo.util.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentService2 extends Fragment{

    private MyGridView gridview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_service2,container,false);
        gridview=(MyGridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(new MyGridAdapter(getActivity()));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

}
