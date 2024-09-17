package com.my.arkku.main;

import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.my.arkku.R;


public class MainActivity extends AppCompatActivity  {

    public static WebView mywebView;
    JavaScriptInterface jsInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mywebView=(WebView) findViewById(R.id.webview);
        mywebView.setWebViewClient(new WebViewClient());


        // Create a new instance JavaScriptInterface and add it to the WebView
        jsInterface = new JavaScriptInterface(this, MainActivity.this);
        mywebView.addJavascriptInterface(jsInterface, "Android");

        // Load HTML to the Webview element
        mywebView.loadUrl("file:///android_asset/index.html");
        WebSettings webSettings=mywebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        mywebView.setWebContentsDebuggingEnabled(true); // debuggausta varten

        // JavaScript ConsoleLoggings
        mywebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("consolelog", consoleMessage.message() + " -- From line " +
                        consoleMessage.lineNumber() + " of " + consoleMessage.sourceId());
                return true;
            }
        });

        mywebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                jsInterface.createUIElements();
            }
        });
    }
}
