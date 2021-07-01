package edu.univdhaka.cse.cse2216.myshop.AddSale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

import edu.univdhaka.cse.cse2216.myshop.R;

public class DiscountDialog extends AppCompatDialogFragment {

    TextView totalAmountTextView;
    TextView errorTextView;
    EditText discountAmount;
    Button confirmButton;
    Button cancelButton;
    DiscountDialogListener listener;
    double total = 0;
    String discount;
    String errorText;

    DiscountDialog(double total) {
        this.total = total;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_sale_discount_dialog, null);

        builder.setView(view);

        totalAmountTextView = view.findViewById(R.id.discount_total_amount);
        errorTextView = view.findViewById(R.id.discount_error_text);
        discountAmount = view.findViewById(R.id.get_discount_amount);
        confirmButton = view.findViewById(R.id.discount_confirm_button);
        cancelButton = view.findViewById(R.id.discount_cancel_button);

        totalAmountTextView.setText((String.valueOf(total) + " " + getResources().getString(R.string.taka_logo)));

        confirmButton.setOnClickListener(v ->
                {
                    discount = discountAmount.getText().toString();
                    if(validDiscount()) {
                        listener.setDiscount(Double.parseDouble(discount));
                        dismiss();
                    } else {
                        errorTextView.setText(errorText);
                    }
                }
            );

        cancelButton.setOnClickListener(v -> dismiss());

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        try {
            listener = (DiscountDialog.DiscountDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement DiscountDialogueLister");
        }
    }

    public interface DiscountDialogListener {
        void setDiscount(double amount);
    }

    boolean validDiscount() {
        if(discount.length() == 0) {
            errorText = getResources().getString(R.string.discount_cant_be_empty);
            return false;
        }
        double discountValue = Double.parseDouble(discount);
        if(discountValue > total) {
            errorText = getResources().getString(R.string.discount_error);
            return false;
        }

        return true;
    }
}
