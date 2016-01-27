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

public class KuaiDiDaiLingActivity extends Activity {
    private ImageView kddl2_activity_back_iv;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuai_di_dai_ling);

        kddl2_activity_back_iv = (ImageView) findViewById(R.id.kddl2_activity_back_iv);
        webView = (WebView) findViewById(R.id.kddlWebView);



        kddl2_activity_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webView.loadUrl("http://form.mikecrm.com/f.php?t=39tiOz");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

        });


    }


}
