package com.gautamk.spendtrack.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by gautam on 29/4/14.
 */
public class DatePickerFragment extends DialogFragment {

    Context context;
    Date date;
    DatePickerDialog.OnDateSetListener listener;

    DatePickerFragment(Context context, DatePickerDialog.OnDateSetListener listener) {
        this.context = context;
        this.date = new Date();
        this.listener = listener;
    }

    DatePickerFragment(Context context, DatePickerDialog.OnDateSetListener listener, Date date) {
        this.context = context;
        this.date = date;
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
        }
        // Use the current date as the default date in the picker

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(context, listener, year, month, day);
    }


}
