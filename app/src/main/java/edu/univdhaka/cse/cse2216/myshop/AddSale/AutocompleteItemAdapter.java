package edu.univdhaka.cse.cse2216.myshop.AddSale;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import edu.univdhaka.cse.cse2216.myshop.R;

public class AutocompleteItemAdapter extends ArrayAdapter<Pair<String, Integer>> {

    private List<Pair<String, Integer>> itemList;

    public AutocompleteItemAdapter(@NonNull Context context,  @NonNull List<Pair<String, Integer>> itemList) {
        super(context, 0, itemList);
        this.itemList = new ArrayList<>(itemList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.autocomplete_item_row, parent, false);
        }

        TextView itemName = convertView.findViewById(R.id.autocomplete_item_name);
        TextView itemQuantity = convertView.findViewById(R.id.autocomplete_item_quantity);

        Pair<String, Integer> item = getItem(position);
        if(item != null) {
            itemName.setText(item.first);
            itemQuantity.setText("(" + Integer.toString(item.second) + ")");
        }

        return convertView;
    }

    private Filter itemFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Pair<String, Integer>> suggestions = new ArrayList<>();

            if(constraint == null || constraint.length() == 0) {
                suggestions.addAll(itemList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Pair<String, Integer> item : itemList) {
                    if(item.first.toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        };

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List)results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Pair<String, Integer>)resultValue).first;
        }
    };
}
