package org.ohana.emailclient.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.ohana.emailclient.model.EmailTreeItem;
import org.ohana.emailclient.view.IconResolver;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.util.List;

/**
 * Fetches the folders in the left sidebar.
 */
public class FetchFolderService extends Service<Void> {
    private Store store;
    private EmailTreeItem<String> rootFolder;
    private List<Folder> folderList;
    private IconResolver iconResolver = new IconResolver();

    public FetchFolderService(Store store, EmailTreeItem<String> rootFolder, List<Folder> folderList) {
        this.store = store;
        this.rootFolder = rootFolder;
        this.folderList = folderList;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                fetchFolders();
                return null;
            }
        };
    }

    /**
     * Calls the foldersHandler function
     * @throws MessagingException
     */
    private void fetchFolders() throws MessagingException {
        Folder[] folders = store.getDefaultFolder().list();
        foldersHandler(folders, rootFolder);
    }

    /**
     * Recursive function to look for subfolder and display them.
     * @param folders
     * @param rootFolder
     * @throws MessagingException
     */
    private void foldersHandler(Folder[] folders, EmailTreeItem<String> rootFolder) throws MessagingException {
        for(Folder folder: folders){
            EmailTreeItem<String> emailTreeItem = new EmailTreeItem<String>(folder.getName());

            folderList.add(folder);
            emailTreeItem.setGraphic(iconResolver.getIconForFolder(folder.getName()));
            rootFolder.getChildren().add((emailTreeItem));
            rootFolder.setExpanded(true);
            messageHandler(folder, emailTreeItem);
            addMessageFolderListener(folder, emailTreeItem);

            if(folder.getType() == Folder.HOLDS_FOLDERS){
                Folder[] subFolders = folder.list();
                foldersHandler(subFolders, emailTreeItem);
            }
        }
    }

    /**
     * A listener for new mails
     * @param folder
     * @param emailTreeItem
     */
    private void addMessageFolderListener(Folder folder, EmailTreeItem<String> emailTreeItem) {
        folder.addMessageCountListener(new MessageCountListener(){
            @Override
            public void messagesAdded(MessageCountEvent messageCountEvent) {
                for (int i = 0; i < messageCountEvent.getMessages().length; i++) {
                    try {
                        Message message = folder.getMessage(folder.getMessageCount()-i);
                        emailTreeItem.addEmailToTop(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void messagesRemoved(MessageCountEvent messageCountEvent) { //Not needed. We handled the deletion ourselves.

            }
        });
    }

    /**
     * Adds the mails to the proper folder
     * @param folder
     * @param emailTreeItem
     */
    private void messageHandler(Folder folder, EmailTreeItem<String> emailTreeItem) {
        Service fetchMessagesService = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        if(folder.getType() != Folder.HOLDS_FOLDERS){
                            folder.open(Folder.READ_WRITE);
                            int folderSize = folder.getMessageCount();
                            for(int i = folderSize; i > 0; i--){
                                emailTreeItem.addEmail(folder.getMessage(i));
                            }
                        }
                        return null;
                    }
                };
            }
        };
        fetchMessagesService.start();
    }
}
