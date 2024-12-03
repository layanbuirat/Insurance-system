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

public class ClaimController implements Initializable {
	@FXML
	private ComboBox<Accident> AccidentIDChoice;

	@FXML
	private Label AccidentIDLab1;

	@FXML
	private TableColumn<Claim, Integer> AccidentIDco1;

	@FXML
	private Button Add;

	@FXML
	private Label CDescriptionLab;

	@FXML
	private TextField CDescriptionTex;

	@FXML
	private TableColumn<Claim, String> CDescriptionco;

	@FXML
	private Label CStatusLab;

	@FXML
	private TextField CStatusTex;

	@FXML
	private TableColumn<Claim, String> CStatusco;

	@FXML
	private Label ClaimAmountLab;

	@FXML
	private TextField ClaimAmountTex;

	@FXML
	private TableColumn<Claim, Double> ClaimAmountco;

	@FXML
	private Label ClaimDateLab;

	@FXML
	private DatePicker ClaimDatePicker;

	@FXML
	private TableColumn<Claim, Date> ClaimDateco;

	@FXML
	private Label ClaimIDLab;

	@FXML
	private TextField ClaimIDTex;

	@FXML
	private TextField ClaimIDToSearch;

	@FXML
	private TableColumn<Claim, Integer> ClaimIDco;

	@FXML
	private TableView<Claim> ClaimTab;

	@FXML
	private Button Delete;

	@FXML
	private Button GoToAccidentPage;

	@FXML
	private Button GoToClaimQuery;

	@FXML
	private Button GoToHomePage;

	@FXML
	private Label InsuranceClaim;

	@FXML
	private Label List_gender;

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
		showClaim();
		setcompobox();
	}

	public void setcompobox() {
		AccidentController bc = new AccidentController();
		ObservableList<Accident> AccidentIDs = bc.returnAccidentid();
		AccidentIDChoice.setItems(AccidentIDs);
	}

	public void GoToClaimQuery() {
		Stage stage = (Stage) GoToClaimQuery.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("ClaimList.fxml"));
			Scene scene = new Scene(root, 900, 800);
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
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void GoToAccidentPage() {
		Stage stage = (Stage) GoToAccidentPage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Accident.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void handleRowSelection(MouseEvent event) {
		int index = ClaimTab.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}

		Claim selectedClaim = ClaimTab.getSelectionModel().getSelectedItem();
		if (selectedClaim != null) {

			ClaimIDTex.setText(String.valueOf(selectedClaim.getClaimID()));
			CDescriptionTex.setText(selectedClaim.getCDescription());
			ClaimDatePicker.setValue(((java.sql.Date) selectedClaim.getClaimDate()).toLocalDate());
			ClaimAmountTex.setText(String.valueOf(selectedClaim.getClaimAmount()));
			CStatusTex.setText(selectedClaim.getCStatus());
			AccidentIDChoice.setValue(new Accident(selectedClaim.getAccidentID(), null, "", ""));
		}
	}

	public ObservableList<Claim> getClaim(String query) {
		ObservableList<Claim> ClaimList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Claim claim = new Claim(rs.getInt("claimID"), rs.getString("cDescription"), rs.getDate("claimDate"),
						rs.getDouble("claimAmount"), rs.getString("cStatus"), rs.getInt("accidentID"));
				ClaimList.add(claim);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ClaimList;
	}

	public void showClaim() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.claims";
		ObservableList<Claim> claimList = getClaim(query);
		ClaimIDco.setCellValueFactory(new PropertyValueFactory<>("ClaimID"));
		CDescriptionco.setCellValueFactory(new PropertyValueFactory<>("CDescription"));
		ClaimDateco.setCellValueFactory(new PropertyValueFactory<>("ClaimDate"));
		ClaimAmountco.setCellValueFactory(new PropertyValueFactory<>("ClaimAmount"));
		CStatusco.setCellValueFactory(new PropertyValueFactory<>("CStatus"));
		AccidentIDco1.setCellValueFactory(new PropertyValueFactory<>("AccidentID"));
		ClaimTab.setItems(claimList);
	}

	private boolean claimIDExists(int claimID) {
		String query = "SELECT COUNT(*) FROM g2_vehicle_insurance_company.claims WHERE ClaimID = ?";
		try (Connection conn = DataBaseConnector.getConnection(); PreparedStatement st = conn.prepareStatement(query)) {

			st.setInt(1, claimID);
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
			int claimID = Integer.parseInt(ClaimIDTex.getText().trim());
			if (claimIDExists(claimID)) {
				displayAlert("claimID already exists.");
				return;
			}

			String query = "INSERT INTO g2_vehicle_insurance_company.claims (ClaimID, CDescription, ClaimDate, ClaimAmount, CStatus, AccidentID) VALUES (?, ?, ?, ?, ?, ?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, claimID);
				preparedStatement.setString(2, CDescriptionTex.getText());
				preparedStatement.setDate(3, java.sql.Date.valueOf(ClaimDatePicker.getValue()));
				preparedStatement.setDouble(4, Double.parseDouble(ClaimAmountTex.getText()));
				preparedStatement.setString(5, CStatusTex.getText());
				preparedStatement.setInt(6, AccidentIDChoice.getValue().getAccidentID());

				preparedStatement.executeUpdate();
			}

			showClaim();
		} catch (NumberFormatException e) {
			displayAlert("Invalid claimID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the insert operation.");
		}
	}

	public void updateRecord() {
		try {
			int claimID = Integer.parseInt(ClaimIDTex.getText().trim());
			String CDescription = CDescriptionTex.getText();
			String ClaimDate = ClaimDatePicker.getValue().toString();
			double ClaimAmount = Double.parseDouble(ClaimAmountTex.getText());
			String CStatus = CStatusTex.getText();
			int AccidentID = AccidentIDChoice.getValue().getAccidentID();

			String query = "UPDATE g2_vehicle_insurance_company.claims SET CDescription = ?, ClaimDate = ?, ClaimAmount = ?, CStatus = ?, AccidentID = ? WHERE ClaimID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, CDescription);
				preparedStatement.setString(2, ClaimDate);
				preparedStatement.setDouble(3, ClaimAmount);
				preparedStatement.setString(4, CStatus);
				preparedStatement.setInt(5, AccidentID);
				preparedStatement.setInt(6, claimID);

				preparedStatement.executeUpdate();
			}
			showClaim();
		} catch (NumberFormatException e) {
			displayAlert("Invalid claimID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the update operation.");
		}
	}

	public void deleteRecord() {
		try {
			int ClaimID = Integer.parseInt(ClaimIDTex.getText().trim());

			// Fetching the record before deletion
			String fetchQuery = "SELECT * FROM g2_vehicle_insurance_company.claims WHERE ClaimID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement fetchStatement = conn.prepareStatement(fetchQuery)) {
				fetchStatement.setInt(1, ClaimID);
				ResultSet rs = fetchStatement.executeQuery();
				if (rs.next()) {
					lastDeletedRecord.put("ClaimID", rs.getInt("ClaimID"));
					lastDeletedRecord.put("CDescription", rs.getString("CDescription"));
					lastDeletedRecord.put("ClaimDate", rs.getDate("ClaimDate"));
					lastDeletedRecord.put("ClaimAmount", rs.getDouble("ClaimAmount"));
					lastDeletedRecord.put("CStatus", rs.getString("CStatus"));
					lastDeletedRecord.put("AccidentID", rs.getInt("AccidentID"));
					recordDeleted = true;
				}
			}

			// Deleting the record
			String query = "DELETE FROM g2_vehicle_insurance_company.claims WHERE ClaimID = ?";
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, ClaimID);
				preparedStatement.executeUpdate();
			}

			showClaim();
		} catch (NumberFormatException e) {
			displayAlert("Invalid ClaimID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the delete operation.");
		}
	}

	public void undoDelete() {
		if (recordDeleted && !lastDeletedRecord.isEmpty()) {
			String query = "INSERT INTO g2_vehicle_insurance_company.claims (ClaimID, CDescription, ClaimDate, ClaimAmount, CStatus, AccidentID) VALUES (?, ?, ?, ?, ?, ?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, (int) lastDeletedRecord.get("ClaimID"));
				preparedStatement.setString(2, (String) lastDeletedRecord.get("CDescription"));
				preparedStatement.setDate(3, (java.sql.Date) lastDeletedRecord.get("ClaimDate"));
				preparedStatement.setDouble(4, (double) lastDeletedRecord.get("ClaimAmount"));
				preparedStatement.setString(5, (String) lastDeletedRecord.get("CStatus"));
				preparedStatement.setInt(6, (int) lastDeletedRecord.get("AccidentID"));

				preparedStatement.executeUpdate();
				recordDeleted = false;
				lastDeletedRecord.clear();
				showClaim();
			} catch (SQLException e) {
				e.printStackTrace();
				displayAlert("Error executing the undo operation.");
			}
		} else {
			displayAlert("No record to undo.");
		}
	}

	public void searchRecord() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.claims WHERE ClaimID = ?";
		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setInt(1, Integer.parseInt(ClaimIDToSearch.getText().trim()));
			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Claim> ClaimList = FXCollections.observableArrayList();
			while (rs.next()) {
				Claim claim = new Claim(rs.getInt("claimID"), rs.getString("cDescription"), rs.getDate("claimDate"),
						rs.getDouble("claimAmount"), rs.getString("cStatus"), rs.getInt("accidentID"));
				ClaimList.add(claim);
			}
			ClaimTab.setItems(ClaimList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

}
