package com.example.voiceguideassistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.example.voiceguideassistance.User.MainActivity;

import org.w3c.dom.Text;

import java.util.Locale;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
TextToSpeech text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        CardView cardView=findViewById(R.id.card);
        cardView.setAlpha(0f);
         text=new TextToSpeech(this, i -> {
            if(i==TextToSpeech.SUCCESS){
                cardView.animate().setDuration(2000).alpha(1f).withStartAction(()->{
                     overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            text.setSpeechRate(1f);
            text.speak("Welcome to Voice Guide Assistant",TextToSpeech.QUEUE_FLUSH,null,null);
                }) .withEndAction(()->{
                    while (text.isSpeaking()){
                        Log.i("Speaking","true");
                    }
                    finish();
            startActivity(new Intent(this, MainActivity.class));
                });
            }
        });



    }
}