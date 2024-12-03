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

public class CustomersListController implements Initializable {
	@FXML
	private TextField BranchNameTex;

	@FXML
	private TableColumn<Customers, String> Branchname;

	@FXML
	private TableColumn<Customers, String> Email;

	@FXML
	private TextField FirstLetterOFCustomertex;

	@FXML
	private Button GoTocustomerpage;

	@FXML
	private TableColumn<Customers, String> Name;

	@FXML
	private Button Search;

	@FXML
	private Button SearchByBranchName;

	@FXML
	private Button ShowData;

	@FXML
	private Label countLabel;

	@FXML
	private TableColumn<Customers, String> customerID;

	@FXML
	private Label customerQuery;

	@FXML
	private TableView<Customers> customerQueryTable;

	@FXML
	private Button goToBasePageList;

	@FXML
	private Text incurancecompany;

	@FXML
	private Label l1;

	@FXML
	private Label l2;

	@FXML
	private AnchorPane pa1;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showCustomers();
	}

	public void GoTocustomerpage() {
		Stage stage = (Stage) GoTocustomerpage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Customers.fxml"));
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

	public ObservableList<Customers> getCustomers(String query) {
		ObservableList<Customers> CustomersList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Customers customers = new Customers();
				customers.setCustomerID(rs.getString("customerID"));
				customers.setCName(rs.getString("cName"));
				customers.setEmail(rs.getString("email"));
				customers.setBranchName(rs.getString("branchName"));

				CustomersList.add(customers);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CustomersList;
	}

	public void showCustomers() {
		String query = "SELECT * FROM customers INNER JOIN branches ON customers.BranchID = branches.BranchID";
		ObservableList<Customers> CustomerssList = getCustomers(query);
		customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
		Name.setCellValueFactory(new PropertyValueFactory<>("cName"));
		Email.setCellValueFactory(new PropertyValueFactory<>("email"));
		Branchname.setCellValueFactory(new PropertyValueFactory<>("branchName"));

		customerQueryTable.setItems(CustomerssList);
	}

	public void searchBybranchName() {
		String query = "SELECT * FROM customers INNER JOIN branches ON customers.BranchID = branches.BranchID WHERE BranchName = ?";
		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, BranchNameTex.getText().trim());
			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Customers> CustomersList = FXCollections.observableArrayList();
			while (rs.next()) {
				Customers customers = new Customers(rs.getString("BranchName"), rs.getString("CustomerID"),
						rs.getString("CName"), rs.getString("Email"));
				CustomersList.add(customers);
			}
			customerQueryTable.setItems(CustomersList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

	public void searchFirst() {
		String query = "SELECT * FROM customers " + "INNER JOIN branches ON customers.BranchID = branches.BranchID "
				+ "WHERE CName LIKE ?";

		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, FirstLetterOFCustomertex.getText().trim() + "%");

			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Customers> CustomersList = FXCollections.observableArrayList();
			while (rs.next()) {
				Customers customers = new Customers(rs.getString("BranchName"), rs.getString("CustomerID"),
						rs.getString("CName"), rs.getString("Email"));
				CustomersList.add(customers);
			}
			customerQueryTable.setItems(CustomersList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}
}
