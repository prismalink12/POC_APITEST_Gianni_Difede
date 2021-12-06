package com.example.finalproject_cryptoapitest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

//creating an adapter object for future underlying data from api to display cryptocurrencies

public class CryptoCoinAdapter extends RecyclerView.Adapter<CryptoCoinAdapter.ViewHolder> {

    private ArrayList<CryptoCoinsList> cryptoCoinsListArrayList;
    private Context context;
    //this is used to show how many decimals we want to display when showing the price for the crypto
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public CryptoCoinAdapter(ArrayList<CryptoCoinsList> cryptoCoinsListArrayList, Context context) {
        this.cryptoCoinsListArrayList = cryptoCoinsListArrayList;
        this.context = context;
    }

    public void filteredList(ArrayList<CryptoCoinsList> filterList){
        cryptoCoinsListArrayList = filterList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CryptoCoinAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.crypto_coin_currency_item,parent,false);
        return new CryptoCoinAdapter.ViewHolder(view);
    }

    //taking from api to display with money sign before and then price after
    @Override
    public void onBindViewHolder(@NonNull CryptoCoinAdapter.ViewHolder holder, int position) {
        CryptoCoinsList cryptoCoinsList = cryptoCoinsListArrayList.get(position);
        holder.cryptoCurrencyNameAd.setText(cryptoCoinsList.getName());
        holder.cryptoCurrencysymbolAd.setText(cryptoCoinsList.getSymbol());
        holder.cryptoCurrencyrateAd.setText("$ " + df2.format(cryptoCoinsList.getPrice()));
    }

    @Override
    public int getItemCount() {
        return cryptoCoinsListArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView cryptoCurrencyNameAd,cryptoCurrencysymbolAd,cryptoCurrencyrateAd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cryptoCurrencyNameAd = itemView.findViewById(R.id.itemNameOfCurrency);
            cryptoCurrencysymbolAd = itemView.findViewById(R.id.itemSymbol);
            cryptoCurrencyrateAd = itemView.findViewById(R.id.itemRateOfCurrency);
        }
    }
}
