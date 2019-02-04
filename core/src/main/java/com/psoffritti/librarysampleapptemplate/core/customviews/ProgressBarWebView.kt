package com.psoffritti.librarysampleapptemplate.core.customviews

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.RelativeLayout
import java.lang.RuntimeException

internal class ProgressBarWebView(context: Context, attrs: AttributeSet?, defStyleAttr: Int): RelativeLayout(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val webView = WebView(context)
    private val progressbar = ProgressBar(context, attrs, android.R.attr.progressBarStyle)

    var onUrlClick: (String) -> Unit = { throw RuntimeException() }

    init {
        progressbar.visibility = View.GONE

        val layoutParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)

        addView(webView, RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        addView(progressbar, layoutParams)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressbar.visibility = View.VISIBLE
            }

            override fun onPageCommitVisible(view: WebView, url: String) {
                super.onPageCommitVisible(view, url)
                progressbar.visibility = View.GONE
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return handleUrl(url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    handleUrl(request.url.toString())
                else
                    return false
            }

            private fun handleUrl(url: String): Boolean {
                return if (url.startsWith("http://") || url.startsWith("https://")) {
                    onUrlClick(url)
                    true
                } else
                    false
            }
        }
    }

    fun loadUrl(url: String?) {
        webView.loadUrl(url)
    }

    fun enableJavascript(enable: Boolean) {
        webView.settings.javaScriptEnabled = enable
    }
}