package com.epitech.EpiAndroid;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.epitech.Model.PostRequestArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * Created by favre_q on 31/01/15.
 */
public class Planning extends Fragment {

    private int StartDay;
    private int StartMonth;
    private int StartYear;
    private int EndDay;
    private int EndMonth;
    private int EndYear;
    Button start;
    Button end;
    JSONArray tabPlanning;
    String arrayPlanning[];
    ListView Planning;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainNavigation) activity).onSectionAttached(2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View input = inflater.inflate(R.layout.fragment_planning, container, false);
        final Button start = (Button) input.findViewById(R.id.BeginDate);
        final Button end = (Button) input.findViewById(R.id.EndDate);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        StartYear = year;
                        StartDay = dayOfMonth;
                        StartMonth = monthOfYear + 1;
                        start.setText(StartYear + getString(R.string.TiretSeparator) + StartMonth + getString(R.string.TiretSeparator) + StartDay);
                        getInfoFromApi();
                    }
                }, StartYear, StartMonth - 1, StartDay);
                dpd.show();
                getInfoFromApi();
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        EndYear = year;
                        EndDay = dayOfMonth;
                        EndMonth = monthOfYear + 1;
                        end.setText(EndYear + getString(R.string.TiretSeparator) + EndMonth + getString(R.string.TiretSeparator) + EndDay);
                        getInfoFromApi();
                    }
                }, EndYear, EndMonth - 1, EndDay);
                dpd.show();
            }
        });
        return input;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Calendar current = Calendar.getInstance();
        StartDay = current.get(Calendar.DAY_OF_MONTH);
        StartMonth = current.get(Calendar.MONTH) + 1;
        StartYear = current.get(Calendar.YEAR);
        current.add(Calendar.DAY_OF_MONTH, 7);
        EndDay = current.get(Calendar.DAY_OF_MONTH);
        EndMonth = current.get(Calendar.MONTH) + 1;
        EndYear = current.get(Calendar.YEAR);
        start = (Button) getView().findViewById(R.id.BeginDate);
        end = (Button) getView().findViewById(R.id.EndDate);
        start.setText(StartYear + getString(R.string.TiretSeparator) + StartMonth + getString(R.string.TiretSeparator) + StartDay);
        end.setText(EndYear + getString(R.string.TiretSeparator) + EndMonth + getString(R.string.TiretSeparator) + EndDay);

        getInfoFromApi();
    }

    private void getInfoFromApi() {
        PostRequestArray reqarray = new PostRequestArray();

        if (!isOnline())
        {
            Toast toast = Toast.makeText(getActivity(), R.string.ErrorNetwork, Toast.LENGTH_LONG);
            toast.show();
            return ;
        }

        try {
            Planning = (ListView) getView().findViewById(R.id.Planning);
            tabPlanning = reqarray.execute(getString(R.string.URLgetPlanning), getString(R.string.token), MainActivity.token.getToken(),
                    getString(R.string.start), Integer.toString(StartYear) + getString(R.string.TiretSeparator) + Integer.toString(StartMonth) + getString(R.string.TiretSeparator) + Integer.toString(StartDay),
                    getString(R.string.end), Integer.toString(EndYear) + getString(R.string.TiretSeparator) + Integer.toString(EndMonth) + getString(R.string.TiretSeparator) + Integer.toString(EndDay)).get();
            String tmparray[] = new String[tabPlanning.length()];
            int nbElem = 0;

            for (int i = 0; i < tabPlanning.length(); i++) {
                try {
                    JSONObject tmp = tabPlanning.getJSONObject(i);
                    if (tmp.getString(getString(R.string.moduleregistered)).equals(getString(R.string.True))) {
                        tmparray[i] = getString(R.string.ModuleDes) + tmp.getString(getString(R.string.TitleModule)) + "\n";
                        tmparray[i] = tmparray[i] + getString(R.string.ActivityDes) + tmp.getString(getString(R.string.acti_title)) + "\n";
                        tmparray[i] = tmparray[i] + getString(R.string.RoomDes) + tmp.getJSONObject(getString(R.string.room)).getString(getString(R.string.code)) + "\n";
                        tmparray[i] = tmparray[i] + getString(R.string.StartDes) + tmp.getString(getString(R.string.start)) + "\n";
                        tmparray[i] = tmparray[i] + getString(R.string.HourDes) + tmp.getString(getString(R.string.nb_hours)) + "\n";
                        nbElem++;
                    } else {
                        tmparray[i] = getString(R.string.Null);
                    }
                } catch (JSONException e) {
                    Toast toast = Toast.makeText(getActivity(), R.string.errorParse, Toast.LENGTH_LONG);
                    toast.show();
               }
            }

            arrayPlanning = new String[nbElem];
            nbElem = 0;
            for (String aTmparray : tmparray)
                if (!aTmparray.equals(getString(R.string.Null)) && nbElem < arrayPlanning.length) {
                    arrayPlanning[nbElem] = aTmparray;
                    nbElem++;
                }
            ArrayAdapter<String> adpater = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, arrayPlanning);
            Planning.setAdapter(adpater);

            } catch(InterruptedException | ExecutionException | NullPointerException e){
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