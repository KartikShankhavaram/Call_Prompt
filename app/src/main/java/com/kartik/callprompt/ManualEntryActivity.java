package com.kartik.callprompt;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.onegravity.contactpicker.contact.Contact;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManualEntryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar2) Toolbar toolbar;
    @BindView(R.id.contactNameEntry) TextInputEditText contactNameEntry;
    @BindView(R.id.contactNumberEntry) TextInputEditText contactNumberEntry;

    @OnClick(R.id.button) public void getDetails() {
        String name = String.valueOf(contactNameEntry.getText());
        String phone = String.valueOf(contactNumberEntry.getText());
        if(phone.length() != 10 || phone.startsWith("0")) {
            contactNumberEntry.setError("Enter a valid number not starting with 0");
        } else {
            ContactInfo c = new ContactInfo();
            c.setContactNumber("+91" + phone);
            c.setContactName(name);
            if(PromptContacts.promptContactList.contains(c)) {
                Toast.makeText(this, "This phone number is already present in your list.", Toast.LENGTH_SHORT).show();
            } else {
                PromptContacts.promptContactList.add(c);
                ArrayList<Object> temp = new ArrayList<>();
                temp.addAll(PromptContacts.promptContactList);
                PromptContacts.tinyDB.putListObject(TinyDB.SAVE_KEY, temp);
            }
            onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle("Enter contact details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
