package google.abdallah.quranproject.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.slider.Slider;
import google.abdallah.quranproject.Compass.CompassActivity;
import google.abdallah.quranproject.R;
public class Settings extends AppCompatActivity {
Slider slider;
Slider slider2;
TextView bismisetting,bismisetting2;
TextView t2,t3;
BottomNavigationView bottomNavigationView;
public static String Translation="SettingTr";
public static SharedPreferences preferencesT;
SharedPreferences.Editor editor;
RadioGroup spinner;
RadioGroup spinner2;
CardView facebook,twitter,gmail;
    MaterialToolbar toolbar;
RadioButton r1,r2,bassit,bassit2,bassit3,bassit4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        slider=findViewById(R.id.slider);
        slider2=findViewById(R.id.slider2);
        spinner2=findViewById(R.id.Qirah);
        toolbar=findViewById(R.id.toolbar);
        t2=findViewById(R.id.fontsizeAr);
        t3=findViewById(R.id.fontsizeMg);
        r1=findViewById(R.id.radio1);
        r2=findViewById(R.id.radio2);
        bassit=findViewById(R.id.bassit);
        bassit2=findViewById(R.id.bassit2);
        bassit3=findViewById(R.id.bassit3);
        bassit4=findViewById(R.id.bassit4);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        t2.setText(String.valueOf(textSizeAr()));
        t3.setText(String.valueOf(textSize()));

        bismisetting=findViewById(R.id.bismisetting);
        preferencesT=getSharedPreferences(Translation, Context.MODE_PRIVATE);
        facebook=findViewById(R.id.facebook);

        spinner=findViewById(R.id.spinner);
        bismisetting2=findViewById(R.id.bismisetting2);
        bottomNavigationView=findViewById(R.id.buttom);

        slider.setValue(textSizeAr());
        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                bismisetting.setTextSize(slider.getValue());
                editor=preferencesT.edit();
                editor.putFloat("SizeAr",slider.getValue());
                editor.commit();
                t2.setText(String.valueOf(slider.getValue()));

            }
        });
        slider2.setValue(textSize());
        slider2.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                bismisetting2.setTextSize(slider.getValue());
                editor=preferencesT.edit();
                editor.putFloat("Size",slider.getValue());
                editor.commit();
                t3.setText(String.valueOf(slider.getValue()));
            }
        });
    final String translation[]=getResources().getStringArray(R.array.Translation);
        if (Translator().equals("French")){
            r2.setChecked(true);
        }else {
            r1.setChecked(true);
        }

        switch (Qirah()){
            case "Abdul_Basit_Murattal_64kbps":
                bassit.setChecked(true);
                break;
            case "Abdurrahmaan_As-Sudais_192kbps":
                bassit2.setChecked(true);
                break;
            case "Abdullah_Matroud_128kbps":
                bassit3.setChecked(true);
                break;
            case "AbdulSamad_64kbps_QuranExplorer.Com":
                bassit4.setChecked(true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + Qirah());
        }
        spinner.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
             switch (spinner.getCheckedRadioButtonId()){
                 case R.id.radio1:
                     editor=preferencesT.edit();
                     editor.putString("T",translation[0]);
                     editor.commit();
                     break;
                 case R.id.radio2:
                     editor=preferencesT.edit();
                     editor.putString("T",translation[1]);
                     editor.commit();
                     break;
             }
            }
        });
        final String Qirah[]=getResources().getStringArray(R.array.QirahAudio);
        final String Qirah2[]=getResources().getStringArray(R.array.Qirah);
        spinner2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (spinner2.getCheckedRadioButtonId()){
                    case R.id.bassit:
                        editor=preferencesT.edit();
                        editor.putString("Qirah",Qirah[0]);
                        editor.commit();
                     break;
                    case R.id.bassit2:
                        editor=preferencesT.edit();
                        editor.putString("Qirah",Qirah[1]);
                        editor.commit();
                        break;
                    case R.id.bassit3:
                        editor=preferencesT.edit();
                        editor.putString("Qirah",Qirah[2]);
                        editor.commit();
                        break;
                    case R.id.bassit4:
                        editor=preferencesT.edit();
                        editor.putString("Qirah",Qirah[3]);
                        editor.commit();
                        break;
                }
            }
        });
facebook.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       /* Uri uri=Uri.parse("https://www.facebook.com/profile.php?id=100007672637038");
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);

        */
    }
});
        bottomNavigationView.setSelectedItemId(R.id.setting);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.setting:
                        Toast.makeText(Settings.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.favori:
                        Intent   intentfav=new Intent(Settings.this, FavoriesActivity.class);
                        startActivity(intentfav);
                        break;
                    case R.id.home:
                        Intent  home=new Intent(Settings.this, MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.prayers:
                        Intent   intent=new Intent(Settings.this, PrayerActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.qibla:
                        Intent   pr=new Intent(Settings.this, CompassActivity.class);
                        startActivity(pr);
                        break;
                }
                return true;
            }
        });
    }
    float textSize(){
        preferencesT=this.getSharedPreferences(Translation, Context.MODE_PRIVATE);
        float r=preferencesT.getFloat("Size",25);
        return r;
    }
    float textSizeAr(){
        preferencesT=this.getSharedPreferences(Translation, Context.MODE_PRIVATE);
        float r=preferencesT.getFloat("SizeAr",25);
        return r;
    }
    String Translator(){
        preferencesT=this.getSharedPreferences(Translation, Context.MODE_PRIVATE);
       String translate=preferencesT.getString("T","Malagasy");
        return translate;
    }
    String Qirah(){
        preferencesT=this.getSharedPreferences(Translation, Context.MODE_PRIVATE);
        String q=preferencesT.getString("Qirah","AbdulSamad_64kbps_QuranExplorer.Com");
        return q;
    }
}