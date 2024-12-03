
public class Customer_Address {
	private int AddressID;
	private String CustomerID;

	public Customer_Address(int addressID, String customerID) {
		super();
		AddressID = addressID;
		CustomerID = customerID;
	}

	public Customer_Address() {
		super();
	}

	public int getAddressID() {
		return AddressID;
	}

	public void setAddressID(int addressID) {
		AddressID = addressID;
	}

	public String getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}

}
