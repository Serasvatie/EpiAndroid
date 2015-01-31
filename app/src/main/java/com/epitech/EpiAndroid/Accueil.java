package com.epitech.EpiAndroid;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.epitech.Model.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by favre_q on 31/01/15.
 */
public class Accueil extends Fragment {

    ImageView photo;
    String login;
    String url;
    JSONObject infos;
    PostRequest req;

    public Accueil()
    {
        photo = (ImageView) getView().findViewById(R.id.Photo);

        try {
            infos = req.execute("http://epitech-api.herokuapp.com/infos", "token", MainActivity.token.getToken()).get();
            login = infos.getJSONObject("infos").getString("login");
            req = null;
            url = req.execute("http://epitech-api.herokuapp.com/photo", "token", MainActivity.token.getToken(), "login", login).get().getString("url");
            photo.setImageBitmap(BitmapFactory.decodeStream(new URL("url").openConnection().getInputStream()));
            Log.d("URL", url);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainNavigation) activity).onSectionAttached(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accueil, container, false);
    }

}
