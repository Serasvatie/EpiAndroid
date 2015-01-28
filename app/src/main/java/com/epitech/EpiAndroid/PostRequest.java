package com.epitech.EpiAndroid;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PostRequest extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(String[] params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        JSONObject result = null;
        HttpResponse response = null;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params.length - 1 / 2);

        for (int i = 1; i + 1 < params.length; i = i + 2)
        {
            nameValuePairs.add(new BasicNameValuePair(params[i], params[i + 1]));
        }

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.d("URL", params[0]);

        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            HttpEntity entity = response.getEntity();
            InputStream instream = null;
            try {
                instream = entity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            result = new JSONObject(convertStreamToString(instream));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // do above Server call here
        return result;
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
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
