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

public class PackageAdapter extends ArrayAdapter<Package> {

    public PackageAdapter(Context context, List<Package> packageList) {
        super(context, R.layout.item_products, packageList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView : LayoutInflater.from(getContext()).inflate(R.layout.item_products, parent, false);

        // Get the current product
        Package packagee = getItem(position);

        // Set the product details to the grid item views
        String imageUrl = ApiConstants.BASE_URL + packagee.getImg();
        ImageView imageView = view.findViewById(R.id.imageView);
        Picasso.get().load(imageUrl).into(imageView);

        TextView textViewName = view.findViewById(R.id.textViewName);
        textViewName.setText(packagee.getName());

        TextView textViewPrice = view.findViewById(R.id.textViewPrice);
        textViewPrice.setText("Price: " + packagee.getPrice());

        // Add more TextViews for other properties

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, for example, show a toast with the product name
//                Toast.makeText(getContext(), "Clicked on " + packagee.getId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), PackageinfoActivity.class);
                intent.putExtra("packageID", String.valueOf(packagee.getId()));
                getContext().startActivity(intent);
                // You can also launch a new activity or perform any other action here
            }
        });


        return view;
    }
}
