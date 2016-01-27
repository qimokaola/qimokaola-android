package com.example.robin.papers.demo.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.util.HTTPPoster;

import org.json.JSONObject;

//购买抢实验服务的Activity
public class BuyExpActivity extends Activity {

    private ImageView back;
    private EditText xuehao,mima,dijizhou,shiyanming,xingqiji,shangxiawu,dijipi;
    private String xuehaos,mimas,dijizhous,shiyanmings,xingqijis,shangxiawus,dijipis;
    private Button putBtn;
    private LinearLayout zhifubao_ll;
    private Handler handler;
    private WebView shiyanweb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_exp);

        //找控件
        back = (ImageView) findViewById(R.id.buy_exp_back_iv);
        xuehao = (EditText) findViewById(R.id.buy_exp_xuehao);
        mima = (EditText) findViewById(R.id.buy_exp_mima);

        dijizhou = (EditText) findViewById(R.id.buy_exp_dijizhou);
        shiyanming = (EditText) findViewById(R.id.buy_exp_shiyanming);
        xingqiji = (EditText) findViewById(R.id.buy_exp_xingqiji);
        shangxiawu = (EditText) findViewById(R.id.buy_exp_shangxiawu);
        dijipi = (EditText) findViewById(R.id.buy_exp_dijipi);
        putBtn = (Button) findViewById(R.id.buy_exp_putBtn);
        zhifubao_ll = (LinearLayout) findViewById(R.id.zhifubao_ll);




//        shiyanweb = (WebView) findViewById(R.id.shiyanweb);

//        shiyanweb.loadUrl("http://robinchen.mikecrm.com/f.php?t=CFGtnt");
//        shiyanweb.getSettings().setJavaScriptEnabled(true);
//        shiyanweb.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                shiyanweb.loadUrl(url);
//                return true;
//            }
//
//        });



        handler = new Handler();

        //点击复制支付宝帐号
        zhifubao_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击复制字符串到粘贴板
                ClipboardManager copy = (ClipboardManager) BuyExpActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText("596954885@qq.com");
                Toast.makeText(getApplicationContext(),"支付宝帐号已复制到粘帖板",Toast.LENGTH_SHORT).show();
            }
        });

        putBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putBtn.setEnabled(false);
                putBtn.setText("提交ing,请安候");

                //学号
                xuehaos = xuehao.getText().toString();
                //密码
                mimas = mima.getText().toString();
                //第几周
                dijizhous = dijizhou.getText().toString();
                //实验名
                shiyanmings = shiyanming.getText().toString();
                //星期几
                xingqijis = xingqiji.getText().toString();
                //上下午
                shangxiawus = shangxiawu.getText().toString();
                //第几批
                dijipis = dijipi.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        final String ret;
                        String json="";
                        JSONObject tObj = new JSONObject();
                        try{
                            tObj.put("xh", xuehaos);
                            tObj.put("pw", mimas);
                            tObj.put("week", dijizhous);
                            tObj.put("shiyanming", shiyanmings);
                            tObj.put("weekday", xingqijis);
                            tObj.put("shangxia", shangxiawus);
                            tObj.put("pici", dijipis);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        json = tObj.toString();
                        try {
                            ret = HTTPPoster.postHttpString("http://fzupapers.sinaapp.com/shiyan.php", "json", json);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (ret.equals("Succeed"))
                                    {
                                        finish();
                                        startActivity(new Intent(BuyExpActivity.this, SubmitSuccessActivity.class));
                                        Toast.makeText(BuyExpActivity.this, "提交成功", Toast.LENGTH_LONG).show();

                                    } else {
                                        String tipMsg = ret;
                                        if (tipMsg.length() == 0) {
                                            tipMsg = "网络错误";
                                        }
                                        finish();
                                        startActivity(new Intent(BuyExpActivity.this, SubmitFailActivity.class));
                                        Toast.makeText(BuyExpActivity.this, "提交失败: "+tipMsg, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    startActivity(new Intent(BuyExpActivity.this, SubmitFailActivity.class));
                                    Toast.makeText(BuyExpActivity.this, "提交失败: 网络错误", Toast.LENGTH_LONG).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }).start();

                //然后post这7个string到服务器
                //回调成功跳转到Submit_success页
//                startActivity(new Intent(BuyExpActivity.this,SubmitSuccessActivity.class));
//                finish();

                //否则跳转到Submit_fail页 并toast显示失败原因
//                startActivity(new Intent(BuyExpActivity.this,SubmitFailActivity.class));
//                finish();





            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
