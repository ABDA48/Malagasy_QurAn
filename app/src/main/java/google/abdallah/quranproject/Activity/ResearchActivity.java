package google.abdallah.quranproject.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import google.abdallah.quranproject.Adapter.ResearchAdapter;
import google.abdallah.quranproject.Database.DatabaseFetchMg;
import google.abdallah.quranproject.Database.DatabaseFetchfr;
import google.abdallah.quranproject.Model.ModelSearch;
import google.abdallah.quranproject.Model.ModelSurat;
import google.abdallah.quranproject.R;



public class ResearchActivity extends AppCompatActivity {
RecyclerView recyclerView;
    List<ModelSurat> researchList;
    private SQLiteOpenHelper openHelperFr;
    private  SQLiteDatabase databaseFR;
    private SQLiteOpenHelper openHelperMg;
    private  SQLiteDatabase databaseMg;
    ModelSurat modelSurat;
    ModelSearch modelSearch;
    Toolbar toolbar;
    Handler handler=new Handler();
    ProgressDialog progressDialog;
    public static String Translation="SettingTr";
    public static SharedPreferences preferencesT;
    ResearchAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);
   toolbar=findViewById(R.id.toolbar);
   setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        openHelperFr=new DatabaseFetchfr(this);
        databaseFR = openHelperFr.getWritableDatabase();
        openHelperMg=new DatabaseFetchMg(this);
        databaseMg=openHelperMg.getWritableDatabase();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menuresearch,menu);
        MenuItem item=menu.findItem(R.id.research);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recyclerView=findViewById(R.id.recyclersearch);

                new DbThread(query).start();


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }
/*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.research){
            Toast.makeText(ResearchActivity.this, "Ok", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }*/

     class  DbThread extends Thread{
         String query;
         DbThread(String quewry)   {
             this.query=quewry;
         }
         @Override
         public void run() {
            initData();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    adapter=new ResearchAdapter(researchList,ResearchActivity.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ResearchActivity.this));
                    adapter.getFilter().filter(query);

                }
            });
         }
     }
    String Translator(){
        preferencesT=this.getSharedPreferences(Translation, Context.MODE_PRIVATE);
       String translate=preferencesT.getString("T","Malagasy");
        return translate;
    }
    private void initData() {
     researchList=new ArrayList<>();
        List<ModelSearch> modelFr;

   String []surat_fr=getResources().getStringArray(R.array.surat_fr);
   if (Translator().equals("French")){
       for (int i = 0; i < surat_fr.length; i++) {
           modelFr=new ArrayList<>();
           modelSurat=new ModelSurat();
           Cursor cursorFr =databaseFR.rawQuery("SELECT c1ayah,c2text FROM verses_content WHERE c0sura="+(i+1),new String[]{});
           int j=0;
           while (cursorFr.moveToNext()){
               modelSearch=new ModelSearch();
               j++;
               modelSearch.setAyat(cursorFr.getString(1));
               modelSearch.setNumber(String.valueOf(j));
               modelFr.add(modelSearch);
           }

           modelSurat.setSurahName(surat_fr[i]);
           modelSurat.setModelSearchList(modelFr);
           researchList.add(modelSurat);
       }
   }else{
       for (int i = 0; i < surat_fr.length; i++) {
           modelFr=new ArrayList<>();
           modelSurat=new ModelSurat();
           Cursor cursorFr =databaseMg.rawQuery("SELECT ayah,text FROM KoranText WHERE surah="+(i+1),new String[]{});
           int j=0;
           while (cursorFr.moveToNext()){
               modelSearch=new ModelSearch();
               j++;
               modelSearch.setAyat(cursorFr.getString(1));
               modelSearch.setNumber(String.valueOf(j));
               modelFr.add(modelSearch);
           }

           modelSurat.setSurahName(surat_fr[i]);
           modelSurat.setModelSearchList(modelFr);
           researchList.add(modelSurat);
       }
   }




    }
}
