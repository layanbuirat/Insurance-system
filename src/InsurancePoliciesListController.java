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

public class InsurancePoliciesListController implements Initializable {
	@FXML
	private TableColumn<InsurancePolicies, Double> CustomPremiumAmount;

	@FXML
	private Label FirstLetterOFpolicyType;

	@FXML
	private TextField FirstLetterOFpolicytex;

	@FXML
	private TableColumn<InsurancePolicies, String> Emailco;

	@FXML
	private Button GonsurancepoliciePage;

	@FXML
	private TableColumn<InsurancePolicies, Integer> PolicyID;

	@FXML
	private TableColumn<InsurancePolicies, String> PolicyNumber;

	@FXML
	private TableColumn<InsurancePolicies, String> Policytype;

	@FXML
	private Button Search;

	@FXML
	private Button SearchByCName;

	@FXML
	private Button ShowData;

	@FXML
	private TableColumn<InsurancePolicies, String> customerName;

	@FXML
	private TextField customerNameTex;

	@FXML
	private Label customerNamelabel;

	@FXML
	private Button goToBasePageList;

	@FXML
	private Text incurancecompanyList;

	@FXML
	private Label insurancepolicieQure;

	@FXML
	private TableView<InsurancePolicies> insurancepolicieTable;

	@FXML
	private AnchorPane p1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showInsurancePolicies();
	}

	public void GonsurancepoliciePageM() {
		Stage stage = (Stage) GonsurancepoliciePage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("InsurancePolicies.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Branches page.");

		}
	}

	public void goToHome() {
		Stage stage = (Stage) goToBasePageList.getScene().getWindow();
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

	public ObservableList<InsurancePolicies> getInsurancePolicies(String query) {
		ObservableList<InsurancePolicies> insurancePoliciesList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				InsurancePolicies insurancePolicies = new InsurancePolicies();
				insurancePolicies.setPolicyID(rs.getInt("policyID"));
				insurancePolicies.setPolicyNumber(rs.getString("policyNumber"));
				insurancePolicies.setPolicyType(rs.getString("policyType"));
				insurancePolicies.setPremiumAmount(rs.getDouble("premiumAmount"));
				insurancePolicies.setCName(rs.getString("CName"));
				insurancePolicies.setEmail(rs.getString("email"));

				insurancePoliciesList.add(insurancePolicies);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return insurancePoliciesList;
	}

	public void showInsurancePolicies() {
		String query = "SELECT * FROM insurance_policies INNER JOIN customers ON insurance_policies.CustomerID = customers.CustomerID";
		ObservableList<InsurancePolicies> insurancePoliciesList = getInsurancePolicies(query);
		PolicyID.setCellValueFactory(new PropertyValueFactory<>("PolicyID"));
		PolicyNumber.setCellValueFactory(new PropertyValueFactory<>("PolicyNumber"));
		Policytype.setCellValueFactory(new PropertyValueFactory<>("PolicyType"));
		CustomPremiumAmount.setCellValueFactory(new PropertyValueFactory<>("PremiumAmount"));
		customerName.setCellValueFactory(new PropertyValueFactory<>("CName"));
		Emailco.setCellValueFactory(new PropertyValueFactory<>("Email"));
		insurancepolicieTable.setItems(insurancePoliciesList);
	}

	public void searchByCName() {
		String query = "SELECT * FROM insurance_policies INNER JOIN customers ON insurance_policies.CustomerID = customers.CustomerID WHERE CName = ?";
		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, customerNameTex.getText().trim());
			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<InsurancePolicies> insurancePoliciesList = FXCollections.observableArrayList();
			while (rs.next()) {
				InsurancePolicies insurancePolicies = new InsurancePolicies(rs.getString("CName"),
						rs.getString("Email"), rs.getInt("PolicyID"), rs.getString("PolicyNumber"),
						rs.getString("PolicyType"), rs.getDouble("PremiumAmount"));
				insurancePoliciesList.add(insurancePolicies);
			}
			insurancepolicieTable.setItems(insurancePoliciesList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

	public void searchFirst() {
		String query = "SELECT * FROM insurance_policies "
				+ "INNER JOIN customers ON insurance_policies.CustomerID = customers.CustomerID "
				+ "WHERE PolicyType LIKE ?";

		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, FirstLetterOFpolicytex.getText().trim() + "%");

			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<InsurancePolicies> InsurancePoliciesList = FXCollections.observableArrayList();
			while (rs.next()) {
				InsurancePolicies insurancePolicies = new InsurancePolicies(rs.getString("CName"),
						rs.getString("Email"), rs.getInt("PolicyID"), rs.getString("PolicyNumber"),
						rs.getString("PolicyType"), rs.getDouble("PremiumAmount"));
				InsurancePoliciesList.add(insurancePolicies);
			}

			insurancepolicieTable.setItems(InsurancePoliciesList);

		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}
}
