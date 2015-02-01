package com.epitech.EpiAndroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
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

        list = (ListView) getView().findViewById(R.id.Token);

        Date cDate = new Date();
        String actu = new SimpleDateFormat(getString(R.string.simpledateformat)).format(cDate);

        Date pDate = new Date(cDate.getTime() - 604800000L);
        String past = new SimpleDateFormat(getString(R.string.simpledateformat)).format(pDate);

        PostRequestArray reqarray = new PostRequestArray();

        try {
            tabToken = reqarray.execute(getString(R.string.URLgetPlanning), getString(R.string.token), MainActivity.token.getToken(), getString(R.string.start), past, getString(R.string.end), actu).get();
            int j = 0;
            for (int i = 0; i < tabToken.length(); i++) {
                JSONObject tmp = tabToken.getJSONObject(i);
                if (!tmp.getString(getString(R.string.event_registered)).equals(getString(R.string.snull)) && !tmp.getString(getString(R.string.event_registered)).equals(getString(R.string.present))) {
                    j++;
                }
            }
            arrayToken = new String[j];

            j = 0;
            for (int i = 0; i < tabToken.length(); i++) {
                JSONObject tmp = tabToken.getJSONObject(i);
                if (!tmp.getString(getString(R.string.event_registered)).equals(getString(R.string.snull)) && !tmp.getString(getString(R.string.event_registered)).equals(getString(R.string.present))) {
                    arrayToken[j] = tmp.getString(getString(R.string.acti_title)) + " - " + tmp.getString(getString(R.string.codeevent)) + "\n" + tmp.getString(getString(R.string.start)) + " - " + tmp.getString(getString(R.string.end));
                    j++;
                    jsonArray.put(tmp);
                }
            }
            ArrayAdapter<String> adpater = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, arrayToken);
            list.setAdapter(adpater);
        } catch (InterruptedException | ExecutionException | JSONException e) {
            Toast toast = Toast.makeText(getActivity(), R.string.errorApi, Toast.LENGTH_LONG);
            toast.show();
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.EnterToken));
                final EditText input = new EditText(getActivity());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_token = input.getText().toString();
                    }
                });
                builder.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

                try {
                    JSONObject tmp = jsonArray.getJSONObject(position);
                    PostRequest req = new PostRequest();
                    req.execute(getString(R.string.URLsetToken), getString(R.string.token), MainActivity.token.getToken(),
                            getString(R.string.scolaryear), tmp.getString(getString(R.string.scolaryear)),
                            getString(R.string.codemodule), tmp.getString(getString(R.string.codemodule)),
                            getString(R.string.codeinstance), tmp.getString(getString(R.string.codeinstance)),
                            getString(R.string.codeacti), tmp.getString(getString(R.string.codeacti)),
                            getString(R.string.codeevent), tmp.getString(getString(R.string.codeevent)),
                            getString(R.string.tokenvalidationcode), m_token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}