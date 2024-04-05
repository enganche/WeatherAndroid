package com.example.weather;

public class WeatherForecast {
    private String dt_txt;
    private double temp;

    public WeatherForecast(String dttxt, double temp){
        this.dt_txt = dttxt;
        this.temp = temp;
    }

    public String getDttxt() {
        return dt_txt;
    }

    public double getTemp() {
        return temp;
    }

    public String toString() {
        return "WeatherForecast{" + "dt_txt='" + dt_txt + '\'' + ", temp=" + temp + '}';
    }
}
