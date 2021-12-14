package org.ohana.emailclient.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ohana.emailclient.EmailManager;
import org.ohana.emailclient.controller.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A factory that generates a view that we need.
 */
public class ViewFactory {
    private EmailManager emailManager;
    private ColorThemes colorThemes = ColorThemes.LIGHT;
    private FontSize fontSize = FontSize.MEDIUM;
    private ArrayList<Stage> activeStage;
    private boolean mainViewInitialized = false;

    public ViewFactory(EmailManager emailManager){
        this.emailManager=emailManager;
        activeStage = new ArrayList<>();
    }

    public ColorThemes getColorThemes() {
        return colorThemes;
    }

    public void setColorThemes(ColorThemes colorThemes) {
        this.colorThemes = colorThemes;
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isMainViewInitialized(){
        return mainViewInitialized;
    }

    private void initStage(AbstractController controller){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getFXMLName()));
        fxmlLoader.setController(controller);
        Parent parent;

        try {
            parent = fxmlLoader.load();
        } catch (IOException e){
            e.printStackTrace();
            return;
        }

        Scene scene = new Scene(parent);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.show();

        activeStage.add(stage);
    }

    /**** Methods to view the windows ****/
    public void showLoginWindow(){
        AbstractController loginWindowController = new LoginWindowController(emailManager, this, "LoginView.fxml");
        initStage(loginWindowController);
    }

    public void showMainWindow(){
        AbstractController mainWindowController = new MainWindowController(emailManager, this, "MainWindow.fxml");
        initStage(mainWindowController);
        mainViewInitialized = true;
    }

    public void showOptionsWindow(){
        AbstractController optionsWindowController = new OptionsWindowController(emailManager, this, "OptionsWindow.fxml");
        initStage(optionsWindowController);
    }

    public void showEmailDetailsWindow(){
        AbstractController controller = new EmailDetailsController(emailManager, this, "EmailDetailsWindow.fxml");
        initStage(controller);
    }

    public void showComposeMessageWindow() {
        AbstractController controller = new ComposeMessageController(emailManager, this, "ComposeMessageWindow.fxml");
        initStage(controller);
    }

    //Close a given stage.
    public void closeStage(Stage stage){
        stage.close();
        activeStage.remove(stage);
    }

    public void updateStyles() {
        for(Stage stage: activeStage){
            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource(ColorThemes.getCSSPath(colorThemes)).toExternalForm());
            scene.getStylesheets().add(getClass().getResource(FontSize.getCSSPath(fontSize)).toExternalForm());
        }
    }
}
