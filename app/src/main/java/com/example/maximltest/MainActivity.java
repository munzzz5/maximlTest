package com.example.maximltest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    public static WebView webview;
    private final String CHANNEL_ID = "webViewChannel";
    receiving recv;
    String urlToLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webview = (WebView) findViewById(R.id.webView);
        //setContentView(webview);
        webview.setWebViewClient(new myclient());
        Intent in=new Intent(this, listenerService.class);
        startForegroundService(in);
        recv=new receiving();
        registerReceiver(recv,new IntentFilter("URL-COPIED"));
        //urlToLoad="www.google.com";
        //urlToLoad=checkUrl(urlToLoad);
        //webview.loadUrl(urlToLoad);



    }
    public static void runUrl(String url1)
    {
        webview.loadUrl(url1);
    }
    public String checkUrl(String urlToLoad)
    {
        if(urlToLoad.contains("www."))
        {
            if(urlToLoad.contains("http://")) {
                return urlToLoad;
            }
            else
            {
                urlToLoad="http://"+urlToLoad;
                return urlToLoad;
            }

        }
        else
        {
            if(urlToLoad.contains("http://")) {
                urlToLoad=urlToLoad.replace("http://","http://www.");
                return urlToLoad;
            }
            else
            {
                urlToLoad="http://www."+urlToLoad;
                return urlToLoad;


            }

        }
    }
}

class myclient extends WebViewClient
{
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
    }
}
class receiving extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {
        String url=intent.getStringExtra("URL");
        MainActivity.runUrl(url);
        System.out.println(url+"RECVD IN ACTIVITY");
    }
}
