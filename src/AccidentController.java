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
import javafx.scene.control.DatePicker;
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

public class AccidentController implements Initializable {

	@FXML
	private Label ADescription;

	@FXML
	private TableColumn<Accident, String> ADescriptionco;

	@FXML
	private TextField ADescriptiont;

	@FXML
	private Label AccidentDate;

	@FXML
	private TableColumn<Accident, Date> AccidentDateco;

	@FXML
	private DatePicker AccidentDated;

	@FXML
	private Label AccidentID;

	@FXML
	private TableColumn<Accident, Integer> AccidentIDco;

	@FXML
	private TextField AccidentIDtex;

	@FXML
	private TableView<Accident> AccidentTabl;

	@FXML
	private Label Accidentvehicle;

	@FXML
	private Button Delete;

	@FXML
	private Button GoToAccidentQuery;

	@FXML
	private Button GoToVihcleinsurancedPage;

	@FXML
	private Button GoToclaimsaccidentPage;

	@FXML
	private Button GoTodriverPage;

	@FXML
	private Button GoToendPage;

	@FXML
	private Label List_gender;

	@FXML
	private Label Location;

	@FXML
	private TableColumn<Accident, String> Locationco;

	@FXML
	private TextField Locationt;

	@FXML
	private Button ReturnHomePage1;

	@FXML
	private Button SearchbyID;

	@FXML
	private Button ShowData;

	@FXML
	private Button add;

	@FXML
	private Text insurancecompany;

	@FXML
	private AnchorPane pa1;

	@FXML
	private AnchorPane pa2;

	@FXML
	private TextField searchbyAccidentID;

	@FXML
	private Button undo;

	@FXML
	private Button update;

	private Map<String, Object> lastDeletedRecord = new HashMap<>();
	private boolean recordDeleted = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showAccident();
	}

	public void GoToHomePage() {
		Stage stage = (Stage) ReturnHomePage1.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Home Page.");
		}
	}

	public void GoToclaimsaccidentPage() {
		Stage stage = (Stage) GoToclaimsaccidentPage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Claim.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading claims accident Page.");

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
			displayAlert("Error loading driver Page.");

		}
	}

	public void GoToAccidentQuery() {
		Stage stage = (Stage) GoToAccidentQuery.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("AccidentList.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Accident Query Page.");

		}
	}

	public void GoToVihclesinsurancedPage() {
		Stage stage = (Stage) GoToVihcleinsurancedPage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Vehicl.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Vihcles insuranced Page.");

		}
	}

	public void GoToendPage() {
		Stage stage = (Stage) GoToendPage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("End.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading end Page.");

		}
	}

	public void handleRowSelection(MouseEvent event) {
		System.out.println("Inside handleRowSelection.");
		int index = AccidentTabl.getSelectionModel().getSelectedIndex();
		System.out.println("Index is :" + index);
		if (index <= -1) {
			System.out.println("No row selected.");
			return;
		}

		Accident selectedAccident = AccidentTabl.getSelectionModel().getSelectedItem();
		if (selectedAccident != null) {
			AccidentIDtex.setText(String.valueOf(selectedAccident.getAccidentID()));
			AccidentDated.setValue(((java.sql.Date) selectedAccident.getAccidentDate()).toLocalDate());
			Locationt.setText(selectedAccident.getLocation());
			ADescriptiont.setText(selectedAccident.getADescription());

		}
	}

	public ObservableList<Accident> getAccident(String query) {
		ObservableList<Accident> accidentList = FXCollections.observableArrayList();
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
					Accident accident = new Accident(rs.getInt("AccidentID"), rs.getDate("AccidentDate"),
							rs.getString("Location"), rs.getString("ADescription"));
					accidentList.add(accident);
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

		return accidentList;
	}

	public void showAccident() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.accidents";
		ObservableList<Accident> accidentsList = getAccident(query);
		AccidentIDco.setCellValueFactory(new PropertyValueFactory<>("accidentID"));
		AccidentDateco.setCellValueFactory(new PropertyValueFactory<>("accidentDate"));
		Locationco.setCellValueFactory(new PropertyValueFactory<>("location"));
		ADescriptionco.setCellValueFactory(new PropertyValueFactory<>("aDescription"));
		AccidentTabl.setItems(accidentsList);
	}

	public void insertRecord() {
		try {
			int accidentID = Integer.parseInt(AccidentIDtex.getText().trim());

			if (AccidentIDExists(accidentID)) {
				displayAlert("AccidentID already exists.");
				return;
			}

			String query = "INSERT INTO g2_vehicle_insurance_company.accidents (AccidentID, AccidentDate, Location,ADescription) VALUES (?, ?, ?,?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, accidentID);
				preparedStatement.setDate(2, java.sql.Date.valueOf(AccidentDated.getValue()));
				preparedStatement.setString(3, Locationt.getText());
				preparedStatement.setString(4, ADescriptiont.getText());

				preparedStatement.executeUpdate();
			}

			showAccident();
		} catch (NumberFormatException e) {
			displayAlert("Invalid accidentID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the insert operation.");
		}
	}

	private boolean AccidentIDExists(int AccidentID) {
		String query = "SELECT COUNT(*) FROM g2_vehicle_insurance_company.accidents WHERE AccidentID = " + AccidentID;
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
			int accidentID = Integer.parseInt(AccidentIDtex.getText().trim());
			String accidentDate = AccidentDated.getValue().toString();
			String Locations = Locationt.getText();
			String Description = ADescriptiont.getText();

			String query = "UPDATE g2_vehicle_insurance_company.accidents SET AccidentDate = ?, Location = ? ,ADescription=? WHERE AccidentID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, accidentDate);
				preparedStatement.setString(2, Locations);
				preparedStatement.setString(3, Description);

				preparedStatement.setInt(4, accidentID);

				preparedStatement.executeUpdate();
			}
			showAccident();
		} catch (NumberFormatException e) {
			displayAlert("Invalid AccidentID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the update operation.");
		}
	}

	public void deleteRecord() {
		try {
			int accidentID = Integer.parseInt(AccidentIDtex.getText().trim());

			// Fetching the record before deletion
			String fetchQuery = "SELECT * FROM g2_vehicle_insurance_company.accidents WHERE AccidentID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement fetchStatement = conn.prepareStatement(fetchQuery)) {
				fetchStatement.setInt(1, accidentID);
				ResultSet rs = fetchStatement.executeQuery();
				if (rs.next()) {
					lastDeletedRecord.put("AccidentID", rs.getInt("AccidentID"));
					lastDeletedRecord.put("AccidentDate", rs.getDate("AccidentDate"));
					lastDeletedRecord.put("Location", rs.getString("Location"));
					lastDeletedRecord.put("ADescription", rs.getString("ADescription"));

					recordDeleted = true;
				}
			}

			// Deleting the record
			String query = "DELETE FROM g2_vehicle_insurance_company.accidents WHERE AccidentID = ?";
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, accidentID);
				preparedStatement.executeUpdate();
			}

			showAccident();
		} catch (NumberFormatException e) {
			displayAlert("Invalid AccidentID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the delete operation.");
		}
	}

	public void undoDelete() {
		if (recordDeleted && !lastDeletedRecord.isEmpty()) {
			String query = "INSERT INTO g2_vehicle_insurance_company.accidents (AccidentID, AccidentDate, Location,ADescription) VALUES (?, ?, ?,?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, (int) lastDeletedRecord.get("AccidentID"));
				preparedStatement.setDate(2, (java.sql.Date) lastDeletedRecord.get("AccidentDate"));
				preparedStatement.setString(3, (String) lastDeletedRecord.get("Location"));
				preparedStatement.setString(4, (String) lastDeletedRecord.get("ADescription"));

				preparedStatement.executeUpdate();

				// Clear the lastDeletedRecord map and reset the flag
				lastDeletedRecord.clear();
				recordDeleted = false;
			} catch (SQLException e) {
				e.printStackTrace();
				displayAlert("Error executing the undo operation.");
			}

			showAccident();
		} else {
			displayAlert("No record to undo.");
		}
	}

	@FXML
	private void searchAccidentByID() {
		try {
			int AccidentID = Integer.parseInt(searchbyAccidentID.getText().trim());
			Accident accident = getAccidentByID(AccidentID);
			ObservableList<Accident> accidentList = FXCollections.observableArrayList();
			if (accident != null) {
				accidentList.add(accident);
				AccidentIDco.setCellValueFactory(new PropertyValueFactory<>("accidentID"));
				AccidentDateco.setCellValueFactory(new PropertyValueFactory<>("accidentDate"));
				Locationco.setCellValueFactory(new PropertyValueFactory<>("location"));
				ADescriptionco.setCellValueFactory(new PropertyValueFactory<>("aDescription"));
				AccidentTabl.setItems(accidentList);

				AccidentDated.setValue(((java.sql.Date) accident.getAccidentDate()).toLocalDate());
				Location.setText(accident.getLocation());
				ADescription.setText(accident.getADescription());

			} else {
				displayAlert("Site with ID " + AccidentID + " not found.");
			}
		} catch (NumberFormatException e) {
			displayAlert("Invalid AccidentID. Please enter a valid integer.");
		} catch (SQLException e) {
			displayAlert("Error occurred while searching for the site. Please try again.");
		}
	}

	private Accident getAccidentByID(int accidentID) throws SQLException {
		String sql = "SELECT * FROM g2_vehicle_insurance_company.accidents WHERE AccidentID = ?";
		try (Connection connection = DataBaseConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, accidentID);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				int id = resultSet.getInt("AccidentID");
				Date accidentDate = resultSet.getDate("AccidentDate");
				String location = resultSet.getString("Location");
				String aDescription = resultSet.getString("ADescription");

				return new Accident(id, accidentDate, location, aDescription);
			}
		}
		return null; // Site with given ID not found
	}

	public ObservableList<Accident> returnAccidentid() {
		ObservableList<Accident> AccidentList = FXCollections.observableArrayList();
		String query = "SELECT AccidentID FROM g2_vehicle_insurance_company.accidents";

		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Accident accident = new Accident(rs.getInt("AccidentID"));
				AccidentList.add(accident);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return AccidentList;
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
