package google.abdallah.quranproject.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.util.ArrayList;
import java.util.List;

import google.abdallah.quranproject.Activity.Main2Activity;
import google.abdallah.quranproject.Activity.ShatredActivity;
import google.abdallah.quranproject.Activity.SurahActivity;
import google.abdallah.quranproject.Activity.TasbihActivity;
import google.abdallah.quranproject.Database.DatabaseTrack;
import google.abdallah.quranproject.Database.DefaultFetch;
import google.abdallah.quranproject.Model.BookmarkModel;
import google.abdallah.quranproject.Model.ModelSurat;
import google.abdallah.quranproject.Model.TrackModel;
import google.abdallah.quranproject.R;


public class RecyclerSurat extends RecyclerView.Adapter<RecyclerSurat.ViewHolder> {
    List<ModelSurat> modelSurats;
    List<View> item = new ArrayList<>();
    Context context;
    private ModelSurat modelSurat;
    SQLiteDatabase databasedef;
    SQLiteOpenHelper openHelper;
    Snackbar snackbar;
    public boolean Flag=false;
    public MediaPlayer player;
    int bismifatiha;
    String bismi;
    String suratName, versenumber;
    FragmentManager fragmentManager;
    Snackbar snackbar2;
    List<ViewHolder> viewHolders = new ArrayList<>();
    SQLiteDatabase databaseTrack;
    List<TrackModel> trackModels; List<MediaPlayer>mediaPlayers=new ArrayList<>();
    SQLiteOpenHelper openHelpTrack;
    LinearLayoutManager manager;
    public static String Translation="SettingTr";
    public static SharedPreferences preferencesT;
    int j = 0;

    ThinDownloadManager thinDownloadManager;
    ProgressDialog downloadprogress;
    AudioManager audioManager;
    public RecyclerSurat(LinearLayoutManager manager, List<ModelSurat> modelSurats, Context context, int bismifatiha, FragmentManager fragmentManager) {
        this.modelSurats = modelSurats;
        this.context = context;
        bismi = context.getResources().getString(R.string.bismi);
        this.manager = manager;
        this.fragmentManager = fragmentManager;
        openHelpTrack = new DatabaseTrack(context);
        databaseTrack = openHelpTrack.getWritableDatabase();
        this.bismifatiha = bismifatiha;
        thinDownloadManager=new ThinDownloadManager();
        downloadprogress=new ProgressDialog(context);
        downloadprogress.setMax(100);
        downloadprogress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

    }


    @NonNull
    @Override
    public RecyclerSurat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(context).inflate(R.layout.layoutayats, parent, false);
        item.add(view);
        return new ViewHolder(view);

    }
    float textSizeAr(){
        preferencesT=context.getSharedPreferences(Translation, Context.MODE_PRIVATE);
        float r=preferencesT.getFloat("SizeAr",30);
        return r;
    }
    float textSize(){
        preferencesT=context.getSharedPreferences(Translation, Context.MODE_PRIVATE);
        float r=preferencesT.getFloat("Size",22);
        return r;
    }
    String Qirah(){
        preferencesT=context.getSharedPreferences(Translation, Context.MODE_PRIVATE);
        String q=preferencesT.getString("Qirah","AbdulSamad_64kbps_QuranExplorer.Com");
        return q;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerSurat.ViewHolder holder, final int position) {
        holder.number.setText(modelSurats.get(position).getNumberSurah());
        holder.textAr.setText(modelSurats.get(position).getSurahNameAr());
        holder.textAr.setTextSize( textSizeAr());
        holder.textFr.setText(modelSurats.get(position).getSurahName());
        holder.textFr.setTextSize(textSize());
        holder.setIsRecyclable(false);
       viewHolders.add(holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar = Snackbar.make(holder.itemView, "", Snackbar.LENGTH_INDEFINITE);
                View view = LayoutInflater.from(context).inflate(R.layout.snacklayout, null);
                LinearLayout play = view.findViewById(R.id.play);
                LinearLayout share = view.findViewById(R.id.share);
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context, ShatredActivity.class);
                      intent.putExtra("Arabic",modelSurats.get(position).getSurahNameAr());
                      intent.putExtra("Translation",modelSurats.get(position).getSurahName());
                        context.startActivity(intent);
                    }
                });

                LinearLayout bookmark = view.findViewById(R.id.bookmark);
                ImageView imageView=view.findViewById(R.id.close);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });


               bookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                        String[] strfr = context.getResources().getStringArray(R.array.surat_fr);
                        suratName = strfr[bismifatiha];
                        versenumber = String.valueOf(position + 1);
                        BokkmakDialogue bokkmakDialogue = new BokkmakDialogue(new BookmarkModel("", suratName, versenumber));
                        bokkmakDialogue.show(fragmentManager, "open");
                        Toast.makeText(context, modelSurats.get(position).getSurahNameAr(), Toast.LENGTH_SHORT).show();
                    }
                });
                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      if (mediaPlayers!=null){
                          for (MediaPlayer m:mediaPlayers)
                              m.release();
                      }
                        snackbar2 = Snackbar.make(holder.itemView, "", Snackbar.LENGTH_INDEFINITE);
                        final View view1 = LayoutInflater.from(context).inflate(R.layout.snack, null);
                        ImageView stop = view1.findViewById(R.id.imageView5);
                        final ImageView playpause = view1.findViewById(R.id.imageView3);
                        ImageView next = view1.findViewById(R.id.imageView4);
                        ImageView prev = view1.findViewById(R.id.imageView2);
                        Slider slider=view1.findViewById(R.id.slidervolume);
                        audioManager= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
                            @Override
                            public void onStartTrackingTouch(@NonNull Slider slider) {

                            }

                            @Override
                            public void onStopTrackingTouch(@NonNull Slider slider) {
                                int v= (int) slider.getValue();
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,v,0);

                            }
                        });
                        stop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                player.stop();
                                snackbar2.dismiss();
                                player.release();
                            }
                        });
                        playpause.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (player.isPlaying()){
                                    playpause.setImageResource(R.drawable.ic_play);
                                    player.pause();

                                }else {
                                    player.start();
                                    playpause.setImageResource(R.drawable.ic_pause_black_24dp);
                                }
                            }
                        });
                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                j++;
                                if (j<viewHolders.size()) {
                                    player.stop();
                                    PlayList(j);
                                }
                            }
                        });
                        prev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                j--;
                                if (j>0){
                                    player.stop();
                                    PlayList(j);
                                }else {
                                    j=0;
                                    player.stop();
                                    PlayList(0);
                                }
                            }
                        });
                        Snackbar.SnackbarLayout snackbarLayout  = (Snackbar.SnackbarLayout) snackbar2.getView();
                        snackbarLayout.setPadding(0, 0, 0, 0);
                        snackbarLayout.addView(view1);
                        snackbarLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        snackbar2.show();
                        PlayList(position);

                    }
                });

                Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
                snackbarLayout.setPadding(0, 0, 0, 0);
                snackbarLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                snackbarLayout.addView(view);
                snackbar.show();
            }
        });
        if (position % 2 != 0) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.cardColor));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorwhite));
        }

    }


    public boolean Take(int i){
        trackModels=new ArrayList<>();
        Cursor c=databaseTrack.rawQuery("select ayah,qirah,uri,juz from QuranAudio where surah="+i+ "   and  qirah=\'"+Qirah()+"\'",new String[]{});
        TrackModel trackModel;
        while (c.moveToNext()){
            trackModel=new TrackModel();
            trackModel.setAyah(c.getString(0));
            trackModel.setQirah(c.getString(1));
            trackModel.setUri(c.getString(2));
            trackModel.setJuz(c.getString(3));
            trackModels.add(trackModel);
        }
        if (trackModels.size()==0){
            final AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("Download");
            builder.setMessage("You have to Download the Audio");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    snackbar2.dismiss();
                    DownloadPreparation();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    snackbar2.dismiss();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
            return false;
        }else {
            int l=0;
            Toast.makeText(context, "Download finished", Toast.LENGTH_SHORT).show();
            for (TrackModel v:trackModels){
                modelSurats.get(l).setUri(Uri.parse(v.getUri()));
                Log.d("TAG:H", "Take: "+trackModels.size()+"--> "+modelSurats.get(l).getUri());
                l++;
                if (l>modelSurats.size()){
                    break;
                }
            }
            return  true;
        }
    }
    public void DownloadPreparation() {
        TrackModel trackModel;
        //String qirah="AbdulSamad_64kbps_QuranExplorer.Com";
        String qirah=Qirah();
        String source="https://everyayah.com/data/";
        int n=bismifatiha+1;
        int pos=postition(bismifatiha);
        for (int i = 1; i < pos+1; i++) {
            trackModel=new TrackModel();
            Uri uri = Uri.parse(source + qirah + "/" + Fetchayat(n) + Fetchayat(i) + ".mp3");
            String dest=context.getExternalCacheDir().toString() +"/"+qirah+"/"+Fetchayat(n) + Fetchayat(i)+".mp3";
            final Uri destination = Uri.parse(dest);
            Log.d("Tag:emp",""+uri);
            Log.d("Tag:w",""+destination);
            trackModel.setQirah(qirah);trackModel.setUri(String.valueOf(uri));
            trackModel.setSurah(String.valueOf(n));
            trackModel.setAyah(String.valueOf(i));trackModel.setJuz(modelSurats.get(i-1).getJuz());
            Audio( trackModel.getSurah(),trackModel.getAyah(),trackModel.getJuz(),trackModel.getQirah(),trackModel.getUri());
            DownloadProcess(destination,trackModel);

        }
    }
    private void DownloadProcess(final Uri destination, final TrackModel trackModel) {
        final  int i=Integer.parseInt(trackModel.getAyah());
        DownloadRequest downloadRequest=new DownloadRequest(Uri.parse(trackModel.getUri()))
                .setRetryPolicy(new DefaultRetryPolicy())
                .setDestinationURI(destination)
                .setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        downloadprogress.setProgress(setP(i));
                        downloadprogress.setTitle("Downloading...");
                        downloadprogress.show();
                        if (i==postition(bismifatiha)){
                            downloadprogress.hide();
                            Take(bismifatiha+1);
                        }
                    }
                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {

                    }

                    @Override
                    public void onProgress(int id, long totalBytes, long downloadedBytes, int progress) {

                    }
                });
        thinDownloadManager.add(downloadRequest);
    }
    public String Fetchayat(int i){
        String s;
        if (i<10){
            s="00"+i;
        }else if(i<100){
            s="0"+i;
        }else {
            s=""+i;
        }
        return s;
    }
    public int setP(int i){
        return (i*100)/modelSurats.size();
    }
    public int postition(int i){
        String []number=context.getResources().getStringArray(R.array.verses_number);
        return Integer.parseInt(number[i]);
    }
    public void Audio(String surah,String ayah,String juz,String qirah,String uri){
        ContentValues contentValues=new ContentValues();
        contentValues.put("surah",surah);
        contentValues.put("ayah",ayah);
        contentValues.put("juz",juz);
        contentValues.put("qirah",qirah);
        contentValues.put("uri",uri);
        long t= databaseTrack.insert("QuranAudio",null,contentValues);
        if (t<0){
            Toast.makeText(context, "Error Occurd ", Toast.LENGTH_SHORT).show();
        }else {
            Log.d("Audio:TAG", "Audio:done");
        }
    }

    private void PlayList(final int position) {

        if (modelSurats.get(position).getUri() != null) {

            player = MediaPlayer.create(context, modelSurats.get(position).getUri());
            mediaPlayers.add(player);

            for (ViewHolder Holder : viewHolders) {
                Holder.itemView.setBackgroundColor(Color.WHITE);
            }
            viewHolders.get(position).itemView.setBackgroundColor(Color.GREEN);
            manager.scrollToPositionWithOffset(position, 0);
            player.start();
            Flag=true;
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    int i = position;
                    i++;
                    if (i < viewHolders.size()) {
                        PlayList(i);
                        j = i;
                    } else {
                        viewHolders.get(position).itemView.setBackgroundColor(Color.WHITE);
                        snackbar2.dismiss();
                    }
                }

            });

        } else {
            if (Take(bismifatiha+1)) {
                PlayList(position);
            }
        }
    }

    private void loveAdapter(int position) {
        String[] strfr = context.getResources().getStringArray(R.array.surat_fr);
        suratName = strfr[bismifatiha];
        versenumber = String.valueOf(position + 1);
        System.out.println("I am" + suratName);
        BokkmakDialogue bokkmakDialogue = new BokkmakDialogue(new BookmarkModel("", suratName, versenumber));
        //   bokkmakDialogue.show(fragmentManager, "open");
    }


    class thrLast extends Thread {
        int pos;

        thrLast(int pos) {
            this.pos = pos;
        }

        @Override
        public void run() {
            super.run();
            lastRead(pos);
        }
    }

    private void lastRead(int position) {
        openHelper = new DefaultFetch(context);
        databasedef = openHelper.getWritableDatabase();
        String[] strfr = context.getResources().getStringArray(R.array.surat_fr);
        suratName = strfr[bismifatiha];
        versenumber = String.valueOf(position + 1);

        ContentValues contentValues = new ContentValues();
        contentValues.put("sourah", suratName);
        contentValues.put("ayat", versenumber);
        contentValues.put("title", "last");
        databasedef.insert("Book_Last", null, contentValues);
    }


    @Override
    public int getItemCount() {
        return modelSurats.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView number;
        TextView textAr;
        TextView textFr;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textAr = itemView.findViewById(R.id.textAr);
            number = itemView.findViewById(R.id.number);
            textFr = itemView.findViewById(R.id.textFr);
        }
    }
}



