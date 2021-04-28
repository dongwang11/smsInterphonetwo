package com.sms.app.interphone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sms.app.interphone.R;
import com.sms.app.interphone.ui.MyApplicatoin;

public class WebViewActivity extends BaseActivity {

    private WebView webView;
    private TextView web_Title;
    private ImageView img_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        setView();

        Intent intent = getIntent();
        if(intent!=null) {

            /*String titlek = intent.getStringExtra(GlobalConsts.ACTIVITY);
            String url = intent.getStringExtra(GlobalConsts.KEY_DATA);*/

        //  web_Title.setText(titlek);

            //检查网络状态

            if(MyApplicatoin.current_network_type != MyApplicatoin.NETWORK_TYPE_NONE){

                setWebSettings();
                setWebViewClient();
                setWebChromeClient();

                //调用webView的loadUrl方法 加载网页
                webView.loadUrl("http://www.baidu.com");

            }else{

                Toast.makeText(WebViewActivity.this,"无可用网络",Toast.LENGTH_LONG).show();

            }

        }
    }

    private void setView() {

        webView = (WebView) findViewById(R.id.web_view);

        web_Title = (TextView) findViewById(R.id.web_main_title);

        img_return = (ImageView) findViewById(R.id.web_main_icon);

        img_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

    }


    //设置WebChromeClient属性
    private void setWebChromeClient() {
        webView.setWebChromeClient(new WebChromeClient(){
        });
    }
    //设置WebViewClient属性
    private void setWebViewClient() {
        //webViewClient中有很多回调方法
        //可以用于与webView进行交互
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置webView的WebSettings属性
     */
    private void setWebSettings() {
        WebSettings settings = webView.getSettings();
        //设置当前webView启动javascript
        //默认为false
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        //显示缩放控制条
        //	settings.setDisplayZoomControls(true);
    }

}
