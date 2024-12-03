import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class QueryController implements Initializable {

	@FXML
	private AnchorPane Homepage;

	@FXML
	private AnchorPane Querypage;

	@FXML
	private Button endbutton;

	@FXML
	private Button goAccidentQuery;

	@FXML
	private Button goAccident_driverQuery;

	@FXML
	private Button goAccident_vehicleQuery;

	@FXML
	private Button goBranchQuery;

	@FXML
	private Button goInstallmentQuery;

	@FXML
	private Button goInsurancepolicyQuery;

	@FXML
	private Button goclaimQuery;

	@FXML
	private Button gocustomerQuery;

	@FXML
	private Button gocustomer_addressQuery;

	@FXML
	private Button gositesQuery;

	@FXML
	private Text insurancecompanyquerylab;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void customerQuery() {
		Stage stage = (Stage) gocustomerQuery.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("CustomersList.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Branches page.");

		}
	}

	public void InstallmentQuery() {
		Stage stage = (Stage) goInstallmentQuery.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("InstallmentsList.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Home page.");

		}

	}

	public void customer_addressQuery() {
		Stage stage = (Stage) gocustomer_addressQuery.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Customer_Address.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Home page.");

		}

	}

	public void AccidentQuery() {
		Stage stage = (Stage) goAccidentQuery.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("AccidentList.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Home page.");

		}

	}

	public void InsurancepolicyQuery() {
		Stage stage = (Stage) goInsurancepolicyQuery.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("InsurancePoliciesList.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Home page.");

		}

	}

	public void Accident_vehicleQuery() {
		Stage stage = (Stage) goAccident_vehicleQuery.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Accident_Vehicle.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Home page.");

		}

	}

	public void sitesQuery() {
		Stage stage = (Stage) gositesQuery.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("SitesList.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Home page.");

		}

	}

	public void Accident_driverQuery() {
		Stage stage = (Stage) goAccident_driverQuery.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("Accident_Drivers.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Home page.");

		}

	}

	public void BranchQuery() {
		Stage stage = (Stage) goBranchQuery.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("branchList.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Home page.");

		}

	}

	public void claimQuery() {
		Stage stage = (Stage) goclaimQuery.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("ClaimList.fxml"));
			Scene scene = new Scene(root, 940, 800);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e1) {
			e1.printStackTrace();
			displayAlert("Error loading Home page.");

		}

	}

	public void TOTHEEND() {
		Stage stage = (Stage) endbutton.getScene().getWindow();
		stage.close();

		try {
			Parent root = FXMLLoader.load(getClass().getResource("End.fxml"));
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

}
