import java.util.Date;

public class Accident extends Drivers {
	private int AccidentID;
	private Date AccidentDate;
	private String Location;
	private String ADescription;

	public int getAccidentID() {
		return AccidentID;
	}

	public void setAccidentID(int accidentID) {
		AccidentID = accidentID;
	}

	public Date getAccidentDate() {
		return AccidentDate;
	}

	public void setAccidentDate(Date accidentDate) {
		AccidentDate = accidentDate;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getADescription() {
		return ADescription;
	}

	public void setADescription(String aDescription) {
		ADescription = aDescription;
	}

	public Accident(int accidentID, Date accidentDate, String location, String aDescription) {
		super();
		AccidentID = accidentID;
		AccidentDate = accidentDate;
		Location = location;
		ADescription = aDescription;
	}

	public Accident() {
		super();
	}

	public Accident(int accidentID) {
		super();
		AccidentID = accidentID;
	}

	public Accident(Date accidentDate, String location) {
		super();
		AccidentDate = accidentDate;
		Location = location;
	}

}
