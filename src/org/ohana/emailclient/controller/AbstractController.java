package org.ohana.emailclient.controller;

import org.ohana.emailclient.EmailManager;
import org.ohana.emailclient.view.ViewFactory;

public abstract class AbstractController {
    protected EmailManager emailManager;
    protected ViewFactory viewFactory;
    protected String FXMLName;

    public AbstractController(EmailManager emailManager, ViewFactory viewFactory, String FXMLName){
        this.emailManager = emailManager;
        this.viewFactory = viewFactory;
        this.FXMLName = FXMLName;
    }

    public String getFXMLName(){
        return this.FXMLName;
    }
}
