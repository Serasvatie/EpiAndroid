package com.epitech.EpiAndroid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URLEncoder;

public class MainActivity extends ActionBarActivity {

    private ProgressBar spinner;
    private TextView Login;
    private TextView Password;
    private TextView Reponse;
    private static final String TAG = "MainActivity";

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

    public void load(View view) throws IOException {
        spinner.setVisibility(View.VISIBLE);
        PostRequest log = new PostRequest();

        Reponse.setText(log.execute("http://epitech-api.herokuapp.com/login?login=" + URLEncoder.encode(Login.getText().toString(), "UTF-8") + "&password=" + URLEncoder.encode(Password.getText().toString(), "UTF-8")).toString());
          //  Reponse.setText(rep.getString("http://epitech-api.herokuapp.com/login?login=" + URLEncoder.encode(Login.getText().toString(), "UTF-8") + "&password=" + URLEncoder.encode(Password.getText().toString(), "UTF-8")));

    }
}
