package com.jeronimo.swingradio

import android.app.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_blog.*

/**
 * Created by gleb on 23.12.15.
 */
class BlogFragment : Fragment() {

    private var url: String? = null
    private var host: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url = resources.getString(R.string.SR_BLOG_URL)
        host = resources.getString(R.string.SR_BLOG_HOST)
        val webViewClient = MyWebViewClient()
        val webChromeClient = WebChromeClient()
        fragment_blog_webView.setWebViewClient(webViewClient)
        fragment_blog_webView.setWebChromeClient(webChromeClient)
        fragment_blog_webView.loadUrl(url)
        fragment_blog_webView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                val webView = v as WebView

                when (keyCode) {
                    KeyEvent.KEYCODE_BACK -> if (webView.canGoBack()) {
                        webView.goBack()
                        return@OnKeyListener true
                    }
                }
            }

            false
        })
    }

    inner class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (Uri.parse(url).host.contains(host!!)) {
                return false
            }

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            view.context.startActivity(intent)
            return true
        }
    }
}
