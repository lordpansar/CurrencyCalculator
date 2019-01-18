package com.example.makem.currencycalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String[] currencyArray;
    private String[] rateArray;
    private ArrayAdapter<String> adapter;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private EditText amountEditText;
    private TextView textViewSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGUI();
    }

    private void initGUI()
    {
        currencyArray = getResources().getStringArray(R.array.currency_name_array);
        rateArray = getResources().getStringArray(R.array.currency_rate_array);

        //Array adapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencyArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Lägg till textchanged eventlyssnare MyTextWatcher till textboxen editTextAmount
        amountEditText = (EditText)findViewById(R.id.editTextAmount);
        amountEditText.addTextChangedListener(new MyTextWatcher());

        textViewSum = (TextView)findViewById(R.id.textViewSum);

        //Populate spinners and add event listeners
        spinnerFrom = (Spinner)findViewById(R.id.spinnerFrom);
        spinnerFrom.setAdapter(adapter);
        spinnerFrom.setOnItemSelectedListener(new CurrencySelectedListener());
        spinnerTo = (Spinner)findViewById(R.id.spinnerTo);
        spinnerTo.setAdapter(adapter);
        spinnerTo.setOnItemSelectedListener(new CurrencySelectedListener());
    }

    private void calculateExchangeRate()
    {
        int selectedIndexFrom = spinnerFrom.getSelectedItemPosition();
        int selectedIndexTo = spinnerTo.getSelectedItemPosition();

        if(selectedIndexFrom > 0 && selectedIndexTo > 0)
        {
            try {
                double from = Double.parseDouble(rateArray[selectedIndexFrom]);
                double to = Double.parseDouble(rateArray[selectedIndexTo]);
                double amount = Double.parseDouble(amountEditText.getText().toString());
                double sum = amount * (from / to);
                textViewSum.setText(String.format("%.2f %s", sum, currencyArray[selectedIndexTo])); //Print sum w. 2 decimals
            } catch (Exception e) {
                Log.v("Tag", "Exception occurred, " + e);
                textViewSum.setText("Incorrect input or settings");
            }
        } else{
            textViewSum.setText("");
        }
    }

    //TextChanged event listener
    private class MyTextWatcher implements TextWatcher
    {
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            calculateExchangeRate();
        }

        public void afterTextChanged(Editable s)
        {
            calculateExchangeRate();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }
    }

    //OnItemSelected event listener
    private class CurrencySelectedListener implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
            calculateExchangeRate();
        }

        public void onNothingSelected(AdapterView<?> parent)
        {

        }
    }

}
