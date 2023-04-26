package com.example.taxcalculatorfrontend.DAL;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ComponentActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TaxesAPI extends IntentService {

    String urlString = "http:/10.0.2.2:8084/api/global-service/calculate?amount=";
    URL url;
    Double amount;
    HttpURLConnection urlConnection = null;
    String apiString="0;0;0;0";

    public TaxesAPI() {
        super("TaxesAPI");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        amount = intent.getExtras().getDouble("amount",0);
        Log.d("DEBUG","Starting API");
        if (intent != null) {
            try{
                String request;
                StringBuilder sb = new StringBuilder();
                sb.append(urlString).append(amount);
                request = sb.toString();
                Log.i("DEBUG","Requesting: "+request);
                url = new URL(request);
                //open URL connection
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                // use a string builder to bufferize the response body
                // read from the input stream
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                sb = new StringBuilder();
                String line;
                while((line = br.readLine())!=null){
                    sb.append(line);
                }

                String body = sb.toString();
                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                Log.i("DEBUG","API SUCCESS");
                Log.d("HTTP-GET",body);
                try
                {
                    Bundle bundle = intent.getExtras();
                    if (bundle!=null){
                        Messenger messenger = (Messenger) bundle.get("messenger");
                        Message msg = Message.obtain();
                        Bundle b = new Bundle();
                        b.putString("msg",body);
                        msg.setData(b);
                        try{
                            messenger.send(msg);
                        } catch (RemoteException e){
                            Log.e("DEBUG","error using messenger");
                        }
                    }
                }
                catch (Exception e){
                    Log.e("DEBUG","Messenger error");
                }


            } catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed to call API", Toast.LENGTH_SHORT).show();
                Log.e("DEBUG","API Failure");
            } finally {
                if(urlConnection!=null)
                {
                    urlConnection.disconnect();
                }
            }
        }

    }
}