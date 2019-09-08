package jp.gr.java_conf.sora.qrcodereader

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem

import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView

class ToolbarCaptureActivity : AppCompatActivity() {
    private var capture: CaptureManager? = null
    private var barcodeScannerView: CompoundBarcodeView? = null

    /**
     * MethodName : onCreate
     * Summary    : ツールバーありのスキャン画面を呼び出す
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.capture_appcompat)
        // ツールバーの設定
        val toolbar = findViewById<Toolbar>(R.id.my_awesome_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val drawable = ColorDrawable(Color.argb(250, 0, 0, 0))
        supportActionBar!!.setBackgroundDrawable(drawable)

        // スキャンレイアウト読み込み
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner)

        // スキャン実行
        capture = CaptureManager(this, barcodeScannerView!!)
        capture!!.initializeFromIntent(intent, savedInstanceState)
        capture!!.decode()
    }

    /**
     * MethodName : onCreateOptionsMenu
     * Summary    : メニューレイアウトをインフレート
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    /**
     * MethodName : onOptionsItemSelected
     * Summary    : タップされたボタンごとの処理を実行する
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // 履歴ボタン
            R.id.action_history -> {
                val intentHistory = Intent(this@ToolbarCaptureActivity, jp.gr.java_conf.sora.qrcodereader.HistoryActivity::class.java)
                // 呼び出し元判定のフラグ
                intentHistory.putExtra("moveFlg", "1")
                startActivity(intentHistory)
                finish()
                return true
            }

            // 戻るボタン
            android.R.id.home -> {
                finishAndRemoveTask()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        capture!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture!!.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return barcodeScannerView!!.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }
}
