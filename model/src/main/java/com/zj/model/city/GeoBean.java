package com.zj.model.city;

import com.zj.model.Refer;

import java.util.List;

public class GeoBean {
	private String code;
	private Refer refer;
	private List<LocationBean> location;

	public String getCode(){
		return code;
	}

	public Refer getRefer(){
		return refer;
	}

	public List<LocationBean> getLocation(){
		return location;
	}

	public static class LocationBean {
		private String country;
		private String fxLink;
		private String utcOffset;
		private String adm2;
		private String tz;
		private String isDst;
		private String lon;
		private String adm1;
		private String type;
		private String name;
		private String rank;
		private String id;
		private String lat;

		public String getCountry(){
			return country;
		}

		public String getFxLink(){
			return fxLink;
		}

		public String getUtcOffset(){
			return utcOffset;
		}

		public String getAdm2(){
			return adm2;
		}

		public String getTz(){
			return tz;
		}

		public String getIsDst(){
			return isDst;
		}

		public String getLon(){
			return lon;
		}

		public String getAdm1(){
			return adm1;
		}

		public String getType(){
			return type;
		}

		public String getName(){
			return name;
		}

		public String getRank(){
			return rank;
		}

		public String getId(){
			return id;
		}

		public String getLat(){
			return lat;
		}

		public void setAdm2(String adm2) {
			this.adm2 = adm2;
		}

		public void setAdm1(String adm1) {
			this.adm1 = adm1;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "LocationBean{" +
					"country='" + country + '\'' +
					", fxLink='" + fxLink + '\'' +
					", utcOffset='" + utcOffset + '\'' +
					", adm2='" + adm2 + '\'' +
					", tz='" + tz + '\'' +
					", isDst='" + isDst + '\'' +
					", lon='" + lon + '\'' +
					", adm1='" + adm1 + '\'' +
					", type='" + type + '\'' +
					", name='" + name + '\'' +
					", rank='" + rank + '\'' +
					", id='" + id + '\'' +
					", lat='" + lat + '\'' +
					'}';
		}
	}

	@Override
	public String toString() {
		return "GeoBean{" +
				"code='" + code + '\'' +
				", refer=" + refer +
				", location=" + location +
				'}';
	}
}