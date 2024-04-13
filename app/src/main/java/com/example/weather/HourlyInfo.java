package com.example.weather;

public class HourlyInfo {
    String time;
    String temp;

    public HourlyInfo(String time, String temp) {
        this.time = time + "h";
        this.temp = temp + "°C";
    }

    public String toString() {
        return "Time: '" + this.time + "', temperature=" + this.temp;
    }

    public String getTime() {
        return this.time;
    }

    public String getTemp() {
        return this.temp;
    }
}
