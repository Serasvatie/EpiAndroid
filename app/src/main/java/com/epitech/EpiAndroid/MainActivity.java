package com.epitech.EpiAndroid;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.epitech.Model.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends ActionBarActivity {

    private ProgressBar spinner;
    private TextView Login;
    private TextView Password;
    private TextView Reponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        Login = (TextView) findViewById(R.id.Login);
        Password = (TextView) findViewById(R.id.Password);
        Reponse = (TextView) findViewById(R.id.result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


    public void load(View view) throws IOException {
        spinner.setVisibility(View.VISIBLE);
        PostRequest log = new PostRequest();

        if (!isOnline())
        {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.ErrorNetwork, Toast.LENGTH_LONG);
            toast.show();
            spinner.setVisibility(View.INVISIBLE);
            return ;
        }

        JSONObject token;
        try {
            token = log.execute("http://epitech-api.herokuapp.com/login",
                    "login", Login.getText().toString(),
                    "password", Password.getText().toString()).get();
            Reponse.setText(token.getString("token"));
        } catch (JSONException | InterruptedException | ExecutionException e) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.ErrorGetToken, Toast.LENGTH_LONG);
            toast.show();
            spinner.setVisibility(View.INVISIBLE);
        }
//        GetInfoFromAPI get = new GetInfoFromAPI();
//        boolean ret = get.DoIt(Token.class, "http://epitech-api.herokuapp.com/login",
//                "login", Login.getText().toString(),
//                "password", Password.getText().toString());
//        if (!ret) {
//        }
    }
}
