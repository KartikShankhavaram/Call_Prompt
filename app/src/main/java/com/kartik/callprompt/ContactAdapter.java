package com.kartik.callprompt;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by kartik on Sat, 24/2/18 in call-prompt.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactViewholder> {

    private ArrayList<ContactInfo> contactList;
    private Context mContext;
    private TinyDB tinyDB;
    final String TAG = this.getClass().getSimpleName();

    public ContactAdapter(ArrayList<ContactInfo> list, Context mContext, TinyDB tinyDB) {
        contactList = list;
        this.mContext = mContext;
        this.tinyDB = tinyDB;
    }
    @Override
    public ContactViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        Log.i("inflater", "Inflated!");

        return new ContactViewholder(itemView);
    }

    @Override
    public void onBindViewHolder(final ContactViewholder holder, final int position) {
        final int adapterPosition = holder.getAdapterPosition();
        final ContactInfo contactInfo = contactList.get(holder.getAdapterPosition());
        Log.i("name", contactInfo.getContactName());
        holder.getContactNameView().setText(contactInfo.getContactName());
        holder.getContactNumberView().setText(contactInfo.getContactNumber());
        Log.i("current", contactInfo.toString());
        holder.getContactDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDeleteMethod(holder.getAdapterPosition());
                Log.i(TAG, "Position" + holder.getAdapterPosition() + "deleted");
            }
        });
        loadImage(holder.getContactImageView(), contactInfo.getContactPhotoURI());
    }

    private void loadImage(ImageView contactImageView, String contactPhotoURI) {
        if(contactPhotoURI != null && !contactPhotoURI.equals("null") && !contactPhotoURI.equals("")) {
            Glide.with(mContext)
                    .load(contactPhotoURI)
                    .into(contactImageView);
        } else {
            contactImageView.setImageResource(R.drawable.ic_person_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    private void callDeleteMethod(int position) {
        contactList.remove(position);
        notifyItemRemoved(position);
        ArrayList<Object> temp = new ArrayList<>();
        temp.addAll(contactList);
        tinyDB.putListObject(TinyDB.SAVE_KEY, temp);
    }
}
