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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CustomersController implements Initializable {

	@FXML
	private Button Add;

	@FXML
	private TableColumn<Customers, Date> BirthDateco;

	@FXML
	private Label BirthDatelab;

	@FXML
	private DatePicker Birthday1;

	@FXML
	private TableColumn<Customers, Integer> BranchIDco;

	@FXML
	private Label BranchIDlab;

	@FXML
	private Label CustomerIDlab;

	@FXML
	private TextField CustomerIDtex;

	@FXML
	private Button Delete;

	@FXML
	private TableColumn<Customers, String> Emailco;

	@FXML
	private Label Emaillab;

	@FXML
	private TableColumn<Customers, String> Genderco;

	@FXML
	private Label Genderlab;

	@FXML
	private Button GoToBranchpage;

	@FXML
	private Button GoToHomePage;

	@FXML
	private Button GoTocustomerQureypage;

	@FXML
	private Button GoTocustomeraddresspage;

	@FXML
	private Button GoToinsurance_policies;

	@FXML
	private Button GoTophonepage;

	@FXML
	private Label List_gender;

	@FXML
	private RadioButton Male_Rad_But;

	@FXML
	private TableColumn<Customers, String> Nameco;

	@FXML
	private Button SearchByID;

	@FXML
	private TextField SearchByIDtex;

	@FXML
	private Button ShowData;

	@FXML
	private Button Update;

	@FXML
	private ComboBox<Branch> choiceBranchID;

	@FXML
	private TableColumn<Customers, String> customerIDco;

	@FXML
	private Label customernamelab;

	@FXML
	private TextField customernametex;

	@FXML
	private Label customerspage;

	@FXML
	private TableView<Customers> customertabl;

	@FXML
	private Text insurancecompany;

	@FXML
	private AnchorPane pa1;

	@FXML
	private AnchorPane pa2;

	@FXML
	private TextField Emailtex;

	@FXML
	private Button undo;

	@FXML
	private TextField Gendertex;

	private Map<String, Object> lastDeletedRecord = new HashMap<>();
	private boolean recordDeleted = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showCustomers();
		setcompobox();
	}

	public void setcompobox() {
		BranchesController bc = new BranchesController();
		ObservableList<Branch> branchIDs = bc.returnBranchid();
		choiceBranchID.setItems(branchIDs);
	}

	public void goToHome() {
		Stage stage = (Stage) GoToHomePage.getScene().getWindow();
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

	public void goToCustomerQuery() {
		Stage stage = (Stage) GoTocustomerQureypage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("CustomersList.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void goToPhone() {
		Stage stage = (Stage) GoTophonepage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Phone.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void goToAddres() {
		Stage stage = (Stage) GoTocustomeraddresspage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Customers_Address.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void goTopolicis() {
		Stage stage = (Stage) GoToinsurance_policies.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("InsurancePolicies.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void goToBranch() {
		Stage stage = (Stage) GoToBranchpage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Branch.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void handleRowSelection(MouseEvent event) {
		int index = customertabl.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}

		Customers selectedCustomers = customertabl.getSelectionModel().getSelectedItem();
		if (selectedCustomers != null) {
			CustomerIDtex.setText(selectedCustomers.getCustomerID());
			customernametex.setText(selectedCustomers.getCName());
			Birthday1.setValue(((java.sql.Date) selectedCustomers.getDateOfBirth()).toLocalDate());
			Emailtex.setText(selectedCustomers.getEmail());
			Gendertex.setText(selectedCustomers.getGender());
			choiceBranchID.setValue(new Branch(selectedCustomers.getBranchID(), "", index));
		}
	}

	public ObservableList<Customers> getCustomers(String query) {
		ObservableList<Customers> branchList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Customers customers = new Customers(rs.getString("CustomerID"), rs.getString("Gender"),
						rs.getString("CName"), rs.getDate("DateOfBirth"), rs.getString("Email"), rs.getInt("BranchID"));
				branchList.add(customers);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return branchList;
	}

	public void showCustomers() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.customers";
		ObservableList<Customers> CustomershList = getCustomers(query);
		customerIDco.setCellValueFactory(new PropertyValueFactory<>("customerID"));
		Nameco.setCellValueFactory(new PropertyValueFactory<>("CName"));
		Genderco.setCellValueFactory(new PropertyValueFactory<>("Gender"));
		BirthDateco.setCellValueFactory(new PropertyValueFactory<>("DateOfBirth"));
		Emailco.setCellValueFactory(new PropertyValueFactory<>("Email"));
		BranchIDco.setCellValueFactory(new PropertyValueFactory<>("BranchID"));
		customertabl.setItems(CustomershList);
	}

	public void insertRecord() {
		try {
			String customerID = CustomerIDtex.getText().trim();

			if (customerIDExists(customerID)) {
				displayAlert("CustomerID already exists.");
				return;
			}

			String query = "INSERT INTO g2_vehicle_insurance_company.customers (CustomerID, Gender, CName, DateOfBirth, Email, BranchID) VALUES (?, ?, ?, ?, ?, ?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, customerID);
				preparedStatement.setString(2, Gendertex.getText());
				preparedStatement.setString(3, customernametex.getText());
				preparedStatement.setDate(4, java.sql.Date.valueOf(Birthday1.getValue()));
				preparedStatement.setString(5, Emailtex.getText());
				preparedStatement.setInt(6, choiceBranchID.getValue().getBranchID());

				preparedStatement.executeUpdate();
			}

			showCustomers();
		} catch (NumberFormatException e) {
			displayAlert("Invalid CustomerID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the insert operation.");
		}
	}

	private boolean customerIDExists(String customerID) {
		String query = "SELECT COUNT(*) FROM g2_vehicle_insurance_company.customers WHERE CustomerID = ?";
		try (Connection conn = DataBaseConnector.getConnection(); PreparedStatement st = conn.prepareStatement(query)) {

			st.setString(1, customerID);
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

	public void updateRecord() {
		try {
			String CustomerID = CustomerIDtex.getText().trim();
			String Gender = Gendertex.getText();
			String CName = customernametex.getText();
			String DateOfBirth = Birthday1.getValue().toString();

			String Email = Emailtex.getText();
			int BranchID = choiceBranchID.getValue().getBranchID();

			String query = "UPDATE g2_vehicle_insurance_company.customers SET Gender = ?, CName = ?, DateOfBirth = ?, Email = ?, BranchID = ? WHERE CustomerID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, Gender);
				preparedStatement.setString(2, CName);
				preparedStatement.setString(3, DateOfBirth);
				preparedStatement.setString(4, Email);
				preparedStatement.setInt(5, BranchID);
				preparedStatement.setString(6, CustomerID);

				preparedStatement.executeUpdate();
			}
			showCustomers();
		} catch (NumberFormatException e) {
			displayAlert("Invalid CustomerID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the update operation.");
		}
	}

	public void deleteRecord() {
		try {
			String CustomerID = CustomerIDtex.getText().trim();

			// Fetching the record before deletion
			String fetchQuery = "SELECT * FROM g2_vehicle_insurance_company.customers WHERE CustomerID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement fetchStatement = conn.prepareStatement(fetchQuery)) {
				fetchStatement.setString(1, CustomerID);
				ResultSet rs = fetchStatement.executeQuery();
				if (rs.next()) {
					lastDeletedRecord.put("CustomerID", rs.getString("CustomerID"));
					lastDeletedRecord.put("Gender", rs.getString("Gender"));
					lastDeletedRecord.put("CName", rs.getString("CName"));
					lastDeletedRecord.put("DateOfBirth", rs.getDate("DateOfBirth"));
					lastDeletedRecord.put("Email", rs.getString("Email"));
					lastDeletedRecord.put("BranchID", rs.getInt("BranchID"));

					recordDeleted = true;
				}
			}

			// Deleting the record
			String query = "DELETE FROM g2_vehicle_insurance_company.Customers WHERE CustomerID = ?";
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, CustomerID);
				preparedStatement.executeUpdate();
			}

			showCustomers();
		} catch (NumberFormatException e) {
			displayAlert("Invalid CustomerID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the delete operation.");
		}
	}

	public void undoDelete() {
		if (recordDeleted && !lastDeletedRecord.isEmpty()) {
			String query = "INSERT INTO g2_vehicle_insurance_company.customers (CustomerID, Gender, CName, DateOfBirth, Email, BranchID) VALUES (?, ?, ?, ?, ?, ?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, (String) lastDeletedRecord.get("CustomerID"));
				preparedStatement.setString(2, (String) lastDeletedRecord.get("Gender"));
				preparedStatement.setString(3, (String) lastDeletedRecord.get("CName"));
				preparedStatement.setDate(4, (java.sql.Date) lastDeletedRecord.get("DateOfBirth"));
				preparedStatement.setString(5, (String) lastDeletedRecord.get("Email"));
				preparedStatement.setInt(6, (int) lastDeletedRecord.get("BranchID"));

				preparedStatement.executeUpdate();
				recordDeleted = false;
				lastDeletedRecord.clear();
				showCustomers();
			} catch (SQLException e) {
				e.printStackTrace();
				displayAlert("Error executing the undo operation.");
			}
		} else {
			displayAlert("No record to undo.");
		}
	}

	public void searchRecord() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.customers WHERE CustomerID = ?";
		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, SearchByIDtex.getText().trim());
			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Customers> customersList = FXCollections.observableArrayList();
			while (rs.next()) {
				Customers customers = new Customers(rs.getString("CustomerID"), rs.getString("Gender"),
						rs.getString("CName"), rs.getDate("DateOfBirth"), rs.getString("Email"), rs.getInt("BranchID"));
				customersList.add(customers);
			}
			customertabl.setItems(customersList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

	public ObservableList<Customers> returnCustomersid() {
		String query = "SELECT c.CustomerID FROM customers c";
		ObservableList<Customers> CustomersidList = FXCollections.observableArrayList();

		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			if (!rs.isBeforeFirst()) {
				System.out.println("No records found for the query: " + query);
				return FXCollections.observableArrayList(); // Return an empty
															// list
			} else {
				while (rs.next()) {
					Customers customers = new Customers(rs.getString("CustomerID"));
					CustomersidList.add(customers);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return CustomersidList;
	}



}
