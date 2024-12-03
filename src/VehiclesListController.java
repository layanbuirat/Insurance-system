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

public class VehiclesListController implements Initializable {
	@FXML
	private Button GoToBasePage;

	@FXML
	private Button GoTodriverpage;

	@FXML
	private TableColumn<Vehicles, Double> PremiumAmount;

	@FXML
	private Button SortASCBYVehicleName;

	@FXML
	private Button SortDESBYModelYear;

	@FXML
	private Button SortDESBYVIN;

	@FXML
	private TableColumn<Vehicles, String> V_id;

	@FXML
	private TableColumn<Vehicles, String> VehicleType;

	@FXML
	private Text incurancecompany;

	@FXML
	private TableColumn<Vehicles, String> name;

	@FXML
	private AnchorPane p1;

	@FXML
	private Label vehiclQuery;

	@FXML
	private TableView<Vehicles> vehiclTable;

	@FXML
	private TextField FirstLetterOFVehicleTypetex;
	@FXML
	private TextField VehicleNameTex;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showVehicles();
	}

	public void GoToVehiclespage() {
		Stage stage = (Stage) GoTodriverpage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Vehicles.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Branches page.");

		}
	}

	public void goToHome() {
		Stage stage = (Stage) GoToBasePage.getScene().getWindow();
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

	public ObservableList<Vehicles> getVehicles(String query) {
		ObservableList<Vehicles> VehiclesList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Vehicles vehicles = new Vehicles();
				vehicles.setVIN(rs.getString("vIN"));
				vehicles.setVehicleName(rs.getString("vehicleName"));
				vehicles.setVehicleType(rs.getString("vehicleType"));
				vehicles.setPremiumAmount(rs.getDouble("premiumAmount"));

				VehiclesList.add(vehicles);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return VehiclesList;
	}

	public void showVehicles() {
		String query = "SELECT * FROM vehicles INNER JOIN insurance_policies ON vehicles.PolicyID = insurance_policies.PolicyID ";
		ObservableList<Vehicles> VehiclesList = getVehicles(query);
		V_id.setCellValueFactory(new PropertyValueFactory<>("VIN"));
		name.setCellValueFactory(new PropertyValueFactory<>("VehicleName"));
		VehicleType.setCellValueFactory(new PropertyValueFactory<>("VehicleType"));
		PremiumAmount.setCellValueFactory(new PropertyValueFactory<>("PremiumAmount"));

		vehiclTable.setItems(VehiclesList);
	}

	public void SearchByVehicleName() {
		String query = "SELECT * FROM vehicles INNER JOIN insurance_policies ON vehicles.PolicyID = insurance_policies.PolicyID WHERE VehicleName = ?";
		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, VehicleNameTex.getText().trim());
			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Vehicles> VehiclesList = FXCollections.observableArrayList();
			while (rs.next()) {
				Vehicles vehicles = new Vehicles(rs.getDouble("PremiumAmount"), rs.getString("VIN"),
						rs.getString("VehicleName"), rs.getString("VehicleType"));
				VehiclesList.add(vehicles);
			}
			vehiclTable.setItems(VehiclesList);
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

			preparedStatement.setString(1, FirstLetterOFVehicleTypetex.getText().trim() + "%");

			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Vehicles> VehiclesList = FXCollections.observableArrayList();
			while (rs.next()) {
				Vehicles vehicles = new Vehicles(rs.getDouble("PremiumAmount"), rs.getString("VIN"),
						rs.getString("VehicleName"), rs.getString("VehicleType"));
				VehiclesList.add(vehicles);
			}
			vehiclTable.setItems(VehiclesList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}
}
