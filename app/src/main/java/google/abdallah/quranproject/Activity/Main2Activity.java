package google.abdallah.quranproject.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import google.abdallah.quranproject.Adapter.RecyclerAyahAdapter;
import google.abdallah.quranproject.Database.DatabaseFetchAr;
import google.abdallah.quranproject.Database.DatabaseFetchMg;
import google.abdallah.quranproject.Database.DatabaseFetchfr;
import google.abdallah.quranproject.Database.DefaultFetch;
import google.abdallah.quranproject.Fragayat.Juzfrag;
import google.abdallah.quranproject.Model.ModelSurat;
import google.abdallah.quranproject.R;

public class Main2Activity extends AppCompatActivity {

RecyclerView recyclerView;
RecyclerAyahAdapter recyclerAyahAdapter;
    Handler fhandler=new Handler();
    ProgressDialog progressDialog;
    SQLiteOpenHelper openHelperMg;
    SQLiteOpenHelper openHelperAr;
    SQLiteOpenHelper openHelperFr;
    private SQLiteDatabase databaseAr;
    private SQLiteDatabase databaseFR;
    private ModelSurat modelSurat;
    SQLiteDatabase databasedef;
    SQLiteOpenHelper openHelper ;
    TextView juzz_number;
    private SQLiteDatabase databasemg;
    MaterialToolbar toolbar;
    public static String Translation="SettingTr";
    public static SharedPreferences preferencesT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        int pos=getIntent().getIntExtra("pos",0);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView=findViewById(R.id.recyclerayat);
        initDB();
        juzz_number=findViewById(R.id.juzz_number);
        juzz_number.setText("JUZ "+(pos+1));
        new threadHandler2(pos).start();



    }
    private void initDB() {
        openHelperAr=new DatabaseFetchAr(this);
        databaseAr=openHelperAr.getWritableDatabase();
        openHelperFr=new DatabaseFetchfr(this);
        databaseFR=openHelperFr.getWritableDatabase();
        openHelper=new DefaultFetch(this);
        databasedef=openHelper.getWritableDatabase();
        openHelperMg=new DatabaseFetchMg(this);
        databasemg=openHelperMg.getWritableDatabase();
    }

    class  threadHandler2 extends Thread{
        int number;
        List<ModelSurat> modelSurats;
        public  threadHandler2(int i){
            this.number=i;
        }

        @Override
        public void run() {
            if (Translator().equals("Malagasy")){
                modelSurats= takeJuzMg(number);
            }else{
                modelSurats= takeJuzFr(number);
            }

            fhandler.post(new Runnable() {
                @Override
                public void run() {
                    LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                    recyclerAyahAdapter=new RecyclerAyahAdapter(layoutManager,modelSurats, getApplicationContext(),number,getSupportFragmentManager());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(recyclerAyahAdapter);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.ItemDecoration decoration=new DividerItemDecoration(getApplicationContext(),layoutManager.getOrientation());
                    recyclerView.addItemDecoration(decoration);

                }
            });
        }
    }
    public List<ModelSurat> takeJuzMg(int i){
        List<ModelSurat> AllGlobal=new ArrayList<>();

        Cursor cursorAr =databaseAr.rawQuery("select sura,ayah,text,juz FROM arabic_text WHERE juz="+(i+1),new String[]{});
        Cursor cursorFr =databasemg.rawQuery("select surah,ayah,text,juz FROM KoranText WHERE juz=\'juz "+(i+1)+"\'" ,new String[]{});

        String a="﴾";
        String aa="﴿";
        while (cursorAr.moveToNext() && cursorFr.moveToNext()) {
            modelSurat = new ModelSurat();
            modelSurat.setNumberSurah(cursorAr.getString(1));
            modelSurat.setSurahNameAr(cursorAr.getString(2)+"   "+aa+countArabic(Integer.parseInt(cursorAr.getString(1)))+a);
            modelSurat.setSurahName(cursorFr.getString(2));
            AllGlobal.add(modelSurat);
        }

        databaseAr.close();
        databasemg.close();
        return AllGlobal;
    }
    String Translator(){
        preferencesT=this.getSharedPreferences(Translation, Context.MODE_PRIVATE);
      String  translate=preferencesT.getString("T","Malagasy");
        return translate;
    }
    public List<ModelSurat> takeJuzFr(int i){
        List<ModelSurat> AllGlobal=new ArrayList<>();

        Cursor cursorAr =databaseAr.rawQuery("select sura,ayah,text,juz FROM arabic_text WHERE juz="+(i+1),new String[]{});
        Cursor cursorFr =databaseFR.rawQuery("select c0sura,c1ayah,c2text,juz from verses_content WHERE juz=\'"+(i+1)+"\'" ,new String[]{});


        String a="﴾";
        String aa="﴿";
        while (cursorAr.moveToNext() && cursorFr.moveToNext()) {
            modelSurat = new ModelSurat();
            modelSurat.setNumberSurah(cursorAr.getString(1));
            modelSurat.setSurahNameAr(cursorAr.getString(2)+"   "+aa+countArabic(Integer.parseInt(cursorAr.getString(1)))+a);
            modelSurat.setSurahName(cursorFr.getString(2));
            AllGlobal.add(modelSurat);
        }

        databaseAr.close();
        databaseFR.close();
        return AllGlobal;
    }
    public String countArabic(int j){
        int r;
        String []arabic=this.getResources().getStringArray(R.array.ayats);
        String s="";
        while (j>0){
            r =j%10;
            s=arabic[r]+s;
            j=j/10;
        }
        return s;
    }
}
