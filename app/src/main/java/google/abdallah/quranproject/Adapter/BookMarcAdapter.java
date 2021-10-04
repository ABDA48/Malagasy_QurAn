package google.abdallah.quranproject.Adapter;

import android.content.Context;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import google.abdallah.quranproject.Model.BookmarkModel;
import google.abdallah.quranproject.R;

public class BookMarcAdapter  extends RecyclerView.Adapter<BookMarcAdapter.viewHolder> {
    List<BookmarkModel> bookmarkModels;
    Context context;

    public BookMarcAdapter(List<BookmarkModel> bookmarkModels, Context context) {
        this.bookmarkModels = bookmarkModels;
        this.context = context;
    }

    @NonNull
    @Override
    public BookMarcAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.itemfavories,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookMarcAdapter.viewHolder holder, int position) {
   holder.nameoftitle.setText(bookmarkModels.get(position).getTitle());
   holder.ayat.setText(bookmarkModels.get(position).getVerseNumber());
   holder.surat.setText(bookmarkModels.get(position).getNameofsurat());

   holder.clicked.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {

       }
   });
    }

    @Override
    public int getItemCount() {
        return bookmarkModels.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder{
        TextView nameoftitle,surat,ayat;
        CardView clicked;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            clicked=itemView.findViewById(R.id.clicked);
            nameoftitle=itemView.findViewById(R.id.title);
            surat=itemView.findViewById(R.id.nameofsurat);
            ayat=itemView.findViewById(R.id.numberofverse);

        }
    }
}
