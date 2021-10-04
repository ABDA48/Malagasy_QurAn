package google.abdallah.quranproject.Prayer;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import google.abdallah.quranproject.Activity.PrayerActivity;
import google.abdallah.quranproject.Compass.GPSTracker;
import google.abdallah.quranproject.Database.DatabaseLocation;
import google.abdallah.quranproject.Model.PrayerModel;

import static google.abdallah.quranproject.Adapter.PrayerSettingDialogue.Keymethode;
import static google.abdallah.quranproject.Adapter.PrayerSettingDialogue.keyVolume;
import static google.abdallah.quranproject.Adapter.PrayerSettingDialogue.nameVolume;
import static google.abdallah.quranproject.Database.DatabaseLocation.TableName;

public class PlayerService extends Service {
    GPSTracker gps;
    Geocoder geocoder ;
    DatabaseLocation databaseLocation;
    SQLiteDatabase liteDatabase;
    String variable;int id=0; String dateHijir;
    List<String>datas;
    private static final String TAG = "prayer";
    String tag_json_obj = "json_obj_req";
    SharedPreferences preferences;public static String name="Gps",KeyLatitude="latitude",Keylongitude="longitude",KeyLocation="location",Keyvariable="keyboolean";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        geocoder=new Geocoder(this, Locale.getDefault());
        databaseLocation=new DatabaseLocation(this);
        liteDatabase=databaseLocation.getWritableDatabase();
        preferences=getSharedPreferences(name,Context.MODE_PRIVATE);
        variable=preferences.getString(keyVolume,"null");
        int years[]={2020,2021,2022};
        if (CheckConnection()) {
           for (int y=0;y<years.length;y++){
               for (int m = 0; m < 12; m++) {
                   insertPrayer(m, years[y]);
               }
           }
        }else{
            Toast.makeText(this,"Please fix your connection",Toast.LENGTH_LONG).show();
        }
        return START_NOT_STICKY;
    }

    public void insertPrayer(final int m, final int y){
datas=new ArrayList<>();

        Log.d("T------>",findLongLat(m,y));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, findLongLat(m,y), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ContentValues values;
                    int n=monthNumber(m,y);
                    for (int i=0;i<n;i++) {

                        JSONObject object = response.getJSONArray("data").getJSONObject(i);
                        JSONObject timings = object.getJSONObject("timings");
                        String fajr = timings.getString("Fajr");
                        String Dhuhr = timings.getString("Dhuhr");
                        String Ansr = timings.getString("Asr");
                        String Maghrib = timings.getString("Maghrib");
                        String Isha = timings.getString("Isha");
                        String Sun = timings.getString("Sunrise");
                        String imsak = timings.getString("Imsak");
                        JSONObject gregorian = object.getJSONObject("date").getJSONObject("gregorian");
                        String date=gregorian.getString("date");
                        JSONObject Hijiri = object.getJSONObject("date").getJSONObject("hijri");
                        String day=Hijiri.getString("day");
                        String mo=Hijiri.getJSONObject("month").getString("en");
                        String year=Hijiri.getString("year");
                        values = new ContentValues();
                        values.put("dateGregore",date);
                        values.put("dateHijri",day+" "+mo+" "+year);
                        values.put("Sunset",Sun);
                        values.put("Fajr",fajr);
                        values.put("Dhuhr",Dhuhr);
                        values.put("Asr",Ansr);
                        values.put("Maghrib",Maghrib);
                        values.put("Isha",Isha);
                        values.put("Imsak",imsak);
                        long l= liteDatabase.insert(TableName,null,values);
                        if (l<0){
                            Toast.makeText(PlayerService.this, "It's not inseted", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.d("Valuse-->","inserted "+values.getAsString("dateGregore"));
                        }
                    }
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString(Keyvariable,"finished");
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error-->: " + error.getMessage());
                // hide the progress dialog
                Toast.makeText(PlayerService.this,"Connection problem", Toast.LENGTH_SHORT).show();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public int monthNumber(int m,int y){
        m++;
        if (m==4 && m==6 && m==9 && m==11){
            return 30;
        }else if (m==2){
            if (y%4==0){
                return 29;
            }else {
                return 28;
            }
        }else {
            return 31;
        }

    }
    public  boolean CheckConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.getType() == connectivityManager.TYPE_WIFI || (networkInfo.getType() == connectivityManager.TYPE_MOBILE)) {
                return true;
            } else {
                return false;
            }
        }else {
            return false;
        }
    }
    public String findLongLat(int m,int y) {

        gps=new GPSTracker(this);
        String latitude="";
        String longitude="";
        String methode=getMthode();
        String month=String.valueOf(m);
        String year=String.valueOf(y);
        if (gps.canGetLocation()){
            latitude=String.valueOf(gps.getLatitude());
            longitude=String.valueOf(gps.getLongitude());
        }else {
            gps.showSettingsAlert();
        }
        // String url = "http://api.aladhan.com/v1/calendar?latitude=51.508515&longitude=-0.1254872&method=2&month=4&year=2017";
        String url = "http://api.aladhan.com/v1/calendar?latitude="+latitude+"&longitude="+longitude+"&method="+methode+"&month="+month+"&year="+year;

        return url;
    }
    public String getMthode(){
        SharedPreferences   preferences=getSharedPreferences(nameVolume, Context.MODE_PRIVATE);
        return String.valueOf(preferences.getInt(Keymethode,1));
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
