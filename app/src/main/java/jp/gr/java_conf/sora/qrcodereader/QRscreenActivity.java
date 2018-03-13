package jp.gr.java_conf.sora.qrcodereader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
        //new IntentIntegrator(QRscreenActivity.this).initiateScan();
        IntentIntegrator integrator = new IntentIntegrator(QRscreenActivity.this);
        integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();

        //ボタンを取得し、タップした時の動作を指定
        Button buttonStartCamera = (Button)findViewById(R.id.buttonStartCamera);
        buttonStartCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG,"buttonStartCamera onClick Start");
                //setContentView(R.layout.activity_qrscreen);
                IntentIntegrator integrator = new IntentIntegrator(QRscreenActivity.this);
                integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data ){
        Log.d(TAG,"onActivityResult Start");
        Log.d(TAG,"requestCode" + requestCode + "resultCode" + resultCode + " data:" + data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // nullの場合
        if (intentResult == null) {
            Log.d(TAG,"Weird");
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (intentResult.getContents() == null) {
            // 戻るボタンをタップした場合
            Log.d(TAG, "Cancelled Scan");
        }else {
            // カメラで読み取った情報をラベルに表示
            Log.d(TAG, "Scanned");
            TextView textViewScannedData = (TextView) findViewById(R.id.textViewScannedData);
            textViewScannedData.setText(intentResult.getContents());
        }
    }

}
