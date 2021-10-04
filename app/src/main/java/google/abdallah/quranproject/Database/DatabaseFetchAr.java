package google.abdallah.quranproject.Database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseFetchAr extends SQLiteAssetHelper {
    public static String DB_Name="quran.ar.db";
    public DatabaseFetchAr(Context context) {
        super(context, DB_Name, null, 1);
    }
}
