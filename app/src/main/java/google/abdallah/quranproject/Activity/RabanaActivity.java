package google.abdallah.quranproject.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import google.abdallah.quranproject.Adapter.RecycleRabana;
import google.abdallah.quranproject.Model.RabannaModel;
import google.abdallah.quranproject.R;

public class RabanaActivity extends AppCompatActivity {
RecyclerView recyclerView;
RecycleRabana recycleRabana;
List<RabannaModel> rabannaModels;
MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rabana);
        recyclerView=findViewById(R.id.recyclerabana);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rabannaModels=new ArrayList<>();
        String []places=this.getResources().getStringArray(R.array.rabanna);
        for(int i=1;i<41;i++){
            rabannaModels.add(new RabannaModel(String.valueOf(i),"Rabanna "+String.valueOf(i),places[i-1]));
        }
        recycleRabana=new RecycleRabana(this,rabannaModels);
        recyclerView.setAdapter(recycleRabana);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}