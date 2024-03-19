package com.example.voiceguideassistance.User;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;

import com.example.voiceguideassistance.databinding.ActivitySmsSenderBinding;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.Objects;

public class SmsSender extends AppCompatActivity {
private SpeechRecognizer speechRecognizer;
private ActivitySmsSenderBinding bind;
int num=1;
TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivitySmsSenderBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        tts=new TextToSpeech(this, i -> {
           if(TextToSpeech.SUCCESS==i){
               speak("Here You Send Sms By Saying The Mobile you add mobile and by saying the message you can add ");
           }
        });
        bind.laymain.setOnClickListener(v->{
            if(!tts.isSpeaking()) {
                num++;
                if (num % 2 == 0) {
                    speechRecognizer.startListening(intent);
                } else {
                    speechRecognizer.stopListening();
                }
            }});

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                bind.listnenin.setText("Listening...");
            }

            @Override
            public void onBeginningOfSpeech() {
                bind.listnenin.setText("Listening...");
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
functions(Objects.requireNonNull(bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)).get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

    }

    private void speak(String f) {
    tts.speak(f, TextToSpeech.QUEUE_FLUSH,null,null);
    }

    private void functions(String s) {
if(s.contains("the mobile number is")){
    bind.mobile.setText(s);
}else if(s.contains("the SMS is")){
    bind.body.setText(s);
}else if(s.contains("send SMS")){
    sendsms(Objects.requireNonNull(bind.mobile.getText()).toString(), Objects.requireNonNull(bind.body.getText()).toString());
}else {
    send();
}
    }

    private void sendsms(String mobile, String body) {
SmsManager cc=SmsManager.getDefault();
        cc.sendTextMessage(mobile,null,body,null,null);
    }

    private void send() {
        String mobile= Objects.requireNonNull(bind.mobile.getText()).toString();
        String body= Objects.requireNonNull(bind.body.getText()).toString();
    if(mobile.isEmpty()){
        speak("Please enter your mobile number by saying the mobile number is");
    }else if(body.isEmpty()){
        speak("Please enter your mobile number by saying the sms is");
    }else{
        speak("tell send sms");
    }
    }
}