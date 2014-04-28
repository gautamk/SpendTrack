package com.gautamk.spendtrack.app;

import android.app.*;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import com.gautamk.spendtrack.app.managers.SpendManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddSpendFragement extends Fragment {

    private OnSpentFragmentInteractionListener mListener;
    private EditText amount;
    private EditText note;
    private Button dateButton;
    private AutoCompleteTextView tag;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SpendManager.Spend spend = null;

    public AddSpendFragement() {
        setHasOptionsMenu(true);
    }

    public AddSpendFragement(SpendManager.Spend spend) {
        this();
        this.spend = spend;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_add_spend_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    private void save_spend() {
        float amount = Float.parseFloat(this.amount.getText().toString());
        String note = this.note.getText().toString();
        String tag = this.tag.getText().toString();
        Date date = (Date) this.dateButton.getTag();
        spend.setAmount(amount);
        spend.setTag(tag);
        spend.setDate(date);
        spend.setNote(note);
        SpendManager.add(spend);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                save_spend();
                break;
            case R.id.delete:
                break;
        }
        mListener.closeSpend();
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (this.spend == null) {
            this.spend = new SpendManager.Spend(getActivity());
        }
        View fragmentView = inflater.inflate(R.layout.fragment_add_spend, container, false);
        this.amount = (EditText) fragmentView.findViewById(R.id.amount);
        this.amount.setText("" + this.spend.getAmount());

        this.note = (EditText) fragmentView.findViewById(R.id.note);
        this.note.setText(spend.getNote());

        this.dateButton = (Button) fragmentView.findViewById(R.id.date);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        dateButton.setText(dateFormat.format(spend.getDate()));
        dateButton.setTag(spend.getDate());
        this.tag = (AutoCompleteTextView) fragmentView.findViewById(R.id.tag);
        this.tag.setText(spend.getTag());
        return fragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSpentFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSpentFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSpentFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);


        public void closeSpend();
    }

    private class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            Date date = (Date) dateButton.getTag();
            if (date != null) {
                c.setTime(date);
            }
            // Use the current date as the default date in the picker

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date date = calendar.getTime();
            dateButton.setTag(date);
            dateButton.setText(dateFormat.format(date));
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        Activity activity = getActivity();
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

}
