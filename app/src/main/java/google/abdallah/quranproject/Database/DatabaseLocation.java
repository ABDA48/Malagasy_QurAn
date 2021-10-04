package google.abdallah.quranproject.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseLocation extends SQLiteOpenHelper {
    public static String DBName="Prayer";
    public static String TableName="location";

    public DatabaseLocation(@Nullable Context context) {
        super(context, DBName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      String sql="CREATE TABLE "+TableName+"(dateGregore Text,"+"dateHijri Text,"+
                "Sunset	TEXT,"+"Fajr  TEXT,"+"Dhuhr	TEXT,"+"Asr	TEXT,"+"Maghrib	TEXT,"+"Isha TEXT,"+"Imsak	TEXT"+");";
      db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
