package com.example.medicinebox;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public ArrayList<Integer> numbersList = new ArrayList<>();

    private static final String TAG = "Medicine activity";

    private Timer timer;
    String idNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startFetchingDataPeriodically();
        Button enterButton = findViewById(R.id.enter_button);
        EditText editText = findViewById(R.id.id_number);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idNumber = editText.getText().toString();
                new DataRequest().execute();
            }
        });
    }

    private class FetchJsonTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://192.168.4.1/state");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                StringBuilder jsonResult = new StringBuilder();
                String line;

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        jsonResult.append(line);
                    }
                    reader.close();
                }

                connection.disconnect();
                return jsonResult.toString();
            } catch (IOException e) {
                Log.e(TAG, "Error fetching JSON data: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            numbersList.clear();
            if (jsonData != null) {
                // Handle the JSON data
                Log.d(TAG, jsonData);
                Pattern pattern = Pattern.compile("-?\\d+"); // This pattern matches integers, including negative numbers
                Matcher matcher = pattern.matcher(jsonData);
                while (matcher.find()) {
                    int number = Integer.parseInt(matcher.group());
                    numbersList.add(number);
                    Log.d(TAG, "onPostExecute: " + number);
                }
            } else {
                Log.d(TAG, "onPostExecute: ");
                // Handle the error
            }
        }
    }

    private class DataRequest extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://192.168.4.1/?id=" + idNumber);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                StringBuilder jsonResult = new StringBuilder();
                String line;

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        jsonResult.append(line);
                    }
                    reader.close();
                }

                connection.disconnect();
                return jsonResult.toString();
            } catch (IOException e) {
                Log.e(TAG, "Error fetching JSON data: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            numbersList.clear();
            if (jsonData != null) {
                // Handle the JSON data
                Log.d(TAG, jsonData);
                Toast.makeText(getApplicationContext(), jsonData, Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "onPostExecute: ");
                // Handle the error
            }
        }
    }
        private void startFetchingDataPeriodically() {
            // Create a Timer instance
            timer = new Timer();

            // Schedule a TimerTask to fetch data every 30 seconds
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    new FetchJsonTask().execute();
                    for(int slot = 0; slot < numbersList.size(); slot++ ) {
                        displayNumberOnButton(slot, numbersList.get(slot));
                        Log.d(TAG, "run: " + numbersList.get(slot));
                    }
                }
            }, 0, 10 * 1000); // 10 seconds in milliseconds
        }

        public void displayNumberOnButton(int position, int numbers) {
        Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;
        String number = String.valueOf(numbers);
        switch (position) {
            case 0:
                btn0 = findViewById(R.id.slot_1);
                btn0.setText(number);
                break;
            case 1:
                btn1 = findViewById(R.id.slot_2);
                btn1.setText(number);
                break;
            case 2:
                btn2 = findViewById(R.id.slot_3);
                btn2.setText(number);
                break;
            case 3:
                btn3 = findViewById(R.id.slot_4);
                btn3.setText(number);
                break;
            case 4:
                btn4 = findViewById(R.id.slot_5);
                btn4.setText(number);
                break;
            case 5:
                btn5 = findViewById(R.id.slot_6);
                btn5.setText(number);
                break;
            case 6:
                btn6 = findViewById(R.id.slot_7);
                btn6.setText(number);
                break;
            case 7:
                btn7 = findViewById(R.id.slot_8);
                btn7.setText(number);
                break;
            case 8:
                btn8 = findViewById(R.id.slot_9);
                btn8.setText(number);
                break;
            default:
                break;
        }
        }

        @Override
        protected void onDestroy() {
            // TODO Auto-generated method stub
            super.onDestroy();
            if (timer != null) {
                timer.cancel();
            }
        }

        @Override
        protected void onPause() {
            // TODO Auto-generated method stub
            super.onPause();
        }
}

