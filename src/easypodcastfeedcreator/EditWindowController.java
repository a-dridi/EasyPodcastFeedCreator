/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easypodcastfeedcreator;

import static easypodcastfeedcreator.MainController.locale;
import easypodcastfeedcreator.model.PodcastEntry;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * PodcastEntry Window
 *
 * @author a-dridi
 */
public class EditWindowController implements Initializable {

    @FXML
    private Label entryTitleColumnLabel = new Label();
    @FXML
    private Label entryDescriptionColumnLabel = new Label();
    @FXML
    private Label entryLinkColumnLabel = new Label();
    @FXML
    private Label entryDateColumnLabel = new Label();
    @FXML
    private Label entryDateColumnDescriptionLabel = new Label();
    @FXML
    private Label entrySummaryColumnLabel = new Label();
    @FXML
    private Label entrySubtitleColumnLabel = new Label();
    @FXML
    private Label entryImageColumnLabel = new Label();
    @FXML
    private Label entryDurationColumnLabel = new Label();
    @FXML
    private Label entryFilelengthColumnLabel = new Label();
    @FXML
    private Label entryTypeColumnLabel = new Label();
    @FXML
    private Button addpodcastentryButton = new Button();
    @FXML
    private Button entriesCloseButton = new Button();

    @FXML
    private TextField entryTitleColumnField = new TextField();
    @FXML
    private TextField entryDescriptionColumnField = new TextField();
    @FXML
    private TextField entryLinkColumnField = new TextField();
    @FXML
    private TextField entryDateColumnField = new TextField();
    @FXML
    private TextField entrySummaryColumnField = new TextField();
    @FXML
    private TextField entrySubtitleColumnField = new TextField();
    @FXML
    private TextField entryImageColumnField = new TextField();
    @FXML
    private TextField entryDurationColumnField = new TextField();
    @FXML
    private TextField entryFilelengthColumnField = new TextField();
    @FXML
    private TextField entryTypeColumnField = new TextField();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadEntrywindowLanguage(Locale.getDefault().getLanguage());

    }

    public void loadEntrywindowLanguage(String languageCode) {

        if (languageCode != null && languageCode != "") {
            locale = new Locale(languageCode);
            MainController.languageBundle = ResourceBundle.getBundle("easypodcastfeedcreator.languages.lang", locale);
            //set text of buttons and labels
            this.addpodcastentryButton.setText(MainController.languageBundle.getString("addpodcastentryButtonTxt"));

            this.entriesCloseButton.setText(MainController.languageBundle.getString("entriesCloseButtonTxt"));
            this.entryTitleColumnLabel.setText(MainController.languageBundle.getString("entryTitleColumnTxt"));
            this.entryDescriptionColumnLabel.setText(MainController.languageBundle.getString("entryDescriptionColumnTxt"));
            this.entryLinkColumnLabel.setText(MainController.languageBundle.getString("entryLinkColumnTxt"));
            this.entryDateColumnLabel.setText(MainController.languageBundle.getString("entryDateColumnTxt"));
            this.entryDateColumnDescriptionLabel.setText(MainController.languageBundle.getString("entryDateColumnDescriptionLabelTxt"));
            this.entrySummaryColumnLabel.setText(MainController.languageBundle.getString("entrySummaryColumnTxt"));
            this.entrySubtitleColumnLabel.setText(MainController.languageBundle.getString("entrySubtitleColumnTxt"));
            this.entryImageColumnLabel.setText(MainController.languageBundle.getString("entryImageColumnTxt"));
            this.entryDurationColumnLabel.setText(MainController.languageBundle.getString("entryDurationColumnTxt"));
            this.entryFilelengthColumnLabel.setText(MainController.languageBundle.getString("entryFilelengthColumnTxt"));
            this.entryTypeColumnLabel.setText(MainController.languageBundle.getString("entryTypeColumnTxt"));

        } else {
            loadEntrywindowLanguage("en");
        }
    }

    /**
     * Add new row to PodcastEntry Table.
     */
    @FXML
    public void addPodcastEntry() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z", Locale.US);

        if (this.entryTitleColumnField.getText().equals("") || this.entryLinkColumnField.getText().equals("")) {

            if (this.entryTitleColumnField.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText(MainController.languageBundle.getString("errorwritetitelTxt"));
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText(MainController.languageBundle.getString("errorwritelinkTxt"));
                alert.show();
            }

        } else {

            PodcastEntry pe = new PodcastEntry();
            pe.setTitle(this.entryTitleColumnField.getText());
            pe.setDescription(this.entryDescriptionColumnField.getText());
            pe.setDuration(this.entryDurationColumnField.getText());
            try {
                if (this.entryFilelengthColumnField == null || this.entryFilelengthColumnField.getText().equals("")) {
                    int filelength = Integer.parseInt(this.entryFilelengthColumnField.getText());
                    pe.setLength(filelength);
                }
                pe.setLink(this.entryLinkColumnField.getText());
                if (this.entryDateColumnField == null || this.entryDateColumnField.getText().equals("")) {
                    pe.setPublishDate(new Date());
                } else {
                    pe.setPublishDate(sdf.parse(this.entryDateColumnField.getText()));
                }
                pe.setSubtitle(this.entrySubtitleColumnField.getText());
                pe.setSummary(this.entrySummaryColumnField.getText());
                pe.setType(this.entryTypeColumnField.getText());
                pe.setUrl(this.entryLinkColumnField.getText());

                System.out.println("DEBUG: addpodcastentry; " + pe.toString());
                MainController.podcastEntriesData.add(pe);

                MainController.podcastEntries.getItems().add((pe));
                MainController.podcastEntries.refresh();

                MainController.stageEntryWindow.close();

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText(MainController.languageBundle.getString("datalengthEnterErrorTxt"));
                alert.show();
            } catch (ParseException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText(MainController.languageBundle.getString("entrypublishdateEnterErrorTxt"));
                alert.show();

            }
        }
    }

    /**
     *
     */
    @FXML
    public void entriesWindowClose() {
        MainController.podcastEntries.refresh();
        MainController.stageEntryWindow.close();
    }

    public Label getEntryTitleColumnLabel() {
        return entryTitleColumnLabel;
    }

    public void setEntryTitleColumnLabel(Label entryTitleColumnLabel) {
        this.entryTitleColumnLabel = entryTitleColumnLabel;
    }

    public Label getEntryDescriptionColumnLabel() {
        return entryDescriptionColumnLabel;
    }

    public void setEntryDescriptionColumnLabel(Label entryDescriptionColumnLabel) {
        this.entryDescriptionColumnLabel = entryDescriptionColumnLabel;
    }

    public Label getEntryLinkColumnLabel() {
        return entryLinkColumnLabel;
    }

    public void setEntryLinkColumnLabel(Label entryLinkColumnLabel) {
        this.entryLinkColumnLabel = entryLinkColumnLabel;
    }

    public Label getEntryDateColumnLabel() {
        return entryDateColumnLabel;
    }

    public void setEntryDateColumnLabel(Label entryDateColumnLabel) {
        this.entryDateColumnLabel = entryDateColumnLabel;
    }

    public Label getEntrySummaryColumnLabel() {
        return entrySummaryColumnLabel;
    }

    public void setEntrySummaryColumnLabel(Label entrySummaryColumnLabel) {
        this.entrySummaryColumnLabel = entrySummaryColumnLabel;
    }

    public Label getEntrySubtitleColumnLabel() {
        return entrySubtitleColumnLabel;
    }

    public void setEntrySubtitleColumnLabel(Label entrySubtitleColumnLabel) {
        this.entrySubtitleColumnLabel = entrySubtitleColumnLabel;
    }

    public Label getEntryImageColumnLabel() {
        return entryImageColumnLabel;
    }

    public void setEntryImageColumnLabel(Label entryImageColumnLabel) {
        this.entryImageColumnLabel = entryImageColumnLabel;
    }

    public Label getEntryDurationColumnLabel() {
        return entryDurationColumnLabel;
    }

    public void setEntryDurationColumnLabel(Label entryDurationColumnLabel) {
        this.entryDurationColumnLabel = entryDurationColumnLabel;
    }

    public Button getAddpodcastentryButton() {
        return addpodcastentryButton;
    }

    public void setAddpodcastentryButton(Button addpodcastentryButton) {
        this.addpodcastentryButton = addpodcastentryButton;
    }

    public Button getEntriesCloseButton() {
        return entriesCloseButton;
    }

    public void setEntriesCloseButton(Button entriesCloseButton) {
        this.entriesCloseButton = entriesCloseButton;
    }

    public TextField getEntryTitleColumnField() {
        return entryTitleColumnField;
    }

    public void setEntryTitleColumnField(TextField entryTitleColumnField) {
        this.entryTitleColumnField = entryTitleColumnField;
    }

    public TextField getEntryDescriptionColumnField() {
        return entryDescriptionColumnField;
    }

    public void setEntryDescriptionColumnField(TextField entryDescriptionColumnField) {
        this.entryDescriptionColumnField = entryDescriptionColumnField;
    }

    public TextField getEntryLinkColumnField() {
        return entryLinkColumnField;
    }

    public void setEntryLinkColumnField(TextField entryLinkColumnField) {
        this.entryLinkColumnField = entryLinkColumnField;
    }

    public TextField getEntryDateColumnField() {
        return entryDateColumnField;
    }

    public void setEntryDateColumnField(TextField entryDateColumnField) {
        this.entryDateColumnField = entryDateColumnField;
    }

    public TextField getEntrySummaryColumnField() {
        return entrySummaryColumnField;
    }

    public void setEntrySummaryColumnField(TextField entrySummaryColumnField) {
        this.entrySummaryColumnField = entrySummaryColumnField;
    }

    public TextField getEntrySubtitleColumnField() {
        return entrySubtitleColumnField;
    }

    public void setEntrySubtitleColumnField(TextField entrySubtitleColumnField) {
        this.entrySubtitleColumnField = entrySubtitleColumnField;
    }

    public TextField getEntryImageColumnField() {
        return entryImageColumnField;
    }

    public void setEntryImageColumnField(TextField entryImageColumnField) {
        this.entryImageColumnField = entryImageColumnField;
    }

    public TextField getEntryDurationColumnField() {
        return entryDurationColumnField;
    }

    public void setEntryDurationColumnField(TextField entryDurationColumnField) {
        this.entryDurationColumnField = entryDurationColumnField;
    }

    public TextField getEntryFilelengthColumnField() {
        return entryFilelengthColumnField;
    }

    public void setEntryFilelengthColumnField(TextField entryFilelengthColumnField) {
        this.entryFilelengthColumnField = entryFilelengthColumnField;
    }

    public TextField getEntryTypeColumnField() {
        return entryTypeColumnField;
    }

    public void setEntryTypeColumnField(TextField entryTypeColumnField) {
        this.entryTypeColumnField = entryTypeColumnField;
    }

    public Label getEntryFilelengthColumnLabel() {
        return entryFilelengthColumnLabel;
    }

    public void setEntryFilelengthColumnLabel(Label entryFilelengthColumnLabel) {
        this.entryFilelengthColumnLabel = entryFilelengthColumnLabel;
    }

    public Label getEntryTypeColumnLabel() {
        return entryTypeColumnLabel;
    }

    public void setEntryTypeColumnLabel(Label entryTypeColumnLabel) {
        this.entryTypeColumnLabel = entryTypeColumnLabel;
    }

    public Label getEntryDateColumnDescriptionLabel() {
        return entryDateColumnDescriptionLabel;
    }

    public void setEntryDateColumnDescriptionLabel(Label entryDateColumnDescriptionLabel) {
        this.entryDateColumnDescriptionLabel = entryDateColumnDescriptionLabel;
    }
}
