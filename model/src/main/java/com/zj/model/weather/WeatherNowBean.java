package com.zj.model.weather;

import com.zj.model.Refer;

public class WeatherNowBean {
    private String fxLink;
    private String code;
    private Refer refer;
    private final NowBaseBean now = new NowBaseBean();
    private String updateTime;

    public String getFxLink() {
        return fxLink;
    }

    public String getCode() {
        return code;
    }

    public Refer getRefer() {
        return refer;
    }

    public NowBaseBean getNow() {
        return now;
    }

    public String getUpdateTime() {
        return updateTime;
    }


    public static class NowBaseBean {
        private String vis;
        private String temp;
        private String obsTime;
        private String icon;
        private String wind360;
        private String windDir;
        private String pressure;
        private String feelsLike;
        private String cloud;
        private String precip;
        private String dew;
        private String humidity;
        private String text;
        private String windSpeed;
        private String windScale;
        private String city;

        public String getVis() {
            return vis;
        }

        public String getTemp() {
            return temp;
        }

        public String getObsTime() {
            return obsTime;
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

        public String getFeelsLike() {
            return feelsLike;
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

        public void setText(String text) {
            this.text = text;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }


}
