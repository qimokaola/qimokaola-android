package com.example.robin.papers.demo.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.robin.papers.R;

public class XtfyActivity extends Activity {
    private WebView xtfyWebView;
    private ImageView xtfy_activity_back_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xtfy);

        xtfy_activity_back_iv = (ImageView) findViewById(R.id.xtfy_activity_back_iv);
        xtfyWebView = (WebView) findViewById(R.id.xtfyWebView);

        xtfy_activity_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        xtfyWebView.loadUrl("http://bbs.fzu.edu.cn/forum.php?forumlist=1&mobile=2");
        xtfyWebView.getSettings().setJavaScriptEnabled(true);
        xtfyWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                xtfyWebView.loadUrl(url);
                return true;
            }

        });

    }


}
