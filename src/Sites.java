
public class Sites {

	private int SiteID;
	private String StreetName;
	private String CityName;

	public int getSiteID() {
		return SiteID;
	}

	public void setSiteID(int siteID) {
		SiteID = siteID;
	}

	public String getStreetName() {
		return StreetName;
	}

	public void setStreetName(String streetName) {
		StreetName = streetName;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public Sites(int siteID, String streetName, String cityName) {
		super();
		SiteID = siteID;
		StreetName = streetName;
		CityName = cityName;
	}

	public Sites() {
		super();
	}

	public Sites(int siteID) {
		super();
		SiteID = siteID;
	}

	public Sites(String streetName, String cityName) {
		super();
		StreetName = streetName;
		CityName = cityName;
	}

}
