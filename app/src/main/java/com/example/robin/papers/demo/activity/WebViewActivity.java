package com.example.robin.papers.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robin.papers.R;

public class WebViewActivity extends Activity {
    private WebView webView;
    private ImageView webViewBack_Iv;
    private TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xtfy);

        webViewBack_Iv = (ImageView) findViewById(R.id.xtfy_activity_back_iv);
        webView = (WebView) findViewById(R.id.xtfyWebView);
        title_tv = (TextView) findViewById(R.id.webview_title);

        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        //支持H5页面
        webView.getSettings().setDomStorageEnabled(true);

//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView.getSettings().setLoadWithOverviewMode(true);

        //网页支持缩放
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        //网页设置屏幕自适应
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);


        String urls = getIntent().getStringExtra("url");
        String titles = getIntent().getStringExtra("title");

        title_tv.setText(titles);

        webViewBack_Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView.loadUrl(urls);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

        });

    }


}
