
public class Branch extends Sites {
	private int BranchID;
	private String BranchName;
	private int SiteID;

	public int getBranchID() {
		return BranchID;
	}

	public void setBranchID(int branchID) {
		BranchID = branchID;
	}

	public String getBranchName() {
		return BranchName;
	}

	public void setBranchName(String branchName) {
		BranchName = branchName;
	}

	public int getSiteID() {
		return SiteID;
	}

	public void setSiteID(int siteID) {
		SiteID = siteID;
	}

	public Branch(int branchID, String branchName, int siteID) {
		super();
		BranchID = branchID;
		BranchName = branchName;
		SiteID = siteID;
	}

	public Branch() {
		super();
	}

	public Branch(int branchID) {
		super();
		BranchID = branchID;
	}

	public Branch(String streetName, String cityName, int branchID, String branchName) {
		super(streetName, cityName);
		BranchID = branchID;
		BranchName = branchName;
	}

	public Branch(String branchName) {
		super();
		BranchName = branchName;
	}

}
