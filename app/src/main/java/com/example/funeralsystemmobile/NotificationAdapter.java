package com.example.funeralsystemmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Notification> {

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        super(context, R.layout.item_notification, notificationList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView : LayoutInflater.from(getContext()).inflate(R.layout.item_notification, parent, false);

        // Get the current product
        Notification notification = getItem(position);

        TextView notificationName = view.findViewById(R.id.textView);
        notificationName.setText(notification.getDescription());

        // Add more TextViews for other properties

        return view;
    }
}
