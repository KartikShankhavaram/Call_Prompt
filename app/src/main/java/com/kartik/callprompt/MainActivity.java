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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.picture.ContactPictureType;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

import static com.kartik.callprompt.PromptContacts.tinyDB;

public class MainActivity extends AppCompatActivity {

    ArrayList<ContactInfo> contacts = PromptContacts.promptContactList;
    private static int REQUEST_CONTACT = 433;
    private static int READ_CONTACTS_REQUESTS = 1;
    private static int TELEPHONE_REQUESTS = 7;
    private static int CALL_REQUESTS = 10;
    ContactAdapter adapter;

    @BindView(R.id.contact_list_view) RecyclerView contactListView;
    @BindView(R.id.addContactsButton) FloatingActionButton addContactsButton;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        tinyDB = new TinyDB(this);
        getTelephonePermissions();
        getCallPermissions();

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
        adapter = new ContactAdapter(this, tinyDB);
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
                data != null && data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA) &&
                !data.getBooleanExtra("dialogReturn", false)) {

            List<Contact> selectedContacts = (List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);
            for (Contact contact : selectedContacts) {
                ContactInfo c = new ContactInfo();
                Uri a;
                c.setContactName(contact.getDisplayName());
                String phone = contact.getPhone(1);
                c.setContactNumber(phone);
                if(!phone.startsWith("+91") && phone.length() == 10) {
                    c.setContactNumber("+91" + phone);
                }
                c.setContactPhotoURI((a = contact.getPhotoUri())!=null?a.toString():null);
                if(!contacts.contains(c))
                    contacts.add(c);
            }
            ArrayList<Object> temp = new ArrayList<>();
            temp.addAll(contacts);
            tinyDB.putListObject(TinyDB.SAVE_KEY, temp);
            adapter = new ContactAdapter(MainActivity.this, tinyDB);
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
        if(requestCode == TELEPHONE_REQUESTS && permissions.length > 0 && permissions[0].equals(Manifest.permission.PROCESS_OUTGOING_CALLS)
                && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, R.string.permission_denied_toast, Toast.LENGTH_LONG).show();
        }
        if(requestCode == CALL_REQUESTS && permissions.length > 0 && permissions[0].equals(Manifest.permission.CALL_PHONE)
                && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, R.string.call_permission_denied_toast, Toast.LENGTH_LONG).show();
        }
    }

    private void getTelephonePermissions() {
        if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, TELEPHONE_REQUESTS);
        }
    }

    private void getCallPermissions() {
        if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUESTS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.customNumbers:
                startActivity(new Intent(MainActivity.this, ManualEntryActivity.class));
        }
        return true;
    }
}
