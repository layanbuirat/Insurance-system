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

public class PhoneListController implements Initializable {

	@FXML
	private TableColumn<Customers, String> CNameco;

	@FXML
	private TextField CustomerNametex;

	@FXML
	private TableColumn<Customers, String> Emailco;

	@FXML
	private TextField Emailnum2tex;

	@FXML
	private TextField Emailtex;

	@FXML
	private Button GoToHomepage;

	@FXML
	private Button GoToPhonePage;

	@FXML
	private TableColumn<Phone, Integer> PhoneIDco;

	@FXML
	private TableColumn<Phone, String> PhoneNumberco;

	@FXML
	private TextField PhoneNumbertex;

	@FXML
	private Label PhoneQuery;

	@FXML
	private TableView<Phone> PhoneTab;

	@FXML
	private Button SearchByCustomerName;

	@FXML
	private Button ShowData;

	@FXML
	private Button ShowNumber;

	@FXML
	private Text incurancecompany;

	@FXML
	private Label l1;

	@FXML
	private Label l11;

	@FXML
	private Label l12;

	@FXML
	private AnchorPane p2;

	@FXML
	private AnchorPane pa1;

	@FXML
	private Button searchByPhoneNumber;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showPhone();
	}

	public void GoToPhonePage() {
		Stage stage = (Stage) GoToPhonePage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Phone.fxml"));
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

	public ObservableList<Phone> getPhone(String query) {
		ObservableList<Phone> PhoneList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Phone phone = new Phone();
				phone.setPhoneID(rs.getInt("PhoneID"));
				phone.setPhoneNumber(rs.getString("PhoneNumber"));
				phone.setCName(rs.getString("CName"));
				phone.setEmail(rs.getString("Email"));
				PhoneList.add(phone);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return PhoneList;
	}

	public void showPhone() {
		String query = "SELECT * FROM phones INNER JOIN customers ON phones.CustomerID = customers.CustomerID";
		ObservableList<Phone> phoneList = getPhone(query);
		PhoneIDco.setCellValueFactory(new PropertyValueFactory<>("PhoneID"));
		PhoneNumberco.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
		CNameco.setCellValueFactory(new PropertyValueFactory<>("CName"));
		Emailco.setCellValueFactory(new PropertyValueFactory<>("Email"));
		PhoneTab.setItems(phoneList);
	}

	private void displayAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public void searchByPhoneNumberM() {
		String query = "SELECT * FROM phones INNER JOIN customers ON phones.CustomerID = customers.CustomerID WHERE PhoneNumber = ?";
		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, PhoneNumbertex.getText().trim());
			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Phone> phoneList = FXCollections.observableArrayList();
			while (rs.next()) {
				Phone phone = new Phone(rs.getString("CName"), rs.getString("Email"), rs.getInt("PhoneID"),
						rs.getString("PhoneNumber"));
				phoneList.add(phone);
			}
			PhoneTab.setItems(phoneList);
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

	public void SearchByCustomerNameM() {
		String query = "SELECT * FROM phones " + "INNER JOIN customers ON phones.CustomerID = customers.CustomerID  "
				+ "WHERE CName = ?";

		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {

			preparedStatement.setString(1, CustomerNametex.getText().trim());

			ResultSet rs = preparedStatement.executeQuery();

			ObservableList<Phone> phoneList = FXCollections.observableArrayList();
			while (rs.next()) {
				Phone phone = new Phone(rs.getString("CName"), rs.getString("Email"), rs.getInt("PhoneID"),
						rs.getString("PhoneNumber"));
				phoneList.add(phone);
			}

			PhoneTab.setItems(phoneList);

		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the search operation.");
		}
	}

	public void NumberOfPhoneNumber() {
		String query = "SELECT COUNT(phones.PhoneID) AS phone_count " + "FROM phones "
				+ "INNER JOIN customers ON phones.CustomerID = customers.CustomerID " + "WHERE customers.email = ?";

		try (Connection conn = DataBaseConnector.getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(query)) {
			preparedStatement.setString(1, Emailtex.getText().trim());
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				int phoneCount = rs.getInt("phone_count");
				Emailnum2tex.setText(String.valueOf(phoneCount));
			} else {
				Emailnum2tex.setText("0");
				displayAlert("No Results");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error");
		}
	}
}
