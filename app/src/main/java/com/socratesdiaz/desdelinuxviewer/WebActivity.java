package com.socratesdiaz.desdelinuxviewer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by socratesdiaz on 9/12/16.
 */
public class WebActivity extends Activity {
    private WebView webView;
    public static String WEB_URL_EXTRA = "WEB_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        String url = getIntent().getStringExtra(WEB_URL_EXTRA);

        webView.loadUrl(url);
    }

}
