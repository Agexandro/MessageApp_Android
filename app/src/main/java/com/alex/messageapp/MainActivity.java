package com.alex.messageapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
private static final int PICK_CONTACT_REQUEST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        renderContact(null);
    }

    private void renderContact(Uri uri) {
        TextView contactName = (TextView) findViewById(R.id.contact_name);
        TextView contactPhone = (TextView) findViewById(R.id.phone_number);
        ImageView contactPhoto = (ImageView) findViewById(R.id.contact_portrait);

        if(uri == null){
            contactName.setText("Select a contact");
            contactPhone.setText("");
            contactPhoto.setImageBitmap(null);
        }else{
            contactName.setText(getDisplayName(uri));
            contactPhone.setText(getDisplayPhone(uri));
            contactPhoto.setImageBitmap(getDisplayPhoto(uri));
        }
    }
    private String getDisplayName(Uri uri) {
        String name = null ;
        Cursor cursor = getContentResolver().query(uri,null,null,null);
        if(cursor.moveToFirst()){
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        return name;
    }
    private String getDisplayPhone(Uri uri) {
        String phone = null;

        Cursor contactCursor = getContentResolver().query(
                uri,new String []{ContactsContract.Contacts._ID},null, null,null);

        String id = null;

        if(contactCursor.moveToFirst()){
            id = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
        }
        contactCursor.close();
             Cursor phoneCursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND "
                     + ContactsContract.CommonDataKinds.Phone.TYPE + " = "
                     + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                    new String[]{id},
                    null);
            if (phoneCursor.moveToFirst()){
                phone = phoneCursor.getString(phoneCursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                ));
            }
            phoneCursor.close();

        return phone;
    }
    private Bitmap getDisplayPhoto(Uri uri) {
        Bitmap photo = null;
        String id = null;

        Cursor contactCursor = getContentResolver().query(
                uri,new String[]{ContactsContract.Contacts._ID}, null,null,null);

        if (contactCursor.moveToFirst()){
            id = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
        }
        contactCursor.close();

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(
                    getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id).longValue())
            );
            if (inputStream != null){
                photo = BitmapFactory.decodeStream(inputStream);
            }
        }catch(IOError e){
            e.printStackTrace();
        }

        return photo;
    }

    public void onUpdateContact(View view) {
        startActivityForResult(
                new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), PICK_CONTACT_REQUEST
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_CONTACT_REQUEST){
            if (resultCode == RESULT_OK){
                renderContact(data.getData());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}