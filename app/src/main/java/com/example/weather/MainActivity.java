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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

    HourlyAdapter hourlyAdapter;
    ListView listViewHourly;

    DailyAdapter dailyAdapter;
    ListView listViewDaily;

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

                    lowestTemp.setText((int)Math.ceil(tempMin) + "°C");
                    highestTemp.setText((int)Math.floor(tempMax) + "°C");
                    mediumWeatherText.setText(mainWeather);
                    feelTemp.setText(String.valueOf(feelLike));
//                    pressurePower.setText((int)Math.round(pressure));
//                    humidityTemp.setText((int)Math.round(humidity));
//                    visibilityPower.setText(visibility);
                    windPower.setText(String.valueOf(windSpeed));
                    processIcon(mainWeather);

                    LinearLayout background = findViewById(R.id.background);
                    if (temp >= 30) {
                        background.setBackgroundResource(R.drawable.hot_weather);
                    }
                    else if (temp <= 10) {
                        background.setBackgroundResource(R.drawable.cold_weather);
                    }
                    else {
                        background.setBackgroundResource(R.drawable.nice_weather);
                    }
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

                    JSONObject currentWeather = current.getJSONArray("weather").getJSONObject(0);
                    String des = (String) currentWeather.get("main");
                    //Lấy thời gian theo giờ
                    HourlyInfo currentHour = new HourlyInfo(date.substring(11,13), temp, des);
                    hourList.add(currentHour);
                }

                hourlyAdapter = new HourlyAdapter(hourList);
                listViewHourly = findViewById(R.id.listViewHourly);
                //Hết phần dự báo thời tiết theo giờ

                //Dự báo thời tiết theo ngày (5 ngày tiếp theo)
                // TO DO
                List<DailyInfo> dayList = new ArrayList<>();

                for (int i = 0; i < 40; i += 8) {
                    double milestoneTempMin = 0.0;
                    double milestoneTempMax = 0.0;
                    JSONObject current = listJsonResponse.getJSONObject(i);
                    String date = (String) current.get("dt_txt");
                    String prettierDate = date.substring(8,10) + "/" + date.substring(5,7);
                    for (int j = 0; j < 8; j++) {
                        JSONObject main = current.getJSONObject("main");
                        double currentTempMin = main.getDouble("temp_min");
                        double currentTempMax = main.getDouble("temp_max");
                        milestoneTempMin += currentTempMin;
                        milestoneTempMax += currentTempMax;
                    }
                    String dayTempMin = String.valueOf(Math.round(milestoneTempMin / 8));
                    String dayTempMax= String.valueOf(Math.round(milestoneTempMax / 8));
                    DailyInfo currentDay = new DailyInfo(prettierDate, dayTempMin, dayTempMax);
                    dayList.add(currentDay);
                }

                dailyAdapter = new DailyAdapter(dayList);
                listViewDaily = findViewById(R.id.listViewDaily);
                //Hết phần dự báo thời tiết theo ngày

                runOnUiThread(() -> {
//                    Log.d("debug", baseJsonResponse.toString());
//                    Log.d("list", listJsonResponse.toString());
                    for (int i = 0; i < 5; i++) {
                        Log.d("date", dayList.get(i).toString());
                    }
                    listViewHourly.setAdapter(hourlyAdapter);
                    listViewDaily.setAdapter(dailyAdapter);


                    int nextHour = Integer.parseInt(hourList.get(0).time.substring(0,2));
                    if (nightCheck(nextHour)) {
                        LinearLayout background = findViewById(R.id.background);
                        background.setBackgroundResource(R.drawable.night_weather);
                        Log.d("midnight", String.valueOf(nextHour));
                    }
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

    private boolean nightCheck(int time) {
        return time < 6 && time > 20;
    }

    private void processIcon(String des) {
        if (des == "Rain") {
            ImageView mediumWeatherIcon = findViewById(R.id.mediumWeatherIcon);
            mediumWeatherIcon.setImageResource(R.drawable.cloud_rain);
        }
    }
}