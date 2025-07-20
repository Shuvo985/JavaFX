
package tutorcentral;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupController implements Initializable {

    @FXML
    private TextField tfName;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfContact;
    @FXML
    private PasswordField pfPass;
    @FXML
    private PasswordField pfCPass;
    @FXML
    private Button signupBtn;
    @FXML
    private Hyperlink SigninLink;
    @FXML
    private Label lblError;
    @FXML
    private ComboBox<String> choiceRole;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        choiceRole.getItems().addAll("Teacher", "Student");
    }    

    @FXML
    private void Signup(ActionEvent event) throws IOException {
        String fullname = tfName.getText().trim();
        String username = tfUsername.getText().trim();
        String contact = tfContact.getText().trim();
        String password = pfPass.getText();
        String confirmPassword = pfCPass.getText();
        String role = choiceRole.getValue();

        if (fullname.isEmpty() || username.isEmpty() || contact.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || role == null) {
            System.out.println("Please fill in all required fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return;
        }

        try (Connection conn = ConnectionDB.getConnection()) {
            PreparedStatement stmt;
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("Username already exists. Please choose a different one.");
                return;
            }


            if (role.equalsIgnoreCase("Teacher")) {

                String sql = "INSERT INTO users (fullname, username, contact, password, role) VALUES (?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, fullname);
                stmt.setString(2, username);
                stmt.setString(3, contact);
                stmt.setString(4, password);
                stmt.setString(5, role.toLowerCase());

                stmt.executeUpdate();
                System.out.println("Teacher registered successfully!");

            } else if (role.equalsIgnoreCase("Student")) {
                String sql = "INSERT INTO users (fullname, username, contact, password, role) VALUES (?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, fullname);
                stmt.setString(2, username);
                stmt.setString(3, contact);
                stmt.setString(4, password);
                stmt.setString(5, role.toLowerCase());

                stmt.executeUpdate();
                System.out.println("Student registered successfully!");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Signin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Signin Form");
            stage.show();
            ((Stage) signupBtn.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error occurred during registration.");
        }
    }

    @FXML
    private void Signin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Signin.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Signin Form");
        stage.show();
        ((Stage) SigninLink.getScene().getWindow()).close();
    }
    
}
