package com.example.funeralsystemmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CheckoutAdapter extends ArrayAdapter<Product> {

    public CheckoutAdapter(Context context, List<Product> productList) {
        super(context, R.layout.item_checkout, productList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView : LayoutInflater.from(getContext()).inflate(R.layout.item_checkout, parent, false);

        // Get the current product
        Product product = getItem(position);

        // Set the product details to the grid item views

        TextView textViewName = view.findViewById(R.id.textViewName);
        textViewName.setText(product.getName());

        TextView textViewPrice = view.findViewById(R.id.textViewPrice);
        textViewPrice.setText("Price: " + product.getPrice());

        // Add more TextViews for other properties

        return view;
    }

}