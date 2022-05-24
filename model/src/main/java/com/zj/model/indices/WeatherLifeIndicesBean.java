package com.zj.model.indices;

import com.zj.model.Refer;

import java.util.List;

public class WeatherLifeIndicesBean {
	private String fxLink;
	private String code;
	private Refer refer;
	private List<WeatherLifeIndicesItem> daily;
	private String updateTime;

	public String getFxLink(){
		return fxLink;
	}

	public String getCode(){
		return code;
	}

	public Refer getRefer(){
		return refer;
	}

	public List<WeatherLifeIndicesItem> getDaily(){
		return daily;
	}

	public String getUpdateTime(){
		return updateTime;
	}

	public static class WeatherLifeIndicesItem{
		private String date;
		private String level;
		private String name;
		private String text;
		private String type;
		private String category;

		public String getDate(){
			return date;
		}

		public String getLevel(){
			return level;
		}

		public String getName(){
			return name;
		}

		public String getText(){
			return text;
		}

		public String getType(){
			return type;
		}

		public String getCategory(){
			return category;
		}
	}


}