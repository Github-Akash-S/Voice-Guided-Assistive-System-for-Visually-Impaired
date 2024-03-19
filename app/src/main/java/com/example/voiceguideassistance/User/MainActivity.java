package com.example.voiceguideassistance.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.voiceguideassistance.Function;
import com.example.voiceguideassistance.Integration.DetectorActivity;
import com.example.voiceguideassistance.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding bind;
    private CountDownTimer count;
int listner=0;
int secondlistner=0;
    Function funtion;
    TextToSpeech tts;
    SpeechRecognizer speech;
    int num=1;
    ArrayList<Mobile>mobileNumbers=new ArrayList<>();
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        speech=SpeechRecognizer.createSpeechRecognizer(this);

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED&&
        ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS},100);
        }else{
       try {
           MobileNumber dd=new MobileNumber(this);
           mobileNumbers=dd.data();
       }catch (Exception ec){
           Toast.makeText(this, ""+ec.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


        final  Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speech.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }


            @Override
            public void onBeginningOfSpeech() {
                bind.listener.setText("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {
            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> ff = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                assert ff != null;
                functions(ff.get(0));
                bind.listener.setText(ff.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        funtion= new Function(this);

        tts=new TextToSpeech(this,t->{
            if(t==TextToSpeech.ERROR){
                funtion.toast("Sorry there is no Speech Speaker");
            }else if(t==TextToSpeech.SUCCESS){
                speakmytext(bind.speker.getText().toString());
                tts.setSpeechRate(1f);
                while (tts.isSpeaking()){

                    Log.i("hsadfsadf",""+tts.isSpeaking());
                    if(!tts.isSpeaking()) {
                        count = new CountDownTimer(20000 * 2, 1000) {
                            @Override
                            public void onTick(long l) {

                                Log.i("sjkhdfgfg", "started");
                                if (listner == 20) {
                                    speech.startListening(intent);
                                } else if (listner == 40) {
                                    speech.stopListening();
                                }
                                listner++;

                            }

                            @Override
                            public void onFinish() {

                            }
                        };
                    }}

            }
        });




if(!SpeechRecognizer.isRecognitionAvailable(this)){
String apppackage="com.google.android.googlequicksearchbox";
try {
startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+apppackage)));
}catch (Exception e){
    Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+apppackage)));
}
}
        bind.textView.setOnClickListener(v->{
            if(!tts.isSpeaking()){
                num++;
                if(num%2==0){
                    speech.startListening(intent);
                }else{
                    speech.stopListening();
                }
            }


        });
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
                checkPermission();
            }
    }

    private void functions(String textfromspeech) {

            if(textfromspeech.contains("add contact")){
                bind.listener.setText("");
                startActivity(new Intent(this, AddContact.class));

            }else if(textfromspeech.contains("call")){
                bind.listener.setText("");
                StringBuilder string=new StringBuilder();

                for (String call : textfromspeech.split("call")) {
                    for (int i = 0; i < call.length(); i++) {
                        if (call.charAt(i) != ' ') {
                            string.append(call.charAt(i));
                        }
                    }
                }


                if(string.chars().allMatch(Character::isDigit)) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + string));
                            startActivity(intent);
                }else {
                    for (int i=0;i<mobileNumbers.size();i++){
                        Mobile m=mobileNumbers.get(i);
                            if(m.name.contains(string.toString().trim())) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + m.mobile));
                                startActivity(intent);
                                break;}
                    }



                }

            }else if(textfromspeech.contains("object detection")){
                bind.listener.setText("");
                startActivity(new Intent(this, DetectorActivity.class));
            }else if(textfromspeech.contains("open SMS")){
                bind.listener.setText("");
                startActivity(new Intent(this,SmsSender.class));
            }



    }



    private void speakmytext(String string) {
    tts.speak(string,TextToSpeech.QUEUE_FLUSH,null,null);
    }

    private void checkPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.RECORD_AUDIO}, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    speech.destroy();
    }


}