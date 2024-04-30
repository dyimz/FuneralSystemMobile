package com.example.funeralsystemmobile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DeceasedAdapter extends ArrayAdapter<Deceased> implements Filterable {
    private List<Deceased> deceasedList;
    private List<Deceased> filteredList;

    public DeceasedAdapter(Context context, List<Deceased> deceasedList) {
        super(context, R.layout.item_deceased, deceasedList);
        this.deceasedList = new ArrayList<>(deceasedList);
        this.filteredList = new ArrayList<>(deceasedList);
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Deceased getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_deceased, parent, false);
            holder = new ViewHolder();
            holder.textViewFullName = convertView.findViewById(R.id.textViewFullName);
            holder.textViewDateofBirth = convertView.findViewById(R.id.textViewDateofBirth);
            holder.textViewDateofDeath = convertView.findViewById(R.id.textViewDateofDeath);
            holder.textViewCauseofDeath = convertView.findViewById(R.id.textViewCauseofDeath);
            holder.itemImage = convertView.findViewById(R.id.itemImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Deceased deceased = getItem(position);
        String fullname = (deceased != null ? deceased.getFname() : "") + " " +
                (deceased.getMname() != null ? deceased.getMname() : "") + " " +
                (deceased.getLname() != null ? deceased.getLname() : "").trim();
        holder.textViewFullName.setText(fullname);
        holder.textViewDateofBirth.setText("Date of Birth: " + (deceased != null ? deceased.getDateofbirth() : ""));
        holder.textViewDateofDeath.setText("Date of Death: " + (deceased != null ? deceased.getDateofdeath() : ""));
        holder.textViewCauseofDeath.setText("Cause of Death: " + (deceased != null ? deceased.getCauseofdeath() : ""));

        if (deceased != null && deceased.getImage() != null && !deceased.getImage().equals("null")) {
            Picasso.get().load(ApiConstants.BASE_URL + deceased.getImage()).into(holder.itemImage);
        } else {
            holder.itemImage.setImageResource(R.drawable.pikeslogo); // Placeholder
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ObituaryinfoActivity.class);
                intent.putExtra("DECEASED_ID", String.valueOf(deceased.getId())); // Assuming getId() returns the ID of the deceased
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView textViewFullName;
        TextView textViewDateofBirth;
        TextView textViewDateofDeath;
        TextView textViewCauseofDeath;
        ImageView itemImage;
    }

    public void setDeceasedList(List<Deceased> newList) {
        this.deceasedList = new ArrayList<>(newList);
        this.filteredList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Deceased> filteredResults = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredResults.addAll(deceasedList);
                } else {
                    String searchStr = constraint.toString().toLowerCase();
                    for (Deceased deceased : deceasedList) {
                        if (deceased.getFname().toLowerCase().contains(searchStr) ||
                                deceased.getMname().toLowerCase().contains(searchStr) ||
                                deceased.getLname().toLowerCase().contains(searchStr)) {
                            filteredResults.add(deceased);
                        }
                    }
                }
                results.values = filteredResults;
                results.count = filteredResults.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList.clear();
                filteredList.addAll((List<Deceased>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}