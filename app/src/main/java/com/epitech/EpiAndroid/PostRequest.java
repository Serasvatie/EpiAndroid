package com.epitech.EpiAndroid;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PostRequest extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(String[] params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        JSONObject result = null;
        HttpResponse response = null;

        // Add your data
        //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        //nameValuePairs.add(new BasicNameValuePair("id", "12345"));
        //nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));

        //try {
          //  httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        //} catch (UnsupportedEncodingException e) {
          //  e.printStackTrace();
        //}

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
