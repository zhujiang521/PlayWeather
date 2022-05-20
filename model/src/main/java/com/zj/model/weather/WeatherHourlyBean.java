package com.zj.model.weather;

import com.zj.model.Refer;

import java.util.ArrayList;
import java.util.List;

public class WeatherHourlyBean {
    private String fxLink;
    private String code;
    private Refer refer;
    private String updateTime;
    private final List<HourlyBean> hourly = new ArrayList<>();

    public String getFxLink() {
        return fxLink;
    }

    public String getCode() {
        return code;
    }

    public Refer getRefer() {
        return refer;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public List<HourlyBean> getHourly() {
        return hourly;
    }

    public static class HourlyBean {
        private String temp;
        private String icon;
        private String wind360;
        private String windDir;
        private String pressure;
        private String fxTime;
        private String pop;
        private String cloud;
        private String precip;
        private String dew;
        private String humidity;
        private String text;
        private String windSpeed;
        private String windScale;

        public String getTemp() {
            return temp;
        }

        public String getIcon() {
            return icon;
        }

        public String getWind360() {
            return wind360;
        }

        public String getWindDir() {
            return windDir;
        }

        public String getPressure() {
            return pressure;
        }

        public String getFxTime() {
            return fxTime;
        }

        public String getPop() {
            return pop;
        }

        public String getCloud() {
            return cloud;
        }

        public String getPrecip() {
            return precip;
        }

        public String getDew() {
            return dew;
        }

        public String getHumidity() {
            return humidity;
        }

        public String getText() {
            return text;
        }

        public String getWindSpeed() {
            return windSpeed;
        }

        public String getWindScale() {
            return windScale;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setFxTime(String fxTime) {
            this.fxTime = fxTime;
        }
    }


}