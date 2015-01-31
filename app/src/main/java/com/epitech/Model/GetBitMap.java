package com.epitech.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

/**
 * Created by favre_q on 31/01/15.
 */
public class GetBitMap extends AsyncTask<String, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(String... params) {
        URL url;
        Bitmap image;
        try
        {
            url = new URL(params[0]);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e)
        {
            return null;
        }
        return image;
    }

}
