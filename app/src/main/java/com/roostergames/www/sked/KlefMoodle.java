package com.roostergames.www.sked;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class KlefMoodle extends AppCompatActivity {
    WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klef_moodle);
        myWebView = (WebView)findViewById(R.id.klefmoodlee);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("https://moodle.kluniversity.in");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myWebView!=null)
        {
            myWebView.destroy();
        }
    }
    @Override
    public void onBackPressed() {
        if(myWebView.canGoBack())
        {
            myWebView.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }
}
