import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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

public class InstallmentsController implements Initializable {

	@FXML
	private Button Add_Button_PatInfo;

	@FXML
	private TableColumn<Installments, Double> Amount;

	@FXML
	private TextField Amounttex;

	@FXML
	private Button Delete;

	@FXML
	private DatePicker DuedateDP;

	@FXML
	private Button GoToEnd;

	@FXML
	private Button GoToinstallmentslist;

	@FXML
	private Button GoToinsurancepolicieview;

	@FXML
	private TableColumn<Installments, Date> PaidDate;

	@FXML
	private TableColumn<Installments, Integer> PolicyID;

	@FXML
	private ComboBox<InsurancePolicies> PolicyIDComboBox;

	@FXML
	private Button ReturnCoverPage1;

	@FXML
	private Button SearchID;

	@FXML
	private TextField SearchIDtex;

	@FXML
	private Button ShowData;

	@FXML
	private Button UnDoDelete;

	@FXML
	private TableColumn<Installments, Date> duedate;

	@FXML
	private TableColumn<Installments, Integer> installmentsID;

	@FXML
	private TextField installmentsIDtex;

	@FXML
	private TableView<Installments> installmentstabl;

	@FXML
	private Label la1;

	@FXML
	private Label la2;

	@FXML
	private Label la3;

	@FXML
	private Label la4;

	@FXML
	private Label la5;

	@FXML
	private Label la7;

	@FXML
	private AnchorPane p1;

	@FXML
	private AnchorPane p2;

	@FXML
	private DatePicker paiddateDP;

	@FXML
	private Text te1;

	@FXML
	private Button update;
	private Map<String, Object> lastDeletedRecord = new HashMap<>();
	private boolean recordDeleted = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showInstallments();
		setcompobox();
	}

	public void setcompobox() {
		InsurancePoliciesController bc = new InsurancePoliciesController();
		ObservableList<InsurancePolicies> InsurancePoliciesIDs = bc.returnPolicyID();
		PolicyIDComboBox.setItems(InsurancePoliciesIDs);
	}

	public void GoToinstallmentslist() {
		Stage stage = (Stage) GoToinstallmentslist.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("InstallmentsList.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void goToHome() {
		Stage stage = (Stage) ReturnCoverPage1.getScene().getWindow();
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

	public void GoToinsurancepolicieview() {
		Stage stage = (Stage) GoToinsurancepolicieview.getScene().getWindow();
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

	public void GoToEnd() {
		Stage stage = (Stage) GoToEnd.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("End.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void handleRowSelection(MouseEvent event) {
		int index = installmentstabl.getSelectionModel().getSelectedIndex();
		if (index <= -1) {
			return;
		}

		Installments selectedInstallments = installmentstabl.getSelectionModel().getSelectedItem();
		if (selectedInstallments != null) {
			installmentsIDtex.setText(String.valueOf(selectedInstallments.getInstallmentID()));
			Amounttex.setText(String.valueOf(selectedInstallments.getAmount()));
			DuedateDP.setValue(((Date) selectedInstallments.getDueDate()).toLocalDate());
			paiddateDP.setValue(((Date) selectedInstallments.getPaidDate()).toLocalDate());
			PolicyIDComboBox
					.setValue(new InsurancePolicies(selectedInstallments.getPolicyID(), "", "", null, null, 0, ""));
		}
	}

	public ObservableList<Installments> getInstallments(String query) {
		ObservableList<Installments> InstallmentsList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Installments installments = new Installments(rs.getInt("installmentID"), rs.getDouble("amount"),
						rs.getDate("dueDate"), rs.getDate("paidDate"), rs.getInt("policyID"));
				InstallmentsList.add(installments);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return InstallmentsList;
	}

	public void showInstallments() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.installments";
		ObservableList<Installments> InstallmentsList = getInstallments(query);
		installmentsID.setCellValueFactory(new PropertyValueFactory<>("installmentID"));
		Amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		duedate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
		PaidDate.setCellValueFactory(new PropertyValueFactory<>("paidDate"));
		PolicyID.setCellValueFactory(new PropertyValueFactory<>("policyID"));

		installmentstabl.setItems(InstallmentsList);
	}

	private boolean InstallmentsIDExists(int InstallmentsID) {
		String query = "SELECT COUNT(*) FROM g2_vehicle_insurance_company.installments WHERE InstallmentID = ?";
		try (Connection conn = DataBaseConnector.getConnection(); PreparedStatement st = conn.prepareStatement(query)) {

			st.setInt(1, InstallmentsID);
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
			int installmentsID = Integer.parseInt(installmentsIDtex.getText().trim());
			if (InstallmentsIDExists(installmentsID)) {
				displayAlert("InstallmentsID already exists.");
				return;
			}

			String query = "INSERT INTO g2_vehicle_insurance_company.installments (InstallmentID, Amount, DueDate, PaidDate, PolicyID) VALUES (?, ?, ?, ?, ?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, installmentsID);
				preparedStatement.setDouble(2, Double.parseDouble(Amounttex.getText()));
				preparedStatement.setDate(3, Date.valueOf(DuedateDP.getValue()));
				preparedStatement.setDate(4, Date.valueOf(paiddateDP.getValue()));
				preparedStatement.setInt(5, PolicyIDComboBox.getValue().getPolicyID());

				preparedStatement.executeUpdate();
			}

			showInstallments();
		} catch (NumberFormatException e) {
			displayAlert("Invalid installmentsID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the insert operation.");
		}
	}

	public void updateRecord() {
		try {
			int installmentsID = Integer.parseInt(installmentsIDtex.getText().trim());
			double Amount1 = Double.parseDouble(Amounttex.getText());
			String Duedate1 = DuedateDP.getValue().toString();
			String paiddate1 = paiddateDP.getValue().toString();
			int PolicyID1 = PolicyIDComboBox.getValue().getPolicyID();

			String query = "UPDATE g2_vehicle_insurance_company.installments SET Amount = ?, DueDate = ?, PaidDate = ?, PolicyID = ? WHERE InstallmentID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setDouble(1, Amount1);
				preparedStatement.setString(2, Duedate1);
				preparedStatement.setString(3, paiddate1);
				preparedStatement.setInt(4, PolicyID1);
				preparedStatement.setInt(5, installmentsID);

				preparedStatement.executeUpdate();
			}
			showInstallments();
		} catch (NumberFormatException e) {
			displayAlert("Invalid installmentsID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the update operation.");
		}
	}

	public void deleteRecord() {
		try {
			int installmentsID1 = Integer.parseInt(installmentsIDtex.getText().trim());

			// Fetching the record before deletion
			String fetchQuery = "SELECT * FROM g2_vehicle_insurance_company.installments WHERE InstallmentID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement fetchStatement = conn.prepareStatement(fetchQuery)) {
				fetchStatement.setInt(1, installmentsID1);
				ResultSet rs = fetchStatement.executeQuery();
				if (rs.next()) {
					lastDeletedRecord.put("InstallmentID", rs.getInt("InstallmentID"));
					lastDeletedRecord.put("Amount", rs.getDouble("Amount"));
					lastDeletedRecord.put("DueDate", rs.getDate("DueDate"));
					lastDeletedRecord.put("PaidDate", rs.getDate("PaidDate"));
					lastDeletedRecord.put("PolicyID", rs.getInt("PolicyID"));
					recordDeleted = true;
				}
			}

			// Deleting the record
			String query = "DELETE FROM g2_vehicle_insurance_company.installments WHERE InstallmentID = ?";
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, installmentsID1);
				preparedStatement.executeUpdate();
			}

			showInstallments();
		} catch (NumberFormatException e) {
			displayAlert("Invalid InstallmentID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the delete operation.");
		}
	}

	public void undoDelete() {
		if (recordDeleted && !lastDeletedRecord.isEmpty()) {
			String query = "INSERT INTO g2_vehicle_insurance_company.installments (InstallmentID, Amount, DueDate, PaidDate, PolicyID) VALUES (?, ?, ?, ?, ?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, (int) lastDeletedRecord.get("InstallmentID"));
				preparedStatement.setDouble(2, (double) lastDeletedRecord.get("Amount"));
				preparedStatement.setDate(3, (Date) lastDeletedRecord.get("DueDate"));
				preparedStatement.setDate(4, (Date) lastDeletedRecord.get("PaidDate"));
				preparedStatement.setInt(5, (int) lastDeletedRecord.get("PolicyID"));

				preparedStatement.executeUpdate();
				recordDeleted = false;
				lastDeletedRecord.clear();
				showInstallments();
			} catch (SQLException e) {
				e.printStackTrace();
				displayAlert("Error executing the undo operation.");
			}
		} else {
			displayAlert("No record to undo.");
		}
	}

	public void searchRecord() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.installments WHERE InstallmentID = ?";
		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setInt(1, Integer.parseInt(SearchIDtex.getText().trim()));
			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Installments> InstallmentsList = FXCollections.observableArrayList();
			while (rs.next()) {
				Installments installments = new Installments(rs.getInt("InstallmentID"), rs.getDouble("Amount"),
						rs.getDate("DueDate"), rs.getDate("PaidDate"), rs.getInt("PolicyID"));
				InstallmentsList.add(installments);
			}
			installmentstabl.setItems(InstallmentsList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

}
