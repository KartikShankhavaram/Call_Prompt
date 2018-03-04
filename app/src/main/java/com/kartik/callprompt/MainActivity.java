package com.kartik.callprompt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.picture.ContactPictureType;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;

public class MainActivity extends AppCompatActivity {

    ArrayList<ContactInfo> contacts = new ArrayList<>();
    RecyclerView contactListView;
    FloatingActionButton addContactsButton;
    private static int REQUEST_CONTACT = 433;
    private static int READ_CONTACTS_REQUESTS = 1;
    ContactAdapter adapter;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tinyDB = new TinyDB(this);

        contactListView = findViewById(R.id.contact_list_view);
        addContactsButton = findViewById(R.id.addContactsButton);

        contactListView.setItemAnimator(new FadeInAnimator());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setInitialPrefetchItemCount(10);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        contactListView.setLayoutManager(layoutManager);

        Log.e("Ah", "Here!");

        ArrayList<Object> tempList = tinyDB.getListObject(TinyDB.SAVE_KEY, ContactInfo.class);
        for(Object object: tempList) {
            contacts.add((ContactInfo)object);
        }
        adapter = new ContactAdapter(contacts, this, tinyDB);
        contactListView.setAdapter(adapter);

        addContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndGetPermissions();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CONTACT && resultCode == Activity.RESULT_OK &&
                data != null && data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA)) {

            List<Contact> selectedContacts = (List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);
            for (Contact contact : selectedContacts) {
                ContactInfo c = new ContactInfo();
                Uri a;
                c.setContactName(contact.getDisplayName());
                c.setContactNumber(contact.getPhone(1));
                c.setContactPhotoURI((a = contact.getPhotoUri())!=null?a.toString():null);
                if(!contacts.contains(c))
                    contacts.add(c);
            }
            ArrayList<Object> temp = new ArrayList<>();
            temp.addAll(contacts);
            tinyDB.putListObject(TinyDB.SAVE_KEY, temp);
            adapter = new ContactAdapter(contacts, MainActivity.this, tinyDB);
            contactListView.setAdapter(adapter);

        }
    }

    public void checkAndGetPermissions() {
        if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_REQUESTS);
        } else {
            selectContacts();
        }
    }

    public void selectContacts() {
        Intent intent = new Intent(MainActivity.this, ContactPickerActivity.class)
                .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE, ContactPictureType.ROUND.name())
                .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION, ContactDescription.ADDRESS.name())
                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER, ContactSortOrder.AUTOMATIC.name())
                .putExtra(ContactPickerActivity.EXTRA_ONLY_CONTACTS_WITH_PHONE, true);
        startActivityForResult(intent, REQUEST_CONTACT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == READ_CONTACTS_REQUESTS && permissions[0].equals(Manifest.permission.READ_CONTACTS)
                && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectContacts();
        }
    }

}
