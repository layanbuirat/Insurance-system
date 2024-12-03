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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Accident_DriversController implements Initializable {
	@FXML
	private TableColumn<Accident_Drivers, Integer> AccidentID;

	@FXML
	private Button Add;

	@FXML
	private Label ClaimQuery;

	@FXML
	private TableView<Accident_Drivers> ClaimTab;

	@FXML
	private TableColumn<Accident_Drivers, String> DriverName;

	@FXML
	private Button GoToHomePage;

	@FXML
	private TableColumn<Accident_Drivers, String> LicenseNumber;

	@FXML
	private ComboBox<Accident> comoAccidentID;

	@FXML
	private ComboBox<Drivers> comodriverID;

	@FXML
	private TableColumn<Accident_Drivers, Integer> driverID;

	@FXML
	private Text incurancecompany;

	@FXML
	private Label labeldriverID;

	@FXML
	private Label lableAccidentID;

	@FXML
	private AnchorPane pa1;

	@FXML
	private AnchorPane pa2;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

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

	public ObservableList<Accident_Drivers> getPhone(String query) {
		ObservableList<Accident_Drivers> Accident_DriversList = FXCollections.observableArrayList();
		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			while (rs.next()) {
				Accident_Drivers accident_Drivers = new Accident_Drivers();
				accident_Drivers.setDriverID(rs.getInt("driverID"));
				accident_Drivers.setAccidentID(rs.getInt("accidentID"));
				accident_Drivers.setLicenseNumber(rs.getString("licenseNumber"));
				accident_Drivers.setDName(rs.getString("dName"));
				Accident_DriversList.add(accident_Drivers);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Accident_DriversList;
	}

	public void showPhone() {
		String query = "SELECT * FROM phones INNER JOIN customers ON phones.CustomerID = customers.CustomerID";
		ObservableList<Accident_Drivers> Accident_DriversList = getPhone(query);
		driverID.setCellValueFactory(new PropertyValueFactory<>("PhoneID"));
		AccidentID.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
		LicenseNumber.setCellValueFactory(new PropertyValueFactory<>("CName"));
		DriverName.setCellValueFactory(new PropertyValueFactory<>("Email"));
		ClaimTab.setItems(Accident_DriversList);
	}

	private void displayAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public void () {
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
}
