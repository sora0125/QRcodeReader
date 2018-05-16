package jp.gr.java_conf.sora.qrcodereader

import android.content.Intent
import android.database.Cursor
import android.database.SQLException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast

class HistoryActivity : AppCompatActivity() {
    private  var historyList: ArrayList<HistoryData> = arrayListOf()

    /**
     *  MethodName : onCreate
     *  Summary    : webviewでプライバシーポリシーページを表示する
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        var helper = QRcodeDatabaseHelper(this)
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
            // 遷移先画面をセット
            val i = Intent(this@HistoryActivity, QRscreenActivity::class.java)
            startActivity(i)
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
        // メニュー：履歴
            R.id.action_history -> {
                val intentHistory =
                        Intent(this@HistoryActivity, jp.gr.java_conf.sora.qrcodereader.HistoryActivity::class.java)
                startActivity(intentHistory)
                finish()
                return true
            }
        // メニュー：プライバシーポリシー
            R.id.action_privacy_policy -> {
                val intentPrivacyPolicy =
                        Intent(this@HistoryActivity, jp.gr.java_conf.sora.qrcodereader.WebViewActivity::class.java)
                startActivity(intentPrivacyPolicy)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
