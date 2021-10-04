package google.abdallah.quranproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import google.abdallah.quranproject.Adapter.BookMarcAdapter;
import google.abdallah.quranproject.Compass.CompassActivity;
import google.abdallah.quranproject.Database.DefaultFetch;
import google.abdallah.quranproject.Model.BookmarkModel;
import google.abdallah.quranproject.R;

public class FavoriesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    BookMarcAdapter adapter;
    List<BookmarkModel> modelList;
    SQLiteDatabase database;
    SQLiteOpenHelper openHelper;
    BottomNavigationView bottomNavigationView;
    MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favories);
        recyclerView = findViewById(R.id.recyclefavories);
        openHelper = new DefaultFetch(this);
        database = openHelper.getWritableDatabase();
        recycBookmark();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bottomNavigationView=findViewById(R.id.buttom);
        bottomNavigationView.setSelectedItemId(R.id.favori);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.setting:
                        Intent  settings=new Intent(FavoriesActivity.this, Settings.class);
                        startActivity(settings);
                        break;
                    case R.id.favori:
                        Toast.makeText(FavoriesActivity.this, "Favories", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.home:
                        Intent  home=new Intent(FavoriesActivity.this, MainActivity.class);
                        startActivity(home);

                        break;
                    case R.id.prayers:
                        Intent   intent=new Intent(FavoriesActivity.this, PrayerActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.qibla:
                        Intent   pr=new Intent(FavoriesActivity.this, CompassActivity.class);
                        startActivity(pr);
                        break;
                    default:

                }
                return true;
            }
        });

    }

    public void initDb() {
        modelList = new ArrayList<>();
        BookmarkModel loveModel;
        Cursor c = database.rawQuery("select title,sourah,ayat FROM Book_Last where title!='last'", new String[]{});
        while (c.moveToNext()) {
            loveModel = new BookmarkModel();
            loveModel.setTitle(c.getString(0));
            loveModel.setNameofsurat(c.getString(1));
            loveModel.setVerseNumber(c.getString(2));

            modelList.add(loveModel);
        }
    }
    private void recycBookmark() {
        initDb();
        adapter = new BookMarcAdapter(modelList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        ItemTouchHelper touchHelper=new ItemTouchHelper(simpleCallback);
        touchHelper.attachToRecyclerView(recyclerView);
    }






    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int from=viewHolder.getAdapterPosition();
            int to=target.getAdapterPosition();
            Collections.swap(modelList,from,to);
            recyclerView.getAdapter().notifyItemMoved(from,to);
            return true;
        }
        BookmarkModel duno=null;

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position=viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    duno = modelList.get(position);
                    modelList.remove(position);
                    delete(duno.getTitle(),duno.getNameofsurat(),duno.getVerseNumber());
                    adapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView, R.string.undo, Snackbar.LENGTH_LONG)
                            .setAction("undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    modelList.add(position, duno);
                                    add(duno.getTitle(),duno.getNameofsurat(),duno.getVerseNumber());
                                    adapter.notifyItemInserted(position);
                                }
                            })
                            .show();
                    break;
            }
        }
    };

    public void delete(String t,String n,String p){
        long a = database.delete("Book_Last","title=? AND sourah=? AND ayat=? ",new String[]{t,n,p});
        if (a!=-1){
            Toast.makeText(this, "deleted into the db", Toast.LENGTH_SHORT).show();
        }
    }
    public  void add(String t,String n,String p){
        ContentValues values=new ContentValues();
        values.put("title",t);
        values.put("sourah",n);
        values.put("ayat",p);
        database.insert("Book_Last",null,values);
    }

}