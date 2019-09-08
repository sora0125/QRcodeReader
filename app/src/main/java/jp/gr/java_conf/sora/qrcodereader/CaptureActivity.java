package jp.gr.java_conf.sora.qrcodereader;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class CaptureActivity extends AppCompatActivity {
    private static final String TAG = CaptureActivity.class.getSimpleName();

    /**
     *  MethodName : onCreate
     *  Summary    : QRコードスキャン画面を起動する
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Log.d(TAG,"onCreate Start");
        super.onCreate(savedInstanceState);
        IntentIntegrator integrator = new IntentIntegrator(this);
        // 独自アクティビティクラスを読み込む
        integrator.setCaptureActivity(ToolbarCaptureActivity.class);
        // 画面の回転に対応する
        integrator.setOrientationLocked(false);
        // 読み取る対象を指定（QRコード）
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        // 読み取り時の音を出さない
        integrator.setBeepEnabled(false);
        // スキャン画面を起動
        integrator.initiateScan();

        MobileAds.initialize(this);
    }

    /**
     *  MethodName : onActivityResult
     *  Summary    : 読み取った情報をインテントにセットし、読み取り結果画面に遷移する
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data ){
        // Log.d(TAG,"onActivityResult Start");
        // Log.d(TAG,"requestCode" + requestCode + "resultCode" + resultCode + " data:" + data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // nullの場合
        if (intentResult == null) {
            // Log.d(TAG,"Weird");
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (intentResult.getContents() == null) {
            // 戻るボタンをタップした場合
            // Log.d(TAG, "Cancelled Scan");
            finishAndRemoveTask();
        }else {
            // カメラで読み取った情報をインテントにセット
            // Log.d(TAG, "Scanned");
            Intent i = new Intent(this, jp.gr.java_conf.sora.qrcodereader.QRscreenActivity.class);
            i.putExtra("scanStr", intentResult.getContents());
            // DB登録処理実行のフラグ
            i.putExtra("moveFlg", "1");
            // 読み取り結果画面に遷移
            startActivity(i);
            finish();
        }
    }
}
