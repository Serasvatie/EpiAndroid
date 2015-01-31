package com.epitech.Model;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PostRequestArray extends AsyncTask<String, Void, JSONArray> {

    @Override
    protected JSONArray doInBackground(String[] params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        HttpResponse response;
        List<NameValuePair> nameValuePairs = new ArrayList<>(params.length - 1 / 2);
        JSONArray result;
        for (int i = 1; i + 1 < params.length; i = i + 2)
        {
            nameValuePairs.add(new BasicNameValuePair(params[i], params[i + 1]));
        }

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            HttpEntity entity = response.getEntity();
            InputStream stream;
            stream = entity.getContent();
            result = new JSONArray(convertStreamToString(stream));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
