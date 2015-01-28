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

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

public class MainActivity extends ActionBarActivity {

    private ProgressBar spinner;
    private TextView Login;
    private TextView Password;
    private TextView Reponse;
    static public JSONObject token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        Login = (TextView) findViewById(R.id.Login);
        Password = (TextView) findViewById(R.id.Password);
        Reponse = (TextView) findViewById(R.id.result);
        token = null;
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
            Toast toast = Toast.makeText(getApplicationContext(), R.string.ErrorNetwork, Toast.LENGTH_SHORT);
            toast.show();
            return ;
        }
        token = log.execute("http://epitech-api.herokuapp.com/login?login=" +
                            URLEncoder.encode(Login.getText().toString(), "UTF-8") +
                            "&password=" + URLEncoder.encode(Password.getText().toString(), "UTF-8"))
                            .get();
    }
}
