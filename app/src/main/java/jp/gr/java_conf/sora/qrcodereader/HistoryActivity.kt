package jp.gr.java_conf.sora.qrcodereader

import android.content.Intent
import android.database.Cursor
import android.database.SQLException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView


class HistoryActivity : AppCompatActivity() {
    private  var historyList: ArrayList<HistoryData> = arrayListOf()

    /**
     *  MethodName : onCreate
     *  Summary    : DBから履歴を取得し、リスト表示する
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // ツールバーをアクションバーとしてセット
        val toolbar: Toolbar = findViewById(R.id.tool_bar_history)
        // タイトルを指定
        toolbar.title = "履歴"

        // ツールバーをアクションバーとして設定
        setSupportActionBar(toolbar)
        // 戻るボタンを表示
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        // AdMobを設定
        val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

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
    }

    /**
     *  MethodName : onKeyDown
     *  Summary    : バックキータップ時に画面遷移する
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 遷移フラグがあればスキャン画面へ遷移
            val getIntent = this.intent
            val scanStr = getIntent.getStringExtra("moveFlg")
            if ("1" == scanStr) {
                val intent = Intent(this@HistoryActivity, CaptureActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@HistoryActivity, QRscreenActivity::class.java)
                startActivity(intent)
            }
            finish()
            return true
        }
        return false
    }

    /**
     *  MethodName : onOptionsItemSelected
     *  Summary    : オプションメニューのリストタップ時の処理
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // 戻るボタン
            android.R.id.home -> {
                // 遷移フラグがあればスキャン画面へ遷移
                val getIntent = this.intent
                val scanStr = getIntent.getStringExtra("moveFlg")
                if ("1" == scanStr) {
                    val intent = Intent(this@HistoryActivity, CaptureActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@HistoryActivity, QRscreenActivity::class.java)
                    startActivity(intent)
                }
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
