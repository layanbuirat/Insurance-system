import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddressListController implements Initializable {

	@FXML
	private TableColumn<Address, Integer> AddressIDco;

	@FXML
	private Label AddressQuery;

	@FXML
	private TableView<Address> AddressTab;

	@FXML
	private TableColumn<Address, String> BuildingNameco;

	@FXML
	private TableColumn<Address, String> BuildingNumberco;

	@FXML
	private TableColumn<Address, String> Cityco;

	@FXML
	private Button GoToAddressPage;

	@FXML
	private Button GoToHomepage;

	@FXML
	private Button SortASCBYBuildingNumber;

	@FXML
	private Button SortDESBYAddressID;

	@FXML
	private Button SortDESBYStreet;

	@FXML
	private TableColumn<Address, String> Streetco;

	@FXML
	private Text incurancecompany;

	@FXML
	private AnchorPane pa1;

	@FXML
	private AnchorPane pa2;

	public void goToAddress() {
		Stage stage = (Stage) GoToAddressPage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Address.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Address page.");

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (AddressIDco == null || Streetco == null || Cityco == null || BuildingNameco == null
				|| BuildingNumberco == null || AddressTab == null) {
			System.err.println("One of the FXML elements is not initialized.");
			return;
		}
		showAddress();
	}

	public ObservableList<Address> getAddress(String query) {
		ObservableList<Address> addressList = FXCollections.observableArrayList();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			conn = DataBaseConnector.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);

			if (!rs.isBeforeFirst()) {
				System.out.println("No records found for the query: " + query);
				return FXCollections.observableArrayList(); // Return an empty
															// list
			} else {
				while (rs.next()) {
					Address address = new Address(rs.getInt("AddressID"), rs.getString("Street"), rs.getString("City"),
							rs.getString("BuildingName"), rs.getString("BuildingNumber"));
					addressList.add(address);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return addressList;
	}

	public void showAddress() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.addresses";
		ObservableList<Address> addressList = getAddress(query);
		AddressIDco.setCellValueFactory(new PropertyValueFactory<>("AddressID"));
		Streetco.setCellValueFactory(new PropertyValueFactory<>("Street"));
		Cityco.setCellValueFactory(new PropertyValueFactory<>("City"));
		BuildingNameco.setCellValueFactory(new PropertyValueFactory<>("BuildingName"));
		BuildingNumberco.setCellValueFactory(new PropertyValueFactory<>("BuildingNumber"));
		AddressTab.setItems(addressList);

	}

	public void sortAddressByAddressIDDesc() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.addresses ORDER BY AddressID DESC";
		ObservableList<Address> addressList = getAddress(query);
		AddressTab.setItems(addressList);
	}

	@FXML
	private void handleSortASCBYBuildingNumber() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.addresses ORDER BY BuildingNumber ASC";
		ObservableList<Address> sortedAddresses = getAddress(query);
		AddressTab.setItems(sortedAddresses);
	}

	public void sortAddressByStreetDesc() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.addresses ORDER BY Street DESC";
		ObservableList<Address> addressList = getAddress(query);
		AddressTab.setItems(addressList);
	}

	private void displayAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
