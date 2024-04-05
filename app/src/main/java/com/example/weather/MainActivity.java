package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView largeName;
    private TextView largeDate;
    private TextView largeTemp;
    private TextView lowestTemp;
    private TextView mediumWeatherText;
    private TextView highestTemp;
    private TextView feelTemp;
    private TextView humidityTemp;
    private TextView windPower;
    private TextView pressurePower;
    private TextView visibilityPower;
    private TextView sunriseTime;
    private TextView sunsetTime;

    private List<WeatherForecast> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        largeName = findViewById(R.id.largeName);
        largeDate = findViewById(R.id.largeDate);
        largeTemp = findViewById(R.id.largeTemp);
        lowestTemp = findViewById(R.id.lowestTemp);
        mediumWeatherText = findViewById(R.id.mediumWeatherText);
        highestTemp = findViewById(R.id.highestTemp);
        feelTemp = findViewById(R.id.highestTemp);
        humidityTemp = findViewById(R.id.humidityTemp);
        windPower = findViewById(R.id.windPower);
        pressurePower = findViewById(R.id.pressurePower);
        visibilityPower = findViewById(R.id.visibilityPower);
        sunriseTime = findViewById(R.id.sunriseTime);
        sunsetTime = findViewById(R.id.sunsetTime);

        showInfo();

        mList = new ArrayList<>();
    }

    private void showInfo() {
        new Thread(() -> {
            try {
                String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=hanoi&units=metric&APPID=ea9122ec794be262145f19f74618ce94";
                URL url = new URL(API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject baseJsonResponse = new JSONObject(response.toString());
                JSONArray weatherJsonResponse = baseJsonResponse.getJSONArray("weather");
                JSONObject mainJsonResponse = baseJsonResponse.getJSONObject("main");

                String name = baseJsonResponse.getString("name");
//
                double temp = mainJsonResponse.getDouble("temp");

                String currentDate = dateGenerate();

                double tempMin = mainJsonResponse.getDouble("temp_min");
                double tempMax = mainJsonResponse.getDouble("temp_max");

                JSONObject mainWeatherObject = weatherJsonResponse.getJSONObject(0);
                String mainWeather = mainWeatherObject.getString("main");

                double feelLike = mainJsonResponse.getDouble("feels_like");
//                int pressure = mainJsonResponse.getInt("pressure");
//                double humidity = mainJsonResponse.getDouble("humidity");
//                double visibility = baseJsonResponse.getDouble("Visibility");

                JSONObject wind = baseJsonResponse.getJSONObject("wind");
                double windSpeed = wind.getDouble("speed");

                runOnUiThread(() -> {
                    Log.d("debug", baseJsonResponse.toString());
                    Log.d("debug", name);
                    Log.d("debug", mainJsonResponse.toString());
                    Log.d("debug", String.valueOf(temp));
                    largeName.setText(name);
                    largeTemp.setText((int)Math.round(temp) + "°C");
                    largeDate.setText(currentDate);

                    lowestTemp.setText((int)Math.round(tempMin) + "°C");
                    highestTemp.setText((int)Math.round(tempMax) + "°C");

                    mediumWeatherText.setText(mainWeather);
                    feelTemp.setText(String.valueOf(feelLike));
//                    pressurePower.setText((int)Math.round(pressure));
//                    humidityTemp.setText((int)Math.round(humidity));
//                    visibilityPower.setText((int)Math.round(visibility));
                    windPower.setText(String.valueOf(windSpeed));

                });
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String dateGenerate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    class fetchData extends Thread{
        String data = "";
        @Override
        public void run() {
            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/forecast?q=hanoi&units=metric&APPID=ea9122ec794be262145f19f74618ce94");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line= bufferedReader.readLine()) != null ){
                    data =  data + line;
                }
                if(!data.isEmpty()){
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray list = jsonObject.getJSONArray("list");
                    for(int i=0; i<list.length(); i++){
                        JSONObject listObject = list.getJSONObject(i);
                        String dt_txt = listObject.getString("dt_txt");
                        JSONObject mainObject = listObject.getJSONObject("main");
                        double temp = mainObject.getDouble("temp");

                        WeatherForecast weatherForecast = new WeatherForecast(dt_txt, temp);
                        mList.add(weatherForecast);
                    }
                }

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}