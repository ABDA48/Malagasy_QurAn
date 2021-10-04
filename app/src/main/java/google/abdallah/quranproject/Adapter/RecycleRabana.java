package google.abdallah.quranproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import google.abdallah.quranproject.Activity.JuzActivity;
import google.abdallah.quranproject.Activity.SurahActivity;
import google.abdallah.quranproject.Model.RabannaModel;
import google.abdallah.quranproject.R;

public class RecycleRabana extends RecyclerView.Adapter<RecycleRabana.ViewHolder> {
    Context context;
    List<RabannaModel> rabannaModels;

    public RecycleRabana(Context context, List<RabannaModel> rabannaModels) {
        this.context = context;
        this.rabannaModels = rabannaModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.itemrabana,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
     holder.rabanna.setText(rabannaModels.get(position).getRabanna());
     holder.number.setText(rabannaModels.get(position).getNumber());
     holder.place.setText(rabannaModels.get(position).getPlace());
     holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent intent = new Intent(context, JuzActivity.class);
             intent.putExtra("pos", position);
             context.startActivity(intent);
         }
     });
    }

    @Override
    public int getItemCount() {
        return rabannaModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
TextView number;
TextView rabanna;
TextView place;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number=itemView.findViewById(R.id.numberSurah);
            rabanna=itemView.findViewById(R.id.namerabana);
            place=itemView.findViewById(R.id.rabanna);
        }
    }
}
