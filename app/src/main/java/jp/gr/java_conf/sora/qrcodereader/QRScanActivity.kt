package jp.gr.java_conf.sora.qrcodereader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.ads.MobileAds
import com.google.zxing.integration.android.IntentIntegrator

/**
 * QRコードスキャン画面
 */
open class QRScanActivity : AppCompatActivity() {
    private val TAG = QRScanActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate Start")
        super.onCreate(savedInstanceState)

        initQRScan()
        // Admob初期化
        MobileAds.initialize(this)
    }

    /**
     * QRコードリーダー画面の初期化
     */
    private fun initQRScan() {
        val integrator = IntentIntegrator(this)
        integrator.apply {
            // 独自アクティビティクラスを読み込む
            captureActivity = ToolbarCaptureActivity::class.java
            // 画面の回転に対応する
            setOrientationLocked(false)
            // 読み取る対象を指定（QRコード）
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            // 読み取り時の音を出さない
            setBeepEnabled(false)
            // スキャン画面を起動
            initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG,"onActivityResult Start")
        Log.d(TAG,"requestCode:$requestCode")
        Log.d(TAG,"resultCode:$resultCode")
        Log.d(TAG,"data:$data")

        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult == null) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }

        if (intentResult.contents != null) {
            // カメラで読み取った情報をインテントにセット
            val intent = Intent(this, ScanResultActivity::class.java)
            intent.putExtra(INTENT_EXTRA_SCAN_STRING, intentResult.contents)
            // DB登録処理実行のフラグ
            intent.putExtra(INTENT_EXTRA_SAVE_FLG, true)
            // 読み取り結果画面に遷移
            startActivity(intent)
            finish()
        }
    }

    companion object {
        // 読み取り結果のキー
        private const val INTENT_EXTRA_SCAN_STRING = "INTENT_EXTRA_SCAN_STRING"
        // DB登録処理実行のフラグ
        private const val INTENT_EXTRA_SAVE_FLG = "INTENT_EXTRA_SAVE_FLG"
    }
}
