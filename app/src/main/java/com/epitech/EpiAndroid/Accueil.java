package com.epitech.EpiAndroid;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epitech.Model.GetBitMap;
import com.epitech.Model.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by favre_q on 31/01/15.
 */
public class Accueil extends Fragment {

    ImageView photo;
    TextView title;
    TextView log;
    String login;
    String url;
    JSONObject infos;

    public Accueil()
    {
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PostRequest req = new PostRequest();
        GetBitMap bitmap = new GetBitMap();
        try {
            photo = (ImageView) getView().findViewById(R.id.Photo);
            title = (TextView) getView().findViewById(R.id.Title);
            log = (TextView) getView().findViewById(R.id.Log);
            infos = req.execute("http://epitech-api.herokuapp.com/infos", "token", MainActivity.token.getToken()).get();
            login = infos.getJSONObject("infos").getString("login");
            req = new PostRequest();
            url = req.execute("http://epitech-api.herokuapp.com/photo", "token", MainActivity.token.getToken(), "login", login).get().getString("url");
            photo.setImageBitmap(bitmap.execute(url).get());
            title.setText(infos.getJSONObject("infos").getString("title"));
            Double nbr = Double.parseDouble(infos.getJSONObject("current").getString("active_log"));
            log.setText(getString(R.string.PatternLog) + String.valueOf(nbr.intValue()) + getString(R.string.Hour));
        } catch (ExecutionException | InterruptedException | JSONException | NullPointerException e) {
            Toast toast = Toast.makeText(getActivity(), R.string.errorApi, Toast.LENGTH_LONG);
            toast.show();
         }
    }
}
