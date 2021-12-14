package org.ohana.emailclient.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import org.ohana.emailclient.model.EmailMessage;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import java.io.IOException;

/**
 * This class sends the rendered message to the user using MIME protocol
 */
public class MessageRenderService extends Service {

    private EmailMessage emailMessage;
    private WebEngine webEngine;
    private StringBuffer stringBuffer;

    public MessageRenderService(WebEngine webEngine) {
        this.webEngine = webEngine;
        this.stringBuffer = new StringBuffer();
        this.setOnSucceeded(e-> displayMessage());
    }

    public void setEmailMessage(EmailMessage emailMessage){
        this.emailMessage = emailMessage;
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                try{
                    loadMessage(); //loads the chosen message
                } catch(Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    private void displayMessage(){
        webEngine.loadContent(stringBuffer.toString()); //renders using WebEngine
    }

    /**
     * Loads the message to the web engine
     * @throws MessagingException
     * @throws IOException
     */
    private void loadMessage() throws MessagingException, IOException {
        stringBuffer.setLength(0);
        Message message = emailMessage.getMessage();
        String contentType = message.getContentType();

        if(isSimpleType(contentType)){
            stringBuffer.append(message.getContent().toString());
        }

        else if(isMultipartType(contentType)){
            Multipart multipart = (Multipart) message.getContent();
            loadMultipart(multipart, stringBuffer);
        }
    }

    private void loadMultipart(Multipart multipart, StringBuffer stringBuffer) throws MessagingException, IOException {
        for (int i = multipart.getCount()-1; i>=0; i--){
            BodyPart bodyPart = multipart.getBodyPart(i);
            String contentType = bodyPart.getContentType();

            if (isSimpleType(contentType)){
                stringBuffer.append(bodyPart.getContent().toString());
            }

            else if(isMultipartType(contentType)){
                Multipart multipart2 = (Multipart) bodyPart.getContent();
                loadMultipart(multipart2, stringBuffer);
            }

            else if(!isTextPlain(contentType)){
                MimeBodyPart attachment = (MimeBodyPart) bodyPart;
                emailMessage.addAttachment(attachment);
            }
        }
    }

    /** Util methods to check the type of the message **/
    private boolean isTextPlain(String contentType){
         return contentType.contains("TEXT/PLAIN");
    }

    private boolean isSimpleType(String contentType){
        return contentType.contains("TEXT/HTML") ||
                contentType.contains("mixed") ||
                contentType.contains("text");
    }

    private boolean isMultipartType(String contentType){
        return contentType.contains("multipart");
    }
}
