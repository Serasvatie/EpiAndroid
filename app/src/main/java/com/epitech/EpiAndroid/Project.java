package com.epitech.EpiAndroid;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.epitech.Model.PostRequest;
import com.epitech.Model.PostRequestArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by favre_q on 02/02/15.
 */
public class Project extends Fragment {

    JSONArray arrayreq;
    String  arrayProject[];
    ListView Project;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainNavigation) activity).onSectionAttached(5);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project, container, false);
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

        PostRequestArray req = new PostRequestArray();
        try {
            Project = (ListView) getView().findViewById(R.id.listProject);
            arrayreq = req.execute(getString(R.string.URLgetProjects), getString(R.string.token), MainActivity.token.getToken()).get();
            String tmparray[] = new String[arrayreq.length()];
            int nbElem = 0;

            for (int i = 0; i < arrayreq.length(); i++) {
                try {
                    JSONObject tmp = arrayreq.getJSONObject(i);
                    if (tmp.getString(getString(R.string.type_acti)).equals(getString(R.string.getProject)) || tmp.getString(getString(R.string.type_acti)).equals(getString(R.string.getProjects))) {
                        tmparray[i] = tmp.getString(getString(R.string.acti_title)) + "\n";
                        tmparray[i] = tmparray[i] + getString(R.string.moduleModule) + tmp.getString(getString(R.string.title_module)) + "\n";
                        tmparray[i] = tmparray[i] + getString(R.string.BeginEvent) + tmp.getString(getString(R.string.begin_event)) + "\n";
                        tmparray[i] = tmparray[i] + getString(R.string.EndEvent) + tmp.getString(getString(R.string.end_event)) + "\n";
                        if (tmp.getString(getString(R.string.registered)).equals("1")) {
                            tmparray[i] = tmparray[i] + getString(R.string.alreadysubscribeclick) + "\n";
                        } else {
                            tmparray[i] = tmparray[i] + getString(R.string.tosubscribe) + "\n";
                        }
                        nbElem++;
                    } else if (tmp.getString(getString(R.string.type_acti)).equals(getString(R.string.Suivis))) {
                        tmparray[i] = tmp.getString(getString(R.string.acti_title)) + "\n";
                        tmparray[i] = tmparray[i] + getString(R.string.moduleModule) + tmp.getString(getString(R.string.title_module)) + "\n";
                        tmparray[i] = tmparray[i] + getString(R.string.InfoCreneau) + tmp.getString(getString(R.string.info_creneau)) + "\n";
                        if (tmp.getString(getString(R.string.registered)).equals("1")) {
                            tmparray[i] = tmparray[i] + getString(R.string.alreadysubscribe) + "\n";
                        }
                        nbElem++;
                    } else {
                        tmparray[i] = getString(R.string.Null);
                    }
                } catch (JSONException e) {
                    Toast toast = Toast.makeText(getActivity(), R.string.errorParse, Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            arrayProject = new String[nbElem];
            nbElem = 0;
            for (String aTmparray : tmparray)
                if (!aTmparray.equals(getString(R.string.Null)) && nbElem < arrayProject.length) {
                    arrayProject[nbElem] = aTmparray;
                    nbElem++;
                }
            ArrayAdapter<String> adpater = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, arrayProject);
            Project.setAdapter(adpater);
            Project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    JSONObject tmp;
                    try {
                        tmp = arrayreq.getJSONObject(position);
                        if (tmp.getString(getString(R.string.type_acti)).equals(getString(R.string.getProject)) || tmp.getString(getString(R.string.type_acti)).equals(getString(R.string.getProjects)))
                        {
                            String str = getString(R.string.features);
                            if (tmp.getString(getString(R.string.registered)).equals("1")) {
                                Toast toast = Toast.makeText(getActivity(), str, Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                Toast toast = Toast.makeText(getActivity(), str, Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    } catch (JSONException e)
                    {
                        Toast toast = Toast.makeText(getActivity(), R.string.errorParse, Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });
        } catch (InterruptedException | ExecutionException e) {
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
