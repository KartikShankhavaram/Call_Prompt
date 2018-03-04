package com.kartik.callprompt;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kartik on Sat, 24/2/18 in call-prompt.
 */

public class ContactViewholder extends RecyclerView.ViewHolder {

    private TextView contactNameView;
    private TextView contactNumberView;
    private ImageView contactImageView;
    private ImageButton contactDeleteButton;

    public ContactViewholder(View itemView) {
        super(itemView);

        // getting reference to all views
        contactNameView = itemView.findViewById(R.id.contact_name);
        contactNumberView = itemView.findViewById(R.id.contact_number);
        contactImageView = itemView.findViewById(R.id.contact_image);
        contactDeleteButton = itemView.findViewById(R.id.deleteContact);

    }

    public TextView getContactNameView() {
        return contactNameView;
    }

    public ImageButton getContactDeleteButton() {
        return contactDeleteButton;
    }

    public TextView getContactNumberView() {
        return contactNumberView;
    }

    public ImageView getContactImageView() {
        return contactImageView;
    }
}
