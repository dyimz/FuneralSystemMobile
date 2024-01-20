package com.example.funeralsystemmobile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Context context, List<Product> productList) {
        super(context, R.layout.item_products, productList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView : LayoutInflater.from(getContext()).inflate(R.layout.item_products, parent, false);

        // Get the current product
        Product product = getItem(position);

        // Set the product details to the grid item views
        String imageUrl = ApiConstants.BASE_URL + product.getImg();
        ImageView imageView = view.findViewById(R.id.imageView);
        Picasso.get().load(imageUrl).into(imageView);

        TextView textViewName = view.findViewById(R.id.textViewName);
        textViewName.setText(product.getName());

        TextView textViewPrice = view.findViewById(R.id.textViewPrice);
        textViewPrice.setText("Price: " + product.getPrice());

        // Add more TextViews for other properties

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, for example, show a toast with the product name
//                Toast.makeText(getContext(), "Clicked on " + product.getId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ProductinfoActivity.class);
                intent.putExtra("productID", String.valueOf(product.getId()));
                getContext().startActivity(intent);
                // You can also launch a new activity or perform any other action here
            }
        });

        return view;
    }
}
