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

/**
 * QRコードリーダー画面
 */
class ToolbarCaptureActivity : AppCompatActivity() {
    private var capture: CaptureManager? = null
    private var barcodeScannerView: CompoundBarcodeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.capture_appcompat)

        // ツールバーの設定
        val toolbar = findViewById<Toolbar>(R.id.my_awesome_toolbar)
        setSupportActionBar(toolbar)

        val drawable = ColorDrawable(Color.argb(250, 0, 0, 0))
        supportActionBar?.setBackgroundDrawable(drawable)

        // スキャンレイアウト読み込み
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner)

        // スキャン実行
        capture = CaptureManager(this, barcodeScannerView)
        capture?.initializeFromIntent(intent, savedInstanceState)
        capture?.decode()
    }

    /**
     * メニューレイアウトをインフレート
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    /**
     * タップされたボタンごとの処理を実行する
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // 履歴ボタン
            R.id.action_history -> {
                val intentHistory = Intent(this, HistoryActivity::class.java)
                startActivity(intentHistory)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        capture?.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture?.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return barcodeScannerView?.onKeyDown(keyCode, event) ?: false ||
                super.onKeyDown(keyCode, event)
    }
}
