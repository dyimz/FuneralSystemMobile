package com.example.funeralsystemmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AnnouncementAdapter extends ArrayAdapter<Announcement> {

    public AnnouncementAdapter(Context context, List<Announcement> announcementList) {
        super(context, R.layout.item_landing, announcementList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView : LayoutInflater.from(getContext()).inflate(R.layout.item_landing, parent, false);

        // Get the current product
        Announcement announcement = getItem(position);

        // Set the product details to the grid item views
        String imageUrl = ApiConstants.BASE_URL + announcement.getImg();
        ImageView imageView = view.findViewById(R.id.imageView);
        Picasso.get().load(imageUrl).into(imageView);

        TextView announcementName = view.findViewById(R.id.announcementName);
        announcementName.setText(announcement.getDescription());
//
//        TextView textViewPrice = view.findViewById(R.id.textViewPrice);
//        textViewPrice.setText("Price: " + product.getPrice());


        // Add more TextViews for other properties

        return view;
    }
}