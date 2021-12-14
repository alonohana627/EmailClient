package org.ohana.emailclient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ohana.emailclient.controller.services.FetchFolderService;
import org.ohana.emailclient.controller.services.FolderUpdateService;
import org.ohana.emailclient.model.EmailAccount;
import org.ohana.emailclient.model.EmailMessage;
import org.ohana.emailclient.model.EmailTreeItem;
import org.ohana.emailclient.view.IconResolver;
import javax.mail.Flags;
import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {
    private EmailMessage selectedMessage;
    private EmailTreeItem<String> selectedFolder;
    private FolderUpdateService folderUpdateService;
    private List<Folder> folderList = new ArrayList<Folder>();
    private EmailTreeItem<String> rootFolder = new EmailTreeItem<String>("");
    private ObservableList<EmailAccount> emailAccounts = FXCollections.observableArrayList();
    private IconResolver iconResolver = new IconResolver();

    public EmailManager(){
        folderUpdateService = new FolderUpdateService(folderList);
        folderUpdateService.start();
    }

    public ObservableList<EmailAccount> getEmailAccounts(){
        return emailAccounts;
    }

    public EmailMessage getSelectedMessage() {
        return selectedMessage;
    }

    public EmailTreeItem<String> getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(EmailTreeItem<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public void setSelectedMessage(EmailMessage selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public EmailTreeItem<String> getRootFolder() {
        return rootFolder;
    }

    public List<Folder> getFolderList() {
        return folderList;
    }

    public void setRead() {
        try {
            selectedMessage.setRead(true);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, true);
            selectedFolder.decrementMessagesCount();
        } catch (Exception e) {

        }
    }

    public void setUnRead() {
        try {
            selectedMessage.setRead(false);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, false);
            selectedFolder.incrementMessagesCount();
        } catch (Exception e) {

        }
    }

    public void deleteSelectedMessage() {
        try {
            selectedMessage.getMessage().setFlag(Flags.Flag.DELETED, true);
            selectedFolder.getEmailMessages().remove(selectedMessage);
        } catch (Exception e) {

        }
    }
    public void addEmailAccount(EmailAccount emailAccount){
        emailAccounts.add(emailAccount);
        EmailTreeItem<String> treeItem = new EmailTreeItem<String>(emailAccount.getAddress());
        treeItem.setGraphic(iconResolver.getIconForFolder(emailAccount.getAddress()));
        FetchFolderService fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem, folderList);
        fetchFolderService.start();
        rootFolder.getChildren().add(treeItem);
    }
}
