package codepath.com.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import codepath.com.nytimessearch.R;

import static android.R.attr.data;
import static android.R.attr.format;
import static codepath.com.nytimessearch.R.id.spinSort;

public class FilterActivity extends AppCompatActivity {

    EditText etDate;
    DatePickerDialog datePickerDialog;
    Spinner spinSorter;
    int mSelected;
    Date returnDate;
    //String returnDate;
    String returnSort;
    String returnCategory = "";
    String checkedArts = "";
    String checkedFashion = "";
    String checkedSports = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        spinSorter = (Spinner) findViewById(R.id.spinSort);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinSorter.setAdapter(adapter);

        //spinner selection
        spinSorter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                returnSort = spinSorter.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                mSelected = 0;
                returnSort = spinSorter.getItemAtPosition(mSelected).toString();
            }

        });


        // initiate the date picker and a button
        etDate = (EditText) findViewById(R.id.etDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String datetime = dateFormat.format(date);
        etDate.setText(datetime);

        // perform click event on edit text
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(FilterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                etDate.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                etDate.setSelection(etDate.getText().length());

            }
        });

    }

    public void returnFilter(View view) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            returnDate = format.parse(etDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //returnDate = etDate.getText().toString();
        String queryDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(returnDate);
        int month = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DATE);
        int yr = cal.get(Calendar.YEAR);
        queryDate = yr + String.format("%02d", (month+1)) + String.format("%02d", dd);


        returnCategory = checkedArts + checkedFashion + checkedSports;

        //Return filter results
        Intent data = new Intent();

        data.putExtra("return_begin_date", queryDate);

        data.putExtra("return_sort", returnSort);
        data.putExtra("return_category", returnCategory.trim());

        setResult(RESULT_OK, data);
        finish();
    }

    public void onCheckboxClicked(View view) {

        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();


        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.cbArts:
                if (checked) {
                    checkedArts = "\"Arts\"" + " ";
                }else {
                    checkedArts = "";
                }
                break;
            case R.id.cbFashion:
                if (checked) {
                    checkedFashion = "\"Fashion & Style\"" + " ";
                }else {
                    checkedFashion = "";
                }
                break;
            case R.id.cbSport:
                if (checked) {
                    checkedSports = "\"Sports\"";
                }else {
                    checkedSports = "";
                }
                break;

        }
    }
}
