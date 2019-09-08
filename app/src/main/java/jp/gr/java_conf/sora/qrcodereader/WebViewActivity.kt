package jp.gr.java_conf.sora.qrcodereader

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

open class WebViewActivity : AppCompatActivity() {
    /**
     *  MethodName : onCreate
     *  Summary    : webviewでプライバシーポリシーページを表示する
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        // ツールバーをアクションバーとしてセット
        val toolbar: Toolbar = findViewById(R.id.tool_bar_privacy_policy)
        // タイトルを指定
        toolbar.title = "プライバシーポリシー"

        // ツールバーをアクションバーとして設定
        setSupportActionBar(toolbar)
        // 戻るボタンを表示
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        // AdMobを設定
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

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

    /**
     *  MethodName : onSupportNavigateUp
     *  Summary    : 戻るボタンタップ時に画面遷移する
     */
    override fun onSupportNavigateUp(): Boolean {
        // 遷移先画面をセット
        val i = Intent(this, QRscreenActivity::class.java)
        startActivity(i)
        finish()
        return true
    }

}