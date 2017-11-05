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
 * This class exposes the coversion of crypto currency details to a the RecyclerView.
 * Created by somto on 10/12/17.
 */

class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.CryptoAdapterViewHolder> {

    private Context mContext;

    /**
     * An on-click handler
     */
    private CryptoOnClickHandler mClickHandler;
    private ArrayList<String> currencyAmount;
    private ArrayList<Integer> currencyImage;
    private ArrayList<String> cryptoCurrencySymbols;
    private ArrayList<String> cryptoName;
    private String foreignCurrencySymbols;

    /**
     * Creates a CryptoAdapter.
     *
     * @param mClickHandler The on-click handler for this adapter. This single handler is called
     *                      when an item is clicked.
     */
    CryptoAdapter(Context context, CryptoOnClickHandler mClickHandler) {
        this.mContext = context;
        this.mClickHandler = mClickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent   The ViewGroup that these ViewHolders are contained within.
     * @param viewType The ViewType integer is used to provide a different layout,
     *                 if the RecyclerView has more than one type of item (which ours does).
     * @return A new CryptoAdapterViewHolder that holds the View for each list item
     */
    @Override
    public CryptoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutForListItem = R.layout.crypto_currency_list;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem, parent, shouldAttachToParentImmediately);
        return new CryptoAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the CryptoCompare
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(CryptoAdapterViewHolder holder, int position) {
        holder.mTextView1.setText(cryptoName.get(position));
        holder.mImageView.setImageResource(currencyImage.get(position));
        holder.mTextView2.setText(foreignCurrencySymbols+" " + currencyAmount.get(position));
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our cryptoCompare query
     */
    @Override
    public int getItemCount() {
        if(null == currencyAmount){
            return 0;
        }else {
            return currencyAmount.size();
        }
    }

    /**
     * This method is used to set the cryptoCompare details on a CryptoAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new CryptoAdapter to display it.
     *
     * @param currencyAmount
     * @param currencyImage
     * @param cryptoCurrencySymbols
     * @param foreignCurrencySymbols
     * @param cryptoName
     */
    void setCryptoJasonData(ArrayList<String> currencyAmount,ArrayList<Integer> currencyImage,
                            ArrayList<String> cryptoCurrencySymbols, String foreignCurrencySymbols, ArrayList<String> cryptoName){
        this.currencyAmount = currencyAmount;
        this.currencyImage = currencyImage;
        this.cryptoCurrencySymbols = cryptoCurrencySymbols;
        this.foreignCurrencySymbols = foreignCurrencySymbols;
        this.cryptoName = cryptoName;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onClick messages.
     */
    interface CryptoOnClickHandler{
        void onClick(String[] string);
    }

    /**
     * Cache of the children views for a labels and cryptoImage list item.
     */
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

        /**
         * This gets called by the child views during a click.
         *
         * @param view The view that was clicked
         */
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
