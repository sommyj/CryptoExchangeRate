package com.sommy.android.cryptoexchangerate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Conversation extends AppCompatActivity implements TextWatcher, View.OnFocusChangeListener {

    private TextView mForeignCurrencylabelTextView;
    private EditText mForeignCurrencyEditText;
    private TextView mCryptoAmountTextView;
    private TextView mCryptoCurrencylabelTextView;
    private EditText mCryptoCurrencyEditText;
    private TextView mForeignAmountTextView;
    private TextView mForeignCurrencySymbolTextView;
    private TextView mCryptoCurrencySymbolTextView;
    private Button mClearButton;

    private String bthForeignCurrencyString;
    private int cryptoCurrencyImage;
    private String cryptoCurrencySymbol;
    private String foreignCurrencySymbol;

    private int whoHasFocus= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        ActionBar actionBar = this.getSupportActionBar();

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mForeignCurrencylabelTextView = (TextView) findViewById(R.id.foriegn_textView);
        mForeignCurrencyEditText = (EditText) findViewById(R.id.foriegn_EditText);
        mCryptoAmountTextView = (TextView) findViewById(R.id.cryptoAmount_TextView);
        mCryptoCurrencylabelTextView = (TextView) findViewById(R.id.crypto_textView);
        mCryptoCurrencyEditText = (EditText) findViewById(R.id.crypto_EditText);
        mForeignAmountTextView = (TextView) findViewById(R.id.foriegnAmount_TextView);
        mForeignCurrencySymbolTextView = (TextView) findViewById(R.id.foreignSymbol_TextView);
        mCryptoCurrencySymbolTextView = (TextView) findViewById(R.id.cryptoSymbol_TextView);
        mClearButton = (Button) findViewById(R.id.clear_button);

        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)){
            String[] strings = intentThatStartedThisActivity.getStringArrayExtra(Intent.EXTRA_TEXT);

            bthForeignCurrencyString = strings[0];
            cryptoCurrencyImage = Integer.parseInt(strings[1]);
            cryptoCurrencySymbol = strings[2];
            foreignCurrencySymbol = strings[3];
            mCryptoCurrencySymbolTextView.setText(cryptoCurrencySymbol);
            mCryptoCurrencyEditText.setHint(R.string.crypto_default_Text);
            mCryptoAmountTextView.setText(cryptoCurrencySymbol+" "+"1.00");
            mForeignCurrencySymbolTextView.setText(foreignCurrencySymbol);
            mForeignAmountTextView.setText(foreignCurrencySymbol+" "+bthForeignCurrencyString);
            mForeignCurrencyEditText.setHint(bthForeignCurrencyString);
        }

//        getActionBar().setIcon(cryptoCurrencyImage);

            mForeignCurrencyEditText.setOnFocusChangeListener(this);
            mForeignCurrencyEditText.addTextChangedListener(this);
            mCryptoCurrencyEditText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        Double foreignAmount = 0.0;
        Double cryptoAmount = 0.0;
        double bthForeignCurrencyString = Double.parseDouble(this.bthForeignCurrencyString);
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);

        if(mForeignCurrencyEditText.getEditableText() == editable) {

            try {
                foreignAmount = Double.parseDouble(mForeignCurrencyEditText.getText().toString());
            }catch (NumberFormatException e){
            }

            double calculatedCryptoAmount = foreignAmount / bthForeignCurrencyString;

            mCryptoCurrencyEditText.setHint(decimalFormat.format(calculatedCryptoAmount).toString());
            mCryptoAmountTextView.setText(cryptoCurrencySymbol+" "+String.valueOf(decimalFormat.format(calculatedCryptoAmount)));
            mForeignAmountTextView.setText(foreignCurrencySymbol+" "+decimalFormat.format(foreignAmount));
            mForeignCurrencyEditText.setHint(foreignAmount.toString());
        }

        else if(mCryptoCurrencyEditText.getEditableText() == editable){

            try {
                cryptoAmount = Double.parseDouble(mCryptoCurrencyEditText.getText().toString());
            }catch (NumberFormatException e){
            }

            double calculatedForeignAmount = cryptoAmount * bthForeignCurrencyString;

            mForeignCurrencyEditText.setText("");
            mForeignCurrencyEditText.setHint(decimalFormat.format(calculatedForeignAmount));
            mForeignAmountTextView.setText(foreignCurrencySymbol+" "+decimalFormat.format(calculatedForeignAmount));
            mCryptoAmountTextView.setText(cryptoCurrencySymbol+" "+decimalFormat.format(cryptoAmount));
        }

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(view.getId() == R.id.foriegn_EditText) {
            if(!(mCryptoCurrencyEditText.getText().toString().equals("")) ){
          mCryptoCurrencyEditText.setText("");
                mCryptoCurrencyEditText.setHint(R.string.crypto_default_Text);
                mCryptoAmountTextView.setText(R.string.crypto_default_Text);
                mForeignAmountTextView.setText(bthForeignCurrencyString);
                mForeignCurrencyEditText.setHint(bthForeignCurrencyString);
            }
        }
    }

    public void clearText(View view){
        mCryptoCurrencyEditText.setText("");
        mForeignCurrencyEditText.setText("");
        mCryptoCurrencyEditText.setHint(R.string.crypto_default_Text);
        mForeignCurrencyEditText.setHint(bthForeignCurrencyString);
        mCryptoAmountTextView.setText(cryptoCurrencySymbol+" "+"1.00");
        mForeignAmountTextView.setText(foreignCurrencySymbol+" "+bthForeignCurrencyString);

    }
}