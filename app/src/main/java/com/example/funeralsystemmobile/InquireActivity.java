package com.example.funeralsystemmobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

public class InquireActivity extends AppCompatActivity {
    private EditText fname, mname, lname, relationship, causeofdeath, sex, religion,
            placeofdeath, citizenship, address, occupation,nameFather, nameMother, idType;
    private EditText standingflowers, lights, candleStand, flooringFlowers, cross, tarpaulin, curtains, candles, balloons, message,
            note, locationFrom, locationTo, nameCemetery, addressCemetery, obituaryDescription, valuableProperty;

    private RadioGroup radioGroupDeathLocation;
    private RadioGroup radioGroupCasketSize, radioGroupRemoveGlass, radioGroupAddLights,
            radioGroupClothesSize, radioGroupInjectFormalin, radioGroupAvailableHearses,
            radioGroupHaveMakeup, radioGroupCremated;
    private String optionDeath;
    private String casketSize, removeGlass, addLights, clothesSize, injectFormalin,
            availableHearses, haveMakeup, cremated;

    private EditText editTextDateOfBirth;
    private EditText editTextDateOfDeath;
    private EditText editTextWakeFrom;
    private EditText editTextWakeTo;
    private Spinner spinnerCivilStatus;
    private Spinner spinnerCategoryOfDeath;
    private Spinner spinnerModeOfPayment;
    private String selectedDateOfBirth = "";
    private String selectedDateOfDeath = "";
    private String selectedWakeFrom = "";
    private String selectedWakeTo = "";
    private String selectedCivilStatus;
    private String selectedCategoryOfDeath;
    private String selectedModeOfPayment;
    private Integer custid, packageID;
    private String token, name, price, inclusions;
    private static final int PICK_IMAGE_IMAGE = 100;
    private static final int PICK_IMAGE_VALID_ID = 101;
    private static final int PICK_IMAGE_PROOF_OF_DEATH = 102;
    private static final int PICK_IMAGE_TRANSFER_PERMIT = 103;
    private static final int PICK_IMAGE_SWAB_TEST = 104;
    private static final int PICK_IMAGE_OTHER_DOCUMENTS = 105;
    private static final int PICK_IMAGE_PROOF_OWNERSHIP = 106;
    private static final int PICK_IMAGE_SIGNATURE = 107;
    private TextView textViewImage, textViewValidId, textViewProofOfDeath, textViewTransferPermit,
            textViewSwabTest, textViewOtherDocuments, textViewProofOwnership, textViewUploadSignature;

    // Variables to hold the base64 encoded images
    private String encodedImagee = "", encodedValidId = "", encodedProofOfDeath = "", encodedTransferPermit = "",
            encodedSwabTest = "", encodedOtherDocuments = "", encodedProofOwnership = "", encodedSignature = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquire);

        custid =  Integer.parseInt(getIdFromSharedPreferences());
        token = getTokenFromSharedPreferences();
        packageID = Integer.parseInt(getIntent().getStringExtra("packageID"));
        name = getIntent().getStringExtra("name");
        price = getIntent().getStringExtra("price");
        inclusions = getIntent().getStringExtra("inclusions");

        TextView packageName = findViewById(R.id.packageName);
        TextView packagePrice = findViewById(R.id.packagePrice);
        TextView packageInclusions = findViewById(R.id.packageInclusions);
        packageName.setText("Package: " + name);
        packagePrice.setText("Price: " + price);
        packageInclusions.setText("Inclusions: " + inclusions);

        editTextDateOfBirth = findViewById(R.id.dateofbirth);
        editTextDateOfDeath = findViewById(R.id.dateofdeath);
        editTextWakeFrom = findViewById(R.id.wakeFrom);
        editTextWakeTo = findViewById(R.id.wakeTo);
        setupDatePicker(editTextDateOfBirth);
        setupDatePicker(editTextDateOfDeath);
        setupDatePicker(editTextWakeFrom);
        setupDatePicker(editTextWakeTo);

        spinnerCivilStatus = findViewById(R.id.civilstatus);
        spinnerCategoryOfDeath = findViewById(R.id.categoryDeath);
        spinnerModeOfPayment = findViewById(R.id.modeOfPayment);
        setupSpinner(spinnerCivilStatus, R.array.civil_status_array);
        setupSpinner(spinnerCategoryOfDeath, R.array.category_of_death_array);
        setupSpinner(spinnerModeOfPayment, R.array.mode_of_payment_array);

        radioGroupDeathLocation = findViewById(R.id.radioGroupoptionDeath);
        radioGroupCasketSize = findViewById(R.id.radioGroupCasketSize);
        radioGroupRemoveGlass = findViewById(R.id.radioGroupRemoveGlass);
        radioGroupAddLights = findViewById(R.id.radioGroupAddLights);
        radioGroupClothesSize = findViewById(R.id.radioGroupClothesSize);
        radioGroupInjectFormalin = findViewById(R.id.radioGroupInjectFormalin);
        radioGroupAvailableHearses = findViewById(R.id.radioGroupAvailableHearses);
        radioGroupHaveMakeup = findViewById(R.id.radioGroupHaveMakeup);
        radioGroupCremated = findViewById(R.id.radioGroupCremated);

        textViewImage = findViewById(R.id.textViewImage);
        textViewValidId = findViewById(R.id.textViewValidId);
        textViewProofOfDeath = findViewById(R.id.textViewProofOfDeath);
        textViewTransferPermit = findViewById(R.id.textViewTransferPermit);
        textViewSwabTest = findViewById(R.id.textViewSwabTest);
        textViewOtherDocuments = findViewById(R.id.textViewOtherDocuments);
        textViewProofOwnership = findViewById(R.id.textViewProofOwnership);
        textViewUploadSignature = findViewById(R.id.textViewUploadSignature);

        setButtonClickListener(R.id.buttonChooseImage, PICK_IMAGE_IMAGE);
        setButtonClickListener(R.id.buttonChooseValidId, PICK_IMAGE_VALID_ID);
        setButtonClickListener(R.id.buttonChooseProofOfDeath, PICK_IMAGE_PROOF_OF_DEATH);
        setButtonClickListener(R.id.buttonChooseTransferPermit, PICK_IMAGE_TRANSFER_PERMIT);
        setButtonClickListener(R.id.buttonSwabTest, PICK_IMAGE_SWAB_TEST);
        setButtonClickListener(R.id.buttonOtherDocuments, PICK_IMAGE_OTHER_DOCUMENTS);
        setButtonClickListener(R.id.buttonProofOwnership, PICK_IMAGE_PROOF_OWNERSHIP);
        setButtonClickListener(R.id.buttonUploadSignature, PICK_IMAGE_SIGNATURE);

        fname = findViewById(R.id.fname);
        mname = findViewById(R.id.mname);
        lname = findViewById(R.id.lname);
        sex = findViewById(R.id.sex);
        occupation = findViewById(R.id.occupation);
        idType = findViewById(R.id.idType);
        relationship = findViewById(R.id.relationship);
        religion = findViewById(R.id.religion);
        address = findViewById(R.id.address);
        citizenship = findViewById(R.id.citizenship);
        nameMother = findViewById(R.id.nameMother);
        nameFather = findViewById(R.id.nameFather);
        causeofdeath = findViewById(R.id.causeofdeath);
        placeofdeath = findViewById(R.id.placeofdeath);
        standingflowers = findViewById(R.id.standingFlowers);
        lights = findViewById(R.id.lights);
        candleStand = findViewById(R.id.candleStand);
        flooringFlowers = findViewById(R.id.flooringFlowers);
        cross = findViewById(R.id.cross);
        tarpaulin = findViewById(R.id.tarpaulin);
        curtains = findViewById(R.id.curtains);
        candles = findViewById(R.id.candles);
        balloons = findViewById(R.id.balloons);
        message = findViewById(R.id.message);
        note = findViewById(R.id.note);
        locationFrom = findViewById(R.id.locationFrom);
        locationTo = findViewById(R.id.locationTo);
        nameCemetery = findViewById(R.id.nameCemetery);
        addressCemetery = findViewById(R.id.addressCemetery);
        obituaryDescription = findViewById(R.id.obituaryDescription);
        valuableProperty = findViewById(R.id.valuableProperty);



        Button orderPackage = findViewById(R.id.orderPackage);
        orderPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderInquiredPackage();
            }
        });
    }

    private void orderInquiredPackage() {
        selectedDateOfBirth = editTextDateOfBirth.getText().toString();
        selectedDateOfDeath = editTextDateOfDeath.getText().toString();
        selectedWakeFrom = editTextWakeFrom.getText().toString();
        selectedWakeTo = editTextWakeTo.getText().toString();

        selectedCivilStatus = spinnerCivilStatus.getSelectedItem().toString();
        selectedCategoryOfDeath = spinnerCategoryOfDeath.getSelectedItem().toString();
        selectedModeOfPayment = spinnerModeOfPayment.getSelectedItem().toString();

        optionDeath = getSelectedOptionText(radioGroupDeathLocation);
        casketSize = getSelectedOptionText(radioGroupCasketSize);
        removeGlass = getSelectedOptionText(radioGroupRemoveGlass);
        addLights = getSelectedOptionText(radioGroupAddLights);
        clothesSize = getSelectedOptionText(radioGroupClothesSize);
        injectFormalin = getSelectedOptionText(radioGroupInjectFormalin);
        availableHearses = getSelectedOptionText(radioGroupAvailableHearses);
        haveMakeup = getSelectedOptionText(radioGroupHaveMakeup);
        cremated = getSelectedOptionText(radioGroupCremated);

        String sfname = fname.getText().toString().trim();
        String smname = mname.getText().toString().trim();
        String slname = lname.getText().toString().trim();
        String ssex = sex.getText().toString().trim();
        //selectedDateOfBirth
        String soccupation = occupation.getText().toString().trim();
        //encodedImagee
        String sidType = idType.getText().toString().trim();
        //encodedValidId
        String srelationship = relationship.getText().toString().trim();
        String sreligion = religion.getText().toString().trim();
        String saddress = address.getText().toString().trim();
        String scitizenship = citizenship.getText().toString().trim();
        //selectedCivilStatus
        String snameMother = nameMother.getText().toString().trim();
        String snameFather = nameFather.getText().toString().trim();

        //optionDeath
        //selectedCategoryOfDeath
        //selectedDateOfDeath
        String scauseofdeath = causeofdeath.getText().toString().trim();
        //encodedProofOfDeath
        String splaceofdeath = placeofdeath.getText().toString().trim();
        //encodedTransferPermit
        //encodedSwabTest
        //encodedOtherDocuments

        //casketSize
        //removeGlass
        //addLights
        //clothesSize
        //injectFormalin
        //availableHearses
        //haveMakeup
        //cremated
        String sstandingflowers = standingflowers.getText().toString().trim();
        String slights = lights.getText().toString().trim();
        String scandleStand = candleStand.getText().toString().trim();
        String sflooringFlowers = flooringFlowers.getText().toString().trim();
        String scross = cross.getText().toString().trim();
        String starpaulin = tarpaulin.getText().toString().trim();
        String scurtains = curtains.getText().toString().trim();
        String scandles = candles.getText().toString().trim();
        String sballoons = balloons.getText().toString().trim();
        String smessage = message.getText().toString().trim();
        String snote = note.getText().toString().trim();

        String slocationFrom = locationFrom.getText().toString().trim();
        String slocationTo = locationTo.getText().toString().trim();
        //selectedWakeFrom
        //selectedWakeTo
        String snameCemetery = nameCemetery.getText().toString().trim();
        String saddressCemetery = addressCemetery.getText().toString().trim();
        String sobituaryDescription = obituaryDescription.getText().toString().trim();

        String svaluableProperty = valuableProperty.getText().toString().trim();
        //encodedProofOwnership
        //encodedSignature
        //selectedModeOfPayment

        if (!sfname.isEmpty() && !smname.isEmpty() && !slname.isEmpty() && !ssex.isEmpty() && !selectedDateOfBirth.isEmpty()
                && !soccupation.isEmpty() && !encodedImagee.isEmpty() && !sidType.isEmpty() && !encodedValidId.isEmpty()
                && !srelationship.isEmpty() && !sreligion.isEmpty() && !saddress.isEmpty() && !scitizenship.isEmpty()
                && !snameMother.isEmpty() && !snameFather.isEmpty()

                && !selectedCategoryOfDeath.isEmpty() && !selectedDateOfDeath.isEmpty()
                && !scauseofdeath.isEmpty() && !encodedProofOfDeath.isEmpty()

                && !sstandingflowers.isEmpty() && !slights.isEmpty() && !scandleStand.isEmpty()
                && !sflooringFlowers.isEmpty() && !scross.isEmpty() && !starpaulin.isEmpty()
                && !scurtains.isEmpty() && !scandles.isEmpty() && !sballoons.isEmpty()
                && !smessage.isEmpty() && !snote.isEmpty()

                && !slocationFrom.isEmpty() && !slocationTo.isEmpty() && !selectedWakeFrom.isEmpty()
                && !selectedWakeTo.isEmpty() && !snameCemetery.isEmpty() && !saddressCemetery.isEmpty()
                && !sobituaryDescription.isEmpty()

                && !svaluableProperty.isEmpty() && !encodedProofOwnership.isEmpty() && !encodedSignature.isEmpty()
                && !selectedModeOfPayment.isEmpty()
        ) {

            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("customerID", custid);
                requestBody.put("fname", sfname);
                requestBody.put("mname", smname);
                requestBody.put("lname", slname);
                requestBody.put("sex", ssex);
                requestBody.put("dateOfBirth", selectedDateOfBirth);
                requestBody.put("occupation", soccupation);
                requestBody.put("image", encodedImagee);
//                requestBody.put("image", "encodedImagee");
                requestBody.put("idType", sidType);
                requestBody.put("validId", encodedValidId);
//                requestBody.put("validId", "encodedValidId");
                requestBody.put("relationship", srelationship);
                requestBody.put("religion", sreligion);
                requestBody.put("address", saddress);
                requestBody.put("citizenship", scitizenship);
                requestBody.put("civilStatus", selectedCivilStatus);
                requestBody.put("nameMother", snameMother);
                requestBody.put("nameFather", snameFather);

                requestBody.put("optionDeath", optionDeath);
                requestBody.put("categoryOfDeath", selectedCategoryOfDeath);
                requestBody.put("dateOfDeath", selectedDateOfDeath);
                requestBody.put("causeofdeath", scauseofdeath);
                requestBody.put("proofOfDeath", encodedProofOfDeath);
//                requestBody.put("proofOfDeath", "encodedProofOfDeath");
                requestBody.put("placeofdeath", splaceofdeath);
                requestBody.put("transferPermit", encodedTransferPermit);
                requestBody.put("swabTest", encodedSwabTest);
                requestBody.put("otherDocuments", encodedOtherDocuments);
//                requestBody.put("transferPermit", "encodedTransferPermit");
//                requestBody.put("swabTest", "encodedSwabTest");
//                requestBody.put("otherDocuments", "encodedOtherDocuments");

                requestBody.put("casketSize", casketSize);
                requestBody.put("removeGlass", removeGlass);
                requestBody.put("addLights", addLights);
                requestBody.put("clothesSize", clothesSize);
                requestBody.put("injectFormalin", injectFormalin);
                requestBody.put("availHearses", availableHearses);
                requestBody.put("haveMakeup", haveMakeup);
                requestBody.put("cremated", cremated);
                requestBody.put("standingflowers", sstandingflowers);
                requestBody.put("lights", slights);
                requestBody.put("scandleStand", scandleStand);
                requestBody.put("flooringFlowers", sflooringFlowers);
                requestBody.put("cross", scross);
                requestBody.put("tarpaulin", starpaulin);
                requestBody.put("curtains", scurtains);
                requestBody.put("candles", scandles);
                requestBody.put("balloons", sballoons);
                requestBody.put("message", smessage);
                requestBody.put("note", snote);

                requestBody.put("locationFrom", slocationFrom);
                requestBody.put("locationTo", slocationTo);
                requestBody.put("wakeFrom", selectedWakeFrom);
                requestBody.put("wakeTo", selectedWakeTo);
                requestBody.put("nameCemetery", snameCemetery);
                requestBody.put("addressCemetery", saddressCemetery);
                requestBody.put("obituaryDescription", sobituaryDescription);

                requestBody.put("valuableProperty", svaluableProperty);
                requestBody.put("proofOwnership", encodedProofOwnership);
                requestBody.put("signature", encodedSignature);
//                requestBody.put("proofOwnership", "encodedProofOwnership");
//                requestBody.put("signature", "encodedSignature");
                requestBody.put("MOP", selectedModeOfPayment);

                requestBody.put("price", price);
                requestBody.put("packageID", packageID);


            } catch (JSONException e) {
                e.printStackTrace();
            }



            // Make a POST request to the login API endpoint
            String url = ApiConstants.inquirePackageURL; // Replace with your actual login API endpoint
            Toast.makeText(getApplicationContext(), "Adding ... ", Toast.LENGTH_SHORT).show();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Assuming the API returns a message upon successful add
                                String message = response.getString("message");
                                Toast.makeText(getApplicationContext(), "Added Successfully ", Toast.LENGTH_SHORT).show();
                                // For example, start a new activity after successful add
                                startActivity(new Intent(InquireActivity.this, ProfileActivity.class));
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
                            Log.e("DeceasedaddActivity", "Error fetching profile: " + error.getMessage());
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

        } else {
            Toast.makeText(getApplicationContext(), "Please Fill out all fields", Toast.LENGTH_SHORT).show();
        }

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

    private void setupDatePicker(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Note: month is zero-based
                String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                editText.setText(selectedDate);
            }
        };

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(InquireActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setupSpinner(Spinner spinner, int arrayResourceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                arrayResourceId,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private String getSelectedOptionText(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        return radioButton != null ? radioButton.getText().toString() : "Not selected";
    }

    private void setButtonClickListener(int buttonId, final int requestCode) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser(requestCode);
            }
        });
    }

    private void openImageChooser(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    String encodedImage = encodeImage(selectedImage);
                    switch (requestCode) {
                        case PICK_IMAGE_IMAGE:
                            textViewImage.setText(selectedImageUri.toString());
                            encodedImagee = encodedImage;
                            break;
                        case PICK_IMAGE_VALID_ID:
                            textViewValidId.setText(selectedImageUri.toString());
                            encodedValidId = encodedImage;
                            break;
                        case PICK_IMAGE_PROOF_OF_DEATH:
                            textViewProofOfDeath.setText(selectedImageUri.toString());
                            encodedProofOfDeath = encodedImage;
                            break;
                        case PICK_IMAGE_TRANSFER_PERMIT:
                            textViewTransferPermit.setText(selectedImageUri.toString());
                            encodedTransferPermit = encodedImage;
                            break;
                        case PICK_IMAGE_SWAB_TEST:
                            textViewSwabTest.setText(selectedImageUri.toString());
                            encodedSwabTest = encodedImage;
                            break;
                        case PICK_IMAGE_OTHER_DOCUMENTS:
                            textViewOtherDocuments.setText(selectedImageUri.toString());
                            encodedOtherDocuments = encodedImage;
                            break;
                        case PICK_IMAGE_PROOF_OWNERSHIP:
                            textViewProofOwnership.setText(selectedImageUri.toString());
                            encodedProofOwnership = encodedImage;
                            break;
                        case PICK_IMAGE_SIGNATURE:
                            textViewUploadSignature.setText(selectedImageUri.toString());
                            encodedSignature = encodedImage;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}