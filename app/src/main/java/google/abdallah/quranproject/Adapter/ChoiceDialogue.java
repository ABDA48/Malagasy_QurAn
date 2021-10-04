package google.abdallah.quranproject.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import google.abdallah.quranproject.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ChoiceDialogue extends DialogFragment {
    int position=0;
    String list[];
    public    ChoiceDialogue(){}
    public ChoiceDialogue(String list[]){
        this.list=list;
    }
    public interface  OnSingleListner{
        void onPositive(int position,String list[]);
        void onNegative(int position);
    }
OnSingleListner listner;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listner=(OnSingleListner)context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        builder.setSingleChoiceItems(list, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                   position=i;
            }
        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     listner.onPositive(position,list);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listner.onNegative(2);
                    }
                });

        return builder.create();
    }
}
