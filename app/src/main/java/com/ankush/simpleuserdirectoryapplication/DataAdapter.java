package com.ankush.simpleuserdirectoryapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter implements Filterable {
    List<uploadData> data;
    Context context;
    private List<uploadData> filtereddata;

    public DataAdapter(Context context,List<uploadData> data) {
        this.data = data;
        this.context=context;
        filtereddata = new ArrayList<>(data);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.rowlayout, parent, false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        return new RowViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RowViewHolder rowViewHolder = (RowViewHolder) holder;

        int rowPos = rowViewHolder.getAdapterPosition();

        // code for thr row Headers inside if and for the data inside else

        if (rowPos == 0) {
            rowViewHolder.tvname.setBackgroundColor(R.color.row);
            rowViewHolder.tvage.setBackgroundColor(R.color.row);
            rowViewHolder.tvphone.setBackgroundColor(R.color.row);
            rowViewHolder.tvadrs.setBackgroundColor(R.color.row);

            rowViewHolder.tvname.setText("Name");
            rowViewHolder.tvage.setText("Age");
            rowViewHolder.tvphone.setText("Phone");
            rowViewHolder.tvadrs.setText("Address");
        }
        else {
            uploadData modal = data.get(rowPos-1);
            rowViewHolder.tvname.setText(modal.getName());
            rowViewHolder.tvage.setText(modal.getAge());
            rowViewHolder.tvphone.setText(modal.getPhone());
            rowViewHolder.tvadrs.setText(modal.getAddress());
        }
    }

    @Override
    public int getItemCount() {
        return data.size()+1;       //size+1 because of the headers
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }           //filterable

    private Filter exampleFilter = new Filter() {                   //to search the data
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<uploadData> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(filtereddata);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (uploadData item : filtereddata) {
                    //data filtering on the basis of name,phone and address
                    if (item.getName().toLowerCase().contains(filterPattern) || item.getPhone().toLowerCase().contains(filterPattern) || item.getAddress().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public class RowViewHolder extends RecyclerView.ViewHolder {
        TextView tvname;
        TextView tvage;
        TextView tvphone;
        TextView tvadrs;

        public RowViewHolder(View itemView) {
            super(itemView);

            tvname = itemView.findViewById(R.id.tvname);
            tvage = itemView.findViewById(R.id.tvage);
            tvphone = itemView.findViewById(R.id.tvphone);
            tvadrs = itemView.findViewById(R.id.tvadrs);
        }
    }


}