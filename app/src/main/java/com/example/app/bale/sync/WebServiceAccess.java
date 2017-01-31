package com.example.app.bale.sync;

import android.os.StrictMode;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by 13108306 on 01/11/2016.
 */
public class WebServiceAccess {
    private int TIMEOUT_MILLISEC = 5000;

    public String get(String URL) {

        HttpClient httpClient = new DefaultHttpClient();

        HttpGet chamadaGet = new HttpGet(URL);
        String retorno = "";

        try {

            // Aqui o ideal é colocar a requisição assíncrona
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpClient.execute(chamadaGet, responseHandler);
            retorno = responseBody;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retorno;
    }
}
