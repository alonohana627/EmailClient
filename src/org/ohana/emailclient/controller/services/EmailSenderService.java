package org.ohana.emailclient.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ohana.emailclient.controller.EmailSendingResult;
import org.ohana.emailclient.model.EmailAccount;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.List;

/**
 * Basic email sender service for sending mail through the program.
 */
public class EmailSenderService extends Service<EmailSendingResult> {

    private final EmailAccount emailAccount;
    private final String subject;
    private final String recipient;
    private final String content;
    private final List<File> attachments;

    public EmailSenderService(EmailAccount emailAccount, String subject, String recipient, String content, List<File> attachments) {
        this.emailAccount = emailAccount;
        this.subject = subject;
        this.recipient = recipient;
        this.content = content;
        this.attachments = attachments;
    }

    /**
     * A task that sends the mail
     * @return - the result of the email sending.
     */
    @Override
    protected Task<EmailSendingResult> createTask() {
        return new Task<EmailSendingResult>() {
            @Override
            protected EmailSendingResult call() throws Exception {
                try {
                    MimeMessage mimeMessage = new MimeMessage(emailAccount.getSession());
                    Multipart mimeMultiPart = new MimeMultipart();
                    BodyPart messageBodyPart = new MimeBodyPart();
                    mimeMessageSetup(mimeMessage, mimeMultiPart, messageBodyPart);
                    if(attachments.size()>0){
                        addAttachments(mimeMultiPart);
                    }
                    Transport sender = emailAccount.getSession().getTransport();
                    sender.connect(
                            emailAccount.getProperties().getProperty("outgoingHost"),
                            emailAccount.getAddress(),
                            emailAccount.getPassword()
                    );
                    sender.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                    sender.close();
                }

                catch (MessagingException e){
                    e.printStackTrace();
                    return EmailSendingResult.FAILED_BY_PROVIDER;
                } catch (Exception e){
                    e.printStackTrace();
                    return EmailSendingResult.FAILED_BY_UNEXPECTED_ERROR;
                }

                return EmailSendingResult.SUCCESS;
            }
        };
    }

    /**
     * A method to setup the message. Helps to module the createTask method
     * @param mimeMessage
     * @param mimeMultiPart
     * @param messageBodyPart
     * @throws MessagingException
     */
    private void mimeMessageSetup(MimeMessage mimeMessage, Multipart mimeMultiPart, BodyPart messageBodyPart) throws MessagingException {
        mimeMessage.setFrom(emailAccount.getAddress());
        mimeMessage.addRecipients(Message.RecipientType.TO, recipient);
        mimeMessage.setSubject(subject);
        messageBodyPart.setContent(content, "text/html");
        mimeMultiPart.addBodyPart(messageBodyPart);
        mimeMessage.setContent(mimeMultiPart);
    }

    /**
     * A method to add the attachments to the mail
     * @param mimeMultiPart - the attachments
     * @throws MessagingException
     */
    private void addAttachments(Multipart mimeMultiPart) throws MessagingException {
        for (File file: attachments) {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource fileSource = new FileDataSource(file.getAbsolutePath());
            attachmentPart.setDataHandler(new DataHandler(fileSource));
            attachmentPart.setFileName(file.getName());
            mimeMultiPart.addBodyPart(attachmentPart);
        }
    }
}
