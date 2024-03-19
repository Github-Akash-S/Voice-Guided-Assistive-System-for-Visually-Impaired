package com.example.voiceguideassistance;

import android.content.Context;
import android.widget.Toast;

public class Function {
Context context;
public Function(Context context){
    this.context=context;
}
public void toast(String string){
    Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
}
}
