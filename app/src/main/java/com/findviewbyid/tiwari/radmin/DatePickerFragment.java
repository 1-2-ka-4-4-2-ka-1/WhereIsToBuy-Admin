package com.findviewbyid.tiwari.radmin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment {

    private int type;

    public DatePickerFragment(int type) {
        this.type = type;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month= c.get(Calendar.MONTH);
        int day= c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener) getActivity(),year,month,day);

    }
}
