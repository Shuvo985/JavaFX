
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SigninController implements Initializable {

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPass;
    @FXML
    private Button signinBtn;
    @FXML
    private Hyperlink SignupLink;
    @FXML
    private Label lblError;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void Signup(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Signup.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Signup Form");
        stage.show();
        ((Stage) SignupLink.getScene().getWindow()).close();
    }

    @FXML
    private void Signin(ActionEvent event) throws IOException {
        String username = tfUsername.getText().trim();
        String password = pfPass.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and Password must not be empty.");
            return;
        }

        try (Connection conn = ConnectionDB.getConnection()) {
            String sql = "SELECT role FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String role = rs.getString("role");

                        String fxmlFile = null;
                        String title = null;

                        if ("Admin".equalsIgnoreCase(role)) {
                            fxmlFile = "AdminDashboard.fxml";
                            title = "Admin Dashboard";
                        } else if ("Teacher".equalsIgnoreCase(role)) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("TeacherDashboard.fxml"));
                            Parent root = loader.load();


                            TeacherDashboardController controller = loader.getController();
                            controller.setTeacherUsername(username);


                            Stage stage = (Stage) lblError.getScene().getWindow(); 
                            stage.setScene(new Scene(root));
                            stage.setTitle("Doctor Dashboard");
                            stage.show();

                            return;
                        } else if ("Student".equalsIgnoreCase(role)) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentDashboard.fxml"));
                            Parent root = loader.load();


                            StudentDashboardController controller = loader.getController();
                            controller.setStudentUsername(username);


                            Stage stage = (Stage) lblError.getScene().getWindow(); 
                            stage.setScene(new Scene(root));
                            stage.setTitle("Student Dashboard");
                            stage.show();

                            return;
                        }


                        if (fxmlFile != null) {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                            Parent root = loader.load();


                            Stage stage = (Stage) lblError.getScene().getWindow(); 
                            stage.setScene(new Scene(root));
                            stage.setTitle(title);
                            stage.show();
                        } else {
                            System.out.println("Unknown role.");
                        }

                    } else {
                        System.out.println("Invalid username or password.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
