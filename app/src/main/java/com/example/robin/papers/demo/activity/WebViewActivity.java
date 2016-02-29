package com.example.robin.papers.demo.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.util.FileUtils;
import com.example.robin.papers.demo.util.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class WebViewActivity extends Activity {
    private static final int FILECHOOSER_RESULTCODE = 101290;
    private WebView webView;
    private ImageView webViewBack_Iv,qq,alipay,phone;
    private TextView title_tv;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessage1;

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

        webView.setWebChromeClient(new XHSWebChromeClient());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //状态栏透明 需要在创建SystemBarTintManager 之前调用。
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //使StatusBarTintView 和 actionbar的颜色保持一致，风格统一。
            tintManager.setStatusBarTintResource(R.color.barcolor);
            // 设置状态栏的文字颜色
            tintManager.setStatusBarDarkMode(true, this);
        }

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (null == mUploadMessage1) return;
                Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                if (result == null) {
                    mUploadMessage1.onReceiveValue(null);
                    mUploadMessage1 = null;
                    return;
                }
                Log.i("UPFILE", "onActivityResult     " + result.toString());
                String path =  FileUtils.getPath(this, result);
                Log.i("UPFILE", "file path: " + path);
                if (TextUtils.isEmpty(path)) {
                    mUploadMessage1.onReceiveValue(null);
                    mUploadMessage1 = null;
                    return;
                }

                Uri uri = Uri.fromFile(new File(path));
                Log.i("UPFILE", "onActivityResult after parser uri:   " + uri.toString());
                mUploadMessage1.onReceiveValue(new Uri[]{uri});
                mUploadMessage1 = null;
            } else {
                Log.i("UPFILE", "4.1");
                if (null == mUploadMessage) return;
                Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                if (result == null) {
                    mUploadMessage.onReceiveValue(null);
                    mUploadMessage = null;
                    return;
                }
                Log.i("UPFILE", "onActivityResult    " + result.toString());
                String path =  FileUtils.getPath(this, result);
                Log.i("UPFILE", "file path: " + path);
                if (TextUtils.isEmpty(path)) {
                    mUploadMessage.onReceiveValue(null);
                    mUploadMessage = null;
                    return;
                }
                Uri uri = Uri.fromFile(new File(path));
                Log.i("UPFILE", "onActivityResult after parser uri:  " + uri.toString());
                mUploadMessage.onReceiveValue(uri);
                mUploadMessage = null;
            }
        }
    }

    public class XHSWebChromeClient extends WebChromeClient {

        // For Android 3.0-
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            Log.i("UPFILE", "in openFile Uri Callback");
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            Log.i("UPFILE", "in openFile Uri Callback has accept Type" + acceptType);
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            String type = TextUtils.isEmpty(acceptType) ? "*/*" : acceptType;
            i.setType(type);
            startActivityForResult(Intent.createChooser(i, "File Chooser"),
                    FILECHOOSER_RESULTCODE);
        }

        // For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            Log.i("UPFILE", "in openFile Uri Callback has accept Type" + acceptType + "has capture" + capture);
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            String type = TextUtils.isEmpty(acceptType) ? "*/*" : acceptType;
            i.setType(type);
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }


        //Android 5.0+
        @Override
        @SuppressLint("NewApi")
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (mUploadMessage1 != null) {
                mUploadMessage1.onReceiveValue(null);
            }
            Log.i("UPFILE", "file chooser params：" + fileChooserParams.toString());
            mUploadMessage1 = filePathCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
//            if (fileChooserParams != null && fileChooserParams.getAcceptTypes() != null
//                    && fileChooserParams.getAcceptTypes().length > 0) {
//                i.setType(fileChooserParams.getAcceptTypes()[0]);
//                Log.i("UPFILE", "file chooser params：" + fileChooserParams.getAcceptTypes()[0] );
//            } else {
//                i.setType("*/*");
//            }
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            return true;
        }
    }

}
