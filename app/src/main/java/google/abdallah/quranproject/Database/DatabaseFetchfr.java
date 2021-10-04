package google.abdallah.quranproject.Database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseFetchfr extends SQLiteAssetHelper {
    public static String DB_Name="quran.fr.db";
    public DatabaseFetchfr(Context context) {
        super(context, DB_Name, null, 1);
    }
}
