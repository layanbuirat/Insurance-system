import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
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

public class InstallmentsListController implements Initializable {

	@FXML
	private TableColumn<Installments, Double> Amount;

	@FXML
	private Button GoToHomepage;

	@FXML
	private Button GoToInstallments;

	@FXML
	private TableColumn<Installments, Date> PaidDate;

	@FXML
	private TextField PolicTypetex;

	@FXML
	private TableColumn<Installments, String> PolicyNumber;

	@FXML
	private TableColumn<Installments, String> PolicyType;

	@FXML
	private Button Search;

	@FXML
	private Button SearchByfirstPolicyType;

	@FXML
	private Button ShowData;

	@FXML
	private TextField datetext;

	@FXML
	private TableColumn<Installments, Date> duedate;

	@FXML
	private Label h2;

	@FXML
	private TableColumn<Installments, Integer> installmentsID;

	@FXML
	private TableView<Installments> installmentsTable1;

	@FXML
	private Label l1;

	@FXML
	private Label la1;

	@FXML
	private AnchorPane p1;

	@FXML
	private AnchorPane p2;

	@FXML
	private Text te1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showInstallments();
	}

	public void GoToInstallmentsM() {
		Stage stage = (Stage) GoToInstallments.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Installments.fxml"));
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

	public ObservableList<Installments> getInstallments(String query) {
		ObservableList<Installments> InstallmentsList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Installments installments = new Installments();
				installments.setInstallmentID(rs.getInt("installmentID"));
				installments.setAmount(rs.getDouble("amount"));
				installments.setDueDate(rs.getDate("dueDate"));
				installments.setPaidDate(rs.getDate("paidDate"));
				installments.setPolicyType(rs.getString("policyType"));
				installments.setPolicyNumber(rs.getString("policyNumber"));

				InstallmentsList.add(installments);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return InstallmentsList;
	}

	public void showInstallments() {
		String query = "SELECT * FROM installments INNER JOIN insurance_policies ON installments.PolicyID = insurance_policies.PolicyID";
		ObservableList<Installments> InstallmentsList = getInstallments(query);
		installmentsID.setCellValueFactory(new PropertyValueFactory<>("InstallmentID"));
		Amount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
		duedate.setCellValueFactory(new PropertyValueFactory<>("DueDate"));
		PaidDate.setCellValueFactory(new PropertyValueFactory<>("PaidDate"));
		PolicyType.setCellValueFactory(new PropertyValueFactory<>("PolicyType"));
		PolicyNumber.setCellValueFactory(new PropertyValueFactory<>("PolicyNumber"));

		installmentsTable1.setItems(InstallmentsList);
	}

	public void searchByPaidDate() {
		String query = "SELECT * FROM installments INNER JOIN insurance_policies ON installments.PolicyID = insurance_policies.PolicyID WHERE PaidDate = ?";
		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, datetext.getText().trim());
			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Installments> InstallmentsList = FXCollections.observableArrayList();
			while (rs.next()) {
				Installments installments = new Installments(rs.getString("PolicyNumber"), rs.getString("PolicyType"),
						rs.getInt("InstallmentID"), rs.getDouble("Amount"), rs.getDate("DueDate"),
						rs.getDate("PaidDate"));
				InstallmentsList.add(installments);
			}
			installmentsTable1.setItems(InstallmentsList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

	public void searchFirst() {
		String query = "SELECT * FROM installments "
				+ "INNER JOIN insurance_policies ON installments.PolicyID = insurance_policies.PolicyID "
				+ "WHERE PolicyType LIKE ?";

		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, PolicTypetex.getText().trim() + "%");

			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Installments> InstallmentsList = FXCollections.observableArrayList();
			while (rs.next()) {
				Installments installments = new Installments(rs.getString("PolicyNumber"), rs.getString("PolicyType"),
						rs.getInt("InstallmentID"), rs.getDouble("Amount"), rs.getDate("DueDate"),
						rs.getDate("PaidDate"));
				InstallmentsList.add(installments);
			}
			installmentsTable1.setItems(InstallmentsList);

		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}
}
