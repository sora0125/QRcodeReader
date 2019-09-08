package jp.gr.java_conf.sora.qrcodereader

import android.content.Intent
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import android.widget.Toast

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

import java.text.SimpleDateFormat
import java.util.Calendar


/**
 * 読み取り結果画面
 */
class QRscreenActivity : AppCompatActivity() {

    /**
     * MethodName : onCreate
     * Summary    : リストの項目のレイアウトを設定する
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Log.d(TAG,"onCreate Start");
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscreen)

        // ツールバーをアクションバーとしてセット
        val toolbar = findViewById<Toolbar>(R.id.tool_bar_qrscreen)
        // タイトルを指定
        toolbar.title = "読み取り結果"
        // ツールバーをアクションバーとして設定
        setSupportActionBar(toolbar)

        // 戻るボタンを表示
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        val i = this.intent
        // インテントから読み取った文字列を取得
        val scanStr = i.getStringExtra("scanStr")
        val textViewScannedData = findViewById<TextView>(R.id.textViewScannedData)
        textViewScannedData.text = scanStr

        // DB登録するためのフラグを取得
        val moveFlg = i.getStringExtra("moveFlg")
        // フラグが立っていたらDBへ登録処理を実行
        if ("1" == moveFlg) {
            val helper = QRcodeDatabaseHelper(this)

            //日付のフォーマットを設定
            val cl = Calendar.getInstance()
            val df = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

            val db = helper.writableDatabase
            // トランザクション開始
            db.beginTransaction()
            try {
                //SQL文を作成し、クエリを実行する
                val strSQL = "INSERT INTO history(url, date) VALUES('" + scanStr + "','" + df.format(cl.time) + "')"
                db.execSQL(strSQL)
                // 成功フラグを立てる
                db.setTransactionSuccessful()
                //                Toast toast = Toast.makeText(this, "DB登録処理成功", Toast.LENGTH_LONG);
                //                toast.show();
            } catch (e: SQLException) {
                e.printStackTrace()
                //                Toast toast = Toast.makeText(this, "DB登録処理失敗", Toast.LENGTH_LONG);
                //                toast.show();
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
     * MethodName : onCreateOptionsMenu
     * Summary    : メニューレイアウトをインフレートする
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * MethodName : onOptionsItemSelected
     * Summary    : オプションメニューのリストタップ時にそれぞれの画面に遷移する
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // メニュー：履歴
            R.id.action_history -> {
                val intentHistory = Intent(this@QRscreenActivity, jp.gr.java_conf.sora.qrcodereader.HistoryActivity::class.java)
                startActivity(intentHistory)
                finish()
                return true
            }
            // メニュー：プライバシーポリシー
            R.id.action_privacy_policy -> {
                //                Toast toast = Toast.makeText(QRscreenActivity.this, "プライバシーポリシーへ遷移します",Toast.LENGTH_LONG);
                //                toast.show();
                val intentPrivacyPolicy = Intent(this@QRscreenActivity, jp.gr.java_conf.sora.qrcodereader.WebViewActivity::class.java)
                startActivity(intentPrivacyPolicy)
                finish()
                return true
            }
            // 戻るボタン
            android.R.id.home -> {
                val i = Intent(this, jp.gr.java_conf.sora.qrcodereader.CaptureActivity::class.java)
                startActivity(i)
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /**
     * MethodName : onKeyDown
     * Summary    : バックキータップ時に画面遷移する
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Log.d(TAG,"onKeyDown Start");
            // 遷移先画面をセット
            val i = Intent(this, jp.gr.java_conf.sora.qrcodereader.CaptureActivity::class.java)
            startActivity(i)
            finish()
            // Log.d(TAG,"onKeyDown Success");
            return true
        }
        // Log.d(TAG,"onKeyDown Failed");
        return false
    }

    companion object {
        private val TAG = QRscreenActivity::class.java.simpleName
    }
}
