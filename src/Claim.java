import java.util.Date;

public class Claim extends Accident {

	private int ClaimID;
	private String CDescription;
	private Date ClaimDate;
	private double ClaimAmount;
	private String CStatus;
	private int AccidentID;

	public int getClaimID() {
		return ClaimID;
	}

	public void setClaimID(int claimID) {
		ClaimID = claimID;
	}

	public String getCDescription() {
		return CDescription;
	}

	public void setCDescription(String cDescription) {
		CDescription = cDescription;
	}

	public Date getClaimDate() {
		return ClaimDate;
	}

	public void setClaimDate(Date claimDate) {
		ClaimDate = claimDate;
	}

	public double getClaimAmount() {
		return ClaimAmount;
	}

	public void setClaimAmount(double claimAmount) {
		ClaimAmount = claimAmount;
	}

	public String getCStatus() {
		return CStatus;
	}

	public void setCStatus(String cStatus) {
		CStatus = cStatus;
	}

	public int getAccidentID() {
		return AccidentID;
	}

	public void setAccidentID(int accidentID) {
		AccidentID = accidentID;
	}

	public Claim(int claimID, String cDescription, Date claimDate, double claimAmount, String cStatus, int accidentID) {
		super();
		ClaimID = claimID;
		CDescription = cDescription;
		ClaimDate = claimDate;
		ClaimAmount = claimAmount;
		CStatus = cStatus;
		AccidentID = accidentID;
	}

	public Claim() {
		super();
	}

	public Claim(Date accidentDate, String location, int claimID, Date claimDate, double claimAmount) {
		super(accidentDate, location);
		ClaimID = claimID;
		ClaimDate = claimDate;
		ClaimAmount = claimAmount;
	}

}
