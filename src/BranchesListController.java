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

public class BranchesListController implements Initializable {

	@FXML
	private TableColumn<Branch, Integer> BranchIDColumn;

	@FXML
	private TableColumn<Branch, String> BranchNameColumn;

	@FXML
	private TextField BranchNameTex;

	@FXML
	private Label BranchesListLab;

	@FXML
	private TableView<Branch> BranchesTable;

	@FXML
	private TableColumn<Branch, String> CityNameColumn;

	@FXML
	private TextField FirstLetterOFCitytex;

	@FXML
	private Button Search;

	@FXML
	private Button SearchByBranchName;

	@FXML
	private TableColumn<Branch, String> StreetNameColumn;

	@FXML
	private Button goToBasePageButton;

	@FXML
	private Button goToBranchButton;
	@FXML
	private Button ShowData;

	@FXML
	private Text insuranceCompanyText;

	@FXML
	private Label l1;

	@FXML
	private Label l2;

	@FXML
	private AnchorPane pan1;

	@FXML
	private AnchorPane pane2;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showBranches();
		// setTableColumns();
	}

	public void goToBranch() {
		Stage stage = (Stage) goToBranchButton.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("braches.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Branches page.");

		}
	}

	public void goToHome() {
		Stage stage = (Stage) goToBasePageButton.getScene().getWindow();
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

	public ObservableList<Branch> getBranches(String query) {
		ObservableList<Branch> branchList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Branch branch = new Branch();
				branch.setBranchID(rs.getInt("BranchID"));
				branch.setBranchName(rs.getString("BranchName"));
				branch.setStreetName(rs.getString("StreetName"));
				branch.setCityName(rs.getString("CityName"));
				branchList.add(branch);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return branchList;
	}

	public void showBranches() {
		String query = "SELECT * FROM branches INNER JOIN sites ON branches.siteID = sites.SiteID";
		ObservableList<Branch> branchList = getBranches(query);
		BranchIDColumn.setCellValueFactory(new PropertyValueFactory<>("branchID"));
		BranchNameColumn.setCellValueFactory(new PropertyValueFactory<>("branchName"));
		StreetNameColumn.setCellValueFactory(new PropertyValueFactory<>("streetName"));
		CityNameColumn.setCellValueFactory(new PropertyValueFactory<>("cityName"));
		BranchesTable.setItems(branchList);
	}

	private void displayAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public void searchRecord() {
		String query = "SELECT * FROM branches INNER JOIN sites ON branches.siteID = sites.SiteID WHERE BranchName = ?";
		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, BranchNameTex.getText().trim());
			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Branch> branchList = FXCollections.observableArrayList();
			while (rs.next()) {
				Branch branch = new Branch(rs.getString("StreetName"), rs.getString("CityName"), rs.getInt("BranchID"),
						rs.getString("BranchName"));
				branchList.add(branch);
			}
			BranchesTable.setItems(branchList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

	public void searchFirst() {
		String query = "SELECT * FROM branches " + "INNER JOIN sites ON branches.siteID = sites.SiteID "
				+ "WHERE CityName LIKE ?";

		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, FirstLetterOFCitytex.getText().trim() + "%");

			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Branch> branchList = FXCollections.observableArrayList();
			while (rs.next()) {
				Branch branch = new Branch(rs.getString("StreetName"), rs.getString("CityName"), rs.getInt("BranchID"),
						rs.getString("BranchName"));
				branchList.add(branch);
			}

			BranchesTable.setItems(branchList);

		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

}
