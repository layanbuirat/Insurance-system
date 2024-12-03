import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DriversListController implements Initializable {

	@FXML
	private TableColumn<Drivers, String> DNameco;

	@FXML
	private TableColumn<Drivers, Integer> DriverIDco;

	@FXML
	private Label DriverQuery;

	@FXML
	private TableView<Drivers> DriverTab;

	@FXML
	private TextField FirstLetterOFDNametex;

	@FXML
	private Button GoToDriverPage;

	@FXML
	private Button GoToHomepage;

	@FXML
	private TableColumn<Drivers, String> LicenseNumberco;

	@FXML
	private Button Search;

	@FXML
	private Button SearchByVehicleName;

	@FXML
	private Button ShowData;

	@FXML
	private TextField VehicleNameTex;

	@FXML
	private TableColumn<Drivers, String> VehicleNameco;

	@FXML
	private TableColumn<Drivers, String> VehicleTypeco1;

	@FXML
	private Text incurancecompany;

	@FXML
	private Label l;

	@FXML
	private Label l2;

	@FXML
	private AnchorPane pa1;

	@FXML
	private AnchorPane pa2;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showDrivers();
	}

	public void GoToDriverPage() {
		Stage stage = (Stage) GoToDriverPage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Drivers.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Branches page.");

		}
	}

	public void goToHome() {
		Stage stage = (Stage) GoToHomepage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Home page.");

		}
	}

	private void displayAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public ObservableList<Drivers> getDrivers(String query) {
		ObservableList<Drivers> DriversList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Drivers drivers = new Drivers();
				drivers.setDriverID(rs.getInt("driverID"));
				drivers.setLicenseNumber(rs.getString("licenseNumber"));
				drivers.setDName(rs.getString("dName"));
				drivers.setVehicleName(rs.getString("vehicleName"));
				drivers.setVehicleType(rs.getString("vehicleType"));

				DriversList.add(drivers);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return DriversList;
	}

	public void showDrivers() {
		String query = "SELECT * FROM drivers INNER JOIN vehicles ON drivers.VIN = vehicles.VIN ";
		ObservableList<Drivers> DriversList = getDrivers(query);
		DriverIDco.setCellValueFactory(new PropertyValueFactory<>("DriverID"));
		LicenseNumberco.setCellValueFactory(new PropertyValueFactory<>("LicenseNumber"));
		DNameco.setCellValueFactory(new PropertyValueFactory<>("DName"));
		VehicleNameco.setCellValueFactory(new PropertyValueFactory<>("VehicleName"));
		VehicleTypeco1.setCellValueFactory(new PropertyValueFactory<>("VehicleType"));

		DriverTab.setItems(DriversList);
	}

	public void SearchByDriversName() {
		String query = "SELECT * FROM drivers INNER JOIN vehicles ON drivers.VIN = vehicles.VIN  WHERE VehicleName = ?";
		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, VehicleNameTex.getText().trim());
			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Drivers> DriversList = FXCollections.observableArrayList();
			while (rs.next()) {
				Drivers drivers = new Drivers(rs.getString("VehicleType"), rs.getString("VehicleName"),
						rs.getInt("DriverID"), rs.getString("LicenseNumber"), rs.getString("DName"));
				DriversList.add(drivers);
			}
			DriverTab.setItems(DriversList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

	public void searchFirst() {
		String query = "SELECT * FROM vehicles INNER JOIN insurance_policies ON vehicles.PolicyID = insurance_policies.PolicyID "
				+ "WHERE VehicleType LIKE ?";

		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, FirstLetterOFDNametex.getText().trim() + "%");

			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Drivers> DriversList = FXCollections.observableArrayList();
			while (rs.next()) {
				Drivers drivers = new Drivers(rs.getString("VehicleType"), rs.getString("VehicleName"),
						rs.getInt("DriverID"), rs.getString("LicenseNumber"), rs.getString("DName"));
				DriversList.add(drivers);
			}
			DriverTab.setItems(DriversList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

}
