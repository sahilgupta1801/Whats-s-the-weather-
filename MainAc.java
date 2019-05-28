package com.sahilgupta.askweather;

import android.content.Context;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    public void findWeather(View view)
    {
        InputMethodManager mgr=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
        try{
            String encode= URLEncoder.encode(editText.getText().toString(),"UTF-8");
            downloadTask task = new downloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=/"+encode+"&APPID=ea574594b9d36ab688642d5fbeab847e");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public class downloadTask extends AsyncTask<String, Void, String>
    {

        @Override

        protected String doInBackground(String... strings) {
            try{
                String result="";
              URL url=new URL(strings[0]);
                HttpURLConnection connection= (HttpURLConnection)url.openConnection();
                InputStream in =connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1)
                {
                    char current=(char)data;
                    result=result+current;
                    data=reader.read();
                }
                return result;
            }
            catch(Exception e)
            {
               e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            try {

                JSONObject jobj = new JSONObject(result);
                String inf=jobj.getString("weather");
                Log.i("weather : ", inf);
                JSONArray jarr=new JSONArray(inf);
                String message="";
                    JSONObject mini =jarr.getJSONObject(0);
                    for(int i=0;i<jarr.length();i++)
                    {
                    String main="";
                    String description="";
                    main=mini.getString("main");
                    description=mini.getString("description");
                        if(main!="" && description!="")
                        {
                            message=message+main+" : "+description +"\r\n" ;
                        }

                    }
                if(message!="")
                {
                    textView.setText(message);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Could not find !",Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"Could not find",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=(EditText)findViewById(R.id.editText);
        textView=(TextView)findViewById(R.id.textView);
    }
}
