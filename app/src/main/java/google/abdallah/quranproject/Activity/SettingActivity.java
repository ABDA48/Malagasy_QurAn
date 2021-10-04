package google.abdallah.quranproject.Activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import google.abdallah.quranproject.Adapter.ChoiceDialogue;
import google.abdallah.quranproject.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class SettingActivity extends AppCompatActivity implements ChoiceDialogue.OnSingleListner{
TextView theme,translation,dowloadManager,location,qirah;
public static SharedPreferences preferencesQ,preferencesT,preferencesTh;SharedPreferences.Editor editor;
public static String Qirah="SettingQ";
    public static String Translation="SettingTr";
    public static String Theme="SettingTheme";
public static String KeyQirat="qirah";
    public static String KeyTranslation="qirah";
    public static String Keytheme="qirah";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getChoosen());
        setContentView(R.layout.activity_setting);
        theme=findViewById(R.id.theme);
        translation=findViewById(R.id.translation);
        dowloadManager=findViewById(R.id.dowloadManager);
        location=findViewById(R.id.location);
        qirah=findViewById(R.id.qira);
        theme.setOnClickListener(clickListener);
        translation.setOnClickListener(clickListener);
        dowloadManager.setOnClickListener(clickListener);
       location.setOnClickListener(clickListener);
       qirah.setOnClickListener(clickListener);
        preferencesQ=getSharedPreferences(Qirah, Context.MODE_PRIVATE);
        preferencesT=getSharedPreferences(Translation, Context.MODE_PRIVATE);
        preferencesTh=getSharedPreferences(Theme,Context.MODE_PRIVATE);


    }

    View.OnClickListener clickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
              String[] list;
            switch (id){
                case R.id.theme:
                     list=getResources().getStringArray(R.array.ThemeChoice);
                    showDialogue(list);
                    break;
                case R.id.translation:
                    list=getResources().getStringArray(R.array.Translation);
                    showDialogue(list);
                    break;
                case R.id.qira:

                    list=getResources().getStringArray(R.array.Qirah);
                    showDialogue(list);
                    break;
            }

        }
    };

    private void showDialogue(String[] list) {
        DialogFragment fragment=new ChoiceDialogue(list);
        fragment.setCancelable(false);
        fragment.show(getSupportFragmentManager(),"Tag");
    }

    @Override
    public void onPositive(int position, String[] list) {
      if (Arrays.equals(list, getResources().getStringArray(R.array.Qirah))){
          editor=preferencesQ.edit();
          editor.putString(KeyQirat,list[position]);
          editor.commit();
          Toast.makeText(this, "I am "+"Qirah", Toast.LENGTH_SHORT).show();
      }else if(Arrays.equals(list, getResources().getStringArray(R.array.Translation))){

          editor=preferencesT.edit();
          editor.putString(KeyTranslation,list[position]);
          editor.commit();
        }else if(Arrays.equals(list, getResources().getStringArray(R.array.ThemeChoice))){
          editor=preferencesTh.edit();
          editor.putString(Keytheme,list[position]);
          editor.commit();
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) recreate();
          else {
              Intent i = new Intent(SettingActivity.this,SettingActivity.class);
              showDialogue(list);
              startActivity(i);
          }
      }
    }

    @Override
    public void onNegative(int position) {
    }
    public int getChoosen(){ preferencesTh=getSharedPreferences(Theme,Context.MODE_PRIVATE);
      String string= preferencesTh.getString(Keytheme," ");
        Toast.makeText(this, "Theme  "+string, Toast.LENGTH_SHORT).show();
       if (string.equals("Theme 2")){
           return R.style.AppTheme2;
       }else {
           return R.style.AppTheme;
       }

    }

}
