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

public class OrderinfoAdapter extends ArrayAdapter<OrderItem> {
    public OrderinfoAdapter(Context context, List<OrderItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_orderitem, parent, false);
        }

        OrderItem item = getItem(position);

        TextView nameTextView = convertView.findViewById(R.id.itemName);
        TextView priceTextView = convertView.findViewById(R.id.itemPrice);
        TextView quantityTextView = convertView.findViewById(R.id.itemQuantity);
        TextView totalTextView = convertView.findViewById(R.id.itemTotal);
        ImageView imageView = convertView.findViewById(R.id.itemImage);

        nameTextView.setText(item.getName());
        priceTextView.setText(String.format("%,d", item.getPrice()));
        quantityTextView.setText("Qty: " + item.getQuantity());
        totalTextView.setText("Total: " + String.format("%,d", item.getPrice() * item.getQuantity()));

        if (item.getImage() != "null") {
            Picasso.get().load(ApiConstants.BASE_URL + item.getImage()).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.pikeslogo); // set a placeholder if no image is available
        }

        return convertView;
    }
}
