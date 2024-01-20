package com.example.funeralsystemmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {
    public OrderAdapter(Context context, List<Order> orderList) {
        super(context, R.layout.item_order, orderList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView : LayoutInflater.from(getContext()).inflate(R.layout.item_order, parent, false);

        // Get the current product
        Order order = getItem(position);

        // Set the product details to the grid item views

        TextView FullName = view.findViewById(R.id.FullName);
        FullName.setText(order.getName());

        TextView Address = view.findViewById(R.id.Address);
        Address.setText(order.getAddress());

        TextView Phone = view.findViewById(R.id.Phone);
        Phone.setText(order.getContact());

        TextView Type = view.findViewById(R.id.Type);
        Type.setText(order.getType());

        TextView MOP = view.findViewById(R.id.MOP);
        MOP.setText(order.getMOP());

        TextView Total = view.findViewById(R.id.Total);
        Total.setText(String.valueOf(order.getTotal()));

        TextView Status = view.findViewById(R.id.Status);
        Status.setText(order.getStatus());


        // Add more TextViews for other properties

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, for example, show a toast with the product name
//                Toast.makeText(getContext(), "Clicked on " + product.getId(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getContext(), ProductinfoActivity.class);
//                intent.putExtra("productID", String.valueOf(product.getId()));
//                getContext().startActivity(intent);
                // You can also launch a new activity or perform any other action here
            }
        });

        return view;
    }
}
