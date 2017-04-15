package com.example.swaroop.msrit_am.webviewforsis;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.swaroop.msrit_am.R;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by swaroop on 2/6/2017.
 */

public class sisweb extends AppCompatActivity {


    String urlcheck;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviewforsis);

        webView=(WebView)findViewById(R.id.sisweb);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
               // Toast.makeText(sisweb.this,url,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //Toast.makeText(sisweb.this,url,Toast.LENGTH_LONG).show();





                webView.evaluateJavascript(
                        "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String html) {

                                urlcheck=html;
                               // if(html.contains("Notice Board"))
                               // Toast.makeText(sisweb.this,html,Toast.LENGTH_LONG).show();
                                // code here


                            }
                        });
                new group().execute();
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Webviewlink.sislink);

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            Toast.makeText(sisweb.this,urlcheck,Toast.LENGTH_SHORT).show();
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    class sisbrowser extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            Log.d(url+"-------","dfs");
            return true;
        }
    }

    class group extends AsyncTask<Object, Object, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {



        }

        @Override
        protected String doInBackground(Object... params) {


            try {
                Document document= Jsoup.parse(urlcheck);
                if(document.toString().contains("Notice Board"))
                    urlcheck="found";
            } catch (Exception e) {
                e.printStackTrace();
            }

            String s="df";
            return s ;
        }




    }
}
