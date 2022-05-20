package com.zj.model.air;

import com.zj.model.Refer;

import java.util.List;

public class AirNowBean {
	private String fxLink;
	private String code;
	private Refer refer;
	private final NowBean now = new NowBean();
	private List<StationItem> station;
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

	public NowBean getNow(){
		return now;
	}

	public List<StationItem> getStation(){
		return station;
	}

	public String getUpdateTime(){
		return updateTime;
	}

	public static class NowBean {
		private String no2;
		private String o3;
		private String level;
		private String pm2p5;
		private String pubTime;
		private String so2;
		private String aqi;
		private String pm10;
		private String category;
		private String co;
		private String primary;

		public String getNo2(){
			return no2;
		}

		public String getO3(){
			return o3;
		}

		public String getLevel(){
			return level;
		}

		public String getPm2p5(){
			return pm2p5;
		}

		public String getPubTime(){
			return pubTime;
		}

		public String getSo2(){
			return so2;
		}

		public String getAqi(){
			return aqi;
		}

		public String getPm10(){
			return pm10;
		}

		public String getCategory(){
			return category;
		}

		public String getCo(){
			return co;
		}

		public String getPrimary(){
			return primary;
		}

		public void setPrimary(String primary) {
			this.primary = primary;
		}
	}


}