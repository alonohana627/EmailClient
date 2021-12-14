package org.ohana.emailclient;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ohana.emailclient.controller.persistence.PersistenceAccess;
import org.ohana.emailclient.controller.persistence.ValidAccount;
import org.ohana.emailclient.controller.services.LoginService;
import org.ohana.emailclient.model.EmailAccount;
import org.ohana.emailclient.view.ViewFactory;

public class Launcher extends Application {
    private PersistenceAccess persistenceAccess = new PersistenceAccess();
    private EmailManager emailManager = new EmailManager();
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * When the program starts, it takes the lists of users on the computer and logs them automatically
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        ViewFactory viewFactory = new ViewFactory(emailManager);
        List<ValidAccount> validAccountList = persistenceAccess.loadFromPersistence();
        if(validAccountList.size() > 0) {
            viewFactory.showMainWindow();
            for (ValidAccount validAccount: validAccountList){
                EmailAccount emailAccount = new EmailAccount(validAccount.getAddress(), validAccount.getPassword());
                LoginService loginService = new LoginService(emailAccount, emailManager);
                loginService.start();
            }
        } else {
            viewFactory.showLoginWindow();
        }
    }

    /**
     * When the program ends, it saves the lists of users on the computer that was logged on in encoded manner
     * @param stage
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        List<ValidAccount> validAccountList = new ArrayList<>();
        for(EmailAccount emailAccount: emailManager.getEmailAccounts()){
            validAccountList.add(new ValidAccount(emailAccount.getAddress(), emailAccount.getPassword()));
        }
        persistenceAccess.saveToPersistence(validAccountList);
    }
}
