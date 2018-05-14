package jp.gr.java_conf.sora.qrcodereader;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


/**
 * 読み取り結果画面
 */
public class QRscreenActivity extends AppCompatActivity {

    private static final String TAG = QRscreenActivity.class.getSimpleName();
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Log.d(TAG,"onCreate Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscreen);

        // ツールバーをアクションバーとしてセット
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);

        // タイトルを指定
        toolbar.setTitle("読み取り結果");


        setSupportActionBar(toolbar);

        Intent i = this.getIntent();
        // インテントから読み取った文字列を取得
        String scanStr = i.getStringExtra("scanStr");
        TextView textViewScannedData = (TextView) findViewById(R.id.textViewScannedData);
        textViewScannedData.setText(scanStr);

        // プライバシーポリシーへのリンク
        String url = "https://sites.google.com/view/sora0125-privacy-policy/";
        String htmlUrl = "<a href=\"" + url + "\">プライバシーポリシー</a>";

        TextView textViewPrivacyPolicy = (TextView) findViewById(R.id.textViewPrivacyPolicy);
        MovementMethod mMethod = LinkMovementMethod.getInstance();
        textViewPrivacyPolicy.setMovementMethod(mMethod);
        CharSequence link = fromHtml(htmlUrl);
        textViewPrivacyPolicy.setText(link);

        // AdMob
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    /**
     * ツールバーのメニュー
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem privacyPolicy = menu.findItem(R.id.action_privacy_policy);

//        privacyPolicy.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                // 遷移先画面をセット
//                Intent i = new Intent(QRscreenActivity.this, jp.gr.java_conf.sora.qrcodereader.WebViewActivity.class);
//                startActivity(i);
//                finish();
//                return false;
//            }
//        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                return super.onOptionsItemSelected(item);
            case R.id.action_privacy_policy:
                Toast toast = Toast.makeText(QRscreenActivity.this, "プライバシーポリシーへ遷移します",Toast.LENGTH_LONG);
                toast.show();
                Intent i = new Intent(QRscreenActivity.this, jp.gr.java_conf.sora.qrcodereader.WebViewActivity.class);
                startActivity(i);
                finish();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    /**
     * バックキータップ時の処理
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Log.d(TAG,"onKeyDown Start");
            // 遷移先画面をセット
            Intent i = new Intent(this, jp.gr.java_conf.sora.qrcodereader.CaptureActivity.class);
            // スキャン画面を表示
            startActivity(i);
            // 読み取り画面を終了
            finish();
            // Log.d(TAG,"onKeyDown Success");
            return true;
        }
        // Log.d(TAG,"onKeyDown Failed");
        return false;
    }

    /**
     * ハイパーリンク化メソッド
     */
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String url){
        Spanned spanned;
        // VersionがNougat（API Level 24）以上か
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(url,Html.FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml(url); //API Level 24以上では非推奨
        }
        return spanned;
    }
}
