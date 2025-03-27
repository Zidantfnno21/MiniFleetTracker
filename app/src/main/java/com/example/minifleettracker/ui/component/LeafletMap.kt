package com.example.minifleettracker.ui.component

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.minifleettracker.viewmodel.FleetViewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LeafletMap(viewModel: FleetViewModel) {
    val context = LocalContext.current
    val vehicleData by viewModel.vehicleData.observeAsState()

    AndroidView(
        factory = {
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    allowFileAccess = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    defaultTextEncodingName = "utf-8"
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                        Log.d("WebView", "${consoleMessage?.message()}")
                        return true
                    }
                }

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        vehicleData?.let { data ->
                            evaluateJavascript(
                                """
                                initMap(${data.latitude}, ${data.longitude});
                                """.trimIndent(),
                                null
                            )
                        }
                    }
                }
                
                loadUrl("file:///android_asset/map.html")
            }
        },
        update = { webView ->
            vehicleData?.let { data ->
                webView.evaluateJavascript(
                    """
                    initMap(${data.latitude}, ${data.longitude});
                    """.trimIndent(),
                    null
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}