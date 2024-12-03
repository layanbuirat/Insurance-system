import java.util.Date;

public class Installments extends InsurancePolicies {

	private int installmentID;
	private double amount;
	private Date dueDate;
	private Date paidDate;
	private int policyID;

	// Constructors, getters, and setters
	public Installments(int installmentID, double amount, Date dueDate, Date paidDate, int policyID) {
		this.installmentID = installmentID;
		this.amount = amount;
		this.dueDate = dueDate;
		this.paidDate = paidDate;
		this.policyID = policyID;
	}

	public Installments() {
		super();
	}

	public int getInstallmentID() {
		return installmentID;
	}

	public void setInstallmentID(int installmentID) {
		this.installmentID = installmentID;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	public int getPolicyID() {
		return policyID;
	}

	public void setPolicyID(int policyID) {
		this.policyID = policyID;
	}

	public Installments(String policyNumber, String policyType, int installmentID, double amount, Date dueDate,
			Date paidDate) {
		super(policyNumber, policyType);
		this.installmentID = installmentID;
		this.amount = amount;
		this.dueDate = dueDate;
		this.paidDate = paidDate;
	}

}
