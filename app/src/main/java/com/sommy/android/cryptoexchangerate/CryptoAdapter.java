package com.sommy.android.cryptoexchangerate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by somto on 10/12/17.
 */

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.CryptoAdapterViewHolder> {

    private Context mContext;
    private CryptoOnClickHandler mClickHandler;
    private ArrayList<String> currencyAmount;
    private ArrayList<Integer> currencyImage;
    private ArrayList<String> cryptoCurrencySymbols;
    private ArrayList<String> cryptoName;
    private String foreignCurrencySymbols;

    public CryptoAdapter(Context context, CryptoOnClickHandler mClickHandler) {
        this.mContext = context;
        this.mClickHandler = mClickHandler;
    }

    @Override
    public CryptoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutForListItem = R.layout.crypto_currency_list;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, parent, shouldAttachToParentImmediately);
        return new CryptoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CryptoAdapterViewHolder holder, int position) {
        holder.mTextView1.setText(cryptoName.get(position));
        holder.mImageView.setImageResource(currencyImage.get(position));
        holder.mTextView2.setText(foreignCurrencySymbols+" " + currencyAmount.get(position));
    }

    @Override
    public int getItemCount() {
        if(null == currencyAmount){
            return 0;
        }else {
            return currencyAmount.size();
        }
    }

    void setCryptoJasonData(ArrayList<String> currencyAmount,ArrayList<Integer> currencyImage,
                            ArrayList<String> cryptoCurrencySymbols, String foreignCurrencySymbols, ArrayList<String> cryptoName){
        this.currencyAmount = currencyAmount;
        this.currencyImage = currencyImage;
        this.cryptoCurrencySymbols = cryptoCurrencySymbols;
        this.foreignCurrencySymbols = foreignCurrencySymbols;
        this.cryptoName = cryptoName;
        notifyDataSetChanged();
    }

    interface CryptoOnClickHandler{
        void onClick(String[] string);
    }

    class CryptoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView mTextView1;
        private final ImageView mImageView;
        private final TextView mTextView2;

        CryptoAdapterViewHolder(View itemView) {
            super(itemView);
            this.mTextView1 = itemView.findViewById(R.id.crypto_name);
            this.mImageView = itemView.findViewById(R.id.crypto_imageView);
            this.mTextView2 = itemView.findViewById(R.id.dollar_textView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            int position = getAdapterPosition();
            String currencyAmountString = currencyAmount.get(position);
            String currencyImageString = currencyImage.get(position).toString();
            String cryptoCurrencyCurrency = cryptoCurrencySymbols.get(position);
            String[] strings = {currencyAmountString, currencyImageString, cryptoCurrencyCurrency, foreignCurrencySymbols};
            Log.i(TAG, currencyAmountString+" "+currencyImageString);
            mClickHandler.onClick(strings);
        }
    }

}
