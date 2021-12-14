package org.ohana.emailclient.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ohana.emailclient.EmailManager;
import org.ohana.emailclient.controller.LoginResult;
import org.ohana.emailclient.model.EmailAccount;

import javax.mail.*;

/**
 * A service class for login
 */
public class LoginService extends Service<LoginResult> {
    EmailAccount emailAccount;
    EmailManager emailManager;

    public LoginService(EmailAccount emailAccount, EmailManager emailManager) {
        this.emailAccount = emailAccount;
        this.emailManager = emailManager;
    }

    @Override
    protected Task<LoginResult> createTask() {
        return new Task<LoginResult>() {
            @Override
            protected LoginResult call() throws Exception {
                return login();
            }
        };
    }

    /**
     * Where the magic happens. connects to mail using IMAP protocol.
     */
    private LoginResult login(){
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount.getAddress(), emailAccount.getPassword());
            }
        };
        try {
            Thread.sleep(1000);
            Session session = Session.getInstance(emailAccount.getProperties(), authenticator);
            emailAccount.setSession(session);
            Store store = session.getStore("imaps");
            store.connect(
                    emailAccount.getProperties().getProperty("incomingHost"),
                    emailAccount.getAddress(),
                    emailAccount.getPassword());
            emailAccount.setStore(store);
            emailManager.addEmailAccount(emailAccount);

        } catch (NoSuchProviderException e){
            e.printStackTrace();
            return LoginResult.FAILED_BY_NETWORK;
        } catch (AuthenticationFailedException e){
            e.printStackTrace();
            return LoginResult.FAILED_BY_CREDENTIALS;
        } catch (MessagingException e){
            e.printStackTrace();
            return LoginResult.FAILED_BY_UNKNOWN_ERROR;
        } catch (Exception e){
            e.printStackTrace();
            return LoginResult.FAILED_BY_UNKNOWN_ERROR;
        }
        return LoginResult.SUCCESS;
    }
}
