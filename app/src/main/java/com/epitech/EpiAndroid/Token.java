package com.epitech.EpiAndroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.epitech.Model.PostRequest;
import com.epitech.Model.PostRequestArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by favre_q on 31/01/15.
 */
public class Token extends Fragment {

    ListView list;
    JSONArray tabToken;
    String    arrayToken[];
    String     m_token;
    JSONArray jsonArray = new JSONArray();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainNavigation) activity).onSectionAttached(3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_token, container, false);
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

        list = (ListView) getView().findViewById(R.id.Token);

        Date cDate = new Date();
        String actu = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

        Date pDate = new Date(cDate.getTime() - 604800000L);
        String past = new SimpleDateFormat("yyyy-MM-dd").format(pDate);

        PostRequestArray reqarray = new PostRequestArray();

        try {
            tabToken = reqarray.execute("http://epitech-api.herokuapp.com/planning", "token", MainActivity.token.getToken(), "start", "2015-01-20", "end", "2015-01-21").get();
            int j = 0;
            for (int i = 0; i < tabToken.length(); i++) {
                JSONObject tmp = tabToken.getJSONObject(i);
                if (!tmp.getString("event_registered").equals("null") && !tmp.getString("event_registered").equals("present")) {
                    j++;
                }
            }
            arrayToken = new String[j];

            j = 0;
            for (int i = 0; i < tabToken.length(); i++) {
                JSONObject tmp = tabToken.getJSONObject(i);
                if (!tmp.getString("event_registered").equals("null") && !tmp.getString("event_registered").equals("present")) {
                    arrayToken[j] = tmp.getString("acti_title") + " - " + tmp.getString("codeevent") + "\n" + tmp.getString("start") + " - " + tmp.getString("end");
                    j++;
                    jsonArray.put(tmp);
                }
            }
            ArrayAdapter<String> adpater = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, arrayToken);
            list.setAdapter(adpater);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Enter Token");
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_token = input.getText().toString();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

                try {
                    JSONObject tmp = jsonArray.getJSONObject(position);
                    PostRequest req = new PostRequest();
                    req.execute("http://epitech-api.herokuapp.com/token", "token", MainActivity.token.getToken(),
                            "scolaryear", tmp.getString("scolaryear"),
                            "codemodule", tmp.getString("codemodule"),
                            "codeinstance", tmp.getString("codeinstance"),
                            "codeacti", tmp.getString("codeacti"),
                            "codeenvent", tmp.getString("codeevent"),
                            "tokenvalidationcode", m_token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}