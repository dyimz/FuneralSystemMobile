package com.example.funeralsystemmobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private ImageView customerImage, ValidId, GcashQr;
    private EditText etFirstName, etLastName, etSex, etAge, etAddress, etContact, etIdType, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private int age;
    private static final int PICK_IMAGE_REQUEST_CUSTOMER = 1;
    private static final int PICK_IMAGE_REQUEST_VALID_ID = 2;
    private static final int PICK_IMAGE_REQUEST_GCASH_QR = 3;
    private Bitmap selectedCustomerImage, selectedValidId, selectedGcashQr;
    Spinner spinnerSex, spinnerValidId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        customerImage = findViewById(R.id.customerImage);
        ValidId = findViewById(R.id.ValidId);
        GcashQr = findViewById(R.id.GcashQr);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);

        etAge = findViewById(R.id.etAge);
        etAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        etAddress = findViewById(R.id.etAddress);
        etContact = findViewById(R.id.etContact);
        etContact.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11), phoneNumberInputFilter});

        spinnerSex = findViewById(R.id.spinnerSex);
        spinnerValidId = findViewById(R.id.spinnerIdType);
        ArrayAdapter<CharSequence> sexAdapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_spinner_item);
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSex.setAdapter(sexAdapter);

        // Populate spinner for Valid Id
        ArrayAdapter<CharSequence> validIdAdapter = ArrayAdapter.createFromResource(this,
                R.array.valid_id_array, android.R.layout.simple_spinner_item);
        validIdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerValidId.setAdapter(validIdAdapter);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        customerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker(PICK_IMAGE_REQUEST_CUSTOMER);
            }
        });

        ValidId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker(PICK_IMAGE_REQUEST_VALID_ID);
            }
        });

        GcashQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker(PICK_IMAGE_REQUEST_GCASH_QR);
            }
        });
        CheckBox checkBoxTerms = findViewById(R.id.checkBoxTerms);
        checkBoxTerms.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Enable or disable the registration button based on whether the checkbox is checked
            btnRegister.setEnabled(isChecked);
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBoxTerms.isChecked()) {
                    registerUser();
                } else {
                   Toast.makeText(RegisterActivity.this, "Please agree to the terms and conditions", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private final InputFilter phoneNumberInputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            // Ensure the input starts with "09"
            if (dstart == 0 && source.length() > 0 && source.charAt(0) != '0') {
                return "09" + source;
            }
            return null;
        }
    };

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
                    Date selectedDate = null;
                    try {
                        selectedDate = sdf.parse((month + 1) + "-" + dayOfMonth + "-" + year);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.setTime(selectedDate);

                    Calendar minAgeCalendar = Calendar.getInstance();
                    minAgeCalendar.add(Calendar.YEAR, -18);

                    if (selectedCalendar.before(minAgeCalendar)) {
                        etAge.setText(sdf.format(selectedCalendar.getTime()));
                        calculateAge(selectedCalendar);
                    } else {
                        Toast.makeText(RegisterActivity.this, "You must be 18 or older.", Toast.LENGTH_SHORT).show();
                    }
                },
                currentYear,
                currentMonth,
                currentDay
        );

        // Set the maximum date allowed (18 years ago)
        calendar.add(Calendar.YEAR, -18);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }
    private void calculateAge(Calendar selectedCalendar) {
        Calendar todayCalendar = Calendar.getInstance();

        age = todayCalendar.get(Calendar.YEAR) - selectedCalendar.get(Calendar.YEAR);
        if (todayCalendar.get(Calendar.DAY_OF_YEAR) < selectedCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
//        Toast.makeText(RegisterActivity.this, "Age: " + age, Toast.LENGTH_SHORT).show();
    }


    private void openImagePicker(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            Bitmap selectedBitmap = decodeSelectedImage(selectedImageUri);

            switch (requestCode) {
                case PICK_IMAGE_REQUEST_CUSTOMER:
                        customerImage.setImageBitmap(selectedBitmap);
                        this.selectedCustomerImage = selectedBitmap;
                    break;
                case PICK_IMAGE_REQUEST_VALID_ID:
                    ValidId.setImageBitmap(selectedBitmap);
                    this.selectedValidId = selectedBitmap;
                    break;
                case PICK_IMAGE_REQUEST_GCASH_QR:
                    GcashQr.setImageBitmap(selectedBitmap);
                    this.selectedGcashQr = selectedBitmap;
                    break;
            }
        }
    }

    private Bitmap decodeSelectedImage(Uri selectedImageUri) {
        try {
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void registerUser() {
        String FirstName = etFirstName.getText().toString().trim();
        String LastName = etLastName.getText().toString().trim();
        String Sex = spinnerSex.getSelectedItem().toString();
        String Birthdate = etAge.getText().toString().trim();
        String Agee = String.valueOf(age);
        String Address = etAddress.getText().toString().trim();
        String Contact = etContact.getText().toString().trim();
        String IdType = spinnerValidId.getSelectedItem().toString();
        String Email = etEmail.getText().toString().trim();
        String Password = etPassword.getText().toString().trim();
        String ConfirmPassword = etConfirmPassword.getText().toString().trim();

        if (!FirstName.isEmpty() && !LastName.isEmpty() && !Sex.isEmpty() && !Birthdate.isEmpty() && !Address.isEmpty() && !Contact.isEmpty() && !IdType.isEmpty() && !Email.isEmpty() && !Password.isEmpty() && !ConfirmPassword.isEmpty()) {
            if (Password.equals(ConfirmPassword)) {
                if (selectedCustomerImage != null) {
                    String encodedValidId = null;
                    String encodedGcashQr = null;
                    if (selectedValidId != null){
                        encodedValidId = encodeImage(selectedValidId);
                    }
                    if (selectedGcashQr != null){
                        encodedGcashQr = encodeImage(selectedGcashQr);
                    }
                    // Convert the Bitmap to a Base64 encoded string
                    String encodedCustomerImage = encodeImage(selectedCustomerImage);

                // Passwords match
                // Your logic here, for example, proceed with registration or other actions

                // Create a JSON object for the request body
                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("fname", FirstName);
                    requestBody.put("lname", LastName);
                    requestBody.put("sex", Sex);
                    requestBody.put("birthdate", Birthdate);
                    requestBody.put("age", Agee);
                    requestBody.put("address", Address);
                    requestBody.put("contact", Contact);
                    requestBody.put("idtype", IdType);
                    requestBody.put("email", Email);
                    requestBody.put("password", Password);
                    requestBody.put("customer_image", encodedCustomerImage);
                    requestBody.put("valid_id", encodedValidId);
                    requestBody.put("gcash_qr", encodedGcashQr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), "Registering ... ", Toast.LENGTH_SHORT).show();

                // Make a POST request to the login API endpoint
                String url = ApiConstants.registerURL; // Replace with your actual login API endpoint
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    // Assuming the API returns a message upon successful login
                                    String message = response.getString("message");
                                    if (message.equals("failed")) {
                                        Toast.makeText(getApplicationContext(), "Email Already Used ", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (message.equals("success")) {
                                        Toast.makeText(getApplicationContext(), "Registered Successfully ", Toast.LENGTH_SHORT).show();
                                        // For example, start a new activity after successful login
                                         startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "Check Internet Connection ", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error response
                                Toast.makeText(getApplicationContext(), "Registration Failed ", Toast.LENGTH_SHORT).show();
                            }
                        });

                // Create a request queue using Volley.newRequestQueue
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                // Add the request to the Volley request queue
                requestQueue.add(request);


            } else {
                // No image selected
                Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                }

            } else {
                // Passwords do not match
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please Fill out all fields", Toast.LENGTH_SHORT).show();
        }
    }
    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


}