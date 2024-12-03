import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SitesListController implements Initializable {

	@FXML
	private TableColumn<Sites, String> cityNameList;

	@FXML
	private Button goToBasePageList;

	@FXML
	private Button goToSiteList;

	@FXML
	private TableColumn<Sites, Integer> siteIDList;

	@FXML
	private Label siteLabList;

	@FXML
	private TableView<Sites> siteTabList;

	@FXML
	private Button sortASCBYStreetName;

	@FXML
	private Button sortDESBYCityName;

	@FXML
	private Button sortDESBYSiteID;

	@FXML
	private TableColumn<Sites, String> streetNameList;

	@FXML
	private AnchorPane pan1;

	@FXML
	private AnchorPane pane2;

	public void goToSites() {
		Stage stage = (Stage) goToSiteList.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Sites.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Sites page.");

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (siteIDList == null || streetNameList == null || cityNameList == null || siteTabList == null) {
			System.err.println("One of the FXML elements is not initialized.");
			return;
		}
		showSites();
	}

	public ObservableList<Sites> getSites(String query) {
		ObservableList<Sites> sitesList = FXCollections.observableArrayList();
		Connection conn = DataBaseConnector.getConnection();
		Statement st = null;
		ResultSet rs = null;

		try {
			st = conn.createStatement();
			rs = st.executeQuery(query);

			if (!rs.isBeforeFirst()) {
				System.out.println("No records found for the query: " + query);
				return sitesList;
			} else {
				while (rs.next()) {
					Sites sites = new Sites(rs.getInt("SiteID"), rs.getString("StreetName"), rs.getString("CityName"));
					sitesList.add(sites);
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

		return sitesList;
	}

	public void showSites() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.Sites";
		ObservableList<Sites> sitesList = getSites(query);
		siteIDList.setCellValueFactory(new PropertyValueFactory<>("siteID"));
		streetNameList.setCellValueFactory(new PropertyValueFactory<>("streetName"));
		cityNameList.setCellValueFactory(new PropertyValueFactory<>("cityName"));
		siteTabList.setItems(sitesList);
	}

	public void sortSitesBySiteIDDesc() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.Sites ORDER BY SiteID DESC";
		ObservableList<Sites> sitesList = getSites(query);
		siteTabList.setItems(sitesList);
	}

	public void sortSitesByCityNameDesc() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.Sites ORDER BY CityName DESC";
		ObservableList<Sites> sitesList = getSites(query);
		siteTabList.setItems(sitesList);
	}

	public void sortSitesByStreetNameAsc() {
		String query = "SELECT * FROM g2_vehicle_insurance_company.Sites ORDER BY StreetName ASC";
		ObservableList<Sites> sitesList = getSites(query);
		siteTabList.setItems(sitesList);
	}

	private void displayAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
