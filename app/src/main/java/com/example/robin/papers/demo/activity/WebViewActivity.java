package com.example.robin.papers.demo.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robin.papers.R;

public class WebViewActivity extends Activity {
    private WebView webView;
    private ImageView webViewBack_Iv,qq,alipay,phone;
    private TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        webViewBack_Iv = (ImageView) findViewById(R.id.xtfy_activity_back_iv);
        webView = (WebView) findViewById(R.id.xtfyWebView);


        qq = (ImageView) findViewById(R.id.qq_web);
        alipay = (ImageView) findViewById(R.id.alipay_web);
        phone = (ImageView) findViewById(R.id.phone_web);

        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击复制字符串到粘贴板
                ClipboardManager copy = (ClipboardManager) WebViewActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText("596954885");
                Toast.makeText(getApplicationContext(), "QQ号已复制到粘帖板", Toast.LENGTH_SHORT).show();
            }
        });

        alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击复制字符串到粘贴板
                ClipboardManager copy = (ClipboardManager) WebViewActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText("596954885@qq.com");
                Toast.makeText(getApplicationContext(), "支付宝帐号已复制到粘帖板", Toast.LENGTH_SHORT).show();
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击复制字符串到粘贴板
                ClipboardManager copy = (ClipboardManager) WebViewActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText("13067402180");
                Toast.makeText(getApplicationContext(), "手机号已复制到粘帖板", Toast.LENGTH_SHORT).show();
            }
        });

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

        if (titles.equals("代抢实验")){
            alipay.setVisibility(View.VISIBLE);
        }
        if (titles.equals("器材正装")){
            phone.setVisibility(View.VISIBLE);
        }

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
