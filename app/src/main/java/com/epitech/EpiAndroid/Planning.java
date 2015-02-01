package com.epitech.EpiAndroid;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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

    private int StartDay = 0;
    private int StartMonth = 0;
    private int StartYear = 0;
    private int EndDay = 0;
    private int EndMonth = 0;
    private int EndYear = 0;
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
                        StartMonth = monthOfYear;
                        start.setText(Integer.toString(StartYear) + "-" + Integer.toString(StartMonth) + "-" + Integer.toString(StartDay));
                    }
                }, StartYear, StartMonth, StartDay);
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
                        EndMonth = monthOfYear;
                        end.setText(Integer.toString(EndYear) + "-" + Integer.toString(EndMonth) + "-" + Integer.toString(EndDay));
                    }
                }, EndYear, EndMonth, EndDay);
                dpd.show();
                getInfoFromApi();
            }
        });
        return input;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Calendar current = Calendar.getInstance();
        StartDay = current.get(Calendar.DAY_OF_MONTH);
        StartMonth = current.get(Calendar.MONTH);
        StartYear = current.get(Calendar.YEAR);
        current.add(Calendar.DAY_OF_MONTH, 7);
        EndDay = current.get(Calendar.DAY_OF_MONTH);
        EndMonth = current.get(Calendar.MONTH);
        EndYear = current.get(Calendar.YEAR);
        start = (Button) getView().findViewById(R.id.BeginDate);
        end = (Button) getView().findViewById(R.id.EndDate);
        start.setText(Integer.toString(StartYear) + "-" + Integer.toString(StartMonth) + "-" + Integer.toString(StartDay));
        end.setText(Integer.toString(EndYear) + "-" + Integer.toString(EndMonth) + "-" + Integer.toString(EndDay));

        getInfoFromApi();
    }

    private void getInfoFromApi()
    {
        PostRequestArray reqarray = new PostRequestArray();

        try {
            Planning = (ListView) getView().findViewById(R.id.Planning);
            tabPlanning = reqarray.execute("http://epitech-api.herokuapp.com/planning", "token", MainActivity.token.getToken(),
                    "start",Integer.toString(StartYear) + "-" + Integer.toString(StartMonth) + "-" + Integer.toString(StartDay),
                    "end",(Integer.toString(EndYear) + "-" + Integer.toString(EndMonth) + "-" + Integer.toString(EndDay))).get();
            arrayPlanning = new String[tabPlanning.length()];
            for (int i = 0; i < tabPlanning.length(); i++) {
                JSONObject tmp = tabPlanning.getJSONObject(i);
                if (tmp.getString("module_registered").equals("true"))
                {
                    arrayPlanning[i] = "Module : " + tmp.getString("titlemodule") + "\n";
                    arrayPlanning[i] = arrayPlanning[i] + "Activity : " + tmp.getString("acti_title") + "\n";
                    arrayPlanning[i] = arrayPlanning[i] + "Room : " + tmp.getJSONObject("room").getString("code") + "\n";
                    arrayPlanning[i] = arrayPlanning[i] + "Start : " + tmp.getString("start") + "\n";
                    arrayPlanning[i] = arrayPlanning[i] + "Hours : " + tmp.getString("nb_hours") + "\n";
                    if (tmp.getString("register_student").equals("true"))
                    {
                        arrayPlanning[i] = arrayPlanning[i] + "Subscribe";
                    }
                    else
                        arrayPlanning[i] = arrayPlanning[i] + "No subscribe";
                }
            }
            ArrayAdapter<String> adpater = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, arrayPlanning);
            Planning.setAdapter(adpater);

        } catch (InterruptedException | ExecutionException | JSONException | NullPointerException e) {
            Toast toast = Toast.makeText(getActivity(), R.string.errorApi, Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
