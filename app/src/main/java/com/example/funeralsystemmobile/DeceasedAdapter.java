package com.example.funeralsystemmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DeceasedAdapter extends ArrayAdapter<Deceased> {

    public DeceasedAdapter(Context context, List<Deceased> deceasedList) {
        super(context, R.layout.item_deceased, deceasedList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView : LayoutInflater.from(getContext()).inflate(R.layout.item_deceased, parent, false);

        Deceased deceased = getItem(position);

        TextView textViewFullName = view.findViewById(R.id.textViewFullName);
        String fullnameText = "Name:  "  + (deceased != null ? deceased.getFname() : "")  + " "
                                            + (deceased != null ? deceased.getMname() : "") + " "
                                            + (deceased != null ? deceased.getLname() : "");
        textViewFullName.setText(fullnameText);

        TextView textViewRelationship = view.findViewById(R.id.textViewRelationship);
        String relationshipText = "Relationship:  " + (deceased != null ? deceased.getRelationship() : "");
        textViewRelationship.setText(relationshipText);

        TextView textViewDateofBirth = view.findViewById(R.id.textViewDateofBirth);
        String DateofBirthText = "Date of Birth:  " + (deceased != null ? deceased.getDateofbirth() : "");
        textViewDateofBirth.setText(DateofBirthText);

        TextView textViewDateofDeath = view.findViewById(R.id.textViewDateofDeath);
        String DateofDeathText = "Date of Death:  " + (deceased != null ? deceased.getDateofdeath() : "");
        textViewDateofDeath.setText(DateofDeathText);

        TextView textViewCauseofDeath = view.findViewById(R.id.textViewCauseofDeath);
        String CauseofDeath = "Cause of Death:  " + (deceased != null ? deceased.getCauseofdeath() : "");
        textViewCauseofDeath.setText(CauseofDeath);


        // Add more TextViews for other properties

        return view;
    }
}
