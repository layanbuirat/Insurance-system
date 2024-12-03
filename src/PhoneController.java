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

public class PhoneController implements Initializable {
	@FXML
	private Button Add;

	@FXML
	private ComboBox<Customers> CustomerIDChoice;

	@FXML
	private Label CustomerIDLab;

	@FXML
	private TableColumn<Phone, Integer> CustomerIDco;

	@FXML
	private Button Delete;

	@FXML
	private Button GoToCustomerPage1;

	@FXML
	private Button GoToHomePage;

	@FXML
	private Button GoToPhonePage;

	@FXML
	private Label List_gender;

	@FXML
	private Label PhoneForCustomer;

	@FXML
	private Label PhoneIDLab;

	@FXML
	private TextField PhoneIDTex;

	@FXML
	private TextField PhoneIDToSearch;

	@FXML
	private TableColumn<Phone, Integer> PhoneIDco;

	@FXML
	private Label PhoneNumberLab;

	@FXML
	private TextField PhoneNumberTex;

	@FXML
	private TableColumn<Phone, String> PhoneNumberco;

	@FXML
	private TableView<Phone> PhoneTab;

	@FXML
	private Button SearchByID;

	@FXML
	private Button ShowData;

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
	private Map<String, Object> lastDeletedRecord = new HashMap<>();
	private boolean recordDeleted = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showPhone();
		setComboBox();
	}

	public void setComboBox() {
		CustomersController s = new CustomersController();
		ObservableList<Customers> CustomersList = s.returnCustomersid();
		CustomerIDChoice.setItems(CustomersList);
	}

	public void GoToPhoneQuery() {
		Stage stage = (Stage) GoToPhonePage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("PhoneList.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void goToHome() {
		Stage stage = (Stage) GoToHomePage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void GoToCustomerPage() {
		Stage stage = (Stage) GoToCustomerPage1.getScene().getWindow();
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

	public void handleRowSelection(MouseEvent event) {
		int index = PhoneTab.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}

		Phone selectedPhone = PhoneTab.getSelectionModel().getSelectedItem();
		if (selectedPhone != null) {
			PhoneIDTex.setText(String.valueOf(selectedPhone.getPhoneID()));
			PhoneNumberTex.setText(selectedPhone.getPhoneNumber());
			CustomerIDChoice.setValue(new Customers(selectedPhone.getCustomerID(), "", "", null, "", 0));
		}
	}

	public ObservableList<Phone> getPhone(String query) {
		ObservableList<Phone> PhoneList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Phone phone = new Phone(rs.getInt("PhoneID"), rs.getString("PhoneNumber"), rs.getString("CustomerID"));
				PhoneList.add(phone);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return PhoneList;
	}

	public void showPhone() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.phones";
		ObservableList<Phone> phoneList = getPhone(query);
		PhoneIDco.setCellValueFactory(new PropertyValueFactory<>("PhoneID"));
		PhoneNumberco.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
		CustomerIDco.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
		PhoneTab.setItems(phoneList);
	}

	private boolean phoneIDExists(int phoneID) {
		String query = "SELECT COUNT(*) FROM g2_vehicle_insurance_company.phones WHERE PhoneID = ?";
		try (Connection conn = DataBaseConnector.getConnection(); PreparedStatement st = conn.prepareStatement(query)) {

			st.setInt(1, phoneID);
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
			int PhoneID = Integer.parseInt(PhoneIDTex.getText().trim());

			if (phoneIDExists(PhoneID)) {
				displayAlert("PhoneID already exists.");
				return;
			}

			String query = "INSERT INTO g2_vehicle_insurance_company.phones (PhoneID, PhoneNumber, CustomerID) VALUES (?, ?, ?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, PhoneID);
				preparedStatement.setString(2, PhoneNumberTex.getText());
				preparedStatement.setString(3, CustomerIDChoice.getValue().getCustomerID());

				preparedStatement.executeUpdate();
			}

			showPhone();
		} catch (NumberFormatException e) {
			displayAlert("Invalid PhoneID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the insert operation.");
		}
	}

	public void updateRecord() {
		try {
			int PhoneID1 = Integer.parseInt(PhoneIDTex.getText().trim());
			String PhoneNumber1 = PhoneNumberTex.getText();
			String CustomerID1 = CustomerIDChoice.getValue().getCustomerID();

			String query = "UPDATE g2_vehicle_insurance_company.phones SET PhoneNumber = ?, CustomerID = ? WHERE PhoneID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, PhoneNumber1);
				preparedStatement.setString(2, CustomerID1);
				preparedStatement.setInt(3, PhoneID1);

				preparedStatement.executeUpdate();
			}
			showPhone();
		} catch (NumberFormatException e) {
			displayAlert("Invalid PhoneID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the update operation.");
		}
	}

	public void deleteRecord() {
		try {
			int PhoneID = Integer.parseInt(PhoneIDTex.getText().trim());

			// Fetching the record before deletion
			String fetchQuery = "SELECT * FROM g2_vehicle_insurance_company.phones WHERE PhoneID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement fetchStatement = conn.prepareStatement(fetchQuery)) {
				fetchStatement.setInt(1, PhoneID);
				ResultSet rs = fetchStatement.executeQuery();
				if (rs.next()) {
					lastDeletedRecord.put("PhoneID", rs.getInt("PhoneID"));
					lastDeletedRecord.put("PhoneNumber", rs.getString("PhoneNumber"));
					lastDeletedRecord.put("CustomerID", rs.getString("CustomerID")); // Change
																						// here
					recordDeleted = true;
				}
			}

			// Deleting the record
			String query = "DELETE FROM g2_vehicle_insurance_company.phones WHERE PhoneID = ?";
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, PhoneID);
				preparedStatement.executeUpdate();
			}

			showPhone();
		} catch (NumberFormatException e) {
			displayAlert("Invalid PhoneID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the delete operation.");
		}
	}

	public void undoDelete() {
		if (recordDeleted && !lastDeletedRecord.isEmpty()) {
			String query = "INSERT INTO g2_vehicle_insurance_company.phones (PhoneID, PhoneNumber, CustomerID) VALUES (?, ?, ?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, (int) lastDeletedRecord.get("PhoneID"));
				preparedStatement.setString(2, (String) lastDeletedRecord.get("PhoneNumber"));
				preparedStatement.setString(3, (String) lastDeletedRecord.get("CustomerID")); // Change
																								// here

				preparedStatement.executeUpdate();
				recordDeleted = false;
				lastDeletedRecord.clear();
				showPhone();
			} catch (SQLException e) {
				e.printStackTrace();
				displayAlert("Error executing the undo operation.");
			}
		} else {
			displayAlert("No record to undo.");
		}
	}

	public void searchRecord() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.phones WHERE PhoneID = ?";
		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setInt(1, Integer.parseInt(PhoneIDToSearch.getText().trim()));
			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Phone> PhoneList = FXCollections.observableArrayList();
			while (rs.next()) {
				Phone phone = new Phone(rs.getInt("PhoneID"), rs.getString("PhoneNumber"), rs.getString("CustomerID"));
				PhoneList.add(phone);
			}
			PhoneTab.setItems(PhoneList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

}
