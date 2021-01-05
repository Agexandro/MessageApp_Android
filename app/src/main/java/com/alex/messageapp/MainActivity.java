package com.alex.messageapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
private static final int PICK_CONTACT_REQUEST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        renderContact();
    }

    private void renderContact() {
        TextView contactName = (TextView) findViewById(R.id.contact_name);
        TextView contactPhone = (TextView) findViewById(R.id.phone_number);
        ImageView contactPhoto = (ImageView) findViewById(R.id.contact_portrait);

        contactName.setText("Select a contact");
        contactPhone.setText("");
        contactPhoto.setImageBitmap(null);
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
                Log.d("selection", data.toString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}