package edu.univdhaka.cse.cse2216.myshop;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class ChangeQuantityDialog extends DialogFragment {
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        builder.setTitle("Quantity ?");

        return builder.create();
    }
}
