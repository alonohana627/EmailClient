package org.ohana.emailclient.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import org.ohana.emailclient.EmailManager;
import org.ohana.emailclient.controller.services.MessageRenderService;
import org.ohana.emailclient.model.EmailMessage;
import org.ohana.emailclient.model.EmailTreeItem;
import org.ohana.emailclient.view.ViewFactory;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class MainWindowController extends AbstractController implements Initializable {

    private MenuItem showMessageDetailsMenuItem = new MenuItem("view details");
    private MenuItem markUnreadMenuItem = new MenuItem("Mark as Unread");
    private MenuItem deleteMessageMenuItem = new MenuItem("Delete Message");

    @FXML
    private TreeView<String> emailsTree;

    @FXML
    private TableView<EmailMessage> emailsTable;


    @FXML
    private TableColumn<EmailMessage, String> senderView;

    @FXML
    private TableColumn<EmailMessage, String> subjectView;

    @FXML
    private TableColumn<EmailMessage, String> recipientView;

    @FXML
    private TableColumn<EmailMessage, Integer> sizeView;

    @FXML
    private TableColumn<EmailMessage, Date> dateView;

    @FXML
    private WebView emailView;

    private MessageRenderService renderService;

    @FXML
    public void openOption(){
        viewFactory.showOptionsWindow();
    }

    @FXML
    void addAccountAction() {
        viewFactory.showLoginWindow();
    }

    @FXML
    void composeMessageAction() {
        viewFactory.showComposeMessageWindow();
    }


    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String FXMLName) {
        super(emailManager, viewFactory, FXMLName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailTreeViewSetup();
        emailsTableViewSetup();
        folderSelectionSetup();
        setBold();
        renderServiceSetup();
        messageSelectionSetup();
        contextMenusSetup();
    }
    /* setup methods and services for the main window */
    private void emailTreeViewSetup(){
        emailsTree.setRoot(emailManager.getRootFolder());
        emailsTree.setShowRoot(false);
    }

    private void emailsTableViewSetup() {
        senderView.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("sender"));
        subjectView.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("subject"));
        recipientView.setCellValueFactory(new PropertyValueFactory<EmailMessage, String>("recipient"));
        sizeView.setCellValueFactory(new PropertyValueFactory<EmailMessage, Integer>("size"));
        dateView.setCellValueFactory(new PropertyValueFactory<EmailMessage, Date>("date"));
        emailsTable.setContextMenu(new ContextMenu(markUnreadMenuItem, deleteMessageMenuItem, showMessageDetailsMenuItem));
    }

    private void folderSelectionSetup() {
        emailsTree.setOnMouseClicked(e->{
            EmailTreeItem<String> item = (EmailTreeItem<String>) emailsTree.getSelectionModel().getSelectedItem();
            if (item != null) {
                emailManager.setSelectedFolder(item);
                emailsTable.setItems(item.getEmailMessages());
            }
        });
    }

    private void setBold() {
        emailsTable.setRowFactory(new Callback<TableView<EmailMessage>, TableRow<EmailMessage>>() {
            @Override
            public TableRow<EmailMessage> call(TableView<EmailMessage> emailMessageTableView) {
                return new TableRow<EmailMessage>() {
                    @Override
                    protected void updateItem(EmailMessage item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            if (item.isRead()) {
                                setStyle("");
                            } else {
                                setStyle("-fx-font-weight: bold");
                            }
                        }
                    }
                };
            }
        });
    }

    private void renderServiceSetup() {
        renderService = new MessageRenderService(emailView.getEngine());
    }

    private void messageSelectionSetup() {
        emailsTable.setOnMouseClicked(e->{
            EmailMessage emailMessage = emailsTable.getSelectionModel().getSelectedItem();
            if(emailMessage!=null){
                if(!emailMessage.isRead()){
                    emailManager.setRead();
                }
                emailManager.setSelectedMessage(emailMessage);
                renderService.setEmailMessage(emailMessage);
                renderService.restart();
            }
        });
    }

    private void contextMenusSetup() {
        markUnreadMenuItem.setOnAction(event -> emailManager.setUnRead());
        deleteMessageMenuItem.setOnAction(event -> {
            emailManager.deleteSelectedMessage();
            emailView.getEngine().loadContent("");
        });
        showMessageDetailsMenuItem.setOnAction(event -> viewFactory.showEmailDetailsWindow());
    }
}