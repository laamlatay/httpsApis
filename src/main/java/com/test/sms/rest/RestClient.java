package com.test.sms.rest;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;

import java.io.IOException;

public class RestClient {
    HttpClient httpClient;

    public RestClient() {
        httpClient = createHttpClient(null, null);
    }


    public int callPostApi(JSONObject jsonObject, String url, String status, String id) {
        PostMethod postMethod = new PostMethod(url);
        int status1 = 0;
        try {
            postMethod.addRequestHeader(new Header("Accept", "application/json"));

            postMethod.addParameter(new NameValuePair("SmsStatus", status));
            postMethod.addParameter(new NameValuePair("MessageSid", id));
            httpClient.executeMethod(postMethod);
            status1 = postMethod.getStatusCode();
            System.out.println(status1);
            postMethod.releaseConnection();
        } catch (IOException e) {
            System.out.println(e);
        }

        return status1;
    }

    public int callPostApi(JSONObject jsonObject, String url, String message, String to, String from) {
        PostMethod postMethod = new PostMethod(url);
        int status1 = 0;
        try {
            postMethod.addRequestHeader(new Header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
            postMethod.addRequestHeader(new Header("Accept", "application/json"));

            postMethod.addParameter(new NameValuePair("To", to));
            postMethod.addParameter(new NameValuePair("Body", message));
            postMethod.addParameter(new NameValuePair("From", from));
            httpClient.executeMethod(postMethod);
            status1 = postMethod.getStatusCode();
            System.out.println(status1);
            postMethod.releaseConnection();
        } catch (IOException e) {
            System.out.println(e);
        }

        return status1;
    }

    public String callGetAPI() {
        GetMethod getMethod = new GetMethod("https://192.168.1.210:7070/test");
        int status1 = 0;
        String response = "";
        try {

            httpClient.executeMethod(getMethod);
            status1 = getMethod.getStatusCode();
            response = getMethod.getResponseBodyAsString();
            System.out.println(status1);
            getMethod.releaseConnection();
        } catch (IOException e) {
            System.out.println(e);
        }
        return response;
    }

    public HttpClient createHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        if (username != null && password != null) {
            Credentials credentials = new UsernamePasswordCredentials(username, password);
            client.getState().setCredentials(AuthScope.ANY, credentials);
        }
        return client;
    }

}
