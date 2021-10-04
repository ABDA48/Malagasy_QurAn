package google.abdallah.quranproject.Fragayat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import google.abdallah.quranproject.Activity.Settings;
import google.abdallah.quranproject.Activity.SurahActivity;
import google.abdallah.quranproject.Adapter.RecyclerSurat;
import google.abdallah.quranproject.Database.DatabaseFetchAr;
import google.abdallah.quranproject.Database.DatabaseFetchMg;
import google.abdallah.quranproject.Database.DatabaseFetchfr;
import google.abdallah.quranproject.Database.DatabaseTrack;
import google.abdallah.quranproject.Database.DefaultFetch;
import google.abdallah.quranproject.Model.ModelSurat;
import google.abdallah.quranproject.Model.TrackModel;
import google.abdallah.quranproject.R;

public class SuratFrag extends Fragment {
    RecyclerView recyclerView;
    SQLiteOpenHelper openHelperAr;
    SQLiteOpenHelper openHelperFr;
    SQLiteOpenHelper openHelperMg;
    private SQLiteDatabase databaseAr;
    private SQLiteDatabase databaseFR;
    private SQLiteDatabase databasemg;
    private ModelSurat modelSurat;
    SQLiteDatabase databasedef;
    SQLiteOpenHelper openHelper ;
    List<ModelSurat> modelSurats;
    RecyclerSurat recyclerSurat;
    NestedScrollView nestedScrollView;
    LinearLayoutManager linearLayoutManager;
    Handler handler=new Handler();
    ProgressDialog progressDialog;
    Context mcontext;
    SQLiteDatabase databaseTrack;
    SQLiteOpenHelper openHelpTrack;
    List<TrackModel> trackModels;
    Set<String> dowloaded=new HashSet<>();
    ThinDownloadManager thinDownloadManager;
    static  SharedPreferences preferences;
    ProgressDialog downloadprogress;
    String qirah; public MenuItem don;
    Handler fhandler=new Handler();
    ConstraintLayout constraintLayout;
    int surah;String surahName;int surahN;
    boolean donload;int k;
    private static final String key="key";
    private static final String key2="key2";
    private static final String key3="key2";
    public static String Translation="SettingTr";
    public static SharedPreferences preferencesT;
    String translate;
    public static SuratFrag newInstance(int str,String surah,int pos){
        SuratFrag suratFrag =new SuratFrag();
        Bundle bundle=new Bundle();
        bundle.putInt(key,str);
        bundle.putString(key2,surah);
        bundle.putInt(key3,pos);
        suratFrag.setArguments(bundle);
        return suratFrag;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_ayat,menu);
        super.onCreateOptionsMenu(menu, inflater);

        don=menu.findItem(R.id.download);



    }
    scrollThread scrollThread=new scrollThread();
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.setting:

                break;
            case R.id.ratting:
                break;
            case R.id.about:

                break;
            case R.id.download:

                if (CheckConnection()) {
                    if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.INTERNET)
                            != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mcontext, new String[]{
                                Manifest.permission.INTERNET,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, 1);
                    } else {
                        DownloadPreparation();
                    }
                } else {
                    String title = "Connection State";
                    String message = "Please check your Connection";
                    Dialogue(title, message);
                }

                break;
            case R.id.jump:


                break;
            case R.id.go:
                try {
                    scrollThread.start();
                } catch (java.lang.IllegalThreadStateException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;

    }


      class scrollThread extends Thread{
        @Override
        public void run() {
            for (int i = 0; i <modelSurats.size(); i++) {
                try {
                    Thread.sleep(5000);

                    fhandler.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollRecycler( k);
                        }
                    });
                    k++;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void scrollRecycler(int k)  {
        try{

            linearLayoutManager.scrollToPositionWithOffset(k,0);
            nestedScrollView.scrollTo(0,0);
            recyclerView.findViewHolderForAdapterPosition(k).itemView.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        }catch(NullPointerException  e){
            e.printStackTrace();

        }
    }
    public  void setAudio(){
         preferences=mcontext.getSharedPreferences(key,Context.MODE_PRIVATE);
         String str="set "+(surahN+1);
        Set<String> d=preferences.getStringSet(str,new HashSet<String>());
        d.add(String.valueOf(surahN+1));
         SharedPreferences.Editor editor=preferences.edit();
         editor.putStringSet(str,d);
         editor.commit();
        don.setIcon(R.drawable.ic_volume_off_black_24dp);
    }

    public int getSurah(){
        preferences=mcontext.getSharedPreferences(key,Context.MODE_PRIVATE);
        return preferences.getInt("Surah",0);
    }


    private void DownloadPreparation() {
        TrackModel trackModel;
        qirah="AbdulSamad_64kbps_QuranExplorer.Com";
        String source="https://everyayah.com/data/";
        int n=surah+1;
        int pos=postition(surah);
        for (int i = 1; i < pos+1; i++) {
            trackModel=new TrackModel();
            Uri uri = Uri.parse(source + qirah + "/" + Fetchayat(n) + Fetchayat(i) + ".mp3");
            String dest=mcontext.getExternalCacheDir().toString() +"/"+qirah+"/"+Fetchayat(n) + Fetchayat(i)+".mp3";
            final Uri destination = Uri.parse(dest);
            Log.d("Tag:emp",""+uri);
            Log.d("Tag:w",""+destination);
            trackModel.setQirah(qirah);trackModel.setUri(String.valueOf(uri));
            trackModel.setSurah(String.valueOf(n));
            trackModel.setAyah(String.valueOf(i));trackModel.setJuz(modelSurats.get(i-1).getJuz());
            Audio( trackModel.getSurah(),trackModel.getAyah(),trackModel.getJuz(),trackModel.getQirah(),trackModel.getUri());
            DownloadProcess(destination,trackModel);
            don.setIcon(R.drawable.ic_volume_off_black_24dp);
        }
    }

    private void Dialogue(String title, String message) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(mcontext);
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

    public  boolean CheckConnection() {
    ConnectivityManager connectivityManager = (ConnectivityManager) mcontext.getSystemService(mcontext.CONNECTIVITY_SERVICE);
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
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext=context;

    }
    public void Take(int i){
       trackModels=new ArrayList<>();
        Cursor c=databaseTrack.rawQuery("select ayah,qirah,uri,juz from QuranAudio where surah="+i,new String[]{});

        TrackModel trackModel;
        while (c.moveToNext()){
            trackModel=new TrackModel();
            trackModel.setAyah(c.getString(0));
            trackModel.setQirah(c.getString(1));
            trackModel.setUri(c.getString(2));
            trackModel.setJuz(c.getString(3));
            trackModels.add(trackModel);
        }
        if (trackModels.size()==0){
            final AlertDialog.Builder builder=new AlertDialog.Builder(mcontext);
            builder.setTitle("Download");
            builder.setMessage("You have to Download the Audio");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DownloadPreparation();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }else {
            int l=0;
            Toast.makeText(mcontext, "Download finished "+trackModels.size(), Toast.LENGTH_SHORT).show();
            for (TrackModel v:trackModels){
                modelSurats.get(l).setUri(Uri.parse(v.getUri()));
                Log.d("TAG:H", "Take: "+trackModels.size()+"--> "+modelSurats.get(l).getUri());
                l++;
                if (l>modelSurats.size()){
                    break;
                }
            }
        }
    }
    public void Audio(String surah,String ayah,String juz,String qirah,String uri){
        ContentValues contentValues=new ContentValues();
        contentValues.put("surah",surah);
        contentValues.put("ayah",ayah);
        contentValues.put("juz",juz);
        contentValues.put("qirah",qirah);
        contentValues.put("uri",uri);
       long t= databaseTrack.insert("QuranAudio",null,contentValues);
       if (t<0){
           Toast.makeText(mcontext, "Error Occurd ", Toast.LENGTH_SHORT).show();
       }else {
           Log.d("Audio:TAG", "Audio:done"+"\n"+"surah "+surah+
                   "\n"+"uri "+uri);
       }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     View view=inflater.inflate(R.layout.surah1,container,false);
        recyclerView=view.findViewById(R.id.surah1);
        constraintLayout=view.findViewById(R.id.constraintLayout2);
        nestedScrollView=view.findViewById(R.id.nested);
        recyclerView.setNestedScrollingEnabled(true);
        surah=0;
        initDB();
        if (getArguments()!=null){
            surah=getArguments().getInt(key);
            surahName=getArguments().getString(key2);
            surahN=getArguments().getInt(key3);
        }


        new threadHandler( surah, 0).start();
        thinDownloadManager=new ThinDownloadManager();
        downloadprogress=new ProgressDialog(mcontext);
        downloadprogress.setMax(100);
        downloadprogress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        if (surah==0||surah==8){
            constraintLayout.setVisibility(View.GONE);
        }else {
            constraintLayout.setVisibility(View.VISIBLE);
        }


     return  view;
    }

    String Translator(){
        preferencesT=mcontext.getSharedPreferences(Translation, Context.MODE_PRIVATE);
        translate=preferencesT.getString("T","Malagasy");

        return translate;
    }
    private void initDB() {
        openHelperAr=new DatabaseFetchAr(mcontext);
        databaseAr=openHelperAr.getWritableDatabase();
        openHelperFr=new DatabaseFetchfr(mcontext);
        databaseFR=openHelperFr.getWritableDatabase();
        openHelper=new DefaultFetch(mcontext);
        databasedef=openHelper.getWritableDatabase();
        openHelperMg=new DatabaseFetchMg(mcontext);
        databasemg=openHelperMg.getWritableDatabase();
        openHelpTrack=new DatabaseTrack(mcontext);
        databaseTrack=openHelpTrack.getWritableDatabase();
    }
    private void DownloadProcess(final Uri destination, final TrackModel trackModel) {
        final  int i=Integer.parseInt(trackModel.getAyah());
        DownloadRequest downloadRequest=new DownloadRequest(Uri.parse(trackModel.getUri()))
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destination)
                .setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        downloadprogress.setTitle("Downloading...");
                        downloadprogress.setProgress(setP(i));

                        downloadprogress.show();
                    if (i==postition(surah)){
                        downloadprogress.hide();
                        Take(surah+1);
                        downloadprogress.hide();

                    }
                    }
                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                        Toast.makeText(mcontext, "I seems you have already downloded it ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downloadedBytes, int progress) {

                    }
                });

        thinDownloadManager.add(downloadRequest);

    }
    class threadHandler extends Thread{
        int number;
        int jump;
        public threadHandler(int i,int jump)
        {
            this.jump=jump;
            this.number=i;
        }

        @Override
        public void run() {
            if (Translator().equals("Malagasy")){
                FetchDataMg(number);
            }else{
                FetchDataFr(number);
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    linearLayoutManager=new LinearLayoutManager(getActivity());
                    recyclerSurat=new RecyclerSurat(linearLayoutManager,modelSurats, mcontext,number,getFragmentManager());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(recyclerSurat);
                    linearLayoutManager.scrollToPositionWithOffset(jump,0);
                    RecyclerView.ItemDecoration decoration=new DividerItemDecoration(mcontext,linearLayoutManager.getOrientation());
                    recyclerView.addItemDecoration(decoration);
                    //progressDialog.hide();
                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mcontext, "Permission am granted", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private List<ModelSurat> FetchDataMg(int i) {
        modelSurats=new ArrayList<>();

        Cursor cursorAr =databaseAr.rawQuery("SELECT sura,ayah,text,juz from arabic_text WHERE sura="+(i+1),new String[]{});
        Cursor cursorMg=databasemg.rawQuery("select surah,ayah,text,juz from KoranText WHERE surah="+(i+1),new String[]{});

        String a="﴾";
        String aa="﴿";
        while (cursorAr.moveToNext() && cursorMg.moveToNext()) {
            modelSurat = new ModelSurat();
            modelSurat.setNumberSurah(cursorMg.getString(1));
            modelSurat.setSurahNameAr(cursorAr.getString(2)+" "+aa+countArabic(Integer.parseInt(cursorAr.getString(1)))+a);
            modelSurat.setSurahName(cursorMg.getString(2));

            modelSurats.add(modelSurat);
        }
        databaseAr.close();
        databasemg.close();
    return modelSurats;
    }
    private List<ModelSurat> FetchDataFr(int i) {
        modelSurats=new ArrayList<>();

        Cursor cursorAr =databaseAr.rawQuery("SELECT sura,ayah,text,juz from arabic_text WHERE sura="+(i+1),new String[]{});
        Cursor cursorFr =databaseFR.rawQuery("select c0sura,c1ayah,c2text,juz from verses_content WHERE c0sura="+(i+1) ,new String[]{});


        String a="﴾";
        String aa="﴿";
        while (cursorAr.moveToNext() && cursorFr.moveToNext()) {
            modelSurat = new ModelSurat();
            modelSurat.setNumberSurah(cursorFr.getString(1));
            modelSurat.setSurahNameAr(cursorAr.getString(2)+" "+aa+countArabic(Integer.parseInt(cursorAr.getString(1)))+a);
            modelSurat.setSurahName(cursorFr.getString(2));
            modelSurats.add(modelSurat);
        }
        databaseAr.close();
        databaseFR.close();
        return modelSurats;
    }

    public String countArabic(int j){
        int r;
        String []arabic=mcontext.getResources().getStringArray(R.array.ayats);
        String s="";
        while (j>0){
            r =j%10;
            s=arabic[r]+s;
            j=j/10;
        }
        return s;
    }

    public int postition(int i){
        String []number=mcontext.getResources().getStringArray(R.array.verses_number);
        return Integer.parseInt(number[i]);
    }
    public String Fetchayat(int i){
        String s;
        if (i<10){
            s="00"+i;
        }else if(i<100){
            s="0"+i;
        }else {
            s=""+i;
        }
        return s;
    }
    public int setP(int i){
        return (i*100)/modelSurats.size();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
if (recyclerSurat.Flag){
    recyclerSurat.player.release();
    recyclerSurat.Flag=false;
}

    }
}
