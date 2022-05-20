package com.zj.model.weather;

import com.zj.model.Refer;

import java.util.ArrayList;
import java.util.List;

public class WeatherDailyBean {
    private String fxLink;
    private String code;
    private Refer refer;
    private final List<DailyBean> daily = new ArrayList<>();
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

    public List<DailyBean> getDaily() {
        return daily;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public static class DailyBean {
        private String moonset;
        private String windSpeedDay;
        private String sunrise;
        private String moonPhaseIcon;
        private String windScaleDay;
        private String windScaleNight;
        private String wind360Day;
        private String iconDay;
        private String wind360Night;
        private String tempMax;
        private String cloud;
        private String textDay;
        private String precip;
        private String textNight;
        private String humidity;
        private String moonPhase;
        private String windDirDay;
        private String windDirNight;
        private String vis;
        private String fxDate;
        private String moonrise;
        private String pressure;
        private String iconNight;
        private String sunset;
        private String windSpeedNight;
        private String uvIndex;
        private String tempMin;

        public String getMoonset() {
            return moonset;
        }

        public String getWindSpeedDay() {
            return windSpeedDay;
        }

        public String getSunrise() {
            return sunrise;
        }

        public String getMoonPhaseIcon() {
            return moonPhaseIcon;
        }

        public String getWindScaleDay() {
            return windScaleDay;
        }

        public String getWindScaleNight() {
            return windScaleNight;
        }

        public String getWind360Day() {
            return wind360Day;
        }

        public String getIconDay() {
            return iconDay;
        }

        public String getWind360Night() {
            return wind360Night;
        }

        public String getTempMax() {
            return tempMax;
        }

        public String getCloud() {
            return cloud;
        }

        public String getTextDay() {
            return textDay;
        }

        public String getPrecip() {
            return precip;
        }

        public String getTextNight() {
            return textNight;
        }

        public String getHumidity() {
            return humidity;
        }

        public String getMoonPhase() {
            return moonPhase;
        }

        public String getWindDirDay() {
            return windDirDay;
        }

        public String getWindDirNight() {
            return windDirNight;
        }

        public String getVis() {
            return vis;
        }

        public String getFxDate() {
            return fxDate;
        }

        public String getMoonrise() {
            return moonrise;
        }

        public String getPressure() {
            return pressure;
        }

        public String getIconNight() {
            return iconNight;
        }

        public String getSunset() {
            return sunset;
        }

        public String getWindSpeedNight() {
            return windSpeedNight;
        }

        public String getUvIndex() {
            return uvIndex;
        }

        public String getTempMin() {
            return tempMin;
        }

        public void setIconDay(String iconDay) {
            this.iconDay = iconDay;
        }

        public void setTempMax(String tempMax) {
            this.tempMax = tempMax;
        }

        public void setFxDate(String fxDate) {
            this.fxDate = fxDate;
        }

        public void setTempMin(String tempMin) {
            this.tempMin = tempMin;
        }
    }


}