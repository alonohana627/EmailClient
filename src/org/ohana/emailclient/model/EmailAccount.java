package org.ohana.emailclient.model;

import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

/**
 * Setup for each Email Account
 */
public class EmailAccount {
    private String address;
    private String password;
    private Properties properties;
    private Store store;
    private Session session;


    public EmailAccount(String address, String password) {
        this.address = address;
        this.password = password;
        //Gmail Properties
        properties = new Properties();
        properties.put("incomingHost", "imap.gmail.com");
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.host", "smtp.gmail.com");
        properties.put("mail.smtps.auth", "true");
        properties.put("outgoingHost", "smtp.gmail.com");
    }

    @Override
    public String toString() {
        return address;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}