import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AccidentListController implements Initializable {

	@FXML
	private TableColumn<Accident, String> ADescription;

	@FXML
	private TableColumn<Accident, Date> AccidentDate;

	@FXML
	private TableColumn<Accident, Integer> AccidentID;

	@FXML
	private TableView<Accident> AccidentTable;

	@FXML
	private Button GoToAccidentpage;

	@FXML
	private Button GoToHomePage;

	@FXML
	private Button GoToendPage;

	@FXML
	private Label List_gender;

	@FXML
	private TableColumn<Accident, String> Location;

	@FXML
	private Button SortASCBYAccidentDate;

	@FXML
	private Button SortDESBYAccidentID;

	@FXML
	private Button SortDESBYLocation;

	@FXML
	private Text la2;

	@FXML
	private Label lab;

	@FXML
	private AnchorPane p1;

	@FXML
	private AnchorPane p2;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showAccident();
	}

	public void goToAccident() {
		Stage stage = (Stage) GoToAccidentpage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Accident.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Accident page.");

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

	public void goToEnd() {
		Stage stage = (Stage) GoToendPage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("End.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading End page.");

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
				return FXCollections.observableArrayList();

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
		AccidentID.setCellValueFactory(new PropertyValueFactory<>("accidentID"));
		AccidentDate.setCellValueFactory(new PropertyValueFactory<>("accidentDate"));
		Location.setCellValueFactory(new PropertyValueFactory<>("location"));
		ADescription.setCellValueFactory(new PropertyValueFactory<>("aDescription"));
		AccidentTable.setItems(accidentsList);
	}

	public void SortDESBYAccidentID() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.accidents ORDER BY AccidentID DESC";
		ObservableList<Accident> accidentList = getAccident(query);
		AccidentTable.setItems(accidentList);
	}

	@FXML
	private void SortDESBYLocation() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.accidents ORDER BY AccidentDate ASC";
		ObservableList<Accident> accidentList = getAccident(query);
		AccidentTable.setItems(accidentList);
	}

	public void SortASCBYAccidentDate() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.accidents ORDER BY Location DESC";
		ObservableList<Accident> accidentList = getAccident(query);
		AccidentTable.setItems(accidentList);
	}

	private void displayAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
