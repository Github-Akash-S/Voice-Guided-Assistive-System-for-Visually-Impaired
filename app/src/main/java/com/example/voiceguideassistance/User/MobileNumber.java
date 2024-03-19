package com.example.voiceguideassistance.User;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Currency;

public class MobileNumber {
    Context context;

    public MobileNumber(Context context) {
        this.context = context;

    }

    @SuppressLint("Range")
    public ArrayList<Mobile> data() {
        ArrayList<Mobile> data = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {

                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String mobile = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (name == null) {
                    name = "ashdfasdjkf";
                }
                if (mobile == null) {
                    mobile = "12873657236743";
                }


                data.add(new Mobile(name
                        , mobile));
            }
        }
        assert cursor != null;
        cursor.close();
        return data;
    }
}
