package google.abdallah.quranproject.Fragayat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import google.abdallah.quranproject.Adapter.RabannaRecycler;
import google.abdallah.quranproject.Adapter.RecyclerAyahAdapter;
import google.abdallah.quranproject.Database.DataFetchRabana;
import google.abdallah.quranproject.Database.DatabaseFetchAr;
import google.abdallah.quranproject.Database.DatabaseFetchMg;
import google.abdallah.quranproject.Database.DatabaseFetchfr;
import google.abdallah.quranproject.Database.DefaultFetch;
import google.abdallah.quranproject.Model.ModelSurat;
import google.abdallah.quranproject.Model.RabannaModel;
import google.abdallah.quranproject.R;
public class Juzfrag extends Fragment {
    RecyclerView recyclerView;
    Handler fhandler=new Handler();


    RabannaRecycler  recyclerAyahAdapter;

    SQLiteOpenHelper openHelperRabanna;

      SQLiteDatabase databaseRabanna;
    public int value;

      RabannaModel rabannaModel;

    Context mcontext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext=context;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
    }

    private static final String key="key";
    public static Juzfrag newInstance(int str){
        Juzfrag suratFrag =new Juzfrag();
        Bundle bundle=new Bundle();
        bundle.putInt(key,str);
        suratFrag.setArguments(bundle);
        return suratFrag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.rabanna,container,false);
       recyclerView=view.findViewById(R.id.rabanna);

          value=0;
        initDB();
        if (getArguments()!=null){
            value=getArguments().getInt(key);
        }

        new threadHandler2(value).start();

       return view;
    }
    private void initDB() {
        openHelperRabanna=new DataFetchRabana(mcontext);
        databaseRabanna=openHelperRabanna.getWritableDatabase();
    }
    class  threadHandler2 extends Thread{
        int number;
        List<RabannaModel> rabannaModels;
        public  threadHandler2(int i){
            this.number=i;
        }

        @Override
        public void run() {
            rabannaModels= takeJuz(number);
            fhandler.post(new Runnable() {
                @Override
                public void run() {
                    LinearLayoutManager layoutManager=new LinearLayoutManager(mcontext,LinearLayoutManager.VERTICAL,false);
                    recyclerAyahAdapter=new RabannaRecycler(mcontext,rabannaModels);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(recyclerAyahAdapter);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.ItemDecoration decoration=new DividerItemDecoration(mcontext,layoutManager.getOrientation());
                    recyclerView.addItemDecoration(decoration);

                }
            });
        }
    }
    public List<RabannaModel> takeJuz(int i){
        List<RabannaModel> AllGlobal=new ArrayList<>();
        final int media[]={R.raw.rabana1,R.raw.rabana2,R.raw.rabana3,R.raw.rabana4,R.raw.rabana5,R.raw.rabana6,R.raw.rabana7,R.raw.rabana8,
                R.raw.rabana9,R.raw.rabana10,R.raw.rabana11,R.raw.rabana12,R.raw.rabana13,R.raw.rabana14,R.raw.rabana15,R.raw.rabana16,R.raw.rabana17,R.raw.rabana18,
                R.raw.rabana19,R.raw.rabana20,R.raw.rabana21,R.raw.rabana22,R.raw.rabana23,R.raw.rabana24,R.raw.rabana25,R.raw.rabana26,R.raw.rabana27,R.raw.rabana28,R.raw.rabana29,
                R.raw.rabana30,R.raw.rabana31,R.raw.rabana32,R.raw.rabana33,R.raw.rabana34,R.raw.rabana35,R.raw.rabana36,R.raw.rabana37,R.raw.rabana38,R.raw.rabana39,
                R.raw.rabana40};
        Cursor cursorAr =databaseRabanna.rawQuery("select arabic,malagasy from Douan where id="+(i+1),new String[]{});

        while (cursorAr.moveToNext()) {
            rabannaModel = new RabannaModel();
            rabannaModel.setRabanna(cursorAr.getString(0));
            rabannaModel.setTranslation(cursorAr.getString(1));
            rabannaModel.setAudio(media[i]);
            AllGlobal.add(rabannaModel);
        }

        databaseRabanna.close();

        return AllGlobal;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
if (recyclerAyahAdapter.EN==true){
    recyclerAyahAdapter.mediaPlayer.release();
}
    }
}
