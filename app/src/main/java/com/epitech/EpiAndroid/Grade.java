package com.epitech.EpiAndroid;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.epitech.Model.PostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by favre_q on 31/01/15.
 */
public class Grade extends Fragment {

    JSONObject jsonreq;
    JSONArray tabModule;
    String arrayModule[];

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainNavigation) activity).onSectionAttached(4);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grade, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isOnline())
        {
            Toast toast = Toast.makeText(getActivity(), R.string.ErrorNetwork, Toast.LENGTH_LONG);
            toast.show();
            return ;
        }

        PostRequest req = new PostRequest();
        ListView modules = (ListView) getView().findViewById(R.id.listModule);
        try
        {
            jsonreq = req.execute(getString(R.string.URLgetModules), getString(R.string.token), MainActivity.token.getToken()).get();
            tabModule = jsonreq.getJSONArray(getString(R.string.modules));

            String tmparray[] = new String[tabModule.length()];
            int nbElem = 0;
            for (int i = 0; i < tabModule.length(); i++) {
                JSONObject tmp = tabModule.getJSONObject(i);
                if (!tmp.getString(getString(R.string.title)).equals("")) {
                    tmparray[i] = getString(R.string.nameModule) + tmp.getString(getString(R.string.title)) + "\n";
                    tmparray[i] = tmparray[i] + getString(R.string.dateSubModule) + tmp.getString(getString(R.string.date_ins)) + "\n";
                    tmparray[i] = tmparray[i] + getString(R.string.gradeModule) + tmp.getString(getString(R.string.grade)) + "\n";
                    tmparray[i] = tmparray[i] + getString(R.string.creditModule) + tmp.getString(getString(R.string.credits)) + "\n";
                    tmparray[i] = tmparray[i] + getString(R.string.moduleModule) + tmp.getString(getString(R.string.codemodule)) + "\n";
                    nbElem++;
                } else {
                    tmparray[i] = getString(R.string.Null);
                }
            }

            arrayModule = new String[nbElem];
            nbElem = 0;
            for (String tmp : tmparray)
            {
                if (!tmp.equals(getString(R.string.Null)) && nbElem < arrayModule.length)
                {
                    arrayModule[nbElem] = tmp;
                    nbElem++;
                }
            }
            ArrayAdapter<String> adpater = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, arrayModule);
            modules.setAdapter(adpater);

        } catch (InterruptedException | ExecutionException | JSONException | NullPointerException e) {
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
