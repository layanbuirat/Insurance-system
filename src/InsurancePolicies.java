import java.util.Date;

public class InsurancePolicies extends Customers {
	private int PolicyID;
	private String PolicyNumber;
	private String PolicyType;
	private Date StartDate;
	private Date EndDate;
	private double PremiumAmount;
	private String CustomerID;

	public int getPolicyID() {
		return PolicyID;
	}

	public void setPolicyID(int policyID) {
		PolicyID = policyID;
	}

	public String getPolicyNumber() {
		return PolicyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		PolicyNumber = policyNumber;
	}

	public String getPolicyType() {
		return PolicyType;
	}

	public void setPolicyType(String policyType) {
		PolicyType = policyType;
	}

	public Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}

	public Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}

	public double getPremiumAmount() {
		return PremiumAmount;
	}

	public void setPremiumAmount(double premiumAmount) {
		PremiumAmount = premiumAmount;
	}

	public String getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}

	public InsurancePolicies(int policyID, String policyNumber, String policyType, Date startDate, Date endDate,
			double premiumAmount, String customerID) {
		super();
		PolicyID = policyID;
		PolicyNumber = policyNumber;
		PolicyType = policyType;
		StartDate = startDate;
		EndDate = endDate;
		PremiumAmount = premiumAmount;
		CustomerID = customerID;
	}

	public InsurancePolicies() {
		super();
	}

	public InsurancePolicies(int policyID) {
		super();
		PolicyID = policyID;
	}

	public InsurancePolicies(String cName, String email, int policyID, String policyNumber, String policyType,
			double premiumAmount) {
		super(cName, email);
		PolicyID = policyID;
		PolicyNumber = policyNumber;
		PolicyType = policyType;
		PremiumAmount = premiumAmount;
	}

	public InsurancePolicies(double premiumAmount) {
		super();
		PremiumAmount = premiumAmount;
	}

	public InsurancePolicies(String policyNumber, String policyType) {
		super();
		PolicyNumber = policyNumber;
		PolicyType = policyType;
	}

}
