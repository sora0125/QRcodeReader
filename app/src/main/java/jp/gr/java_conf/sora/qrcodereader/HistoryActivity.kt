package jp.gr.java_conf.sora.qrcodereader

import android.database.Cursor
import android.database.SQLException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

/**
 * 履歴画面
 */
class HistoryActivity : AppCompatActivity() {
    // 履歴リスト
    private  var historyList: ArrayList<HistoryData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // ツールバーをアクションバーとしてセット
        val toolbar: Toolbar = findViewById(R.id.tool_bar_history)
        // タイトルを指定
        toolbar.title = getString(R.string.history_toolbar_title)

        // ツールバーをアクションバーとして設定
        setSupportActionBar(toolbar)
        // 戻るボタンを表示
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val helper = QRcodeDatabaseHelper(this)
        val db = helper.readableDatabase
        var cs: Cursor? = null

        try {
            // 検索するカラムをセット
            val cols = arrayOf("url", "date")
            // SQLを実行
            cs = db.query("history", cols, null, null,
                    null, null,"date desc", null)

            // 取得結果の最初から最後までループ
            var eol: Boolean = cs.moveToFirst()
            if (eol) {
                while (eol) {
                    val historyData = HistoryData(cs.getString(0), cs.getString(1))
                    historyList.add(historyData)
                    eol = cs.moveToNext()
                }

            } else {
                Toast.makeText(this, "データがありません", Toast.LENGTH_LONG).show()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            val toast = Toast.makeText(this, "DB検索処理失敗", Toast.LENGTH_LONG)
            toast.show()
        } finally {
            cs?.close()
            db.close()
        }
        // アダプターをリストビューにセット
        val adapter = HistoryAdapter(this, historyList)
        val list: ListView = findViewById(R.id.history_list)
        list.adapter = adapter

        // AdMobを設定
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }


    /**
     * オプションメニューのリストタップ時の処理
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // 戻るボタン
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
