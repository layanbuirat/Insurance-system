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

public class DriversController implements Initializable {

	@FXML
	private Button Add;

	@FXML
	private Label AgeLab;

	@FXML
	private TextField AgeTex;

	@FXML
	private TableColumn<Drivers, Integer> Ageco;

	@FXML
	private Label DNameLab;

	@FXML
	private TextField DNameTex;

	@FXML
	private TableColumn<Drivers, String> DNameco;

	@FXML
	private Button Delete;

	@FXML
	private Label Driver;

	@FXML
	private Label DriverIDLab;

	@FXML
	private TextField DriverIDTex;

	@FXML
	private TextField DriverIDToSearch;

	@FXML
	private TableColumn<Drivers, Integer> DriverIDco;

	@FXML
	private TableView<Drivers> DriverTab;

	@FXML
	private Button GoToBasePage;

	@FXML
	private Button GoToDriver;

	@FXML
	private Button GoToVehiclesPage1;

	@FXML
	private Label LicenseNumberLab;

	@FXML
	private TextField LicenseNumberTex;

	@FXML
	private TableColumn<Drivers, String> LicenseNumberco;

	@FXML
	private Label List_gender;

	@FXML
	private Button SearchByID;

	@FXML
	private Button ShowData;

	@FXML
	private Button UnDoDelete;

	@FXML
	private ComboBox<Vehicles> VINCoboBox;

	@FXML
	private Label VINLab;

	@FXML
	private TableColumn<Drivers, String> VINco;

	@FXML
	private Text insurancecompany;

	@FXML
	private AnchorPane pa1;

	@FXML
	private AnchorPane pa2;

	@FXML
	private Button update;

	private Map<String, Object> lastDeletedRecord = new HashMap<>();
	private boolean recordDeleted = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showDrivers();
		setcompobox();

	}

	public void setcompobox() {
		VehiclesController d = new VehiclesController();
		ObservableList<Vehicles> vehicleIDs = d.returnVehiclesid();
		VINCoboBox.setItems(vehicleIDs);
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

	public void goTodriverQuery() {
		Stage stage = (Stage) GoToDriver.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("DriversList.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void goToVehicle() {
		Stage stage = (Stage) GoToVehiclesPage1.getScene().getWindow();
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

	public void handleRowSelection(MouseEvent event) {
		int index = DriverTab.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}

		Drivers selectedDriverTab = DriverTab.getSelectionModel().getSelectedItem();
		if (selectedDriverTab != null) {
			DriverIDTex.setText(String.valueOf(selectedDriverTab.getDriverID()));
			LicenseNumberTex.setText(selectedDriverTab.getLicenseNumber());
			DNameTex.setText(selectedDriverTab.getDName());
			AgeTex.setText(String.valueOf(selectedDriverTab.getAge()));
			VINCoboBox.setValue(new Vehicles(selectedDriverTab.getVIN(), "", "", 0, 0));
		}
	}

	public ObservableList<Drivers> getDrivers(String query) {
		ObservableList<Drivers> InstallmentsList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Drivers drivers = new Drivers(rs.getInt("DriverID"), rs.getString("LicenseNumber"),
						rs.getString("DName"), rs.getInt("Age"), rs.getString("VIN"));
				InstallmentsList.add(drivers);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return InstallmentsList;
	}

	public void showDrivers() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.drivers";
		ObservableList<Drivers> DriversshList = getDrivers(query);
		DriverIDco.setCellValueFactory(new PropertyValueFactory<>("DriverID"));
		LicenseNumberco.setCellValueFactory(new PropertyValueFactory<>("LicenseNumber"));
		DNameco.setCellValueFactory(new PropertyValueFactory<>(" DName"));
		Ageco.setCellValueFactory(new PropertyValueFactory<>("Age"));
		VINco.setCellValueFactory(new PropertyValueFactory<>("VIN"));
		DriverTab.setItems(DriversshList);
	}

	public void insertRecord() {
		try {
			int driverID = Integer.parseInt(DriverIDTex.getText().trim());

			if (DriverIDExists(driverID)) {
				displayAlert("DriverID already exists.");
				return;
			}

			String query = "INSERT INTO g2_vehicle_insurance_company.drivers (DriverID, LicenseNumber, DName, Age, VIN) VALUES ( ?, ?, ?, ?, ?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, driverID);
				preparedStatement.setString(2, LicenseNumberTex.getText());
				preparedStatement.setString(3, DNameTex.getText());
				preparedStatement.setInt(4, Integer.parseInt(AgeTex.getText()));
				preparedStatement.setString(5, VINCoboBox.getValue().getVIN());

				preparedStatement.executeUpdate();
			}

			showDrivers();
		} catch (NumberFormatException e) {
			displayAlert("Invalid DriversID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the insert operation.");
		}
	}

	private boolean DriverIDExists(int DriverID) {
		String query = "SELECT COUNT(*) FROM g2_vehicle_insurance_company.drivers WHERE DriverID = ?";
		try (Connection conn = DataBaseConnector.getConnection(); PreparedStatement st = conn.prepareStatement(query)) {

			st.setInt(1, DriverID);
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
			int DriverID1 = Integer.parseInt(DriverIDTex.getText().trim());
			String LicenseNumber1 = LicenseNumberTex.getText();
			String DName1 = DNameTex.getText();
			int Age1 = Integer.parseInt(AgeTex.getText().trim());
			String VINCoboBox1 = VINCoboBox.getValue().getVIN();

			String query = "UPDATE g2_vehicle_insurance_company.drivers SET LicenseNumber = ?, DName = ?, Age = ?, VIN = ? WHERE DriverID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, LicenseNumber1);
				preparedStatement.setString(2, DName1);
				preparedStatement.setInt(3, Age1);
				preparedStatement.setString(4, VINCoboBox1);
				preparedStatement.setInt(5, DriverID1);

				preparedStatement.executeUpdate();
			}
			showDrivers();
		} catch (NumberFormatException e) {
			displayAlert("Invalid DriverID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the update operation.");
		}
	}

	public void deleteRecord() {
		try {
			int DriverID1 = Integer.parseInt(DriverIDTex.getText().trim());

			// Fetching the record before deletion
			String fetchQuery = "SELECT * FROM g2_vehicle_insurance_company.drivers WHERE DriverID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement fetchStatement = conn.prepareStatement(fetchQuery)) {
				fetchStatement.setInt(1, DriverID1);
				ResultSet rs = fetchStatement.executeQuery();
				if (rs.next()) {
					lastDeletedRecord.put("DriverID", rs.getInt("DriverID"));
					lastDeletedRecord.put("LicenseNumber", rs.getString("LicenseNumber"));
					lastDeletedRecord.put("DName", rs.getString("DName"));
					lastDeletedRecord.put("Age", rs.getInt("Age"));
					lastDeletedRecord.put("VIN", rs.getString("VIN"));
					recordDeleted = true;
				}
			}

			// Deleting the record
			String query = "DELETE FROM g2_vehicle_insurance_company.drivers WHERE DriverID = ?";
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, DriverID1);
				preparedStatement.executeUpdate();
			}

			showDrivers();
		} catch (NumberFormatException e) {
			displayAlert("Invalid DriverID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the delete operation.");
		}
	}

	public void undoDelete() {
		if (recordDeleted && !lastDeletedRecord.isEmpty()) {
			String query = "INSERT INTO g2_vehicle_insurance_company.drivers (DriverID, LicenseNumber, DName, Age, VIN) VALUES (?, ?, ?, ?, ?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, (int) lastDeletedRecord.get("DriverID"));
				preparedStatement.setString(2, (String) lastDeletedRecord.get("LicenseNumber"));
				preparedStatement.setString(3, (String) lastDeletedRecord.get("DName"));
				preparedStatement.setInt(4, (int) lastDeletedRecord.get("Age"));
				preparedStatement.setString(5, (String) lastDeletedRecord.get("VIN"));

				preparedStatement.executeUpdate();
				recordDeleted = false;
				lastDeletedRecord.clear();
				showDrivers();
			} catch (SQLException e) {
				e.printStackTrace();
				displayAlert("Error executing the undo operation.");
			}
		} else {
			displayAlert("No record to undo.");
		}
	}

	public void searchRecord() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.drivers WHERE DriverID = ?";
		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setInt(1, Integer.parseInt(DriverIDToSearch.getText().trim()));
			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Drivers> driversList = FXCollections.observableArrayList();
			while (rs.next()) {
				Drivers drivers = new Drivers(rs.getInt("DriverID"), rs.getString("LicenseNumber"),
						rs.getString("DName"), rs.getInt("Age"), rs.getString("VIN"));
				driversList.add(drivers);
			}
			DriverTab.setItems(driversList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

}
