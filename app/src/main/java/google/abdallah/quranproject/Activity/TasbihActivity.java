package google.abdallah.quranproject.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import google.abdallah.quranproject.BuildConfig;
import google.abdallah.quranproject.R;

public class TasbihActivity extends AppCompatActivity {
TextView Arabic,Translator;
ConstraintLayout sharedlayout;
Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasbih);
        Arabic=findViewById(R.id.textArabe);
        Translator=findViewById(R.id.textMalagasy);
        sharedlayout=findViewById(R.id.shareImg);
        btn=findViewById(R.id.forsharing);
        String ar=getIntent().getStringExtra("Arabic");
       String tr=getIntent().getStringExtra("Translation");
       Arabic.setText(ar);
       Translator.setText(tr);
btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        shared();
    }
});
    }
    void shared(){
        Bitmap bitmap=getBitmapFromView(sharedlayout);
        Uri uri =saveImage(bitmap);
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/png");
        startActivity(intent);

    }
    private Uri saveImage(Bitmap image) {
        //TODO - Should be processed in another thread

        Uri uri = null;
        try {

            File file = new File(getExternalCacheDir(), "shareb1.png");

            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName()+".provider", file);

        } catch (IOException e) {
            Log.d("TAG", "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;
    }
    @SuppressLint("ResourceAsColor")
    private Bitmap getBitmapFromView(View view){
        Bitmap bitmap=Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        Drawable bg=view.getBackground();
        if (bg!=null){
            bg.draw(canvas);
        }else {
            canvas.drawColor(android.R.color.white);
        }
        view.draw(canvas);


        return bitmap;
    }
}