package com.example.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MainActivity extends AppCompatActivity {
    private String city;
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


    List<WeatherForecast> mList;

    HourlyAdapter hourlyAdapter;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //Truy cập vào vị trí
//            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, location -> {
//                Log.d("debug", "NETWORK_PROVIDER: " + location.getLatitude() + ", " + location.getLongitude());
//            }, null);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, location -> {
//                Log.d("debug", "Vị trí: " + location.getLatitude() + ", " + location.getLongitude());
//            });
        }

        double lat = 21.0245;
        double lon = 105.8412;

        city = "lat=" + lat + "&lon=" + lon;

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



        mList = new ArrayList<>();
        final EditText editTextSearch = findViewById(R.id.searchBox);
        Button buttonSearch = findViewById(R.id.buttonSearch);

        buttonSearch.setOnClickListener(v -> {
            String nextCity = editTextSearch.getText().toString();
            showAll("q=" + nextCity);
        });

        showAll(city);
    }

    private void showAll(String query) {
        showInfo(query);
        showForecast(query);
    }

    private void showInfo(String query) {
        new Thread(() -> {
            try {
                String API_URL = "https://api.openweathermap.org/data/2.5/weather?"+ query +"&units=metric&APPID=ea9122ec794be262145f19f74618ce94";
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
                double temp = mainJsonResponse.getDouble("temp");

                String currentDate = dateGenerate();

                double tempMin = mainJsonResponse.getDouble("temp_min");
                double tempMax = mainJsonResponse.getDouble("temp_max");

                JSONObject mainWeatherObject = weatherJsonResponse.getJSONObject(0);
                String mainWeather = mainWeatherObject.getString("main");

                double feelLike = mainJsonResponse.getDouble("feels_like");
//                int pressure = mainJsonResponse.getInt("pressure");
//                double humidity = mainJsonResponse.getDouble("humidity");
//                int visibility = baseJsonResponse.getInt("visibility");

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
//                    visibilityPower.setText(visibility);
                    windPower.setText(String.valueOf(windSpeed));

                });
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    //test
    private void showForecast(String query) {
        new Thread(() -> {
            try {
                String API_URL = "https://api.openweathermap.org/data/2.5/forecast?"+ query + "&units=metric&APPID=ea9122ec794be262145f19f74618ce94";
                URL url = new URL(API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                //Object toàn bộ thông tin
                JSONObject baseJsonResponse = new JSONObject(response.toString());
                //Array dự báo
                JSONArray listJsonResponse = baseJsonResponse.getJSONArray("list");
                //Dự báo thời tiết theo giờ (24h)
                List<HourlyInfo> hourList = new ArrayList<>();

                for (int i = 0; i < 9; i++) {
                    JSONObject current = listJsonResponse.getJSONObject(i);
                    String date = (String) current.get("dt_txt");
                    JSONObject main = current.getJSONObject("main");
                    String temp = String.valueOf(Math.round( main.getDouble("temp")));
                    //Lấy thời gian theo giờ
                    HourlyInfo currentHour = new HourlyInfo(date.substring(11,13), temp);
                    hourList.add(currentHour);
                }

                hourlyAdapter = new HourlyAdapter(hourList);
                listView = findViewById(R.id.listView);
                //Hết phần dự báo thời tiết theo giờ

                //Dự báo thời tiết theo ngày (5 ngày tiếp theo)
                // TO DO
//                List<DailyInfo> dayList = new ArrayList<>();
//
//                for (int i = 0; i < 40; i += 8) {
//                    double milestoneTempMin = 0.0;
//                    double milestoneTempMax = 0.0;
//                    JSONObject current = listJsonResponse.getJSONObject(i);
//                    String date = (String) current.get("dt_txt");
//                    for (int j = 0; j < 8; j++) {
//                        JSONObject main = current.getJSONObject("main");
//                        double currentTempMin = main.getDouble("temp_min");
//                        double currentTempMax = main.getDouble("temp_max");
//                        milestoneTempMin += currentTempMin;
//                        milestoneTempMax += currentTempMax;
//                    }
//                    String dayTempMin = String.valueOf((int) (milestoneTempMin / 8));
//                    String dayTempMax= String.valueOf((int) (milestoneTempMax / 8));
//                    DailyInfo currentDay = new DailyInfo(date.substring(8,10), dayTempMin, dayTempMax);
//                    dayList.add(currentDay);
//                }
                //Hết phần dự báo thời tiết theo ngày

                runOnUiThread(() -> {
//                    Log.d("debug", baseJsonResponse.toString());
//                    Log.d("list", listJsonResponse.toString());
                    for (int i = 0; i < 8; i++) {
                        Log.d("date", hourList.get(i).toString());
                    }
                    listView.setAdapter(hourlyAdapter);
                });
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    //Test weather forecast

    private String dateGenerate() {
        DateFormat dateFormat = new SimpleDateFormat("E, MMM dd yyyy");
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