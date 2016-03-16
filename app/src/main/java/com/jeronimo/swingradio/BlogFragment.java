package com.jeronimo.swingradio;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by gleb on 23.12.15.
 */
public class BlogFragment extends Fragment {

    WebView webView;
    private String url;
    private String host;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        url = getResources().getString(R.string.SR_BLOG_URL);
        host = getResources().getString(R.string.SR_BLOG_HOST);
        webView = (WebView)view.findViewById(R.id.fragment_blog_webView);
        WebViewClient webViewClient = new MyWebViewClient();
        WebChromeClient webChromeClient = new WebChromeClient();
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);
        webView.loadUrl(url);
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    WebView webView = (WebView) v;

                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (webView.canGoBack()) {
                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }

                return false;
            }
        });
        return view;
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(Uri.parse(url).getHost().contains(host)) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }
    }
}
