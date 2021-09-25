package com.adpumb.ads.util;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;



public class HttpResponse {
    private HttpsURLConnection urlConnection;

    public boolean isSucess() {
        return isSucess;
    }

    private boolean isSucess;
    private int responseCode;
    private String response;

    public HttpResponse(boolean isSucess,int responseCode,String response){
        this.isSucess = isSucess;
        this.responseCode = responseCode;
        this.response = response;
    }
    public HttpResponse(HttpsURLConnection urlConnection) {
        this.urlConnection = urlConnection;
        try {
            parse();
        } catch (IOException e) {
            //this is fatal. Should do something here
            isSucess = false;
            responseCode = 0;
        }
    }

    private void parse() throws IOException {
        int responseCode = urlConnection.getResponseCode();
        if(responseCode<400){
            isSucess = true;
        }else{
            isSucess = false;
        }
        InputStream inputStream = isSucess?urlConnection.getInputStream():urlConnection.getErrorStream();
        response = read(inputStream);
    }

    private String read(InputStream inputStream){
        StringBuilder builder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()){
            builder.append(scanner.next());
        }
        return builder.toString();
    }

    public int statusCode(){
        return responseCode;
    }

    public String responseString(){
        return response;
    }

    public <T> T responseObject(Class<T> type){
        return new Gson().fromJson(response,type);
    }

}
