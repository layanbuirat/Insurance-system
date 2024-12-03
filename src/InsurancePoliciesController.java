import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InsurancePoliciesController implements Initializable {

	@FXML
	private ComboBox<Customers> CustomerIDcombobox1;

	@FXML
	private Button Delete;

	@FXML
	private Button GoToBasePage;

	@FXML
	private Button GoTocustomerPage;

	@FXML
	private Button GoToinstallmentspage;

	@FXML
	private Button GoTovehiclpage;

	@FXML
	private Button GopolicieQueryage;

	@FXML
	private TextField Policytypecombobox;

	@FXML
	private TextField SearchByIDtex;

	@FXML
	private Button ShowData;

	@FXML
	private Button add;

	@FXML
	private TableColumn<InsurancePolicies, String> customeridco;

	@FXML
	private TableColumn<InsurancePolicies, Date> enddateco;

	@FXML
	private TableView<InsurancePolicies> insurancepolicieTable;

	@FXML
	private AnchorPane insurancepoliciy;

	@FXML
	private Label l1;

	@FXML
	private Label l2;

	@FXML
	private Label l3;

	@FXML
	private Label l4;

	@FXML
	private Label l5;

	@FXML
	private Label l6;

	@FXML
	private Label l7;

	@FXML
	private Text l8;

	@FXML
	private Label l9;

	@FXML
	private AnchorPane p1;

	@FXML
	private TableColumn<InsurancePolicies, Integer> policyid;

	@FXML
	private TableColumn<InsurancePolicies, String> policynumberco;

	@FXML
	private TableColumn<InsurancePolicies, String> policytypeco;

	@FXML
	private TableColumn<InsurancePolicies, Double> premiumamountco;

	@FXML
	private Button searchpolicy;

	@FXML
	private TableColumn<InsurancePolicies, Date> startdateco;

	@FXML
	private TextField tPremiumAmount;

	@FXML
	private DatePicker tenddate;

	@FXML
	private TextField tid;

	@FXML
	private TextField tpolicynumber;

	@FXML
	private DatePicker tstartdate;

	@FXML
	private Button undo;

	@FXML
	private Button update;

	private Map<String, Object> lastDeletedRecord = new HashMap<>();
	private boolean recordDeleted = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showInsurancePolicies();
		setcompobox();
	}

	public void setcompobox() {
		CustomersController bc = new CustomersController();
		ObservableList<Customers> CustomersIDs = bc.returnCustomersid();
		CustomerIDcombobox1.setItems(CustomersIDs);
	}

	public void GopolicieQueryage() {
		Stage stage = (Stage) GopolicieQueryage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("InsurancePoliciesList.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void goToHome() {
		Stage stage = (Stage) GoToBasePage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void GoTovehiclpage() {
		Stage stage = (Stage) GoTovehiclpage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Vehicles.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void GoTocustomerPage() {
		Stage stage = (Stage) GoTocustomerPage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Customers.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void GoToinstallmentspage() {
		Stage stage = (Stage) GoToinstallmentspage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Installments.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void handleRowSelection(MouseEvent event) {
		int index = insurancepolicieTable.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}

		InsurancePolicies selectedInsurancePolicies = insurancepolicieTable.getSelectionModel().getSelectedItem();
		if (selectedInsurancePolicies != null) {

			tid.setText(String.valueOf(selectedInsurancePolicies.getPolicyID()));
			tpolicynumber.setText(selectedInsurancePolicies.getPolicyNumber());
			Policytypecombobox.setText(selectedInsurancePolicies.getPolicyType());
			tstartdate.setValue(((java.sql.Date) selectedInsurancePolicies.getStartDate()).toLocalDate());
			tenddate.setValue(((java.sql.Date) selectedInsurancePolicies.getEndDate()).toLocalDate());
			tPremiumAmount.setText(String.valueOf(selectedInsurancePolicies.getPremiumAmount()));
			CustomerIDcombobox1.setValue(new Customers(selectedInsurancePolicies.getCustomerID()));
		}
	}

	public ObservableList<InsurancePolicies> getInsurancePolicies(String query) {
		ObservableList<InsurancePolicies> insurancePoliciesList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				InsurancePolicies insurancePolicies = new InsurancePolicies(rs.getInt("PolicyID"),
						rs.getString("PolicyNumber"), rs.getString("PolicyType"), rs.getDate("StartDate"),
						rs.getDate("EndDate"), rs.getDouble("PremiumAmount"), rs.getString("CustomerID"));
				insurancePoliciesList.add(insurancePolicies);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return insurancePoliciesList;
	}

	public void showInsurancePolicies() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.insurance_policies";
		ObservableList<InsurancePolicies> InsurancePoliciesList = getInsurancePolicies(query);
		policyid.setCellValueFactory(new PropertyValueFactory<>("PolicyID"));
		policynumberco.setCellValueFactory(new PropertyValueFactory<>("PolicyNumber"));
		policytypeco.setCellValueFactory(new PropertyValueFactory<>("PolicyType"));
		startdateco.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
		enddateco.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
		premiumamountco.setCellValueFactory(new PropertyValueFactory<>("PremiumAmount"));
		customeridco.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
		insurancepolicieTable.setItems(InsurancePoliciesList);
	}

	private boolean InsurancePoliciesIDExists(int InsurancePoliciesID) {
		String query = "SELECT COUNT(*) FROM g2_vehicle_insurance_company.insurance_policies WHERE PolicyID = ?";
		try (Connection conn = DataBaseConnector.getConnection(); PreparedStatement st = conn.prepareStatement(query)) {

			st.setInt(1, InsurancePoliciesID);
			ResultSet rs = st.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				return true;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private void displayAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public void insertRecord() {
		try {
			int PolicyID1 = Integer.parseInt(tid.getText().trim());
			if (InsurancePoliciesIDExists(PolicyID1)) {
				displayAlert("PolicyID already exists.");
				return;
			}

			String query = "INSERT INTO g2_vehicle_insurance_company.insurance_policies (PolicyID, PolicyNumber, PolicyType, StartDate, EndDate, PremiumAmount,CustomerID) VALUES (?,?, ?, ?, ?, ?, ?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, PolicyID1);
				preparedStatement.setString(2, tpolicynumber.getText());
				preparedStatement.setString(3, Policytypecombobox.getText());
				preparedStatement.setDate(4, java.sql.Date.valueOf(tstartdate.getValue()));
				preparedStatement.setDate(5, java.sql.Date.valueOf(tenddate.getValue()));
				preparedStatement.setDouble(6, Double.parseDouble(tPremiumAmount.getText()));
				preparedStatement.setString(7, CustomerIDcombobox1.getValue().getCustomerID());

				preparedStatement.executeUpdate();
			}

			showInsurancePolicies();
		} catch (NumberFormatException e) {
			displayAlert("Invalid PolicyID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the insert operation.");
		}
	}

	public void updateRecord() {
		try {
			int tid1 = Integer.parseInt(tid.getText().trim());
			String tpolicynumber1 = tpolicynumber.getText();
			String Policytypecombobox1 = Policytypecombobox.getText();
			String tstartdate1 = tstartdate.getValue().toString();
			String tenddate1 = tenddate.getValue().toString();
			double tPremiumAmount1 = Double.parseDouble(tPremiumAmount.getText());
			String CustomerIDcombobox11 = CustomerIDcombobox1.getValue().getCustomerID();

			String query = "UPDATE g2_vehicle_insurance_company.insurance_policies  SET PolicyNumber = ?, PolicyType = ?, StartDate = ?, EndDate = ?, PremiumAmount = ?,CustomerID=? WHERE PolicyID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, tpolicynumber1);
				preparedStatement.setString(2, Policytypecombobox1);
				preparedStatement.setString(3, tstartdate1);
				preparedStatement.setString(4, tenddate1);
				preparedStatement.setDouble(5, tPremiumAmount1);
				preparedStatement.setString(6, CustomerIDcombobox11);
				preparedStatement.setInt(7, tid1);

				preparedStatement.executeUpdate();
			}
			showInsurancePolicies();
		} catch (NumberFormatException e) {
			displayAlert("Invalid PolicyID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the update operation.");
		}
	}

	public void deleteRecord() {
		try {
			int tid1 = Integer.parseInt(tid.getText().trim());

			// Fetching the record before deletion
			String fetchQuery = "SELECT * FROM g2_vehicle_insurance_company.insurance_policies WHERE PolicyID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement fetchStatement = conn.prepareStatement(fetchQuery)) {
				fetchStatement.setInt(1, tid1);
				ResultSet rs = fetchStatement.executeQuery();
				if (rs.next()) {
					lastDeletedRecord.put("PolicyID", rs.getInt("PolicyID"));
					lastDeletedRecord.put("PolicyNumber", rs.getString("PolicyNumber"));
					lastDeletedRecord.put("PolicyType", rs.getString("PolicyType"));
					lastDeletedRecord.put("StartDate", rs.getDate("StartDate"));
					lastDeletedRecord.put("EndDate", rs.getDate("EndDate"));
					lastDeletedRecord.put("PremiumAmount", rs.getDouble("PremiumAmount"));
					lastDeletedRecord.put("CustomerID", rs.getString("CustomerID"));
					recordDeleted = true;
				}
			}

			// Deleting the record
			String query = "DELETE FROM g2_vehicle_insurance_company.insurance_policies WHERE PolicyID = ?";
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, tid1);
				preparedStatement.executeUpdate();
			}

			showInsurancePolicies();
		} catch (NumberFormatException e) {
			displayAlert("Invalid PolicyID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the delete operation.");
		}
	}

	public void undoDelete() {
		if (recordDeleted && !lastDeletedRecord.isEmpty()) {
			String query = "INSERT INTO g2_vehicle_insurance_company.insurance_policies (PolicyID, PolicyNumber, PolicyType, StartDate, EndDate, PremiumAmount,CustomerID) VALUES (?, ?, ?, ?, ?, ?,?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, (int) lastDeletedRecord.get("PolicyID"));
				preparedStatement.setString(2, (String) lastDeletedRecord.get("PolicyNumber"));
				preparedStatement.setString(3, (String) lastDeletedRecord.get("PolicyType"));
				preparedStatement.setDate(4, (java.sql.Date) lastDeletedRecord.get("StartDate"));
				preparedStatement.setDate(5, (java.sql.Date) lastDeletedRecord.get("EndDate"));
				preparedStatement.setDouble(6, (double) lastDeletedRecord.get("PremiumAmount"));
				preparedStatement.setString(7, (String) lastDeletedRecord.get("CustomerID"));
				preparedStatement.executeUpdate();
				recordDeleted = false;
				lastDeletedRecord.clear();
				showInsurancePolicies();
			} catch (SQLException e) {
				e.printStackTrace();
				displayAlert("Error executing the undo operation.");
			}
		} else {
			displayAlert("No record to undo.");
		}
	}

	public void searchRecord() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.insurance_policies WHERE PolicyID = ?";
		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setInt(1, Integer.parseInt(SearchByIDtex.getText().trim()));
			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<InsurancePolicies> InsurancePoliciesList = FXCollections.observableArrayList();
			while (rs.next()) {
				InsurancePolicies insurancePolicies = new InsurancePolicies(rs.getInt("PolicyID"),
						rs.getString("PolicyNumber"), rs.getString("PolicyType"), rs.getDate("StartDate"),
						rs.getDate("EndDate"), rs.getDouble("PremiumAmount"), rs.getString("CustomerID"));
				InsurancePoliciesList.add(insurancePolicies);
			}
			insurancepolicieTable.setItems(InsurancePoliciesList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

	public ObservableList<InsurancePolicies> returnPolicyID() {
		String query = "SELECT i.PolicyID FROM insurance_policies i";
		ObservableList<InsurancePolicies> InsurancePoliciesidList = FXCollections.observableArrayList();

		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			if (!rs.isBeforeFirst()) {
				System.out.println("No records found for the query: " + query);
				return FXCollections.observableArrayList(); // Return an empty
															// list
			} else {
				while (rs.next()) {
					InsurancePolicies insurancePolicies = new InsurancePolicies(rs.getInt("PolicyID"));
					InsurancePoliciesidList.add(insurancePolicies);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return InsurancePoliciesidList;
	}
}