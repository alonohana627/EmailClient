package org.ohana.emailclient.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.ohana.emailclient.EmailManager;
import org.ohana.emailclient.view.ColorThemes;
import org.ohana.emailclient.view.FontSize;
import org.ohana.emailclient.view.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class OptionsWindowController extends AbstractController implements Initializable {
    @FXML
    Slider fontSizeModifier;

    @FXML
    ChoiceBox<ColorThemes> colorThemeModifier;

    public OptionsWindowController(EmailManager emailManager, ViewFactory viewFactory, String FXMLName) {
        super(emailManager, viewFactory, FXMLName);
    }

    @FXML
    public void applyButton(){
        viewFactory.setColorThemes(colorThemeModifier.getValue());
        viewFactory.setFontSize(FontSize.values()[(int)(fontSizeModifier.getValue())]);
        viewFactory.updateStyles();
    }

    @FXML
    public void cancelButton(){
        Stage optionStage = (Stage) fontSizeModifier.getScene().getWindow();
        viewFactory.closeStage(optionStage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        themePickerInit();
        fontSizeModifierInit();
    }

    private void fontSizeModifierInit() {
        fontSizeModifier.setMin(0);
        fontSizeModifier.setMax(FontSize.values().length-1);
        fontSizeModifier.setValue(viewFactory.getFontSize().ordinal());
        fontSizeModifier.setMajorTickUnit(1);
        fontSizeModifier.setMinorTickCount(0);
        fontSizeModifier.setBlockIncrement(1);
        fontSizeModifier.setSnapToTicks(true);
        fontSizeModifier.setShowTickMarks(true);
        fontSizeModifier.setShowTickLabels(true);
        fontSizeModifier.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double sliderValues) {
                int i = sliderValues.intValue();
                return FontSize.values()[i].toString();
            }

            @Override
            public Double fromString(String s) {
                return null;
            }
        });
        fontSizeModifier.valueProperty().addListener((obs, oldVal, newVal)->{
            fontSizeModifier.setValue(newVal.intValue());
        });
    }

    private void themePickerInit() {
        colorThemeModifier.setItems(FXCollections.observableArrayList(ColorThemes.values()));
        colorThemeModifier.setValue(viewFactory.getColorThemes());
    }
}
