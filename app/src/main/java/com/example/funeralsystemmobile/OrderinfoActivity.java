package com.example.funeralsystemmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderinfoActivity extends AppCompatActivity {
    private String productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderinfo);
        Intent intent = getIntent();
        productID = intent.getStringExtra("productID");
//        Toast.makeText(this, "Product ID: " + productID, Toast.LENGTH_LONG).show();

        Integer custid =  Integer.parseInt(getIdFromSharedPreferences());
        String token = getTokenFromSharedPreferences();
        String url = ApiConstants.orderDetails + productID;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response from the profile endpoint
                        try {
                            JSONObject order = response.getJSONObject("order");
                            // Extract data from the order object
                            String orderNumber = order.getString("id");
                            String address = order.getString("address");
                            String contact = order.getString("contact");
                            String discounted = order.getString("discounted");
                            String subtotal = order.getString("subtotal");
                            String total = order.getString("total_price");
                            String paymentMethod = order.getString("MOP");
                            String status = order.getString("status");
                            String type = order.getString("type");
                            String paid = order.getString("paymentstatus");
                            String pendingbalance = order.getString("pendingbalance");

                            String packageName = order.getString("packageName");
                            String requeststatus = order.getString("requeststatus");
                            String wakefrom = order.getString("durationfrom");
                            String waketo = order.getString("durationto");

                            ((TextView) findViewById(R.id.orderNumberTextView)).setText(orderNumber);
                            ((TextView) findViewById(R.id.addressTextView)).setText(address);
                            ((TextView) findViewById(R.id.contactTextView)).setText(contact);
                            ((TextView) findViewById(R.id.discountedTextView)).setText(discounted);
                            ((TextView) findViewById(R.id.subtotalTextView)).setText(subtotal);
                            ((TextView) findViewById(R.id.totalTextView)).setText(total);
                            ((TextView) findViewById(R.id.paymentMethodTextView)).setText(paymentMethod);
                            ((TextView) findViewById(R.id.statusTextView)).setText(status);
                            ((TextView) findViewById(R.id.typeTextView)).setText(type);
                            ((TextView) findViewById(R.id.paidTextView)).setText(paid);
                            ((TextView) findViewById(R.id.pendingTextView)).setText(pendingbalance);

                            TableRow packageRow = (TableRow) findViewById(R.id.packageRow);
                            TableRow reqStatusRow = (TableRow) findViewById(R.id.reqStatusRow);
                            TableRow wakeRow = (TableRow) findViewById(R.id.wakeRow);
                            TableRow appointmentRow = (TableRow) findViewById(R.id.appointmentRow);
                            Button cancelButton = (Button) findViewById(R.id.cancelButton);
                            Button extendOrderButton = (Button) findViewById(R.id.extendOrderButton);

                            cancelButton.setVisibility(View.GONE);
                            extendOrderButton.setVisibility(View.GONE);

                            ((TextView) findViewById(R.id.packageTextView)).setText(packageName);
                            ((TextView) findViewById(R.id.reqStatusTextView)).setText(requeststatus);
                            if (requeststatus == "null") {
                                ((TextView) findViewById(R.id.reqStatusTextView)).setText(" ");
                            }
                            ((TextView) findViewById(R.id.wakeTextView)).setText(wakefrom + "-" + waketo);
                            ((TextView) findViewById(R.id.appointmentTextView)).setText(wakefrom + "-" + waketo);

                            if (status.equals("CANCELLED")) {
//                                Toast.makeText(getApplicationContext(), "cancelled", Toast.LENGTH_LONG).show();
                                cancelButton.setVisibility(View.GONE);
                                extendOrderButton.setVisibility(View.GONE);
                            } else {
                                if (packageName == "null") {
//                                Toast.makeText(getApplicationContext(), "isnull", Toast.LENGTH_LONG).show();
                                    packageRow.setVisibility(View.GONE);
                                    reqStatusRow.setVisibility(View.GONE);
                                    wakeRow.setVisibility(View.GONE);
                                    appointmentRow.setVisibility(View.GONE);
                                }

                                if (status.equals("PLACED") && paid.equals("NOT PAID")) {
//                                Toast.makeText(getApplicationContext(), "isnull", Toast.LENGTH_LONG).show();
                                    cancelButton.setVisibility(View.VISIBLE);
                                }

                                if (requeststatus.equals("null") && type.equals("PACKAGE") && status.equals("PLACED")) {
//                                    Toast.makeText(getApplicationContext(), "isnull", Toast.LENGTH_LONG).show();
                                    extendOrderButton.setVisibility(View.VISIBLE);
                                }
                            }

                            JSONArray orderItemsArray = response.getJSONArray("orderlines");
                            ArrayList<OrderItem> orderItems = new ArrayList<>();

                            for (int i = 0; i < orderItemsArray.length(); i++) {
                                JSONObject item = orderItemsArray.getJSONObject(i);

                                int id = item.getInt("id");
                                int orderId = item.getInt("order_id");  // assuming each item includes an order_id
                                String name = item.getString("name");
                                String image = item.optString("image", null);  // using optString to handle optional fields
                                int price = item.getInt("price");
                                int quantity = item.getInt("quantity");

                                orderItems.add(new OrderItem(id, orderId, name, image, price, quantity));
                            }

                            ListView listView = findViewById(R.id.productsListView);
                            OrderinfoAdapter adapter = new OrderinfoAdapter(OrderinfoActivity.this, orderItems);
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("OrderinfoActivity", "Error parsing JSON response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(getApplicationContext(), "Check Internet Connection ", Toast.LENGTH_SHORT).show();
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

        // Create a request queue using Volley.newRequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Add the request to the Volley request queue
        requestQueue.add(request);

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelorder();
            }
        });

        Button extendOrderButton = findViewById(R.id.extendOrderButton);
        extendOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderinfoActivity.this, ExtendorderActivity.class);
                intent.putExtra("productID", productID);
                startActivity(intent);
            }
        });
    }
    private void cancelorder() {
        String token = getTokenFromSharedPreferences();
        String url = ApiConstants.cancelOrder + productID;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response from the profile endpoint
                        try {
                            String message = response.getString("message");
                            if (message.equals("success")){
                                Intent intent = new Intent(OrderinfoActivity.this, ProfileActivity.class);
                                intent.putExtra("productID", productID);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("OrderinfoActivity", "Error parsing JSON response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(getApplicationContext(), "Check Internet Connection ", Toast.LENGTH_SHORT).show();
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

        // Create a request queue using Volley.newRequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Add the request to the Volley request queue
        requestQueue.add(request);

    }

    private String getIdFromSharedPreferences() {
        // Retrieve the id from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("custid", "");
    }
    private String getTokenFromSharedPreferences() {
        // Retrieve the token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }
}