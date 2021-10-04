package google.abdallah.quranproject.Activity;
import google.abdallah.quranproject.Adapter.FragAdapter;
import google.abdallah.quranproject.Adapter.RabannaRecycler;
import google.abdallah.quranproject.Adapter.RecycleRabana;
import google.abdallah.quranproject.Fragayat.Juzfrag;
import google.abdallah.quranproject.Model.RabannaModel;
import google.abdallah.quranproject.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class JuzActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    ViewPager2 viewPager2;
    TabLayout tabLayout;
      int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juz);
        toolbar=findViewById(R.id.toolbar);
        viewPager2=findViewById(R.id.viewpager);
        tabLayout=findViewById(R.id.toolbarLayout);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
          pos=getIntent().getIntExtra("pos",0);


        FragAdapter fragAdapter= new FragAdapter(this);
        for (int i = 0; i <40 ; i++) {
            fragAdapter.addFragMent(Juzfrag.newInstance(i));
        }
        viewPager2.setAdapter(fragAdapter);
        tabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });
        ViewCompat.setLayoutDirection(viewPager2, ViewCompat.LAYOUT_DIRECTION_RTL);
        viewPager2.setCurrentItem(pos,true);

        // tabLayout.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);
        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText("Rabanna "+(position+1));
            }
        });
        tabLayoutMediator.attach();
    }


}
