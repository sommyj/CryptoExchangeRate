package com.sommy.android.cryptoexchangerate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sommy.android.cryptoexchangerate.utilities.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, AdapterView.OnItemSelectedListener, CryptoAdapter.CryptoOnClickHandler {

    private final static int CRYPTO_LOADER = 0;
    private final static String COUNTRY_KEY = "countryCode";
    private String countryCode = "NGN";
    private String TAG = MainActivity.class.getSimpleName();
    private String foreignCurrencySymbols = "₦";

    private RecyclerView mRecyclerView;
    private CryptoAdapter mCryptoAdapter;
    private Spinner mSpinner;
    private ProgressBar mPbIndicator;
    private TextView mErrorTextView;
    private Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSpinner = (Spinner) findViewById(R.id.country_spinner);
        mSpinner.setOnItemSelectedListener(this);
        mPbIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorTextView = (TextView) findViewById(R.id.error_textView);
        bundle.putString(COUNTRY_KEY, countryCode);

        /*
        Setting the dropdown list
         */
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.country_currency_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);

        /*
        Setting the RecyclerView
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mCryptoAdapter = new CryptoAdapter(MainActivity.this, this);
        mRecyclerView.setAdapter(mCryptoAdapter);
    }

    /**
     * This method start the content view if there is active network or send and error message if not.
     */
    protected void onStart() {
        super.onStart();
        getSupportLoaderManager().initLoader(CRYPTO_LOADER, bundle, this);
    }

    /**
     * This method will make the error message visible and hide the ListView.
     */
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mSpinner.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the View for the crypto data visible and
     * hide the error message.
     */
    private void showCryptoDataView() {
        mErrorTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mSpinner.setVisibility(View.VISIBLE);
    }

    /**
     * This method is used to fetch for crypto currency compared to the country currency using the CryptoCompare.com API.
     *
     * @param id   The LoaderManager an ID
     * @param args The bundle that will receive data from initialize loader.
     * @return An AsyncTaskLoader that will return the Loader to the onLoadFinished method.
     */
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String mCryptoJson;

            @Override
            protected void onStartLoading() {
                mSpinner.setVisibility(View.INVISIBLE);
                mPbIndicator.setVisibility(View.VISIBLE);
                if (null != mCryptoJson) {
                    deliverResult(mCryptoJson);
                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                String countryCode = args.getString(COUNTRY_KEY);

                final String url = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=BTC,ETH&tsyms=" +
                        countryCode;
                try {
                    return NetworkUtils.run(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void deliverResult(String cryptoJson) {
                mCryptoJson = cryptoJson;
                super.deliverResult(cryptoJson);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mSpinner.setVisibility(View.VISIBLE);
        mPbIndicator.setVisibility(View.INVISIBLE);
        if (null == data) {
            showErrorMessage();
            Toast.makeText(MainActivity.this, "Check your internet connection", Toast.LENGTH_LONG).show();
        } else {
            ArrayList<String> countryCryptoCurrencyAmount = new ArrayList<>();
            ArrayList<Integer> countryCryptoCurrencyImage = new ArrayList<>();
            ArrayList<String> cryptoCurrencySymbols = new ArrayList<>();
            ArrayList<String> cryptoName = new ArrayList<>();
            String countryBitcoin = "";
            String countryEthereum = "";

            JSONObject jsonObject;
            JSONObject bitcoin;
            JSONObject ethereum;
            try {
                jsonObject = new JSONObject(data);
                bitcoin = jsonObject.getJSONObject("BTC");
                ethereum = jsonObject.getJSONObject("ETH");
                countryBitcoin = bitcoin.getString(countryCode);
                countryEthereum = ethereum.getString(countryCode);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            countryCryptoCurrencyAmount.add(countryBitcoin);
            countryCryptoCurrencyAmount.add(countryEthereum);
            Log.i("JSON", countryCryptoCurrencyAmount.get(0));

            countryCryptoCurrencyImage.add(R.drawable.btc);
            countryCryptoCurrencyImage.add(R.drawable.eth);

            cryptoCurrencySymbols.add("B");
            cryptoCurrencySymbols.add("Ξ");

            cryptoName.add("Bitcoin");
            cryptoName.add("Ethereum");


            mCryptoAdapter.setCryptoJasonData(countryCryptoCurrencyAmount, countryCryptoCurrencyImage,
                    cryptoCurrencySymbols, foreignCurrencySymbols, cryptoName);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    /**
     * if an item is clicked in the drop down list it launches you to the particular currency of the country currency
     *
     * @param adapterView
     * @param view
     * @param i           position of item
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        countryCode = adapterView.getItemAtPosition(i).toString();
        mRecyclerView.setVisibility(View.INVISIBLE);
        bundle.putString(COUNTRY_KEY, countryCode);
        getSupportLoaderManager().restartLoader(CRYPTO_LOADER, bundle, this);

//        Passing symbols value to each variable
        switch (countryCode) {
            case "NGN":
                foreignCurrencySymbols = "₦";
                break;
            case "USD":
                foreignCurrencySymbols = "$";
                break;
            case "EUR":
                foreignCurrencySymbols = "€";
                break;
            case "CAD":
                foreignCurrencySymbols = "C$";
                break;
            case "CNH":
                foreignCurrencySymbols = "人民币";
                break;
            case "GBP":
                foreignCurrencySymbols = "£";
                break;
            case "JPY":
                foreignCurrencySymbols = "¥";
                break;
            case "AUD":
                foreignCurrencySymbols = "AU$";
                break;
            case "NZD":
                foreignCurrencySymbols = "NZ$";
                break;
            case "MXN":
                foreignCurrencySymbols = "Mex$";
                break;
            case "ZAR":
                foreignCurrencySymbols = "R";
                break;
            case "KES":
                foreignCurrencySymbols = "KSh";
                break;
            case "RUB":
                foreignCurrencySymbols = "ᵱ";
                break;
            case "MYR":
                foreignCurrencySymbols = "RM";
                break;
            case "SEK":
                foreignCurrencySymbols = "kr";
                break;
            case "HKD":
                foreignCurrencySymbols = "元";
                break;
            case "BRL":
                foreignCurrencySymbols = "R$";
                break;
            case "GHS":
                foreignCurrencySymbols = "GH₵";
                break;
            case "KRW":
                foreignCurrencySymbols = "₩";
                break;
            case "INR":
                foreignCurrencySymbols = "₹";
                break;
            default:
                foreignCurrencySymbols = "";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * If an item is clicked, it launches you to the child class(Conversation.java).
     * and it passes some info with the use of intent.
     *
     * @param strings The conversion of crypto currency to country currency data to be displayed.
     */
    @Override
    public void onClick(String[] strings) {
        Intent startConversationActvityIntent = new Intent(MainActivity.this, Conversation.class);
        startConversationActvityIntent.putExtra(Intent.EXTRA_TEXT, strings);

        startActivity(startConversationActvityIntent);

        Log.i(TAG, strings[0] + ", " + strings[1]);
    }

    /**
     * This method is used when we are resetting data, so that at one point in time during a
     * refresh of our data, you can see that there is no data showing.
     */
    private void invalidateData() {
        ArrayList<String> countryCryptoCurrencyAmount = new ArrayList<>();
        ArrayList<Integer> countryCryptoCurrencyImage = new ArrayList<>();
        ArrayList<String> cryptoCurrencySymbols = new ArrayList<>();
        ArrayList<String> cryptoName = new ArrayList<>();
        String foreignCurrencySymbols = "";
        mCryptoAdapter.setCryptoJasonData(countryCryptoCurrencyAmount, countryCryptoCurrencyImage, cryptoCurrencySymbols, foreignCurrencySymbols, cryptoName);
        showCryptoDataView();
    }

    /**
     * Adding Refresh menu
     *
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.index, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Adding Refresh menuItem
     *
     * @param menuItem
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemThatWasSelected = menuItem.getItemId();
        if (itemThatWasSelected == R.id.action_refresh) {
            invalidateData();
            bundle.putString(COUNTRY_KEY, countryCode);
            getSupportLoaderManager().restartLoader(CRYPTO_LOADER, bundle, this);

        }
        return super.onOptionsItemSelected(menuItem);
    }

}
