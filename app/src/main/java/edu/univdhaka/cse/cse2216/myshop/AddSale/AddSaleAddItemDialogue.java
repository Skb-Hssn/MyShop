package edu.univdhaka.cse.cse2216.myshop.AddSale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import edu.univdhaka.cse.cse2216.myshop.R;

public class AddSaleAddItemDialogue extends AppCompatDialogFragment{

    private AutoCompleteTextView itemNameAutoCompleteTextView;
    private EditText itemQuantityEditText;
    private AddSaleAddItemDialogueListener listener;
    private Button confirmButton;
    private Button cancelButton;
    private TextView errorMassage;
    String errorMassageText;
    String itemName;
    String itemQuantity;

   private ArrayList<Pair<String, Integer>> itemList;

    AddSaleAddItemDialogue(ArrayList<Pair<String, Integer>> itemList) {
        this.itemList = itemList;
    }

    @Override
    public @NotNull Dialog onCreateDialog(Bundle saveInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item_in_add_sale, null);

        builder.setView(view);

        itemQuantityEditText = view.findViewById(R.id.get_item_quantity);
        itemNameAutoCompleteTextView = view.findViewById(R.id.get_item_name);
        confirmButton = view.findViewById(R.id.add_item_dialog_confirm_button);
        cancelButton = view.findViewById(R.id.add_item_dialog_cancel_button);
        errorMassage = view.findViewById(R.id.error_massage);

        confirmButton.setOnClickListener(v ->
                {
                    itemName = itemNameAutoCompleteTextView.getText().toString();
                    itemQuantity = itemQuantityEditText.getText().toString();

                    if(validItem()) {
                        listener.applyTexts(itemName, itemQuantity);
                        dismiss();
                    } else {
                        errorMassage.setText(errorMassageText);
                    }
                }
            );

        cancelButton.setOnClickListener(v -> dismiss());

        AutocompleteItemAdapter adapter = new AutocompleteItemAdapter(getContext(), itemList);
        itemNameAutoCompleteTextView.setAdapter(adapter);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        try {
            listener = (AddSaleAddItemDialogueListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement AddSaleAddItemDialogueLister");
        }
    }

    public interface AddSaleAddItemDialogueListener {
        void applyTexts(String itemName, String itemQuantity);
    }

    private boolean validItem() {
        if(itemQuantity == null || itemQuantity.length() == 0) {
            errorMassageText = getResources().getString(R.string.quantity_cant_be_empty);
            return false;
        }

        for(Pair<String, Integer> p : itemList) {
            if(p.first.equals(itemName)) {
                if(p.second < Integer.parseInt(itemQuantity)) {
                    errorMassageText = getResources().getString(R.string.not_enough_quantity);
                    return false;
                } else {
                    return true;
                }
            }
        }

        errorMassageText = getResources().getString(R.string.item_not_found);
        return false;
    }
}
