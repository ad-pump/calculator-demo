package com.adpumb.ads.util;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HttpConnection {
    public static String POST = "POST";
    public static String GET = "GET";
    private String url;
    private Map<String,String> headers;
    private String verb = GET;
    public HttpConnection(String url){
        this.url = url;
        this.headers = new HashMap<>();
    }
    public void setVerb(String verb){
        this.verb = verb;
    }
    public void addHeader(String header,String value){
        this.headers.put(header,value);
    }

    public HttpResponse connect(String body){
        try {
            URL connectUrl = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) connectUrl.openConnection();
            connection.setRequestMethod(verb);
            setHeaders(connection);
            writeBody(body, connection);
            return new HttpResponse(connection);
        } catch (IOException e) {
            return new HttpResponse(false,0,e.getMessage());
        }
    }

    private void writeBody(String body, HttpsURLConnection connection) throws IOException {
        if(body!=null){
            connection.getOutputStream().write(body.getBytes());
        }
    }

    private void setHeaders(HttpsURLConnection connection) {
        for(Map.Entry<String,String> entry:headers.entrySet()){
            connection.setRequestProperty(entry.getKey(),entry.getValue());
        }
    }
}
