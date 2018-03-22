package jp.gr.java_conf.sora.qrcodereader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class QRscreenActivity extends AppCompatActivity {

    private static final String TAG = QRscreenActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscreen);

        Intent i = this.getIntent();
        // インテントから読み取った文字列を取得
        String scanStr = i.getStringExtra("scanStr");
        TextView textViewScannedData = (TextView) findViewById(R.id.textViewScannedData);
        textViewScannedData.setText(scanStr);
    }

    /**
     * バックキータップ時の処理
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG,"onKeyDown Start");
            // 遷移先画面をセット
            Intent i = new Intent(this, jp.gr.java_conf.sora.qrcodereader.CaptureActivity.class);
            // スキャン画面を表示
            startActivity(i);
            // 読み取り画面を終了
            finish();
            Log.d(TAG,"onKeyDown Success");
            return true;
        }
        Log.d(TAG,"onKeyDown Failed");
        return false;
    }
}
