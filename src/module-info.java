module emailclient {
    requires javafx.web;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires activation;
    requires java.mail;
    requires java.desktop;

    opens org.ohana.emailclient;
    opens org.ohana.emailclient.view;
    opens org.ohana.emailclient.controller;
    opens org.ohana.emailclient.controller.services;
    opens org.ohana.emailclient.model;
}