package br.edu.ifpb.serviceapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpService extends IntentService{

    private final String HTTPSERVICE_TAG = "Http_Service";

    private OkHttpClient client;
    private Request request;

    public HttpService() {
        super("HttpService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        client  = new OkHttpClient();

        String email = intent.getStringExtra("email");
        String pass = intent.getStringExtra("password");

        FormBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", pass)
                .build();
        request = new Request.Builder()
                .url("http://ag-ifpb-sgd-server.herokuapp.com/login")
                .post(formBody)
                .build();
        try {
            Log.v(HTTPSERVICE_TAG, "Enviando solicitação ...");
            Log.v(HTTPSERVICE_TAG, "Email: "+email+" Senha: "+pass);
            Response response = client.newCall(request).execute();
            Log.v(HTTPSERVICE_TAG, "Response code: "+response.code()+"");
            sendBroadcast(response.code() == 200);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void sendBroadcast (boolean success){
        Intent intent = new Intent ("android.intent.action.MAIN");
        intent.putExtra("success", success);
        this.sendBroadcast(intent);
    }

}
