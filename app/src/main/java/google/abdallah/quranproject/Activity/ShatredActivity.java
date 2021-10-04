package google.abdallah.quranproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import google.abdallah.quranproject.R;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShatredActivity extends AppCompatActivity {
    TextView Arabic,Translator;
    ConstraintLayout sharedlayout;
    Button btn;
    Slider slider1;
    Slider slide2;
    ImageView bg1,bg2,bg3;
    String imagebg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shatred);
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
        slider1=findViewById(R.id.slider);
        slide2=findViewById(R.id.slider2);
        bg1=findViewById(R.id.bg1);
        bg2=findViewById(R.id.bg2);
        bg3=findViewById(R.id.bg3);
        imagebg="img2.png";
        bg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedlayout.setBackground(getResources().getDrawable(R.drawable.img));
                imagebg="img.png";
            }
        });
        bg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedlayout.setBackground(getResources().getDrawable(R.drawable.img2));
                imagebg="img2.png";
            }
        });
        bg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedlayout.setBackground(getResources().getDrawable(R.drawable.img3));
                imagebg="img3.png";
            }
        });
        slider1.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Arabic.setTextSize(slider.getValue());
                Toast.makeText(ShatredActivity.this, ""+slider.getValue(), Toast.LENGTH_SHORT).show();
            }
        });
        slide2.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Translator.setTextSize(slider.getValue());
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

            File file = new File(getExternalCacheDir(), imagebg);

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