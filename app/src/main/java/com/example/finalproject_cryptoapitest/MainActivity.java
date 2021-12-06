package com.example.finalproject_cryptoapitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText cryptoSearch;
    private RecyclerView currenciesCoins;
    private ProgressBar loadingPB;
    private CryptoCoinAdapter cryptoCoinAdapter;
    private ArrayList<CryptoCoinsList> cryptoCoinsListArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //used for interactions from displayed crypto page
        cryptoSearch = findViewById(R.id.cryptoCoinSearch);
        currenciesCoins = findViewById(R.id.cryptoCoinCurrencies);
        loadingPB = findViewById(R.id.cryptoCoinProgressBar);

        cryptoCoinsListArrayList = new ArrayList<>();
        cryptoCoinAdapter = new CryptoCoinAdapter(cryptoCoinsListArrayList, this);
        currenciesCoins.setLayoutManager(new LinearLayoutManager(this));
        currenciesCoins.setAdapter(cryptoCoinAdapter);
        getcryptoCoinCurrencyData();

        cryptoSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    filterCryptoCoinCurrencies(s.toString());
            }
        });

    }

    //search function to find what currency I would like to via typing it in and searching through json data through the cryptosearch listner above
    private void filterCryptoCoinCurrencies(String currency){
        ArrayList<CryptoCoinsList> filterList = new ArrayList<>();
        for(CryptoCoinsList item: cryptoCoinsListArrayList){
            if(item.getName().toLowerCase().contains(currency.toLowerCase())){
                filterList.add(item);
            }
        }
        if(filterList.isEmpty()){
            Toast.makeText(this,"USD not found for Coin", Toast.LENGTH_SHORT).show();
        }
        else{
            cryptoCoinAdapter.filteredList(filterList);
        }

    }

    //request call from website using api key
    //using a for loop to take data from api key
    private void getcryptoCoinCurrencyData(){
        loadingPB.setVisibility(View.VISIBLE);
        String url =    "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                try {
                    JSONArray dataArray = response.getJSONArray("data");    //here we are going to the data part of the information from the api, we need to extract further information so we must go deeper
                    for(int i=0; i<dataArray.length();i++){
                        JSONObject dataObj = dataArray.getJSONObject(i);
                        String name = dataObj.getString("name");            // used to get name of crypto from key
                        String symbol = dataObj.getString("symbol");        //used to take symbol of crypto from key
                        JSONObject quote = dataObj.getJSONObject("quote");        //need to first go to quote in data to then go to USD and then to price get the the price of each crypto in USD amount
                        JSONObject USD = quote.getJSONObject("USD");
                        double price = USD.getDouble("price");              //setting out original variable price to USD amount from json call.
                        cryptoCoinsListArrayList.add(new CryptoCoinsList(name, symbol, price));     //adding to the list to be displayed
                    }
                    cryptoCoinAdapter.notifyDataSetChanged();                      //notifier
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed to get the data from API key", Toast.LENGTH_SHORT).show();
                }

            } //IF FAIL
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to get the data from API key", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY", "f814a8ec-c25a-45d5-80ab-f08cf18abab3");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}