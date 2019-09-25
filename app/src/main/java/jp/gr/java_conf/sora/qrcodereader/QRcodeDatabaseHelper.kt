package jp.gr.java_conf.sora.qrcodereader

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * データベースのヘルパークラス
 */
class QRcodeDatabaseHelper(context: Context)
    : SQLiteOpenHelper(context, DBNAME, null, VERSION) {

    /**
     * テーブルを作成
     */
    override fun onCreate(db: SQLiteDatabase?) {
        val strSQL = "CREATE TABLE history (url TEXT, date TEXT,PRIMARY KEY(url, date))"
        db?.execSQL(strSQL)
    }

    /**
     * データベースがバージョンアップした時にテーブルを作り直す
     */    
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val strSQL = "DROP TABLE IF EXISTS history"
        db?.execSQL(strSQL)
        onCreate(db)
    }

    companion object {
        // データベース名
        private const val DBNAME: String = "qrcodedb.sqlite"
        // バージョン
        private const val VERSION: Int = 1
    }
}