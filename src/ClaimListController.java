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

public class ClaimListController implements Initializable {
	@FXML
	private TableColumn<Claim, Date> AccidentDateco;

	@FXML
	private TextField AccidentDatetex;

	@FXML
	private TextField AccidentinDatetex;

	@FXML
	private TableColumn<Claim, Double> ClaimAmountco;

	@FXML
	private TableColumn<Claim, Date> ClaimDateco;

	@FXML
	private TextField ClaimDatetex;

	@FXML
	private TableColumn<Claim, Integer> ClaimIDco;

	@FXML
	private Label ClaimQuery;

	@FXML
	private TableView<Claim> ClaimTab;

	@FXML
	private Button GoToClaimPage;

	@FXML
	private Button GoToHomePage;

	@FXML
	private Button HowMuchAccidentinDate;

	@FXML
	private TableColumn<Claim, String> Locationco;

	@FXML
	private TextField Locationtex;

	@FXML
	private Button SearchByClaimDate;

	@FXML
	private Button SearchByLocation;

	@FXML
	private Button ShowData;

	@FXML
	private Text incurancecompany;

	@FXML
	private Label l1;

	@FXML
	private Label l11;

	@FXML
	private Label l111;

	@FXML
	private AnchorPane pa1;

	@FXML
	private AnchorPane pa2;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showClaim();
	}

	public void GoToClaimPage() {
		Stage stage = (Stage) GoToClaimPage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Claim.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Branches page.");

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

	public ObservableList<Claim> getPhone(String query) {
		ObservableList<Claim> claimList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Claim claim = new Claim();
				claim.setClaimID(rs.getInt("claimID"));
				claim.setClaimDate(rs.getDate("claimDate"));
				claim.setClaimAmount(rs.getDouble("claimAmount"));
				claim.setAccidentDate(rs.getDate("accidentDate"));
				claim.setLocation(rs.getString("location"));

				claimList.add(claim);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return claimList;
	}

	public void showClaim() {
		String query = "SELECT * FROM claims INNER JOIN accidents ON claims.AccidentID = accidents.AccidentID";
		ObservableList<Claim> claimList = getPhone(query);
		ClaimIDco.setCellValueFactory(new PropertyValueFactory<>("ClaimID"));
		ClaimDateco.setCellValueFactory(new PropertyValueFactory<>("ClaimDate"));
		ClaimAmountco.setCellValueFactory(new PropertyValueFactory<>("ClaimAmount"));
		AccidentDateco.setCellValueFactory(new PropertyValueFactory<>("AccidentDate"));
		Locationco.setCellValueFactory(new PropertyValueFactory<>("Location"));
		ClaimTab.setItems(claimList);
	}

	public void searchByClaimDate() {
		String query = "SELECT * FROM claims INNER JOIN accidents ON claims.AccidentID = accidents.AccidentID WHERE ClaimDate = ?";
		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, ClaimDatetex.getText().trim());
			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Claim> ClaimList = FXCollections.observableArrayList();
			while (rs.next()) {
				Claim claim = new Claim(rs.getDate("AccidentDate"), rs.getString("Location"), rs.getInt("ClaimID"),
						rs.getDate("ClaimDate"), rs.getDouble("ClaimAmount"));
				ClaimList.add(claim);
			}
			ClaimTab.setItems(ClaimList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

	public void SearchByLocation() {
		String query = "SELECT * FROM claims " + "INNER JOIN accidents ON claims.AccidentID = accidents.AccidentID  "
				+ "WHERE Location = ?";

		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, Locationtex.getText().trim());

			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Claim> ClaimList = FXCollections.observableArrayList();
			while (rs.next()) {
				Claim claim = new Claim(rs.getDate("AccidentDate"), rs.getString("Location"), rs.getInt("ClaimID"),
						rs.getDate("ClaimDate"), rs.getDouble("ClaimAmount"));
				ClaimList.add(claim);
			}

			ClaimTab.setItems(ClaimList);

		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

}
