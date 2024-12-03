
public class Address {

	private int AddressID;
	private String Street;
	private String City;
	private String BuildingName;
	private String BuildingNumber;

	public int getAddressID() {
		return AddressID;
	}

	public void setAddressID(int addressID) {
		AddressID = addressID;
	}

	public String getStreet() {
		return Street;
	}

	public void setStreet(String street) {
		Street = street;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getBuildingName() {
		return BuildingName;
	}

	public void setBuildingName(String buildingName) {
		BuildingName = buildingName;
	}

	public String getBuildingNumber() {
		return BuildingNumber;
	}

	public void setBuildingNumber(String buildingNumber) {
		BuildingNumber = buildingNumber;
	}

	public Address(int addressID, String street, String city, String buildingName, String buildingNumber) {
		super();
		AddressID = addressID;
		Street = street;
		City = city;
		BuildingName = buildingName;
		BuildingNumber = buildingNumber;
	}

	public Address() {
		super();
	}

}
