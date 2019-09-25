package jp.gr.java_conf.sora.qrcodereader

import android.content.Intent
import android.database.Cursor
import android.database.SQLException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import android.widget.Toast

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

import java.text.SimpleDateFormat
import java.util.*


/**
 * 読み取り結果画面
 */
class ScanResultActivity : AppCompatActivity() {
    private val TAG = ScanResultActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate Start")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscreen)

        // ツールバーをアクションバーとしてセット
        val toolbar = findViewById<Toolbar>(R.id.tool_bar_qrscreen)
        // タイトルを指定
        toolbar.title = getString(R.string.title_name)
        // ツールバーをアクションバーとして設定
        setSupportActionBar(toolbar)

        // 戻るボタンを表示
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val intent = this.intent
        // インテントから読み取った文字列を取得
        val scanStr = intent.getStringExtra(INTENT_EXTRA_SCAN_STRING) ?: ""
        val textViewScannedData = findViewById<TextView>(R.id.textViewScannedData)
        textViewScannedData.text = scanStr

        // DB登録するためのフラグを取得
        val saveFlg = intent.getBooleanExtra(INTENT_EXTRA_SAVE_FLG, false)
        // フラグが立っていたらDBへ登録処理を実行
        if (saveFlg) {
            //日付のフォーマットを設定
            val calendar = Calendar.getInstance()
            val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPANESE)

            // DBのヘルパークラスのインスタンス生成
            val helper = QRcodeDatabaseHelper(this)
            val db = helper.writableDatabase

            // トランザクション開始
            db.beginTransaction()
            try {
                //SQL文を作成し、クエリを実行する
                val strSQL = "INSERT INTO history(url, date) VALUES('" + scanStr + "','" + format.format(calendar.time) + "')"
                db.execSQL(strSQL)
                // 成功フラグを立てる
                db.setTransactionSuccessful()
            } catch (e: SQLException) {
                e.printStackTrace()

            } finally {
                // トランザクション終了
                db.endTransaction()
                db.close()
            }
        } else {
            val helper = QRcodeDatabaseHelper(this)
            val db = helper.readableDatabase
            var cs: Cursor? = null

            try {
                // 検索するカラムをセット
                val cols = arrayOf("url", "date")
                // SQLを実行
                cs = db.query("history", cols, null, null, null, null, null, null)

                // 取得結果の先頭に移動
                val eol = cs!!.moveToFirst()
                if (eol) {
                    // 取得結果の末尾に移動
                    cs.moveToLast()
                    val strUrl = cs.getString(0)
                    // 取得データをセット
                    textViewScannedData.text = strUrl

                }
            } catch (e: SQLException) {
                e.printStackTrace()
                val toast = Toast.makeText(this, "DB検索処理失敗", Toast.LENGTH_LONG)
                toast.show()
            } finally {
                cs?.close()
                db.close()
            }
        }

        // AdMobを設定
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    /**
     * メニューレイアウトをインフレートする
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * オプションメニューのリストタップ時にそれぞれの画面に遷移する
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // メニュー：履歴
            R.id.action_history -> {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                return true
            }
            // メニュー：プライバシーポリシー
            R.id.action_privacy_policy -> {
                val intent = Intent(this, WebViewActivity::class.java)
                startActivity(intent)
                return true
            }
            // 戻るボタン
            android.R.id.home -> {
                val intent = Intent(this, QRScanActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, QRScanActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        // 読み取り結果のキー
        private const val INTENT_EXTRA_SCAN_STRING = "INTENT_EXTRA_SCAN_STRING"
        // DB登録処理実行のフラグ
        private const val INTENT_EXTRA_SAVE_FLG = "INTENT_EXTRA_SAVE_FLG"
    }
}
