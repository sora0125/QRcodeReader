package jp.gr.java_conf.sora.qrcodereader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

/**
 * webviewでプライバシーポリシーページを表示する
 */
class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        // ツールバーをアクションバーとしてセット
        val toolbar: Toolbar = findViewById(R.id.tool_bar_privacy_policy)
        // タイトルを指定
        toolbar.title = getString(R.string.privacy_policy_title)

        // ツールバーをアクションバーとして設定
        setSupportActionBar(toolbar)
        // 戻るボタンを表示
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val myWebView: WebView = findViewById(R.id.webView_policy)
        myWebView.apply {
            // リンクタップ時に標準ブラウザを起動させないようにする
            webChromeClient = WebChromeClient()

            val url = getString(R.string.privacy_policy_url)
            loadUrl(url)
        }

        // AdMobを設定
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    /**
     * 戻るボタンタップ時に画面遷移する
     */
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}