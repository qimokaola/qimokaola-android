package com.example.robin.papers.demo.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.util.HTTPPoster;

import org.json.JSONObject;

public class KuaiDiActivity extends Activity {
    private ImageView kuai_di_back_iv;
    private EditText kuai_di_message,kuai_di_name,kuai_di_phone,kuai_di_adress;
    private Button kuai_di_submit;
    private String message,name,phone,address;
    private LinearLayout kuai_di_kefu,kuai_di_zhifubao_ll;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuai_di);

        handler = new Handler();

        kuai_di_back_iv = (ImageView) findViewById(R.id.kuai_di_back_iv);
        kuai_di_kefu = (LinearLayout) findViewById(R.id.kuai_di_kefu);
        kuai_di_zhifubao_ll = (LinearLayout) findViewById(R.id.kuai_di_zhifubao_ll);

        kuai_di_message = (EditText) findViewById(R.id.kuai_di_message);
        kuai_di_name = (EditText) findViewById(R.id.kuai_di_name);
        kuai_di_phone = (EditText) findViewById(R.id.kuai_di_phone);
        kuai_di_submit = (Button) findViewById(R.id.kuai_di_submit);
        kuai_di_adress = (EditText) findViewById(R.id.kuai_di_adress);

        kuai_di_zhifubao_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击复制字符串到粘贴板
                ClipboardManager copy = (ClipboardManager) KuaiDiActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText("596954885@qq.com");
                Toast.makeText(getApplicationContext(),"支付宝帐号已复制到粘帖板",Toast.LENGTH_SHORT).show();
            }
        });

        kuai_di_kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击复制字符串到粘贴板
                ClipboardManager copy = (ClipboardManager) KuaiDiActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText("2984859939");
                Toast.makeText(getApplicationContext(),"客服帐号已复制到粘帖板",Toast.LENGTH_SHORT).show();
            }
        });

        kuai_di_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kuai_di_submit.setEnabled(false);
                kuai_di_submit.setText("提交ing,请安候");
                message = kuai_di_message.getText().toString();
                name = kuai_di_name.getText().toString();
                phone = kuai_di_phone.getText().toString();
                address = kuai_di_adress.getText().toString();


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        final String ret;
                        String json="";
                        JSONObject tObj = new JSONObject();
                        try{
                            tObj.put("name", name);
                            tObj.put("msg", message);
                            tObj.put("addr", address);
                            tObj.put("phone", phone);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        json = tObj.toString();
                        try {
                            ret = HTTPPoster.postHttpString("http://fzupapers.sinaapp.com/express.php", "json", json);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (ret.equals("Succeed"))
                                    {
                                        finish();
                                        startActivity(new Intent(KuaiDiActivity.this, SubmitSuccessActivity.class));
                                        Toast.makeText(KuaiDiActivity.this, "提交成功,快递正向你赶来~~", Toast.LENGTH_LONG).show();
                                    } else {
                                        String tipMsg = ret;
                                        if (tipMsg.length() == 0) {
                                            tipMsg = "网络错误";
                                        }
                                        finish();
                                        startActivity(new Intent(KuaiDiActivity.this, SubmitFailActivity.class));
                                        Toast.makeText(KuaiDiActivity.this, "提交失败: "+tipMsg, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    startActivity(new Intent(KuaiDiActivity.this, SubmitFailActivity.class));
                                    Toast.makeText(KuaiDiActivity.this, "网络错误", Toast.LENGTH_LONG).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });


        kuai_di_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
