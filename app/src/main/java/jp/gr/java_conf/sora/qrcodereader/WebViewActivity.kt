package jp.gr.java_conf.sora.qrcodereader

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebView

open class WebViewActivity : AppCompatActivity() {
    /**
     *  MethodName : onCreate
     *  Summary    : webviewでプライバシーポリシーページを表示する
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        val myWebView: WebView = findViewById(R.id.webView_policy)
        // リンクタップ時に標準ブラウザを起動させないようにする
        myWebView.webChromeClient = WebChromeClient()

        val url = "https://sites.google.com/view/sora0125-privacy-policy/"
        myWebView.loadUrl(url)
    }

    /**
     *  MethodName : onKeyDown
     *  Summary    : バックキータップ時に画面遷移する
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 遷移先画面をセット
            val i = Intent(this, QRscreenActivity::class.java)
            startActivity(i)
            finish()
            return true
        }
        return false
    }

}