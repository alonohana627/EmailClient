package org.ohana.emailclient.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.ohana.emailclient.EmailManager;
import org.ohana.emailclient.controller.services.LoginService;
import org.ohana.emailclient.model.EmailAccount;
import org.ohana.emailclient.view.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginWindowController extends AbstractController implements Initializable {
    @FXML
    private Label errorLabel;

    @FXML
    private TextField emailAddressField;

    @FXML
    private PasswordField passwordField;

    public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String FXMLName) {
        super(emailManager, viewFactory, FXMLName);
    }

    @FXML
    void loginButtonAction() {
        if(isValidFields()){
            EmailAccount emailAccount = new EmailAccount(emailAddressField.getText(), passwordField.getText());
            LoginService loginService = new LoginService(emailAccount, emailManager);
            loginService.start();
            loginService.setOnSucceeded(event -> {
                LoginResult emailLoginResult= loginService.getValue();
                switch (emailLoginResult) {
                    case SUCCESS:
                        if(!viewFactory.isMainViewInitialized()){
                            viewFactory.showMainWindow();
                        }
                        Stage stage = (Stage) errorLabel.getScene().getWindow();
                        viewFactory.closeStage(stage);
                        return;
                    case FAILED_BY_CREDENTIALS:
                        errorLabel.setText("You entered the wrong Email! or password.");
                        return;
                    default:
                        return;
                }
            });
        }
    }

    private boolean isValidFields() {
        if(emailAddressField.getText().isEmpty()) {
            errorLabel.setText("Please fill email");
            return false;
        }
        if(passwordField.getText().isEmpty()) {
            errorLabel.setText("Please fill password");
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
