package org.ohana.emailclient.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import org.ohana.emailclient.EmailManager;
import org.ohana.emailclient.controller.services.MessageRenderService;
import org.ohana.emailclient.model.EmailMessage;
import org.ohana.emailclient.view.ViewFactory;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * Generic Controller for RightClick+ViewDetail
 */
public class EmailDetailsController extends AbstractController implements Initializable
{
    private static final String LOCATION_OF_DOWNLOAD = System.getProperty("user.home")+"/downloads/";

    @FXML
    private WebView webView;

    @FXML
    private Label attachmentLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label senderLabel;

    @FXML
    private HBox hBoxDownloads;

    public EmailDetailsController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EmailMessage emailMessage = emailManager.getSelectedMessage();
        subjectLabel.setText(emailMessage.getSubject());
        senderLabel.setText(emailMessage.getSender());

        loadAttachments(emailMessage);

        MessageRenderService messageRenderService = new MessageRenderService(webView.getEngine());
        messageRenderService.setEmailMessage(emailMessage);
        messageRenderService.restart();
    }

    /**
     * Loads the attachments the user attach to the email.
     * @param emailMessage
     */
    private void loadAttachments(EmailMessage emailMessage){
        if(emailMessage.isHasAttachments()){
            for(MimeBodyPart attachment: emailMessage.getAttachmentList()){
                try{
                    AttachmentButton button = new AttachmentButton(attachment);
                    hBoxDownloads.getChildren().add(button);
                } catch (MessagingException e){
                    e.printStackTrace();
                }
            }
        } else {
            attachmentLabel.setText("");
        }
    }

    /** Basically, we using another variation of button so we could download the file.
     *
     */
    private class AttachmentButton extends Button{
        private MimeBodyPart attachment;
        private String downloadPath;

        public AttachmentButton(MimeBodyPart attachment) throws MessagingException {
            this.attachment = attachment;
            this.setText(attachment.getFileName());
            this.downloadPath = LOCATION_OF_DOWNLOAD+attachment.getFileName();
            this.setOnAction(e->downloadAction());
        }

        private void downloadAction(){
            blueColor();
            Service service = new Service() {
                @Override
                protected Task createTask() {
                    return new Task() {
                        @Override
                        protected Object call() throws Exception {
                            attachment.saveFile(downloadPath);
                            return null;
                        }
                    };
                }
            };
            service.restart();
            service.setOnSucceeded(e->{
                greenColor();
                this.setOnAction( e2->{
                    File file = new File(downloadPath);
                    Desktop desktop = Desktop.getDesktop();
                    if(file.exists()){
                        try {
                            desktop.open(file);
                        } catch (Exception exp) {
                            exp.printStackTrace();
                        }
                    }
                });
            });
        }

        private void blueColor(){
            this.setStyle("-fx-background-color: Blue");
        }

        private void greenColor(){
            this.setStyle("-fx-background-color: Green");
        }
    }
}
