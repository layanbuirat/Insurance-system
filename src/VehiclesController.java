import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VehiclesController implements Initializable {
	@FXML
	private Button Add;

	@FXML
	private Button Delete;

	@FXML
	private Button GoToAccidentPage;

	@FXML
	private Button GoToHomePage;

	@FXML
	private Button GoTodriverPage;

	@FXML
	private Button GoTopolicyinsurancePage;

	@FXML
	private Button GoTovehicleQure;

	@FXML
	private TableColumn<Vehicles, Integer> ModelYearco;

	@FXML
	private Label Model_Year;

	@FXML
	private Label PolicyID;

	@FXML
	private TableColumn<Vehicles, Integer> PolicyIDco;

	@FXML
	private TextField SearchByVINtext;

	@FXML
	private Button ShowData;

	@FXML
	private Button UnDoDelete;

	@FXML
	private Label VIN;

	@FXML
	private TableColumn<Vehicles, String> VINco;

	@FXML
	private TextField VINtext;

	@FXML
	private Label VehicleName;

	@FXML
	private TableColumn<Vehicles, String> VehicleNameco;

	@FXML
	private TextField VehicleNametext;

	@FXML
	private Label VehicleType;

	@FXML
	private TextField VehicleTypeText;

	@FXML
	private TableColumn<Vehicles, String> VehicleTypeco;

	@FXML
	private TableView<Vehicles> VihcleTable;

	@FXML
	private Label car;

	@FXML
	private ComboBox<InsurancePolicies> choicePolicyID;

	@FXML
	private Text insurancecompany;

	@FXML
	private AnchorPane pa1;

	@FXML
	private Button search;

	@FXML
	private TextField textModel_Year;

	@FXML
	private Button update;

	private Map<String, Object> lastDeletedRecord = new HashMap<>();
	private boolean recordDeleted = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showVehicles();
		setComboBox();
	}

	public void setComboBox() {
		InsurancePoliciesController d = new InsurancePoliciesController();
		ObservableList<InsurancePolicies> policyIDs = d.returnPolicyID();
		choicePolicyID.setItems(policyIDs);
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

	public void GoTovehicleQurey() {
		Stage stage = (Stage) GoTovehicleQure.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("VehiclesList.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void GoTodriverPage() {
		Stage stage = (Stage) GoTodriverPage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Drivers.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void GoToAccidentVihclePage() {
		Stage stage = (Stage) GoToAccidentPage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Accident_Vehicle.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void GoTopolicyinsurancePage() {
		Stage stage = (Stage) GoTopolicyinsurancePage.getScene().getWindow();
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

	public void handleRowSelection(MouseEvent event) {
		int index = VihcleTable.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}

		Vehicles selectedDriverTab = VihcleTable.getSelectionModel().getSelectedItem();
		if (selectedDriverTab != null) {
			VINtext.setText(String.valueOf(selectedDriverTab.getVIN()));
			VehicleNametext.setText(selectedDriverTab.getVehicleName());
			textModel_Year.setText(String.valueOf(selectedDriverTab.getModelYear()));
			VehicleTypeText.setText(selectedDriverTab.getVehicleType());
			choicePolicyID.setValue(new InsurancePolicies(selectedDriverTab.getPolicyID()));
		}
	}

	public ObservableList<Vehicles> getVehicles(String query) {
		ObservableList<Vehicles> InstallmentsList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Vehicles vehicles = new Vehicles(rs.getString("VIN"), rs.getString("VehicleName"),
						rs.getString("VehicleType"), rs.getInt("ModelYear"), rs.getInt("PolicyID"));
				InstallmentsList.add(vehicles);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return InstallmentsList;
	}

	public void showVehicles() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.vehicles";
		ObservableList<Vehicles> VehicleshList = getVehicles(query);
		VINco.setCellValueFactory(new PropertyValueFactory<>("VIN"));
		VehicleNameco.setCellValueFactory(new PropertyValueFactory<>("VehicleName"));
		VehicleTypeco.setCellValueFactory(new PropertyValueFactory<>("VehicleType"));
		ModelYearco.setCellValueFactory(new PropertyValueFactory<>("ModelYear"));
		PolicyIDco.setCellValueFactory(new PropertyValueFactory<>("PolicyID"));
		VihcleTable.setItems(VehicleshList);
	}

	public void insertRecord() {
		try {
			String VIN1 = VINtext.getText().trim();

			if (vehicleExists(VIN1)) {
				displayAlert("VIN already exists.");
				return;
			}

			String query = "INSERT INTO g2_vehicle_insurance_company.vehicles (VIN, VehicleName, VehicleType, ModelYear, PolicyID) VALUES ( ?, ?, ?, ?, ?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, VIN1);
				preparedStatement.setString(2, VehicleNametext.getText());
				preparedStatement.setString(3, VehicleTypeText.getText());
				preparedStatement.setInt(4, Integer.parseInt(textModel_Year.getText()));
				preparedStatement.setInt(5, choicePolicyID.getValue().getPolicyID());

				preparedStatement.executeUpdate();
			}

			showVehicles();
		} catch (NumberFormatException e) {
			displayAlert("Invalid Model Year. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the insert operation.");
		}
	}

	private boolean vehicleExists(String VIN) {
		String query = "SELECT COUNT(*) FROM g2_vehicle_insurance_company.vehicles WHERE VIN = ?";
		try (Connection conn = DataBaseConnector.getConnection(); PreparedStatement st = conn.prepareStatement(query)) {

			st.setString(1, VIN);
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
			String vin1 = VINtext.getText().trim();
			String vehicleName1 = VehicleNametext.getText();
			String vehicleType1 = VehicleTypeText.getText();
			int modelYear1 = Integer.parseInt(textModel_Year.getText());
			int policyID1 = choicePolicyID.getValue().getPolicyID();

			String query = "UPDATE g2_vehicle_insurance_company.vehicles SET VehicleName = ?, VehicleType = ?, ModelYear = ?, PolicyID = ? WHERE VIN = ?";

			Connection conn = DataBaseConnector.getConnection();
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, vehicleName1);
				preparedStatement.setString(2, vehicleType1);
				preparedStatement.setInt(3, modelYear1);
				preparedStatement.setInt(4, policyID1);
				preparedStatement.setString(5, vin1);

				preparedStatement.executeUpdate();
			}

			showVehicles();
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the update operation.");
		} catch (NumberFormatException e) {
			displayAlert("Invalid Model Year. Please enter a valid integer.");
		}
	}

	public void deleteRecord() {
		try {
			String vin1 = VINtext.getText().trim();
			if (!vehicleExists(vin1)) {
				displayAlert("VIN does not exist.");
				return;
			}

			// Save the last deleted record for undo
			lastDeletedRecord.put("VIN", vin1);
			lastDeletedRecord.put("VehicleName", VehicleNametext.getText());
			lastDeletedRecord.put("VehicleType", VehicleTypeText.getText());
			lastDeletedRecord.put("ModelYear", Integer.parseInt(textModel_Year.getText()));
			lastDeletedRecord.put("PolicyID", choicePolicyID.getValue().getPolicyID());

			String query = "DELETE FROM g2_vehicle_insurance_company.vehicles WHERE VIN = ?";

			Connection conn = DataBaseConnector.getConnection();
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, vin1);

				preparedStatement.executeUpdate();
			}

			showVehicles();
			recordDeleted = true;
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the delete operation.");
		} catch (NumberFormatException e) {
			displayAlert("Invalid Model Year. Please enter a valid integer.");
		}
	}

	public void undoDelete() {
		if (!recordDeleted) {
			displayAlert("No record to undo.");
			return;
		}

		try {
			String query = "INSERT INTO g2_vehicle_insurance_company.vehicles (VIN, VehicleName, VehicleType, ModelYear, PolicyID) VALUES (?, ?, ?, ?, ?)";

			Connection conn = DataBaseConnector.getConnection();
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, (String) lastDeletedRecord.get("VIN"));
				preparedStatement.setString(2, (String) lastDeletedRecord.get("VehicleName"));
				preparedStatement.setString(3, (String) lastDeletedRecord.get("VehicleType"));
				preparedStatement.setInt(4, (Integer) lastDeletedRecord.get("ModelYear"));
				preparedStatement.setInt(5, (Integer) lastDeletedRecord.get("PolicyID"));

				preparedStatement.executeUpdate();
			}

			showVehicles();
			recordDeleted = false;
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the undo operation.");
		}
	}

	public void searchRecord() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.vehicles WHERE VIN = ?";
		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, SearchByVINtext.getText().trim());
			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Vehicles> VehiclesList = FXCollections.observableArrayList();
			while (rs.next()) {
				Vehicles vehicles = new Vehicles(rs.getString("VIN"), rs.getString("VehicleName"),
						rs.getString("VehicleType"), rs.getInt("ModelYear"), rs.getInt("PolicyID"));
				VehiclesList.add(vehicles);
			}
			VihcleTable.setItems(VehiclesList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

	public ObservableList<Vehicles> returnVehiclesid() {
		String query = "SELECT v.VIN FROM vehicles v";
		ObservableList<Vehicles> VehiclesidList = FXCollections.observableArrayList();

		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			if (!rs.isBeforeFirst()) {
				System.out.println("No records found for the query: " + query);
				return FXCollections.observableArrayList(); // Return an empty
															// list
			} else {
				while (rs.next()) {
					Vehicles vehicles = new Vehicles(rs.getString("VIN"));
					VehiclesidList.add(vehicles);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return VehiclesidList;
	}

}
