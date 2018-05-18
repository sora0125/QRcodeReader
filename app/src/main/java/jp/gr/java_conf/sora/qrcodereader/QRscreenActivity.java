package jp.gr.java_conf.sora.qrcodereader;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * 読み取り結果画面
 */
public class QRscreenActivity extends AppCompatActivity {

    private static final String TAG = QRscreenActivity.class.getSimpleName();
    private AdView mAdView;
    private QRcodeDatabaseHelper helper = null;

    /**
     *  MethodName : onCreate
     *  Summary    : リストの項目のレイアウトを設定する
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Log.d(TAG,"onCreate Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscreen);

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = findViewById(R.id.tool_bar_qrscreen);
        // タイトルを指定
        toolbar.setTitle("読み取り結果");
        // ツールバーをアクションバーとして設定
        setSupportActionBar(toolbar);

        // 戻るボタンを表示
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent i = this.getIntent();
        // インテントから読み取った文字列を取得
        String scanStr = i.getStringExtra("scanStr");
        TextView textViewScannedData = findViewById(R.id.textViewScannedData);
        textViewScannedData.setText(scanStr);

        // DB登録するためのフラグを取得
        String moveFlg = i.getStringExtra("moveFlg");
        // フラグが立っていたらDBへ登録処理を実行
        if ("1".equals(moveFlg)) {
            helper = new QRcodeDatabaseHelper(this);

            //日付のフォーマットを設定
            Calendar cl = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            SQLiteDatabase db = helper.getWritableDatabase();
            // トランザクション開始
            db.beginTransaction();
            try {
                //SQL文を作成し、クエリを実行する
                String strSQL = "INSERT INTO history(url, date) VALUES('" + scanStr + "','" + df.format(cl.getTime()) + "')";
                db.execSQL(strSQL);
                // 成功フラグを立てる
                db.setTransactionSuccessful();
                Toast toast = Toast.makeText(this, "DB登録処理成功", Toast.LENGTH_LONG);
                toast.show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(this, "DB登録処理失敗", Toast.LENGTH_LONG);
                toast.show();
            } finally {
                // トランザクション終了
                db.endTransaction();
                db.close();
            }
        } else {
            SQLiteOpenHelper helper = new QRcodeDatabaseHelper(this);
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cs = null;

            try {
                // 検索するカラムをセット
                String[] cols = {"url", "date"};
                // SQLを実行
                cs = db.query("history", cols, null, null, null,
                        null,null, null);

                // 取得結果の先頭に移動
                Boolean eol = cs.moveToFirst();
                if (eol) {
                    // 取得結果の末尾に移動
                    cs.moveToLast();
                    String strUrl = cs.getString(0);
                    // 取得データをセット
                    textViewScannedData.setText(strUrl);

                } else {
                    Toast.makeText(this, "データがありません", Toast.LENGTH_LONG).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Toast toast = Toast.makeText(this, "DB検索処理失敗", Toast.LENGTH_LONG);
                toast.show();
            } finally {
                cs.close();
                db.close();
            }
        }

        // AdMobを設定
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    /**
     *  MethodName : onCreateOptionsMenu
     *  Summary    : メニューレイアウトをインフレートする
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *  MethodName : onOptionsItemSelected
     *  Summary    : オプションメニューのリストタップ時にそれぞれの画面に遷移する
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // メニュー：履歴
            case R.id.action_history:
                Intent intentHistory = new Intent(QRscreenActivity.this, jp.gr.java_conf.sora.qrcodereader.HistoryActivity.class);
                startActivity(intentHistory);
                finish();
                return true;
                // メニュー：プライバシーポリシー
            case R.id.action_privacy_policy:
//                Toast toast = Toast.makeText(QRscreenActivity.this, "プライバシーポリシーへ遷移します",Toast.LENGTH_LONG);
//                toast.show();
                Intent intentPrivacyPolicy = new Intent(QRscreenActivity.this, jp.gr.java_conf.sora.qrcodereader.WebViewActivity.class);
                startActivity(intentPrivacyPolicy);
                finish();
                return true;
            // 戻るボタン
            case android.R.id.home:
                Intent i = new Intent(this, jp.gr.java_conf.sora.qrcodereader.CaptureActivity.class);
                startActivity(i);
                finish();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    /**
     *  MethodName : onKeyDown
     *  Summary    : バックキータップ時に画面遷移する
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Log.d(TAG,"onKeyDown Start");
            // 遷移先画面をセット
            Intent i = new Intent(this, jp.gr.java_conf.sora.qrcodereader.CaptureActivity.class);
            startActivity(i);
            finish();
            // Log.d(TAG,"onKeyDown Success");
            return true;
        }
        // Log.d(TAG,"onKeyDown Failed");
        return false;
    }
}
