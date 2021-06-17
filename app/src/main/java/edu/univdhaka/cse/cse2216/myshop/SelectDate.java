package edu.univdhaka.cse.cse2216.myshop;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import edu.univdhaka.cse.cse2216.myshop.Database.FirebaseDatabase;
import edu.univdhaka.cse.cse2216.myshop.History.CartAdaptor;

public class SelectDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private CartAdaptor cartAdaptor;
    private TextView dateText;
    public SelectDate(CartAdaptor cartAdaptor,TextView dateText)
    {
        this.cartAdaptor = cartAdaptor;
        this.dateText = dateText;
    }
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        filter adaptor
        month++;
        String yearString,monthString,dayString;
        if(month < 10)
        {
            monthString = "0"+String.valueOf(month);
        }
        else
        {
            monthString = String.valueOf(month);
        }
        if(dayOfMonth < 10)
        {
            dayString = "0"+String.valueOf(dayOfMonth);
        }
        else
        {
            dayString = String.valueOf(dayOfMonth);
        }
        yearString = String.valueOf(year);
        String date = yearString+"-"+monthString+"-"+dayString;
        Log.d("noman",date);
        Log.d("noman",String.valueOf(month));
        dateText.setText(date);
        FirebaseDatabase.getCarts(view.getContext(),cartAdaptor,date);

    }
}
