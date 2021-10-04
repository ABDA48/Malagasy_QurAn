package google.abdallah.quranproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import google.abdallah.quranproject.Adapter.BokkmakDialogue;
import google.abdallah.quranproject.Adapter.FragAdapter;
import google.abdallah.quranproject.Database.DefaultFetch;
import google.abdallah.quranproject.Fragayat.*;

import google.abdallah.quranproject.Model.BookmarkModel;
import google.abdallah.quranproject.Model.TrackModel;
import google.abdallah.quranproject.R;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.thin.downloadmanager.ThinDownloadManager;

import java.util.List;

public class SurahActivity extends AppCompatActivity implements BokkmakDialogue.BookmarkListner {
    Toolbar toolbar;
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    SQLiteDatabase databasedef;
    SQLiteOpenHelper openHelper ;
    String[] p;
    ProgressDialog progressDialog;
 private static List<TrackModel> trackModels;
    ThinDownloadManager thinDownloadManager;
    List<String> uriList;
  public  boolean serial=false;
    public int j;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager2=findViewById(R.id.viewpager);
        tabLayout=findViewById(R.id.toolbarLayout);

        int pos=getIntent().getIntExtra("pos",0);
       FragAdapter fragAdapter= new FragAdapter(this);
        p= getResources().getStringArray(R.array.surat_fr);
        for (int i = 0; i <114 ; i++) {
            fragAdapter.addFragMent( SuratFrag.newInstance(i,p[i],pos));
        }
        viewPager2.setAdapter(fragAdapter);
        tabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
         viewPager2.setCurrentItem(pos,false);
         viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
             @Override
             public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                 super.onPageScrolled(position, positionOffset, positionOffsetPixels);
             }

             @Override
             public void onPageSelected(int position) {
                 super.onPageSelected(position);

             }

             @Override
             public void onPageScrollStateChanged(int state) {
                 super.onPageScrollStateChanged(state);

             }
         });

        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
              tab.setText( p[position]);
            }
        });
        tabLayoutMediator.attach();



        openHelper = new DefaultFetch(this);
        databasedef = openHelper.getWritableDatabase();


    }


    /*private void DownloadProcess(Uri uri, final Uri destination) {

        DownloadRequest downloadRequest=new DownloadRequest(uri)
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destination)
                .setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        progressDialog.hide();
                        uriList.add(String.valueOf(destination));
                        j++;
                        progressDialog.setProgress(setP(j));
                        progressDialog.show();
                        if (uriList.size()==7){
                            Toast.makeText(SurahActivity.this, "uri size is "+7, Toast.LENGTH_SHORT).show();
                            initMedia();
                            progressDialog.hide();
                        }
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {

                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downloadedBytes, int progress) {

                    }
                });

        thinDownloadManager.add(downloadRequest);
    }
    void initMedia(){
        int i=0;
        for (String uri:uriList){
            trackModels.get(i).setUri(uri);
            Audio(uri);
            i++;
        }
    }
    public void Audio(String uri){
        ContentValues contentValues=new ContentValues();
        contentValues.put("title","A");
        contentValues.put("subtitle","b");
        contentValues.put("uri",uri);
    //    databaseTrack.insert("Track",null,contentValues);
    }*/
    @Override
    public void love(BookmarkModel bookmarkModel) {
        Toast.makeText(this, bookmarkModel.getTitle()+" "+
                bookmarkModel.getNameofsurat(), Toast.LENGTH_SHORT).show();
        ContentValues contentValues=new ContentValues();
        contentValues.put("sourah",bookmarkModel.getNameofsurat());
        contentValues.put("ayat",bookmarkModel.getVerseNumber());
        contentValues.put("title",bookmarkModel.getTitle());
        System.out.println(" i am title :"+bookmarkModel.getTitle());
        databasedef.insert("Book_Last",null,contentValues);
    }
    public int setP(int i){
        return (i*100)/7;
    }


}
