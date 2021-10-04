package google.abdallah.quranproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import google.abdallah.quranproject.Adapter.JumpDialogue;
import google.abdallah.quranproject.Adapter.PagerAdapter;
import google.abdallah.quranproject.Compass.CompassActivity;
import google.abdallah.quranproject.R;
import google.abdallah.quranproject.fragment.Bookmark;
import google.abdallah.quranproject.fragment.Juz;
import google.abdallah.quranproject.fragment.surah;

import static google.abdallah.quranproject.Activity.SettingActivity.Keytheme;
import static google.abdallah.quranproject.Activity.SettingActivity.Theme;
import static google.abdallah.quranproject.Activity.SettingActivity.preferencesTh;


public class MainActivity extends AppCompatActivity {// implements JumpDialogue.JumpListner {
MaterialToolbar toolbar;
ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TabLayout tabLayout;
    JumpDialogue jumpDialogue;
    BottomNavigationView bottomNavigationView;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getChoosen());
        setContentView(R.layout.activity_main);
toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main.class));
            }
        });
viewPager=findViewById(R.id.viewpager);
tabLayout=findViewById(R.id.toolbarLayout);
bottomNavigationView=findViewById(R.id.buttom);

imageView=findViewById(R.id.search);
imageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(MainActivity.this,ResearchActivity.class);
         startActivity(intent);
    }
});
pagerAdapter=new PagerAdapter(getSupportFragmentManager());
pagerAdapter.addFrag(new surah(),"Surah");
pagerAdapter.addFrag(new Juz(),"JUZ");

viewPager.setAdapter(pagerAdapter);
tabLayout.setupWithViewPager(viewPager);
bottomNavigationView.setSelectedItemId(R.id.home);
bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.setting:
                Intent   intent=new Intent(MainActivity.this,Settings.class);
                startActivity(intent);
                break;
            case R.id.favori:
                Intent   intentfav=new Intent(MainActivity.this,FavoriesActivity.class);
                startActivity(intentfav);
                break;
            case R.id.home:
                Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.prayers:
               Intent   pr=new Intent(MainActivity.this, PrayerActivity.class);
                startActivity(pr);

                break;
            case R.id.qibla:
                Intent compass=new Intent(MainActivity.this, CompassActivity.class);
                startActivity(compass);
                break;
            default:

        }
        return true;
    }
});
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_sitting,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.setting:
                Intent intent2=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent2);
             break;
            case R.id.ratting:
                Intent intent=new Intent(MainActivity.this,ResearchActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                Toast.makeText(MainActivity.this, "about", Toast.LENGTH_SHORT).show();
                break;
            case R.id.search:
                jumpDialogue=new JumpDialogue();
                jumpDialogue.show(this.getSupportFragmentManager(),"open");
                break;

        }
        return true;
    }

    @Override
    public void jumpMethode(int i, int j) {
        Toast.makeText(this, "sourat "+i+" "+"ayat "+j, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(MainActivity.this,Main2Activity.class);
        intent.putExtra("int",i);
        intent.putExtra("name","jump");
        intent.putExtra("position",j);
        startActivity(intent);
    }
    */
    public int getChoosen(){
        preferencesTh=getSharedPreferences(Theme, Context.MODE_PRIVATE);
        String string= preferencesTh.getString(Keytheme," ");
       // Toast.makeText(this, "Theme  "+string, Toast.LENGTH_SHORT).show();
        if (string.equals("Theme 2")){
            return R.style.AppTheme2;
        }else {
            return R.style.AppTheme;
        }

    }
}
