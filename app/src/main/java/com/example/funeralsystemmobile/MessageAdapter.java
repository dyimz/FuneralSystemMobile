package com.example.funeralsystemmobile;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private int userFromId;
    public MessageAdapter(Context context, List<Message> messageList, int userFromId) {
        super(context, R.layout.item_message, messageList);
        this.userFromId = userFromId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView : LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);

        Message message = getItem(position);

        TextView textViewBody = view.findViewById(R.id.messageTextView);

        if (message.getFromId() == userFromId) {
            textViewBody.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        } else {
            textViewBody.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
        textViewBody.setText(message.getBody());
        return view;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Message getItem(int position) {
        // Return the item at the correct position based on the reversed count
        return super.getItem(getCount() - position - 1);
    }
}
