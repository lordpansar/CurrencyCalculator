package com.example.makem.currencycalculator;

//import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
//import android.view.Menu;
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
        initGUI(); //Tilldela alla controllers eventlyssnare, värden m.m
    }

    private void initGUI()
    {
        //Sätt värden för arrayer
        currencyArray = getResources().getStringArray(R.array.currency_name_array);
        rateArray = getResources().getStringArray(R.array.currency_rate_array);

        //Arrayadapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencyArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Lägg till textchanged eventlyssnare MyTextWatcher till textboxen editTextAmount
        amountEditText = (EditText)findViewById(R.id.editTextAmount);
        amountEditText.addTextChangedListener(new MyTextWatcher());

        //Sätt instansvariabeln textViewSum
        textViewSum = (TextView)findViewById(R.id.textViewSum);

        //Sätt värden till spinners med valutor samt eventlyssnare CurrencySelectedListener
        spinnerFrom = (Spinner)findViewById(R.id.spinnerFrom);
        spinnerFrom.setAdapter(adapter);
        spinnerFrom.setOnItemSelectedListener(new CurrencySelectedListener());
        spinnerTo = (Spinner)findViewById(R.id.spinnerTo);
        spinnerTo.setAdapter(adapter);
        spinnerTo.setOnItemSelectedListener(new CurrencySelectedListener());
    }

    //Metod som gör uträkningen och presenterar den i GUI
    private void calculateExchangeRate()
    {
        //Få index på valt alternativ i spinners
        int selectedIndexFrom = spinnerFrom.getSelectedItemPosition();
        int selectedIndexTo = spinnerTo.getSelectedItemPosition();

        //Om valda valutors index är 0 så har användaren inte valt två valutor att konvertera
        if(selectedIndexFrom > 0 && selectedIndexTo > 0)
        {
            try {
                double from = Double.parseDouble(rateArray[selectedIndexFrom]); //Ta index som motsvarar vald valuta, och hämta motsvarande index i värdearrayen i strings.xml
                double to = Double.parseDouble(rateArray[selectedIndexTo]); //Ta index som motsvarar vald valuta, och hämta motsvarande index i värdearrayen i strings.xml
                double amount = Double.parseDouble(amountEditText.getText().toString()); //Hämta input från amountEditTextcontrollern
                double sum = amount * (from / to); //Själva uträkningen för konverteringen
                textViewSum.setText(String.format("%.2f %s", sum, currencyArray[selectedIndexTo])); //Skriv ut summan med två decimaler samt vilken valuta som konverteringen gjordes TILL
            } catch (Exception e) {
                Log.v("Tag", "Exception occurred, " + e); //Om det sker en exception, t ex att användaren inte matat in siffror, logga i Verbos
                textViewSum.setText("Incorrect input or settings"); //Skriv ut ett generellt felmeddelande till GUI
            }
        } else{
            textViewSum.setText(""); //Om användaren inte har valt valuta till eller från, sätt textViewSum till ingen text alls
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
