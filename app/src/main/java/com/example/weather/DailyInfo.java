package com.example.weather;

public class DailyInfo {
    String time;
    String tempMax;
    String tempMin;

    public DailyInfo(String time, String tempMax, String tempMin) {
        this.time = time + "h";
        this.tempMax = tempMax + "°C";
        this.tempMin = tempMin + "°C";
    }
    public String toString() {
        return "Time: '" + this.time + "', min=" + this.tempMin + ", max=" + this.tempMax;
    }

    public String getTime() {
        return this.time;
    }
    public String getTempMin() {
        return this.tempMin;
    }
    public String getTempMax() {
        return this.tempMax;
    }
}