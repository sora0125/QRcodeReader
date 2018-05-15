package jp.gr.java_conf.sora.qrcodereader

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class QRcodeDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DBNAME, null, VERSION) {
    companion object {
        private const val DBNAME: String = "qrcodedb.sqlite"
        private const val VERSION: Int = 1
    }

    override fun onOpen(db: SQLiteDatabase) {
            super.onOpen(db)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val strSQL = "CREATE TABLE history (url TEXT, date TEXT,PRIMARY KEY(url, date))"
        db?.execSQL(strSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val strSQL = "DROP TABLE IF EXISTS history"
        db?.execSQL(strSQL)
        onCreate(db)
    }


}