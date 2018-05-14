package jp.gr.java_conf.sora.qrcodereader

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

open class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_privacy_policy)

        val myWebView: WebView = findViewById(R.id.webView_policy)
        // リンクタップ時に標準ブラウザを起動させないようにする
        //myWebView.webViewClient = WebViewClient()
        myWebView.webChromeClient = WebChromeClient()

        val url = "https://sites.google.com/view/sora0125-privacy-policy/"
        myWebView.loadUrl(url)
        //myWebView.settings.javaScriptEnabled = true
    }

    /**
     * バックキータップ時の処理
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 遷移先画面をセット
            val i = Intent(this, QRscreenActivity::class.java)
            // 遷移先画面を表示
            startActivity(i)
            // 現在の画面を終了
            finish()
            return true
        }
        return false
    }

}