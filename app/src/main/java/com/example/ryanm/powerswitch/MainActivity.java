package com.example.ryanm.powerswitch;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    static final String api_url = "http://10.0.0.192/";
    static final String filename = "togglepower.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button toggle = (Button) findViewById(R.id.button);
        toggle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new TogglePower().execute();
            }
        });
    }

    class TogglePower extends AsyncTask<Void, Void, String>{
        protected void onPreExecute() {
            TextView status = (TextView) findViewById(R.id.status);

            status.setText(R.string.wait_message);
        }

        protected String doInBackground(Void... urls){
            try{
                URL url = new URL(api_url + filename);
                System.out.println(url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                try{
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;
                    while((line = br.readLine()) != null)
                    {
                        System.out.println(line);
                    }
                    br.close();
                }
                finally{
                    urlConnection.disconnect();
                }
                return "Power Toggled";
            }
            catch(Exception e)
            {
                return e.getMessage();
            }
        }

        protected void onPostExecute(String response){
            if(response == null){
                response = "Error";
            }

            TextView status = (TextView) findViewById(R.id.status);

            status.setText(response);
        }
    }
}
