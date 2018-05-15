package jp.gr.java_conf.sora.qrcodereader

import android.content.Intent
import android.database.Cursor
import android.database.SQLException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val data: ArrayList<String> = ArrayList()

        var helper = QRcodeDatabaseHelper(this)
        val db = helper.readableDatabase
        var cs: Cursor? = null

        try {
            val cols = arrayOf("url", "date")
            cs = db.query("history", cols, null, null,
                    null, null,"date desc", null)
            var eol: Boolean = cs.moveToFirst()
            if (eol) {
                while (eol) {
                    data.add(cs.getString(1)+ " " + cs.getString(0))
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
        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        val list: ListView = findViewById(R.id.history_list)
        list.adapter = adapter
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
