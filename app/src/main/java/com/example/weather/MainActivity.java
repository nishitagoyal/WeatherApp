package com.example.weather;

import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.sql.StatementEvent;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    Button button;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);


    }
        public void getweather(View view)
        {
            try {
                DownloadTask task = new DownloadTask();
                String encodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");
                task.execute("http://openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=b6907d289e10d714a6e88b30761fae22");
                InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Couldn't Load Weather", Toast.LENGTH_SHORT).show();

            }

        }
    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try{
                url = new URL (urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                int data = inputStreamReader.read();
                while(data!=-1)
                {
                    char current = (char) data;
                    result +=current;
                    data = inputStreamReader.read();

                }
                return result;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Couldn't Load Weather",Toast.LENGTH_SHORT).show();

                return null;
            }
        }
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray jsonArray = new JSONArray(weatherInfo);
                String message = "";
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String main = jsonObject1.getString("main");
//                    if(main=="Clear")
//                    {
//                       imageView.setImageResource(R.drawable.clear);
//                    }
//                    else if(main== " ")
//                    {
//                        imageView.setImageResource(R.drawable.weather);
//                    }
//                    else
//                    {
//                        imageView.setImageResource(R.drawable.rain);
//                    }
                    String description = jsonObject1.getString("description");
                    if(!main.equals(" ") && !description.equals(" "))
                    {
                        message = main + " : " + description + "\r\n";
                    }
                }
                if(!message.equals(" "))
                {
                    textView.setText(message);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Couldn't Load Weather",Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Couldn't Load Weather",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
