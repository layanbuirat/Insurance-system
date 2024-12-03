
public class Drivers extends Vehicles {
	private int DriverID;
	private String LicenseNumber;
	private String DName;
	private int Age;
	private String VIN;

	public int getDriverID() {
		return DriverID;
	}

	public void setDriverID(int driverID) {
		DriverID = driverID;
	}

	public String getLicenseNumber() {
		return LicenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		LicenseNumber = licenseNumber;
	}

	public String getDName() {
		return DName;
	}

	public void setDName(String dName) {
		DName = dName;
	}

	public int getAge() {
		return Age;
	}

	public void setAge(int age) {
		Age = age;
	}

	public String getVIN() {
		return VIN;
	}

	public void setVIN(String vIN) {
		VIN = vIN;
	}

	public Drivers(int driverID, String licenseNumber, String dName, int age, String vIN) {
		super();
		DriverID = driverID;
		LicenseNumber = licenseNumber;
		DName = dName;
		Age = age;
		VIN = vIN;
	}

	public Drivers() {
		super();
	}

	public Drivers(String vehicleName, String vehicleType, int driverID, String licenseNumber, String dName) {
		super(vehicleName, vehicleType);
		DriverID = driverID;
		LicenseNumber = licenseNumber;
		DName = dName;
	}

}
