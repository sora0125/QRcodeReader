package jp.gr.java_conf.sora.qrcodereader;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

public class ToolbarCaptureActivity extends AppCompatActivity {
    private CaptureManager capture;
    private CompoundBarcodeView barcodeScannerView;

    /**
     *  MethodName : onCreate
     *  Summary    : ツールバーありのスキャン画面を呼び出す
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.capture_appcompat);
        // ツールバーの設定
        Toolbar toolbar = findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("読み取り");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ColorDrawable drawable = new ColorDrawable(Color.argb(250, 0, 0, 0));
//        drawable.setAlpha(0);
        getSupportActionBar().setBackgroundDrawable(drawable);

        // スキャンレイアウト読み込み
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);

        // スキャン実行
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    /**
     *  MethodName : onCreateOptionsMenu
     *  Summary    : メニューレイアウトをインフレート
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    /**
     *  MethodName : onOptionsItemSelected
     *  Summary    : タップされたボタンごとの処理を実行する
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 履歴ボタン
            case R.id.action_history:
                Intent intentHistory = new Intent(ToolbarCaptureActivity.this, jp.gr.java_conf.sora.qrcodereader.HistoryActivity.class);
                // 呼び出し元判定のフラグ
                intentHistory.putExtra("moveFlg", "1");
                startActivity(intentHistory);
                finish();
                return true;

                // 戻るボタン
            case android.R.id.home:
                finishAndRemoveTask();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
