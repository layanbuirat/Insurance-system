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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddressController implements Initializable {

	@FXML
	private Button Add;

	@FXML
	private Label AddressForCustomer;

	@FXML
	private Label AddressIDLab;

	@FXML
	private TextField AddressIDTex;

	@FXML
	private TextField AddressIDToSearch;

	@FXML
	private TableColumn<Address, Integer> AddressIDco;

	@FXML
	private TableView<Address> AddressTab;

	@FXML
	private TextField BuildingNameTex;

	@FXML
	private TableColumn<Address, String> BuildingNameco;

	@FXML
	private Label BuildingNumberLab;

	@FXML
	private TextField BuildingNumberTex;

	@FXML
	private TableColumn<Address, String> BuildingNumberco;

	@FXML
	private Label CityLab;

	@FXML
	private TextField CityTex;

	@FXML
	private TableColumn<Address, String> Cityco;

	@FXML
	private Button Delete;

	@FXML
	private Button GoToAddressList;

	@FXML
	private Button GoToBasePage;

	@FXML
	private Button GoToCustomer_AddressPage;

	@FXML
	private Label List_gender;

	@FXML
	private Button SearchByID;

	@FXML
	private Label StreetLab;

	@FXML
	private TextField StreetTex;

	@FXML
	private TableColumn<Address, String> Streetco;

	@FXML
	private Button UnDoDelete;

	@FXML
	private Text insurancecompany;

	@FXML
	private AnchorPane pa1;

	@FXML
	private AnchorPane pa2;

	@FXML
	private Button update;
	@FXML
	private Button ShowData;

	private Map<String, Object> lastDeletedRecord = new HashMap<>();
	private boolean recordDeleted = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		showAddress();

	}

	public void goToAddressQuery() {
		Stage stage = (Stage) GoToAddressList.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("AddressList.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Address Query page.");

		}
	}

	public void GoToCustomer_AddressPage() {
		Stage stage = (Stage) GoToCustomer_AddressPage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Customer_Address.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Customer_Address Page.");

		}
	}

	public void GoToHomePage() {
		Stage stage = (Stage) GoToBasePage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Home page.");

		}
	}

	public void handleRowSelection(MouseEvent event) {
		System.out.println("Inside handleRowSelection.");
		int index = AddressTab.getSelectionModel().getSelectedIndex();
		System.out.println("Index is :" + index);
		if (index <= -1) {
			System.out.println("No row selected.");
			return;
		}

		Address selecteAddress = AddressTab.getSelectionModel().getSelectedItem();
		if (selecteAddress != null) {
			AddressIDTex.setText(String.valueOf(selecteAddress.getAddressID()));
			StreetTex.setText(selecteAddress.getStreet());
			CityTex.setText(selecteAddress.getCity());
			BuildingNameTex.setText(selecteAddress.getBuildingName());
			BuildingNumberTex.setText(selecteAddress.getBuildingNumber());

		}
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

	public void insertRecord() {
		try {
			int addressID = Integer.parseInt(AddressIDTex.getText().trim());

			if (addressIDExists(addressID)) {
				displayAlert("AddressID already exists.");
				return;
			}

			String query = "INSERT INTO g2_vehicle_insurance_company.addresses (AddressID, Street, City,BuildingName,BuildingNumber) VALUES (?, ?, ?,?,?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, addressID);
				preparedStatement.setString(2, StreetTex.getText());
				preparedStatement.setString(3, CityTex.getText());
				preparedStatement.setString(4, BuildingNameTex.getText());
				preparedStatement.setString(5, BuildingNumberTex.getText());

				preparedStatement.executeUpdate();
			}

			showAddress();
		} catch (NumberFormatException e) {
			displayAlert("Invalid AddressID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the insert operation.");
		}
	}

	private boolean addressIDExists(int addressID) {
		String query = "SELECT COUNT(*) FROM g2_vehicle_insurance_company.addresses WHERE AddressID = " + addressID;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		boolean exists = false;

		try {
			conn = DataBaseConnector.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);

			if (rs.next() && rs.getInt(1) > 0) {
				exists = true;
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

		return exists;
	}

	private void displayAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public void updateRecord() {
		try {
			int addressID = Integer.parseInt(AddressIDTex.getText().trim());
			String streetName = StreetTex.getText();
			String cityName = CityTex.getText();
			String buildingName = BuildingNameTex.getText();
			String buildingNumber = BuildingNumberTex.getText();

			String query = "UPDATE g2_vehicle_insurance_company.addresses SET Street = ?, City = ?, BuildingName = ?, BuildingNumber = ? WHERE AddressID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, streetName);
				preparedStatement.setString(2, cityName);
				preparedStatement.setString(3, buildingName);
				preparedStatement.setString(4, buildingNumber);
				preparedStatement.setInt(5, addressID);

				preparedStatement.executeUpdate();
			}
			showAddress();
		} catch (NumberFormatException e) {
			displayAlert("Invalid AddressID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the update operation.");
		}
	}

	public void deleteRecord() {
		try {
			int AddressID = Integer.parseInt(AddressIDTex.getText().trim());

			// Fetching the record before deletion
			String fetchQuery = "SELECT * FROM g2_vehicle_insurance_company.addresses WHERE AddressID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement fetchStatement = conn.prepareStatement(fetchQuery)) {
				fetchStatement.setInt(1, AddressID);
				ResultSet rs = fetchStatement.executeQuery();
				if (rs.next()) {
					lastDeletedRecord.put("AddressID", rs.getInt("AddressID"));
					lastDeletedRecord.put("Street", rs.getString("Street"));
					lastDeletedRecord.put("City", rs.getString("City"));
					lastDeletedRecord.put("BuildingName", rs.getString("BuildingName"));
					lastDeletedRecord.put("BuildingNumber", rs.getString("BuildingNumber"));
					recordDeleted = true;
				}
			}

			// Deleting the record
			String query = "DELETE FROM g2_vehicle_insurance_company.addresses WHERE AddressID = ?";
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, AddressID);
				preparedStatement.executeUpdate();
			}

			showAddress();
		} catch (NumberFormatException e) {
			displayAlert("Invalid AddressID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the delete operation.");
		}
	}

	public void undoDelete() {
		if (recordDeleted && !lastDeletedRecord.isEmpty()) {
			String query = "INSERT INTO g2_vehicle_insurance_company.addresses (AddressID, Street, City,BuildingName,BuildingNumber) VALUES (?, ?, ?,?,?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, (int) lastDeletedRecord.get("AddressID"));
				preparedStatement.setString(2, (String) lastDeletedRecord.get("Street"));
				preparedStatement.setString(3, (String) lastDeletedRecord.get("City"));
				preparedStatement.setString(4, (String) lastDeletedRecord.get("BuildingName"));
				preparedStatement.setString(5, (String) lastDeletedRecord.get("BuildingNumber"));
				preparedStatement.executeUpdate();

				// Clear the lastDeletedRecord map and reset the flag
				lastDeletedRecord.clear();
				recordDeleted = false;
			} catch (SQLException e) {
				e.printStackTrace();
				displayAlert("Error executing the undo operation.");
			}

			showAddress();
		} else {
			displayAlert("No record to undo.");
		}
	}

	@FXML
	private void searchAddressByID() {
		try {
			int addressID = Integer.parseInt(AddressIDToSearch.getText().trim());
			Address address = getAddressByID(addressID);
			ObservableList<Address> addressList = FXCollections.observableArrayList();
			if (address != null) {
				addressList.add(address);
				AddressIDco.setCellValueFactory(new PropertyValueFactory<>("AddressID"));
				Streetco.setCellValueFactory(new PropertyValueFactory<>("Street"));
				Cityco.setCellValueFactory(new PropertyValueFactory<>("City"));
				BuildingNameco.setCellValueFactory(new PropertyValueFactory<>("BuildingName"));
				BuildingNumberco.setCellValueFactory(new PropertyValueFactory<>("BuildingNumber"));
				AddressTab.setItems(addressList);

				StreetTex.setText(address.getStreet());
				CityTex.setText(address.getCity());
				BuildingNameTex.setText(address.getBuildingName());
				BuildingNumberTex.setText(address.getBuildingNumber());
			} else {
				displayAlert("Address with ID " + addressID + " not found.");
			}
		} catch (NumberFormatException e) {
			displayAlert("Invalid AddressID. Please enter a valid integer.");
		} catch (SQLException e) {
			displayAlert("Error occurred while searching for the Address. Please try again.");
		}
	}

	private Address getAddressByID(int AddressID) throws SQLException {
		String sql = "SELECT * FROM g2_vehicle_insurance_company.addresses WHERE AddressID = ?";
		try (Connection connection = DataBaseConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, AddressID);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				int id = resultSet.getInt("AddressID");
				String streetName = resultSet.getString("Street");
				String cityName = resultSet.getString("City");
				String BuildingName = resultSet.getString("BuildingName");
				String BuildingNumber = resultSet.getString("BuildingNumber");
				return new Address(id, streetName, cityName, BuildingName, BuildingNumber);
			}
		}
		return null; // Site with given ID not found
	}

	public void executeQuery(String query) {
		Connection conn = DataBaseConnector.getConnection();
		try (Statement st = conn.createStatement()) {
			st.executeUpdate(query);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
