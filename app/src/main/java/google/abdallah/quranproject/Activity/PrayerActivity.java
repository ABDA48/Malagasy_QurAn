package google.abdallah.quranproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import google.abdallah.quranproject.Adapter.ChoiceDialogue;
import google.abdallah.quranproject.Adapter.PrayerSettingDialogue;
import google.abdallah.quranproject.Compass.CompassActivity;
import google.abdallah.quranproject.Compass.GPSTracker;
import google.abdallah.quranproject.Database.DatabaseLocation;
import google.abdallah.quranproject.Model.PrayerModel;
import google.abdallah.quranproject.Prayer.AppController;
import google.abdallah.quranproject.Prayer.PlayerService;
import google.abdallah.quranproject.R;
import google.abdallah.quranproject.Receiver.Azan;

import static google.abdallah.quranproject.Adapter.PrayerSettingDialogue.KeyMethodeName;
import static google.abdallah.quranproject.Adapter.PrayerSettingDialogue.Keymethode;
import static google.abdallah.quranproject.Adapter.PrayerSettingDialogue.keyVolume;
import static google.abdallah.quranproject.Adapter.PrayerSettingDialogue.nameVolume;
import static google.abdallah.quranproject.Database.DatabaseLocation.TableName;

public class PrayerActivity extends AppCompatActivity implements ChoiceDialogue.OnSingleListner {
    private static final String TAG = "prayer";
    String tag_json_obj = "json_obj_req";
    // String url = "http://api.aladhan.com/v1/calendar?latitude=51.508515&longitude=-0.1254872&method=2&month=4&year=2017";
    //  String dateUrl="http://api.aladhan.com/v1/gToH?date=08-12-2017";
    TextView timFajr, timeDhuhr, timeAnsr, timeMaghrib, timeIcha,dateGregore,timeImsak,timeSun;
    ImageView voicefajr,voiceansr,voicemaghrib,voicedhuhr,voiceicha;
    ConstraintLayout F,D,A,M,I;
    TextView locationCity,dateHijr;
    GPSTracker gps;
    Geocoder geocoder ;

    MaterialToolbar toolbar;DialogFragment fragment;
    ProgressDialog progressDialog;
    DatabaseLocation databaseLocation;
    SQLiteDatabase liteDatabase;
    String variable;int id=0;
    BottomNavigationView bottomNavigationView;
    RadioButton btnrad,btnrad2;
    RadioGroup radioGroup;
    SharedPreferences preferences;
    SharedPreferences pref;
    public static String n="Azan";
    public static String name="Gps",KeyLatitude="latitude",Keylongitude="longitude",KeyLocation="location",Keyvariable="keyboolean",
            Keyazanfajr="keyazanfajr",Keyazandhuhr="Keyazandhuhr",Keyazanansr="Keyazanansr",Keyazanmaghrib="keyazanmaghrib",Keyazanisha="Keyazanisha";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       btnrad=findViewById(R.id.azanon); btnrad2=findViewById(R.id.azanof);radioGroup=findViewById(R.id.btngrp);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        INITIALIZATION();
        dateGregore.setText(getDateGre());
        geocoder=new Geocoder(this, Locale.getDefault());
        databaseLocation=new DatabaseLocation(this);
        liteDatabase=databaseLocation.getWritableDatabase();
        preferences=getSharedPreferences(name,Context.MODE_PRIVATE);
        pref=getSharedPreferences(n,Context.MODE_PRIVATE);
        bottomNavigationView=findViewById(R.id.buttom);
        bottomNavigationView.setSelectedItemId(R.id.prayers);
        variable=preferences.getString(Keyvariable,"null");
        if (variable.equals("null")) {
    if (CheckConnection()) {
        Calendar calendar=Calendar.getInstance();
        insertPrayer(calendar.get(Calendar.DAY_OF_MONTH)-1);
        Intent intent=new Intent(PrayerActivity.this, PlayerService.class);
        startService(intent);
    } else {
        String title = "Connection State";
        String message = "Please check your Connection";
        Dialogue(title, message);
    }
}else {
 PrayerModel prayerModel=SelectPrayer(getDateGre());
    timFajr.setText(timereals(prayerModel.getFajr()));
    timeDhuhr.setText(timereals(prayerModel.getDhuhr()));
    timeAnsr.setText(timereals(prayerModel.getAsr()));
    timeMaghrib.setText(timereals(prayerModel.getMaghrib()));
    timeIcha.setText(timereals(prayerModel.getIsha()));
    timeSun.setText(timereals(prayerModel.getSunset()));
    timeImsak.setText(timereals(prayerModel.getImsak()));
    dateHijr.setText(prayerModel.getDateHijri());

}
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET,Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }else{
            locationCity.setText(findLocation());
        }



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.setting:
                        Intent   intent=new Intent(PrayerActivity.this, Settings.class);
                        startActivity(intent);
                        break;
                    case R.id.favori:
                        Intent   intentfav=new Intent(PrayerActivity.this, FavoriesActivity.class);
                        startActivity(intentfav);
                        break;
                    case R.id.home:
                        Intent  home=new Intent(PrayerActivity.this, MainActivity.class);
                        startActivity(home);

                        break;
                    case R.id.prayers:
                        Toast.makeText(PrayerActivity.this, "PrayerAcivity", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.qibla:
                        Intent   pr=new Intent(PrayerActivity.this, CompassActivity.class);
                        startActivity(pr);
                        break;
                    default:

                }
                return true;
            }
        });

        final Thread thread=new Thread(new Runnable() {

            @Override
            public void run() {
                while (getThreadAzan()){
                    try {

                        Thread.sleep(1000);
                        Calendar t=getCalendarTimeNow();
                         azanMaiker(t);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Stoped");

            }
        });
        if (getAzanState().equals("ON")){
            btnrad.setChecked(true);
        }else{
            btnrad2.setChecked(true);
        }

      radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup radioGroup, int i) {

              switch (radioGroup.getCheckedRadioButtonId()){
                  case R.id.azanon:
                      thread.start();
                      SharedPreferences.Editor editor=pref.edit();
                      editor.putString("AzanState","ON");
                      editor.putBoolean("thread",true);
                      editor.commit();
                      break;
                  case R.id.azanof:
                      SharedPreferences.Editor editor2=pref.edit();
                      editor2.putString("AzanState","OF");
                      editor2.putBoolean("thread",false);
                      editor2.commit();
                      break;
              }
          }
      });

    }
    String getAzanState(){
        String b=pref.getString("AzanState","OF");
        return  b;
    }
    boolean getThreadAzan(){
        boolean b=pref.getBoolean("thread",true);
        return  b;
    }

    private void INITIALIZATION() {

        timFajr = findViewById(R.id.timeFajr);
        timeImsak=findViewById(R.id.timeImsak);
        timeSun=findViewById(R.id.timeSun);
        timeDhuhr = findViewById(R.id.timedhuhr);
        timeAnsr = findViewById(R.id.timeAnsr);
        timeMaghrib = findViewById(R.id.timemaghrib);
        timeIcha = findViewById(R.id.timeIcha);
        locationCity=findViewById(R.id.locationCity);
        dateHijr=findViewById(R.id.dateHijr);
        dateGregore=findViewById(R.id.dateGregore);

        voicefajr=findViewById(R.id.voicefajr);
        voicedhuhr=findViewById(R.id.voicedhuhr);
        voiceansr=findViewById(R.id.voiceAnsr);
        voicemaghrib=findViewById(R.id.voiceMaghrib);
        voiceicha=findViewById(R.id.voiceIcha);
        F=findViewById(R.id.F);
        D=findViewById(R.id.D);
        A=findViewById(R.id.A);
        M=findViewById(R.id.M);
        I=findViewById(R.id.I);
        F.setOnClickListener(listener);
        D.setOnClickListener(listener);
        M.setOnClickListener(listener);
        I.setOnClickListener(listener);
        A.setOnClickListener(listener);
    }

    void azanMaiker(Calendar t){
        List<String> swalatTime=new ArrayList<>();
        String s1=timFajr.getText().toString();
        String s2=timeDhuhr.getText().toString();
        String s3=timeAnsr.getText().toString();
        String s4=timeMaghrib.getText().toString();
        String s5=timeIcha.getText().toString();

/*
        String s1="16:34";
        String s2="16:36";
        String s3="16:38";
        String s4="16:40";
        String s5="16:42";

         */

        if (!(s1.equals(""))&&!(s2.equals(""))&&!(s3.equals(""))&&!(s4.equals(""))&&!(s5.equals(""))){
            swalatTime.add(s1);
            swalatTime.add(s2);
            swalatTime.add(s3);
            swalatTime.add(s4);
            swalatTime.add(s5);
        }
        System.out.println("time -->");

        for (String s:swalatTime){
            Calendar time=timePrepare(s);
            if (time.compareTo(t)==0){
                setAzan(time);
                System.out.println("time -->"+time);
            }
        }

        return  ;
    }

    private Calendar getCalendarTimeNow() {
        Calendar t;
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate =df.format(c);
        Log.d("TAGGG",formattedDate);

        Calendar timenow=timePrepare(formattedDate);
        t=timenow;
        return t;
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            switch (id){
                case R.id.F:
                    boolean isAzanf=preferences.getBoolean(Keyazanfajr,false);
                    if (isAzanf){
                        AzanCancel(0,timFajr.getText().toString());

                    }else {
                        AzanDialogue(0,timFajr.getText().toString());
                        F.setBackgroundColor(getResources().getColor(R.color.cardColor));
                    }
                    break;
                case R.id.D:
                    boolean isAzand=preferences.getBoolean(Keyazandhuhr,false);
                    if (isAzand){
                        AzanCancel(1,timeDhuhr.getText().toString());
                    }else {
                        AzanDialogue(1,timeDhuhr.getText().toString());
                    }
                    break;
                case R.id.A:
                    boolean isAzana=preferences.getBoolean(Keyazanansr,false);
                    if (isAzana){
                        AzanCancel(2,timeAnsr.getText().toString());
                    }else {
                        AzanDialogue(2,timeAnsr.getText().toString());
                    }
                    break;
                case R.id.M:
                    boolean isAzanm=preferences.getBoolean(Keyazanmaghrib,false);
                    if (isAzanm){
                        AzanCancel(3,timeMaghrib.getText().toString());
                    }else {
                        AzanDialogue(3,timeMaghrib.getText().toString());
                    }
                    break;
                case R.id.I:
                    boolean isAzani=preferences.getBoolean(Keyazanisha,false);
                    if (isAzani){
                        AzanCancel(2,timeIcha.getText().toString());
                    }else {
                        AzanDialogue(2,timeIcha.getText().toString());
                    }
                    break;
            }
        }
    };
public  void AzanCancel(final int salat,final String s){
    AlertDialog.Builder builder=new AlertDialog.Builder(this);
    builder.setTitle("Azan")
            .setMessage("Do you want the Azan to be set off ?")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancelAzan(timePrepare(s));
                    switch (salat){
                        case 0:
                            preferences.edit().putBoolean(Keyazanfajr,false).commit();
                            voicefajr.setImageResource(R.drawable.ic_volume_off_black_24dp);
                            F.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                            break;
                        case 1:
                            preferences.edit().putBoolean(Keyazandhuhr,false).commit();
                            D.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                            voicedhuhr.setImageResource(R.drawable.ic_volume_off_black_24dp);
                            break;
                        case 2:
                            preferences.edit().putBoolean(Keyazanansr,false).commit();
                            A.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                            voiceansr.setImageResource(R.drawable.ic_volume_off_black_24dp);
                            break;
                        case 3:
                            preferences.edit().putBoolean(Keyazanmaghrib,false).commit();
                            D.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                            voicemaghrib.setImageResource(R.drawable.ic_volume_off_black_24dp);
                            break;
                        case 4:
                            preferences.edit().putBoolean(Keyazanisha,false).commit();
                            I.setBackgroundColor(getResources().getColor(R.color.colorwhite));
                            voiceicha.setImageResource(R.drawable.ic_volume_off_black_24dp);
                            break;
                    }
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
    AlertDialog dialog=builder.create();
    dialog.setCancelable(true);
    dialog.show();
}
    public void AzanDialogue(final int salat,final String azantime) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Azan")
                .setMessage("Do you want the Azan to be set on ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setAzan(timePrepare(azantime));
                        switch (salat){
                            case 0:
                                preferences.edit().putBoolean(Keyazanfajr,true).commit();
                                voicefajr.setImageResource(R.drawable.ic_volume_up_black_24dp);
                                F.setBackgroundColor(getResources().getColor(R.color.cardColor2));
                             break;
                            case 1:
                                preferences.edit().putBoolean(Keyazandhuhr,true).commit();
                                D.setBackgroundColor(getResources().getColor(R.color.cardColor2));
                                voicedhuhr.setImageResource(R.drawable.ic_volume_up_black_24dp);
                                break;
                            case 2:
                                A.setBackgroundColor(getResources().getColor(R.color.cardColor2));
                                preferences.edit().putBoolean(Keyazanansr,true).commit();
                                voiceansr.setImageResource(R.drawable.ic_volume_up_black_24dp);
                                break;
                            case 3:
                                M.setBackgroundColor(getResources().getColor(R.color.cardColor2));
                                preferences.edit().putBoolean(Keyazanmaghrib,true).commit();
                                voicemaghrib.setImageResource(R.drawable.ic_volume_up_black_24dp);
                                break;
                            case 4:
                                voiceicha.setImageResource(R.drawable.ic_volume_up_black_24dp);
                                I.setBackgroundColor(getResources().getColor(R.color.cardColor2));
                                preferences.edit().putBoolean(Keyazanisha,true).commit();
                                break;
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog=builder.create();
        dialog.setCancelable(true);
        dialog.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length!=0&& grantResults[0]== PackageManager.PERMISSION_GRANTED && requestCode==1){
            locationCity.setText(findLocation());
        }else{
            locationCity.setText(findLocation());
        }
    }

    private String getDateGre() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate =df.format(c);
        return formattedDate;
    }

    public String findLongLat() {
       Calendar calendar=Calendar.getInstance();

        gps=new GPSTracker(this);
        String latitude="";
        String longitude="";
        String methode=getMthode();
        String month=String.valueOf(calendar.get(Calendar.MONTH)+1);
        String year=String.valueOf(calendar.get(Calendar.YEAR));
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
    public String findLocation(){
        String location="";
        gps=new GPSTracker(this);

        if (gps.canGetLocation()){
            try {
                Location l=gps.getLocation();
                List<Address> addresses = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
                StringBuffer stringBuffer = new StringBuffer();
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    stringBuffer.append(  address.getAdminArea() + ",");
                    stringBuffer.append( address.getSubAdminArea());
                    stringBuffer.append("-" + address.getCountryName() );
                    location=stringBuffer.toString();
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString(KeyLocation,location);
                    editor.commit();
                } else {
                  location=preferences.getString(KeyLocation,"location not find");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            gps.showSettingsAlert();
        }
        return location;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.prayer,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.location:
                progressDialog=new ProgressDialog(this);
                progressDialog.setTitle("Searching for Location");
                progressDialog.show();
                lookLocalisation();
                break;
            case R.id.Qibla:
                Intent intent=new Intent(this, CompassActivity.class);
                startActivity(intent);
                break;
            case R.id.setting:
                 fragment=new PrayerSettingDialogue();
                fragment.setCancelable(false);
                fragment.show(getSupportFragmentManager(),"Tag Frag");
                break;
        }
        return true;
    }

    @Override
    public void onPositive(int position, String[] list) {
        preferences=getSharedPreferences(nameVolume, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=preferences.edit();
        editor.putInt(Keymethode,position);
        editor.putString(KeyMethodeName,list[position]);
        editor.commit();
        fragment.dismiss();
        DialogFragment fragment1=new PrayerSettingDialogue();
        fragment1.setCancelable(false);
        fragment1.show(getSupportFragmentManager(),"Tag Frag");
    }

    @Override
    public void onNegative(int position) {

    }
    public String getMthode(){
        SharedPreferences   preferences=getSharedPreferences(nameVolume,Context.MODE_PRIVATE);
       return String.valueOf(preferences.getInt(Keymethode,1));
    }
    void lookLocalisation(){
        gps=new GPSTracker(this);
        String latitude="";
        String longitude="";
        if (gps.canGetLocation()){
            latitude=String.valueOf(gps.getLatitude());
            longitude=String.valueOf(gps.getLongitude());
        }else {
            gps.showSettingsAlert();
        }
        preferences=getSharedPreferences(name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(KeyLatitude,latitude);
        editor.putString(Keylongitude,longitude);
        editor.commit();
        Toast.makeText(this, "location finished", Toast.LENGTH_SHORT).show();
    }
    public  boolean CheckConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.getType() == connectivityManager.TYPE_WIFI || (networkInfo.getType() == connectivityManager.TYPE_MOBILE)) {
                return true;
            } else {
                Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public void insertPrayer(final int i){
        Log.d(TAG, "insertPrayer: -->"+i+" and "+findLongLat());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, findLongLat(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

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


                     timFajr.setText(fajr);
                     timeDhuhr.setText(Dhuhr);
                     timeAnsr.setText(Ansr);
                     timeMaghrib.setText(Maghrib);
                     timeIcha.setText(Isha);
                     timeSun.setText(Sun);
                     timeImsak.setText(imsak);
                     dateHijr.setText(day+" "+mo+" "+year);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error-->: " + error.getMessage());
                // hide the progress dialog

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    public  PrayerModel SelectPrayer(String s){
        PrayerModel prayerModel=new PrayerModel();
    String sql="select Sunset,Fajr,Dhuhr,Asr,Maghrib,Isha,Imsak,dateGregore,dateHijri from "+TableName+ " where dateGregore="+"\'"+s+"\'";
        Cursor c=liteDatabase.rawQuery(sql,new String[]{});
        while (c.moveToNext()){
            prayerModel.setSunset(c.getString(0));
            prayerModel.setFajr(c.getString(1));
            prayerModel.setDhuhr(c.getString(2));
            prayerModel.setAsr(c.getString(3));
            prayerModel.setMaghrib(c.getString(4));
            prayerModel.setIsha(c.getString(5));
            prayerModel.setImsak(c.getString(6));
            prayerModel.setDateHijri(c.getString(8));

        }
            return prayerModel;
    }
    private void Dialogue(String title, String message) {
        final android.app.AlertDialog.Builder builder=new android.app.AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    String timereals(String s ){
      String []s1=s.split(" ");
      return s1[0];
    }
    Calendar timePrepare(String s){
        String s1[]=s.split(":");
        int hour=Integer.parseInt(s1[0]);
        int minut=Integer.parseInt(s1[1]);
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,minut);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        return calendar;
    }
void setAzan(Calendar c){
        Intent in=new Intent(this, Azan.class);
    PendingIntent p1=PendingIntent.getBroadcast(this,0,in,0);
    AlarmManager man= (AlarmManager) getSystemService(ALARM_SERVICE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        man.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),p1);
    }else {
        man.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),p1);
    }
}
    void cancelAzan(Calendar c){
        Intent in=new Intent(this, Azan.class);
        PendingIntent p1=PendingIntent.getBroadcast(this,0,in,0);
        AlarmManager man= (AlarmManager) getSystemService(ALARM_SERVICE);
        man.cancel(p1);
    }
}





