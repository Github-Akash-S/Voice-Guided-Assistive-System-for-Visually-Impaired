package com.example.voiceguideassistance.User;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.voiceguideassistance.databinding.ActivityAddContactBinding;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class AddContact extends AppCompatActivity {
private ActivityAddContactBinding bind;
TextToSpeech text;
private CountDownTimer count;
private SpeechRecognizer speechRecognizer;
private Intent intent;
private int listener=0;
private int num=1;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityAddContactBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        text=new TextToSpeech(this, i -> {
            if(i==TextToSpeech.SUCCESS){
                speak("Hey User here you can add contact");
            }
        });
        bind.go.setOnClickListener(v->{
        if(!text.isSpeaking()){
            function();
        }
        });
         intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());

        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);


        speechRecognizer.setRecognitionListener(new RecognitionListener() {

            @Override
            public void onReadyForSpeech(Bundle bundle) {
                bind.state.setText("Listening....");
            text.stop();
            }


            @Override
            public void onBeginningOfSpeech() {
                text.stop();
                bind.state.setText("Listening....");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
bind.state.setText("");
            }

            @Override
            public void onError(int i) {
                bind.state.setText("listening Stopped");
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> dd = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                assert dd != null;
                count.start();
                statechange(dd.get(0));
                bind.state.setText(dd.get(0));
            }


            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        bind.lay.setOnClickListener(v->{
         num++;
            if(!text.isSpeaking()){
                if(num%2==0){
                    speechRecognizer.startListening(intent);
                }else{
                    speechRecognizer.stopListening();
                }
            }
        });

                count=new CountDownTimer(1000000,1000) {
                    @Override
                    public void onTick(long l) {
                        listener++;
                        if(listener%10==0){
                            function();
                        }
                    }

                    @Override
                    public void onFinish() {
                        Log.i("Finished","Finis");
                    }
                }.start();


    }

    private void speak(String textt) {
        text.speak(textt,TextToSpeech.QUEUE_FLUSH,null,null);
    }



    private void function() {
        String name= Objects.requireNonNull(bind.name.getText()).toString().trim();
        String mobile= Objects.requireNonNull(bind.mobile.getText()).toString().trim();

        if(!text.isSpeaking()){
            if(name.isEmpty()){
                speak("Please Say the name of contact BY Saying The Name is");
                speechRecognizer.startListening(intent);
                count.cancel();
            }else if(mobile.isEmpty()){
                speak("Please Say the Mobile number of "+name+" BY Saying The Mobile number is");
                speechRecognizer.startListening(intent);
                count.cancel();
            }else{
            savenumber(name,mobile);
            }
        }

    }

    private void savenumber(String name, String mobile) {

try {
    count.cancel();
        finish();
        Intent intent1=new Intent(Intent.ACTION_INSERT);
        intent1.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent1.putExtra(ContactsContract.Intents.Insert.NAME,name);
        intent1.putExtra(ContactsContract.Intents.Insert.PHONE,mobile);
        startActivity(intent1);
}catch (Exception e){
    speak(e.getMessage());
        }
    }

    private void statechange(String state) {
        String statelowercase=state.toLowerCase();
        if(statelowercase.contains("go back")){
            finish();
        }else if(statelowercase.contains("the name is")){
            StringBuilder k= new StringBuilder();
            for (String item : statelowercase.split("the name is")) {
                    k.append(item);
            }
            bind.name.setText(k);
        }else if(statelowercase.contains("the mobile number is")){
            StringBuilder k= new StringBuilder();
            for (String item : statelowercase.split("the mobile number is")) {
                    k.append(item);
            }
            bind.mobile.setText(k);
        }else if(statelowercase.contains("save the number")){
            function();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    count.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    speechRecognizer.destroy();
    }
}