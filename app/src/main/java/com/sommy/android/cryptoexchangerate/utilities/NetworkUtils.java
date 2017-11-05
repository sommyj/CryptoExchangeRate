package com.sommy.android.cryptoexchangerate.utilities;

import android.net.Uri;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by somto on 10/14/17.
 */

public final class NetworkUtils {

    private final static String CRYPTO_BASE_URL = "https://min-api.cryptocompare.com/data/pricemulti" +
            "?fsyms=BTC,ETH&tsyms=NGN,NGR,USD,EUR,GBP,CNY,JPY,RUB,CNH,AUD,INR,CAD,BRL,SEK,GHS,ZAR,MXN,KRW,NZD,HKD";
//    private final static  String QUERY_PARAM = "q";
//    private final static String queryValue = "location:lagos+language:java";

    /**
     * Builds the URL used to query Github
     *
     * @return The URL to use to query the github server.
     */
    public static URL buildUrl(){
//        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon()
//                .appendQueryParameter(QUERY_PARAM, queryValue)
//                .build();

        Uri builtUri = Uri.parse(CRYPTO_BASE_URL);

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String run(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
