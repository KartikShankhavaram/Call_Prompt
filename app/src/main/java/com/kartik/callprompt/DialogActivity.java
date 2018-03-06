package com.kartik.callprompt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by kartik on Tue, 6/3/18 in call-prompt.
 */

public class DialogActivity extends AppCompatActivity {

    Button negativeButton;
    Button positiveButton;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);

        setFinishOnTouchOutside(false);

        negativeButton = findViewById(R.id.negativeButton);
        positiveButton = findViewById(R.id.positiveButton);

        Intent a = getIntent();
        phoneNumber = a.getStringExtra("phone");

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class)
                        .putExtra("dialogReturn", true)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));*/
                DialogActivity.this.finish();
            }
        });
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                PromptContacts.showDialog = false;
                DialogActivity.this.finish();
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
            }
        });
    }
}
