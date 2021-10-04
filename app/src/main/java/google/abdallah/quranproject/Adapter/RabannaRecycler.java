package google.abdallah.quranproject.Adapter;

import android.content.Context;
import android.icu.text.UFormat;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import google.abdallah.quranproject.Activity.JuzActivity;
import google.abdallah.quranproject.Fragayat.Juzfrag;
import google.abdallah.quranproject.Model.RabannaModel;
import google.abdallah.quranproject.R;

public class RabannaRecycler extends RecyclerView.Adapter<RabannaRecycler.ViewH> {
    Context context;
    List<RabannaModel> model;
   public MediaPlayer mediaPlayer;
   public boolean EN=false;
boolean f=false;
    public RabannaRecycler(Context context, List<RabannaModel> model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewH(LayoutInflater.from(context).inflate(R.layout.juz1,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewH holder, final int position) {
    holder.textAr.setText(model.get(position).getRabanna());
    holder.textMg.setText(model.get(position).getTranslation());

    holder.actionButton.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            EN=true;
          if (mediaPlayer==null || f==false){
             mediaPlayer= MediaPlayer.create(context,model.get(position).getAudio());
              f=true;
              mediaPlayer.start();
              holder.actionButton.setImageResource(R.drawable.rabana_stop);
          }else{
              f=false;
              mediaPlayer.stop();
              holder.actionButton.setImageResource(R.drawable.rabanna_play);
          }

          mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
              @Override
              public void onCompletion(MediaPlayer mediaPlayer) {
                  mediaPlayer.stop();
                  holder.actionButton.setImageResource(R.drawable.rabanna_play);
                  f=false;
              }
          });
        }
    });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    class ViewH extends RecyclerView.ViewHolder{
TextView textAr;
TextView textMg;
FloatingActionButton actionButton;
        public ViewH(@NonNull View itemView) {
            super(itemView);
            textAr=itemView.findViewById(R.id.Ar);
            textMg=itemView.findViewById(R.id.Mg);
            actionButton=itemView.findViewById(R.id.clickAudio);
        }
    }
}
