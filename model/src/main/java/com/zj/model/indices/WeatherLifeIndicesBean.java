package com.zj.model.indices;

import com.zj.model.Refer;

import java.util.List;

public class WeatherLifeIndicesBean {
	private String fxLink;
	private String code;
	private Refer refer;
	private List<DailyItem> daily;
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

	public List<DailyItem> getDaily(){
		return daily;
	}

	public String getUpdateTime(){
		return updateTime;
	}
}