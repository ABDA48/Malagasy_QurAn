package google.abdallah.quranproject.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DataFetchRabana extends SQLiteAssetHelper {
    public static String DB_Name="rabanna.db";
    public DataFetchRabana(Context context) {
        super(context, DB_Name, null, 1);
    }
}
