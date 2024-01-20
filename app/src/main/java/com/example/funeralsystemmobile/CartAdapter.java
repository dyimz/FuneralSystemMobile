package com.example.funeralsystemmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CartAdapter extends ArrayAdapter<Product> {

    public CartAdapter(Context context, List<Product> productList) {
        super(context, R.layout.item_cart, productList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView : LayoutInflater.from(getContext()).inflate(R.layout.item_cart, parent, false);

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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        ImageButton btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, for example, show a toast with the product name
                // You can also launch a new activity or perform any other action here
                String sprodid =  String.valueOf(product.getId());
                Integer custid =  Integer.parseInt(getIdFromSharedPreferences());
                String scustid =  custid.toString();
                String token = getTokenFromSharedPreferences();

                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("customerID", scustid);
                    requestBody.put("productID", sprodid);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String addToCartURL = ApiConstants.cartDeleteURL;
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, addToCartURL, requestBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    // Assuming the API returns a message upon successful add
                                    String message = response.getString("message");
                                    Toast.makeText(getContext(), " Deleted ", Toast.LENGTH_SHORT).show();
                                    // For example, start a new activity after successful add
                                    getContext().startActivity(new Intent(getContext(), CartActivity.class));
                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "Check Internet Connection ", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error response
                                Log.e("CartActivity", "Error fetching profile: " + error.getMessage());
                            }
                        }) {
                    @Override
                    public java.util.Map<String, String> getHeaders() {
                        // Add the token to the headers of the request
                        java.util.Map<String, String> headers = new java.util.HashMap<>();
                        headers.put("Authorization", "Bearer " + token);
                        return headers;
                    }
                };

                requestQueue.add(request);

            }
        });

        return view;
    }

    private String getIdFromSharedPreferences() {
        // Retrieve the id from SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("custid", "");
    }
    private String getTokenFromSharedPreferences() {
        // Retrieve the token from SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }
}