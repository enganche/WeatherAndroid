package com.example.weather;

public class HourlyInfo {
    String time;
    String temp;
    String des;

    public HourlyInfo(String time, String temp, String des) {
        this.time = time + "h";
        this.temp = temp + "°C";
        this.des = des;
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
    public String getDescription() {
        return this.des;
    }
}
