
public class Vehicles extends InsurancePolicies {

	private String VIN;
	private String VehicleName;
	private String VehicleType;
	private int ModelYear;
	private int PolicyID;

	public String getVIN() {
		return VIN;
	}

	public void setVIN(String vIN) {
		VIN = vIN;
	}

	public String getVehicleName() {
		return VehicleName;
	}

	public void setVehicleName(String vehicleName) {
		VehicleName = vehicleName;
	}

	public String getVehicleType() {
		return VehicleType;
	}

	public void setVehicleType(String vehicleType) {
		VehicleType = vehicleType;
	}

	public int getModelYear() {
		return ModelYear;
	}

	public void setModelYear(int modelYear) {
		ModelYear = modelYear;
	}

	public int getPolicyID() {
		return PolicyID;
	}

	public void setPolicyID(int policyID) {
		PolicyID = policyID;
	}

	public Vehicles(String vIN, String vehicleName, String vehicleType, int modelYear, int policyID) {
		super();
		VIN = vIN;
		VehicleName = vehicleName;
		VehicleType = vehicleType;
		ModelYear = modelYear;
		PolicyID = policyID;
	}

	public Vehicles() {
		super();
	}

	public Vehicles(String vIN) {
		super();
		VIN = vIN;
	}

	public Vehicles(double premiumAmount, String vIN, String vehicleName, String vehicleType) {
		super(premiumAmount);
		VIN = vIN;
		VehicleName = vehicleName;
		VehicleType = vehicleType;
	}

	public Vehicles(String vehicleName, String vehicleType) {
		super();
		VehicleName = vehicleName;
		VehicleType = vehicleType;
	}

}
