package ru.sber.converter.interactor;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import ru.sber.converter.domain.RatesResponse;
import ru.sber.mvp.InteractorCallback;
import ru.sber.storage.RatesTable;
import ru.sber.util.XmlUtils;

/**
 * Created by Vitaly on 19.11.2017.
 */

class GetRatesAsyncTask extends AsyncTask<Void, Void, Response<RatesResponse>> {

    private final static String CURRENCIES_URL = "http://www.cbr.ru/scripts/XML_daily.asp";

    private final InteractorCallback<RatesResponse> mCallback;

    GetRatesAsyncTask(InteractorCallback<RatesResponse> callback) {
        mCallback = callback;
    }

    @Override
    protected Response<RatesResponse> doInBackground(Void... voids) {
        try {
            String raw = getRawResponse();
            RatesResponse ratesResponse = XmlUtils.get().deserialize(raw, RatesResponse.class);

            if (ratesResponse == null || ratesResponse.currencyInfoList == null ||
                    ratesResponse.currencyInfoList.isEmpty()) {
                if (isCancelled()) {
                    return new Response<>(null, null);
                }
                ratesResponse = new RatesResponse();
                ratesResponse.currencyInfoList = RatesTable.get();
                if (ratesResponse.currencyInfoList.isEmpty()) {
                    return new Response<>(new NullPointerException("Response is null"));
                }
                return new Response<>(ratesResponse);
            }
            RatesTable.replace(ratesResponse.currencyInfoList);
            return new Response<>(ratesResponse);
        } catch (Throwable t) {
            if (isCancelled()) {
                return new Response<>(null, null);
            }
            RatesResponse ratesResponse = new RatesResponse();
            ratesResponse.currencyInfoList = RatesTable.get();
            if (ratesResponse.currencyInfoList.isEmpty()) {
                return new Response<>(t);
            }
            return new Response<>(ratesResponse);
        }
    }

    @Override
    protected void onPostExecute(Response<RatesResponse> ratesResponseResponse) {
        super.onPostExecute(ratesResponseResponse);
        if (isCancelled() || mCallback == null) {
            return;
        }

        if (ratesResponseResponse.getError() != null) {
            mCallback.onError(ratesResponseResponse.getError());
            return;
        }

        mCallback.onSuccess(ratesResponseResponse.getBody());
    }

    private String getRawResponse() throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(CURRENCIES_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();

            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),
                    Charset.forName("windows-1251")));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
