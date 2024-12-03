import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SitesController implements Initializable {

	@FXML
	private TableColumn<Sites, String> CityNameView;

	@FXML
	private Button Delete;

	@FXML
	private Button undo;

	@FXML
	private Button Undo;

	@FXML
	private Button GoToBasePage;

	@FXML
	private Button GoToSitesList;

	@FXML
	private Button Search;

	@FXML
	private TableColumn<Sites, Integer> SiteIDView;

	@FXML
	private Label Sites_viewlab;

	@FXML
	private TableColumn<Sites, String> StreetNameView;

	@FXML
	private Button add;

	@FXML
	private AnchorPane anchorpan22;

	@FXML
	private AnchorPane anchorpane2;

	@FXML
	private Label citylabview;

	@FXML
	private TextField citytfview;

	@FXML
	private Text insurancecompanysitesview;

	@FXML
	private TableView<Sites> siteTableView;

	@FXML
	private TextField siteidTetosearch;

	@FXML
	private TextField siteidtfview;

	@FXML
	private Label siteldLabview;

	@FXML
	private Label streetlabview;

	@FXML
	private TextField streettfview;

	@FXML
	private Button update;

	@FXML
	private Button ShowData;

	private Map<String, Object> lastDeletedRecord = new HashMap<>();
	private boolean recordDeleted = false;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		showSites();
	}

	public void goToSitesList() {
		Stage stage = (Stage) GoToSitesList.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("SitesList.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Sites Query page.");

		}
	}

	public void goBranch() {
		Stage stage = (Stage) GoToBasePage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("braches.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Home page.");

		}

	}

	public void GoToHomePage() {
		Stage stage = (Stage) GoToBasePage.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
			Scene scene = new Scene(root, 900, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Home page.");

		}
	}

	public void handleRowSelection(MouseEvent event) {
		System.out.println("Inside handleRowSelection.");
		int index = siteTableView.getSelectionModel().getSelectedIndex();
		System.out.println("Index is :" + index);
		if (index <= -1) {
			System.out.println("No row selected.");
			return;
		}

		Sites selectedSites = siteTableView.getSelectionModel().getSelectedItem();
		if (selectedSites != null) {
			siteidtfview.setText(String.valueOf(selectedSites.getSiteID()));
			streettfview.setText(selectedSites.getStreetName());
			citytfview.setText(selectedSites.getCityName());
		}
	}

	public ObservableList<Sites> getSites(String query) {
		ObservableList<Sites> sitesList = FXCollections.observableArrayList();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			conn = DataBaseConnector.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);

			if (!rs.isBeforeFirst()) {
				System.out.println("No records found for the query: " + query);
				return FXCollections.observableArrayList(); // Return an empty
															// list
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
		SiteIDView.setCellValueFactory(new PropertyValueFactory<>("SiteID"));
		StreetNameView.setCellValueFactory(new PropertyValueFactory<>("StreetName"));
		CityNameView.setCellValueFactory(new PropertyValueFactory<>("CityName"));
		siteTableView.setItems(sitesList);
	}

	public void insertRecord() {
		try {
			int siteID = Integer.parseInt(siteidtfview.getText().trim());

			if (siteIDExists(siteID)) {
				displayAlert("SiteID already exists.");
				return;
			}

			String query = "INSERT INTO g2_vehicle_insurance_company.Sites (SiteID, StreetName, CityName) VALUES (?, ?, ?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, siteID);
				preparedStatement.setString(2, streettfview.getText());
				preparedStatement.setString(3, citytfview.getText());

				preparedStatement.executeUpdate();
			}

			showSites();
		} catch (NumberFormatException e) {
			displayAlert("Invalid SiteID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the insert operation.");
		}
	}

	private boolean siteIDExists(int siteID) {
		String query = "SELECT COUNT(*) FROM g2_vehicle_insurance_company.Sites WHERE SiteID = " + siteID;
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		boolean exists = false;

		try {
			conn = DataBaseConnector.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(query);

			if (rs.next() && rs.getInt(1) > 0) {
				exists = true;
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

		return exists;
	}

	private void displayAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public void updateRecord() {
		try {
			int siteID = Integer.parseInt(siteidtfview.getText().trim());
			String streetName = streettfview.getText();
			String cityName = citytfview.getText();

			String query = "UPDATE g2_vehicle_insurance_company.Sites SET StreetName = ?, CityName = ? WHERE SiteID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, streetName);
				preparedStatement.setString(2, cityName);
				preparedStatement.setInt(3, siteID);

				preparedStatement.executeUpdate();
			}
			showSites();
		} catch (NumberFormatException e) {
			displayAlert("Invalid SiteID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the update operation.");
		}
	}

	public void deleteRecord() {
		try {
			int siteID = Integer.parseInt(siteidtfview.getText().trim());

			// Fetching the record before deletion
			String fetchQuery = "SELECT * FROM g2_vehicle_insurance_company.Sites WHERE SiteID = ?";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement fetchStatement = conn.prepareStatement(fetchQuery)) {
				fetchStatement.setInt(1, siteID);
				ResultSet rs = fetchStatement.executeQuery();
				if (rs.next()) {
					lastDeletedRecord.put("SiteID", rs.getInt("SiteID"));
					lastDeletedRecord.put("StreetName", rs.getString("StreetName"));
					lastDeletedRecord.put("CityName", rs.getString("CityName"));
					recordDeleted = true;
				}
			}

			// Deleting the record
			String query = "DELETE FROM g2_vehicle_insurance_company.Sites WHERE SiteID = ?";
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, siteID);
				preparedStatement.executeUpdate();
			}

			showSites();
		} catch (NumberFormatException e) {
			displayAlert("Invalid SiteID. Please enter a valid integer.");
		} catch (SQLException e) {
			e.printStackTrace();
			displayAlert("Error executing the delete operation.");
		}
	}

	public void undoDelete() {
		if (recordDeleted && !lastDeletedRecord.isEmpty()) {
			String query = "INSERT INTO g2_vehicle_insurance_company.Sites (SiteID, StreetName, CityName) VALUES (?, ?, ?)";
			Connection conn = DataBaseConnector.getConnection();

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setInt(1, (int) lastDeletedRecord.get("SiteID"));
				preparedStatement.setString(2, (String) lastDeletedRecord.get("StreetName"));
				preparedStatement.setString(3, (String) lastDeletedRecord.get("CityName"));

				preparedStatement.executeUpdate();

				// Clear the lastDeletedRecord map and reset the flag
				lastDeletedRecord.clear();
				recordDeleted = false;
			} catch (SQLException e) {
				e.printStackTrace();
				displayAlert("Error executing the undo operation.");
			}

			showSites();
		} else {
			displayAlert("No record to undo.");
		}
	}

	@FXML
	private void searchSiteByID() {
		try {
			int siteID = Integer.parseInt(siteidTetosearch.getText().trim());
			Sites site = getSiteByID(siteID);
			ObservableList<Sites> siteList = FXCollections.observableArrayList();
			if (site != null) {
				siteList.add(site);
				SiteIDView.setCellValueFactory(new PropertyValueFactory<>("siteID"));
				StreetNameView.setCellValueFactory(new PropertyValueFactory<>("streetName"));
				CityNameView.setCellValueFactory(new PropertyValueFactory<>("cityName"));
				siteTableView.setItems(siteList);

				streettfview.setText(site.getStreetName());
				citytfview.setText(site.getCityName());
			} else {
				displayAlert("Site with ID " + siteID + " not found.");
			}
		} catch (NumberFormatException e) {
			displayAlert("Invalid SiteID. Please enter a valid integer.");
		} catch (SQLException e) {
			displayAlert("Error occurred while searching for the site. Please try again.");
		}
	}

	private Sites getSiteByID(int siteID) throws SQLException {
		String sql = "SELECT * FROM g2_vehicle_insurance_company.Sites WHERE SiteID = ?";
		try (Connection connection = DataBaseConnector.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, siteID);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				int id = resultSet.getInt("SiteID");
				String streetName = resultSet.getString("StreetName");
				String cityName = resultSet.getString("CityName");
				return new Sites(id, streetName, cityName);
			}
		}
		return null; // Site with given ID not found
	}

	public ObservableList<Sites> returnsitesid() {
		String query = "SELECT s.SiteID FROM sites s";
		ObservableList<Sites> sitesidList = FXCollections.observableArrayList();

		try (Connection conn = DataBaseConnector.getConnection();
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query)) {

			if (!rs.isBeforeFirst()) {
				System.out.println("No records found for the query: " + query);
				return FXCollections.observableArrayList(); // Return an empty
															// list
			} else {
				while (rs.next()) {
					Sites sites = new Sites(rs.getInt("SiteID"));
					sitesidList.add(sites);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return sitesidList;
	}

	public void executeQuery(String query) {
		Connection conn = DataBaseConnector.getConnection();
		try (Statement st = conn.createStatement()) {
			st.executeUpdate(query);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
