package com.epitech.EpiAndroid;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epitech.Model.GetBitMap;
import com.epitech.Model.PostRequest;
import com.epitech.Model.PostRequestArray;

import org.json.JSONArray;
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
    ListView list;
    String login;
    String url;
    JSONObject infos;
    JSONArray tabMessage;
    String    arrayMessage[];

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

        if (!isOnline())
        {
            Toast toast = Toast.makeText(getActivity(), R.string.ErrorNetwork, Toast.LENGTH_LONG);
            toast.show();
            return ;
        }

        PostRequest req = new PostRequest();
        PostRequestArray reqarray = new PostRequestArray();
        GetBitMap bitmap = new GetBitMap();
        try {
            photo = (ImageView) getView().findViewById(R.id.Photo);
            title = (TextView) getView().findViewById(R.id.Title);
            log = (TextView) getView().findViewById(R.id.Log);
            list = (ListView) getView().findViewById(R.id.LastMessage);

            infos = req.execute(getString(R.string.URLgetInfo), getString(R.string.token), MainActivity.token.getToken()).get();
            login = infos.getJSONObject(getString(R.string.info)).getString(getString(R.string.Login));

            req = new PostRequest();
            url = req.execute(getString(R.string.URLgetPhoto), getString(R.string.token), MainActivity.token.getToken(), getString(R.string.Login), login).get().getString(getString(R.string.url));
            photo.setImageBitmap(bitmap.execute(url).get());

            tabMessage = reqarray.execute(getString(R.string.URLgetMessages), getString(R.string.token), MainActivity.token.getToken()).get();
            arrayMessage = new String[tabMessage.length()];
            for (int i = 0; i < tabMessage.length(); i++) {
                JSONObject tmp = tabMessage.getJSONObject(i);
                int indexHtml = tmp.getString(getString(R.string.title)).indexOf(getString(R.string.HTMLLink));
                if (indexHtml == -1) {
                    arrayMessage[i] = tmp.getString(getString(R.string.title)) + " " + tmp.getString(getString(R.string.date));
                } else {
                    arrayMessage[i] = tmp.getString(getString(R.string.title)).substring(0, indexHtml) + Html.fromHtml(tmp.getString(getString(R.string.title)).substring(indexHtml)) + " " + tmp.getString(getString(R.string.date));
                }
            }

            ArrayAdapter<String> adpater = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, arrayMessage);
            list.setAdapter(adpater);

            title.setText(infos.getJSONObject(getString(R.string.info)).getString(getString(R.string.title)));
            Double nbr = Double.parseDouble(infos.getJSONObject(getString(R.string.current)).getString(getString(R.string.active_log)));
            log.setText(getString(R.string.PatternLog) + String.valueOf(nbr.intValue()) + getString(R.string.Hour));

        } catch (ExecutionException | InterruptedException | JSONException | NullPointerException e) {
            Toast toast = Toast.makeText(getActivity(), R.string.errorApi, Toast.LENGTH_LONG);
            toast.show();
         }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}