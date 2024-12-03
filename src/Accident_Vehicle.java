
public class Accident_Vehicle {
	private int AccidentID;
	private String VIN;

	public int getAccidentID() {
		return AccidentID;
	}

	public void setAccidentID(int accidentID) {
		AccidentID = accidentID;
	}

	public String getVIN() {
		return VIN;
	}

	public void setVIN(String vIN) {
		VIN = vIN;
	}

	public Accident_Vehicle(int accidentID, String vIN) {
		super();
		AccidentID = accidentID;
		VIN = vIN;
	}

	public Accident_Vehicle() {
		super();
	}

}
