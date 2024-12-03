import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EndController implements Initializable {
	@FXML
	private Button ReturnCoverPage;

	@FXML
	private AnchorPane p1;

	@FXML
	private AnchorPane p2;

	@FXML
	private Text t1;

	@FXML
	private TextField t10;

	@FXML
	private TextField t11;

	@FXML
	private TextField t12;

	@FXML
	private TextField t13;

	@FXML
	private TextField t14;

	@FXML
	private TextField t15;

	@FXML
	private Text t16;

	@FXML
	private TextField t17;

	@FXML
	private Text t18;

	@FXML
	private Text t19;

	@FXML
	private Text t2;

	@FXML
	private Text t3;

	@FXML
	private TextField t4;

	@FXML
	private TextField t5;

	@FXML
	private TextField t6;

	@FXML
	private TextField t7;

	@FXML
	private TextField t8;

	@FXML
	private TextField t9;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void goTOHome() {
		Stage stage = (Stage) ReturnCoverPage.getScene().getWindow();
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

}
