package google.abdallah.quranproject.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import google.abdallah.quranproject.R;

public class PrayerSettingDialogue extends AppCompatDialogFragment  {
    LinearLayout calcul;TextView progresstext; TextView methode;SeekBar seekBar;
     SharedPreferences preferences;public static String nameVolume="Prayer",keyVolume="Azan",Keymethode="methode",KeyMethodeName="nameMethode";
    String[]listMethode={"Shia Ithna-Ansari","University of Islamic Sciences, Karachi","Islamic Society of North America","Muslim World League",
            "Umm Al-Qura University, Makkah","Egyptian General Authority of Survey","Institute of Geophysics, University of Tehran","Gulf Region","Kuwait",
            "Qatar","Majlis Ugama Islam Singapura, Singapore","Union Organization islamic de France"," Diyanet İşleri Başkanlığı, Turkey",
            "Spiritual Administration of Muslims of Russia"};
    int i;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view=getActivity().getLayoutInflater().inflate(R.layout.chosesettingprayer,null);
         calcul=view.findViewById(R.id.Methode);
        progresstext=view.findViewById(R.id.progress);
        methode=view.findViewById(R.id.methode);
         seekBar=view.findViewById(R.id.seekbar);
         preferences=getActivity().getSharedPreferences(nameVolume,Context.MODE_PRIVATE);
         final SharedPreferences.Editor editor=preferences.edit();
         seekBar.setMax(100);
         seekBar.setProgress(getVolume());
        progresstext.setText(getVolume()+"%");
         seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
             @Override
             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                 progresstext.setText(progress+"%");
             }
             @Override
             public void onStartTrackingTouch(SeekBar seekBar) {

             }
             @Override
             public void onStopTrackingTouch(SeekBar seekBar) {

             }
         });

         builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 i=seekBar.getProgress();
                 editor.putInt(keyVolume,i);
                 editor.commit();
             }
         });
         calcul.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 showDialogue(listMethode);
             }
         });
         getMthode();
         builder.setView(view);
        return builder.create();
    }
    private void showDialogue(String[] list) {
        DialogFragment fragment=new ChoiceDialogue(list);
        fragment.setCancelable(false);
        fragment.show(getActivity().getSupportFragmentManager(),"Tag");
    }
public int getVolume(){
    preferences=getActivity().getSharedPreferences(nameVolume,Context.MODE_PRIVATE);
    return preferences.getInt(keyVolume,20);
}
    public void getMthode(){
        SharedPreferences   preferences=getActivity().getSharedPreferences(nameVolume,Context.MODE_PRIVATE);
        methode.setText(preferences.getString(KeyMethodeName,"Shia Ithna-Ansari"));
    }
}
