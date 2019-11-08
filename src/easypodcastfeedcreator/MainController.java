/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easypodcastfeedcreator;

import easypodcastfeedcreator.model.LanguageEntry;
import easypodcastfeedcreator.model.PodcastEntry;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author a-dridi
 */
public class MainController implements Initializable {

    public static boolean createiTunesPodcastFile = true;

    public ObservableList categoryList;
    private List podcastTopTemplateLines;
    private List podcastEntriesTemplateLines;
    private List podcastFooterTemplateLines;
    //File which is used to save the edited or new created podcast feed file -  a list of lines
    private List<String> editfile;

    //The chosen language of the application interface
    public static Locale locale;
    //The texts (strings) for the selected language that will be displayed
    //through out the application
    public static ResourceBundle languageBundle;

    //Set true when a new file was created or edited
    private boolean fileEdited;

    //File used to edit existing xml file
    private File xmlfile;

    //Components for MainWindow
    @FXML
    private BorderPane mainwindowView;

    @FXML
    private ImageView logoApp;
    @FXML
    private Label welcomeText = new Label();
    @FXML
    private Button newfileButton;
    @FXML
    private Button newFileStandardButton = new Button();
    @FXML
    private Button openfileButton;
    @FXML
    private ComboBox languageSelector;
    @FXML
    private Label copyrightLabel;

    //Components for EditWindow
    @FXML
    private BorderPane editwindowView;

    @FXML
    private TextField titleField = new TextField();
    @FXML
    private TextField linkField = new TextField();
    @FXML
    private TextField descriptionField = new TextField();
    @FXML
    private ComboBox<LanguageEntry> languageList = new ComboBox<LanguageEntry>();
    //Will be used to query the language code for a selected language by the user through langueList (which saves only Language Names, but not codes)
    private List<LanguageEntry> languageCodeNameList = new ArrayList<>();

    @FXML
    private TextField subtitleField = new TextField();
    @FXML
    private TextField authorField = new TextField();
    @FXML
    private TextField summaryField = new TextField();
    @FXML
    private TextField keywordsField = new TextField();
    @FXML
    private TextField ownernameField = new TextField();
    @FXML
    private TextField owneremailField = new TextField();
    @FXML
    private ToggleGroup explicitGroup = new ToggleGroup();
    @FXML
    private RadioButton explicityesRadio = new RadioButton();
    @FXML
    private RadioButton explicitnoRadio = new RadioButton();
    @FXML
    private TextField imageField = new TextField();
    @FXML
    private ComboBox<String> categoryCombobox = new ComboBox<String>();

    @FXML
    private TableColumn entryTitleColumn = new TableColumn();
    @FXML
    private TableColumn entryDescriptionColumn = new TableColumn();
    @FXML
    private TableColumn entryLinkColumn = new TableColumn();
    @FXML
    private TableColumn entryDateColumn = new TableColumn();
    @FXML
    private TableColumn entrySummaryColumn = new TableColumn();
    @FXML
    private TableColumn entrySubtitleColumn = new TableColumn();
    @FXML
    private TableColumn entryImageColumn = new TableColumn();
    @FXML
    private TableColumn entryDurationColumn = new TableColumn();
    @FXML
    public static TableView<PodcastEntry> podcastEntries = new TableView<PodcastEntry>();

    @FXML
    private Label titleLabel;
    @FXML
    private Label linkTitle;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label languageLabel;
    @FXML
    private Label subtitleLabel = new Label();
    @FXML
    private Label authorLabel = new Label();
    @FXML
    private Label summaryLabel = new Label();
    @FXML
    private Label keywordsLabel = new Label();
    @FXML
    private Label ownernameLabel = new Label();
    @FXML
    private Label owneremailLabel = new Label();
    @FXML
    private Label explicitLabel = new Label();
    @FXML
    private Label imagesLabel = new Label();
    @FXML
    private Label categoryLabel = new Label();
    @FXML
    private Button addpodcastentryButton = new Button();
    @FXML
    private Button saveButton;
    @FXML
    private Button saveandcloseButton;
    @FXML
    private Label filenameTextview = new Label();

    public static ObservableList<PodcastEntry> podcastEntriesData = FXCollections.observableArrayList(new PodcastEntry());

    //Languages List
    public static Map<String, String> langList = new TreeMap<String, String>() {
        {
            put("en", "English");
            put("de", "Deutsch");
        }
    };

    @FXML
    private Button deletepodcastentryButton = new Button();

    public static Stage stageEntryWindow;

    /**
     * Initialize all elements (ui, etc.) for all windows (fxml files). All fxml
     * files use the controller class: MainController
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Initialize application logo and Language selector
        Image image = new Image(getClass().getResourceAsStream("easy_podcast_feed_creator_logo_wide.png"));
        try {
            this.logoApp.setImage(image);
            for (String lang : langList.values()) {
                this.languageSelector.getItems().add(lang);
            }
            //Check if user's default system language is available. If not, then use English as language
            if (langList.containsKey(Locale.getDefault().getLanguage())) {
                this.loadLanguage(Locale.getDefault().getLanguage());
                this.languageSelector.getSelectionModel().select(langList.get(Locale.getDefault().getLanguage()));
            } else {
                this.loadLanguage("en");
                this.languageSelector.getSelectionModel().select(langList.get("en"));
            }
        } catch (NullPointerException e) {
            //PodcastEntriesWindow opened or another popup window
        }

        // Read podcast categories from categories_file.txt and load categories list for EditWindow
        try (BufferedReader br = new BufferedReader(new InputStreamReader((getClass().getResourceAsStream("categories_file.txt")), "UTF-8"))) {
            this.categoryList = FXCollections.observableArrayList(br.lines().collect(Collectors.toList()));
            this.categoryCombobox.getItems().addAll(this.categoryList.sorted());
        } catch (IOException e) {
            System.out.println("Error file cannot be read. Check if file categories_file.txt exists and it is not damaged.");
            System.out.println("Error descriptions: " + e.getMessage());
        }

        //Read xml templates that will be used to create a new podcast feed or to edit a podcast feed. Top - Entries - Footer
        try (BufferedReader br = new BufferedReader(new InputStreamReader((getClass().getResourceAsStream("podcastfeed_top_template.xml")), "UTF-8"))) {
            this.podcastTopTemplateLines = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Error file cannot be read. Check if file podcastfeed_top_template.xml exists and it is not damaged.");
            System.out.println("Error descriptions: " + e.getMessage());
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader((getClass().getResourceAsStream("podcastfeed_entries_template.xml")), "UTF-8"))) {
            this.podcastEntriesTemplateLines = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Error file cannot be read. Check if file podcastfeed_entries_template.xml exists and it is not damaged.");
            System.out.println("Error descriptions: " + e.getMessage());
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader((getClass().getResourceAsStream("podcastfeed_footer_template.xml")), "UTF-8"))) {
            this.podcastFooterTemplateLines = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Error file cannot be read. Check if file podcastfeed_footer_template.xml exists and it is not damaged.");
            System.out.println("Error descriptions: " + e.getMessage());
        }

        //Load Language list
        try (BufferedReader br = new BufferedReader(new InputStreamReader((getClass().getResourceAsStream("language_list.csv")), "UTF-8"))) {
            List<String> languageListFile = br.lines().collect(Collectors.toList());
            //Skip header
            languageListFile.remove(0);
            for (String line : languageListFile) {
                String[] lineArray = line.split(",");
                this.languageCodeNameList.add(new LanguageEntry(lineArray[0], lineArray[lineArray.length - 1]));
                this.languageList.getItems().add(new LanguageEntry(lineArray[0], lineArray[lineArray.length - 1]));
            }

        } catch (IOException e) {
            System.out.println("Error file cannot be read. Check if file language_list.csv exists and it is not damaged.");
            System.out.println("Error descriptions: " + e.getMessage());
        }

        //Initialize ui elemts for EditWindow and PodcastEntriesWindow
        try {
            this.filenameTextview.setText(this.xmlfile.getAbsolutePath());
        } catch (NullPointerException e) {
            //New file is created
            this.filenameTextview.setText(MainController.languageBundle.getString("newFileEditorTxt"));
        }

    }

    /**
     * Create new Podcast feed file and save what the user did enter on the ui.
     *
     * @param event
     */
    @FXML
    public void handleSaveFile(ActionEvent event) {
        if (this.titleField.getText() == null || this.titleField.getText().equals("")) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(MainController.languageBundle.getString("errorwritetitelTxt"));
            alert.show();
        } else if (this.languageList.getValue() == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(MainController.languageBundle.getString("errorselectlanguageTxt"));
            alert.show();

        } else if (this.categoryCombobox.getValue() == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(MainController.languageBundle.getString("errorselectcategoryTxt"));
            alert.show();
        } else {
            //Save file

            //Show file selection prompt if user did not select a file name.
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Podcast file (*.xml)", "*.xml");
            fc.getExtensionFilters().add(extFilter);
            fc.setSelectedExtensionFilter(new ExtensionFilter("XML File", "*.xml"));
            fc.setTitle(MainController.languageBundle.getString("savefileTxt"));
            try {
                this.xmlfile = fc.showSaveDialog((Stage) this.mainwindowView.getScene().getWindow());
            } catch (NullPointerException e) {
                //EditWindow is opened
                this.xmlfile = fc.showSaveDialog((Stage) this.editwindowView.getScene().getWindow());
            }
            if (this.createiTunesPodcastFile) {
                writeItunesFile();
            } else {
                //Create normal podcast file
                writeStandardFile();
            }
        }
    }

    /**
     * Create new Podcast feed file and save what the user did enter on the ui.
     * Then close the application
     *
     * @param event
     */
    @FXML
    public void handleSaveCloseFile(ActionEvent event) {

        if (this.titleField.getText() == null || this.titleField.getText().equals("")) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(MainController.languageBundle.getString("errorwritetitelTxt"));
            alert.show();
        } else if (this.createiTunesPodcastFile && this.languageList.getValue() == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(MainController.languageBundle.getString("errorselectlanguageTxt"));
            alert.show();
        } else if (this.createiTunesPodcastFile && this.categoryCombobox.getValue() == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(MainController.languageBundle.getString("errorselectcategoryTxt"));
            alert.show();
        } else {
            //Save file
            if (this.xmlfile == null) {
                //Show file selection prompt if user did not select a file name.
                FileChooser fc = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Podcast file (*.xml)", "*.xml");
                fc.getExtensionFilters().add(extFilter);
                fc.setSelectedExtensionFilter(new ExtensionFilter("XML File", "*.xml"));
                fc.setTitle(MainController.languageBundle.getString("savefileTxt"));
                try {
                    this.xmlfile = fc.showSaveDialog((Stage) this.mainwindowView.getScene().getWindow());
                } catch (NullPointerException e) {
                    //EditWindow is opened
                    this.xmlfile = fc.showSaveDialog((Stage) this.editwindowView.getScene().getWindow());
                }
            }
            if (this.createiTunesPodcastFile) {
                writeItunesFile();
            } else {
                //Create normal podcast file
                writeStandardFile();
            }

            Stage openedstage;
            try {
                openedstage = (Stage) this.mainwindowView.getScene().getWindow();
                this.fileEdited = true;
            } catch (NullPointerException e) {
                openedstage = (Stage) this.editwindowView.getScene().getWindow();
            }
            openedstage.close();
        }
    }

    /**
     * Create new podcast feed file. New window (EditWindow) will be opened
     * instead of this window and the user can edit the new podcast feed
     * application and save it where he wants.
     *
     * @param event
     */
    @FXML
    public void handleNewFileButton(ActionEvent event) throws IOException {
        this.createiTunesPodcastFile = true;
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("EditWindow.fxml"));
        Parent root = (Parent) fxmlloader.load();
        //Close Mainwindow and open editwindow which will be used to create new podcast feed
        Stage openedstage;
        try {
            openedstage = (Stage) this.mainwindowView.getScene().getWindow();
            this.fileEdited = true;
        } catch (NullPointerException e) {
            openedstage = (Stage) this.editwindowView.getScene().getWindow();
        }
        openedstage.close();

        this.entryTitleColumn = new TableColumn(MainController.languageBundle.getString("entryTitleColumnTxt"));
        this.entryTitleColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("title"));
        this.entryDateColumn = new TableColumn(MainController.languageBundle.getString("entryDateColumnTxt"));
        this.entryDateColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("publishDate"));
        this.entryDescriptionColumn = new TableColumn(MainController.languageBundle.getString("entryDescriptionColumnTxt"));
        this.entryDescriptionColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("description"));
        this.entryDurationColumn = new TableColumn(MainController.languageBundle.getString("entryDurationColumnTxt"));
        this.entryDurationColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("duration"));
        this.entryImageColumn = new TableColumn(MainController.languageBundle.getString("entryImageColumnTxt"));
        this.entryImageColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("url"));
        this.entryLinkColumn = new TableColumn(MainController.languageBundle.getString("entryLinkColumnTxt"));
        this.entryLinkColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("link"));
        this.entrySubtitleColumn = new TableColumn(MainController.languageBundle.getString("entrySubtitleColumnTxt"));
        this.entrySubtitleColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("subtitle"));
        this.entrySummaryColumn = new TableColumn(MainController.languageBundle.getString("entrySummaryColumnTxt"));
        this.entrySummaryColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("summary"));
        this.podcastEntries.setEditable(true);
        TableColumn deleteCol = new TableColumn(MainController.languageBundle.getString("deleteButtonTxt"));

        //Add column that has the delete button for every row to delete the row that its delete button was clicked.
        deleteCol.setCellValueFactory(new PropertyValueFactory<>("deletebutton"));
        Callback<TableColumn<PodcastEntry, String>, TableCell<PodcastEntry, String>> cellFactory = new Callback<TableColumn<PodcastEntry, String>, TableCell<PodcastEntry, String>>() {
            @Override
            public TableCell<PodcastEntry, String> call(TableColumn<PodcastEntry, String> param) {
                final TableCell<PodcastEntry, String> cell = new TableCell<PodcastEntry, String>() {
                    final Button btn = new Button(MainController.languageBundle.getString("deleteButtonTxt"));

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            this.setGraphic(null);
                            this.setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                //Selected row/PodcastEntry entry of table
                                PodcastEntry podcastentry = this.getTableView().getItems().get(this.getIndex());
                                System.out.println("DEBUG: selected value: " + podcastentry.getDuration());
                            });
                            this.setGraphic(btn);
                            this.setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        deleteCol.setCellFactory(cellFactory);
        MainController.podcastEntries.setItems(MainController.podcastEntriesData);
        MainController.podcastEntries.getColumns().addAll(entryTitleColumn, entryDateColumn, entryDescriptionColumn, entryDurationColumn, entryImageColumn, entryLinkColumn, entrySubtitleColumn, entrySummaryColumn, deleteCol);

        MainController.podcastEntries.setPlaceholder(new Label(MainController.languageBundle.getString("tableNoPodcastEntriesTxt")));
        MainController.podcastEntries.refresh();

        //Load edit window
        Stage stage = new Stage();
        stage.setTitle("Easy Podcast Feed Creator");
        stage.setScene(new Scene(root));
        Image icon = new Image(getClass().getResourceAsStream("application_icon.png"));
        stage.getIcons().add(icon);
        stage.show();

    }

    @FXML
    public void handleNewStandardFileButton(ActionEvent event) throws IOException {
        this.createiTunesPodcastFile = false;
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("EditWindow.fxml"));
        Parent root = (Parent) fxmlloader.load();
        //Close Mainwindow and open editwindow which will be used to create new podcast feed
        Stage openedstage;
        try {
            openedstage = (Stage) this.mainwindowView.getScene().getWindow();
            this.fileEdited = true;
        } catch (NullPointerException e) {
            openedstage = (Stage) this.editwindowView.getScene().getWindow();
        }
        openedstage.close();

        this.subtitleField.setEditable(false);
        this.subtitleField.setVisible(false);
        this.subtitleField.setEditable(false);
        this.subtitleLabel.setVisible(false);
        this.authorField.setVisible(false);
        this.authorLabel.setVisible(false);
        this.summaryField.setVisible(false);
        this.summaryLabel.setVisible(false);
        this.keywordsField.setVisible(false);
        this.keywordsLabel.setVisible(false);
        this.ownernameField.setVisible(false);
        this.ownernameLabel.setVisible(false);
        this.owneremailField.setVisible(false);
        this.owneremailLabel.setVisible(false);
        this.explicityesRadio.setVisible(false);
        this.explicitnoRadio.setVisible(false);
        this.explicitLabel.setVisible(false);
        this.categoryCombobox.setVisible(false);
        this.categoryLabel.setVisible(false);
        this.imageField.setVisible(false);
        this.imagesLabel.setVisible(false);

        this.subtitleLabel.setText("-");
        this.authorLabel.setText("-");
        this.summaryLabel.setText("-");
        this.keywordsLabel.setText("-");
        this.ownernameLabel.setText("-");
        this.owneremailLabel.setText("-");
        this.explicitLabel.setText("-");
        this.explicityesRadio.setText("-");
        this.explicitnoRadio.setText("-");
        this.imagesLabel.setText("-");
        this.categoryLabel.setText("-");

        //Load edit window
        Stage stage = new Stage();
        stage.setTitle("Easy Podcast Feed Creator");
        stage.setScene(new Scene(root));
        Image icon = new Image(getClass().getResourceAsStream("application_icon.png"));
        stage.getIcons().add(icon);
        stage.show();

        this.entryTitleColumn = new TableColumn(MainController.languageBundle.getString("entryTitleColumnTxt"));
        this.entryTitleColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("title"));
        this.entryDateColumn = new TableColumn(MainController.languageBundle.getString("entryDateColumnTxt"));
        this.entryDateColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("publishDate"));
        this.entryDescriptionColumn = new TableColumn(MainController.languageBundle.getString("entryDescriptionColumnTxt"));
        this.entryDescriptionColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("description"));
        this.entryDurationColumn = new TableColumn(MainController.languageBundle.getString("entryDurationColumnTxt"));
        this.entryDurationColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("duration"));
        this.entryImageColumn = new TableColumn(MainController.languageBundle.getString("entryImageColumnTxt"));
        this.entryImageColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("url"));
        this.entryLinkColumn = new TableColumn(MainController.languageBundle.getString("entryLinkColumnTxt"));
        this.entryLinkColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("link"));
        this.entrySubtitleColumn = new TableColumn(MainController.languageBundle.getString("entrySubtitleColumnTxt"));
        this.entrySubtitleColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("subtitle"));
        this.entrySummaryColumn = new TableColumn(MainController.languageBundle.getString("entrySummaryColumnTxt"));
        this.entrySummaryColumn.setCellValueFactory(new PropertyValueFactory<PodcastEntry, Object>("summary"));
        this.podcastEntries.setEditable(true);
        TableColumn deleteCol = new TableColumn(MainController.languageBundle.getString("deleteButtonTxt"));

        //Add column that has the delete button for every row to delete the row that its delete button was clicked.
        deleteCol.setCellValueFactory(new PropertyValueFactory<>("deletebutton"));
        Callback<TableColumn<PodcastEntry, String>, TableCell<PodcastEntry, String>> cellFactory = new Callback<TableColumn<PodcastEntry, String>, TableCell<PodcastEntry, String>>() {
            @Override
            public TableCell<PodcastEntry, String> call(TableColumn<PodcastEntry, String> param) {
                final TableCell<PodcastEntry, String> cell = new TableCell<PodcastEntry, String>() {
                    final Button btn = new Button(MainController.languageBundle.getString("deleteButtonTxt"));

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            this.setGraphic(null);
                            this.setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                //Selected row/PodcastEntry entry of table
                                PodcastEntry podcastentry = this.getTableView().getItems().get(this.getIndex());
                                System.out.println("DEBUG: selected value: " + podcastentry.getDuration());
                            });
                            this.setGraphic(btn);
                            this.setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        deleteCol.setCellFactory(cellFactory);
        MainController.podcastEntries.setItems(MainController.podcastEntriesData);
        MainController.podcastEntries.getColumns().addAll(entryTitleColumn, entryDateColumn, entryDescriptionColumn, entryDurationColumn, entryImageColumn, entryLinkColumn, entrySubtitleColumn, entrySummaryColumn, deleteCol);

        MainController.podcastEntries.setPlaceholder(new Label(MainController.languageBundle.getString("tableNoPodcastEntriesTxt")));

    }

    /**
     * Open a file chooser dialog. If user selected a functional (must be an
     * itunes podcast xml file) file, then the edit window will be opened. There
     * the user can edit the xml file and save it.
     *
     * @param event
     */
    @FXML
    public void handleOpenFileButton(ActionEvent event) throws IOException {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Podcast file (*.xml)", "*.xml");
        fc.getExtensionFilters().add(extFilter);
        fc.setSelectedExtensionFilter(new ExtensionFilter("XML File", "*.xml"));
        fc.setTitle(MainController.languageBundle.getString("openfileTxt"));
        try {
            this.xmlfile = fc.showOpenDialog((Stage) this.mainwindowView.getScene().getWindow());
        } catch (NullPointerException e) {
            //EditWindow is opened
            this.xmlfile = fc.showOpenDialog((Stage) this.editwindowView.getScene().getWindow());
        }
        try {
            if (!(this.xmlfile.getPath()).contains("xml")) {
                //No xml file selected
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText(MainController.languageBundle.getString("errorfiletypeTxt"));
                alert.show();
            } else {

                //Read opened file
                try (BufferedReader br = Files.newBufferedReader(Paths.get(this.xmlfile.getAbsolutePath()))) {
                    this.editfile = br.lines().collect(Collectors.toList());
                    if (this.editfile.size() > 0 && (this.editfile.get(1)).equals("<rss xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\" version=\"2.0\">")) {
                        //Correct type of file selected
                        this.filenameTextview.setText(this.xmlfile.getAbsolutePath());
                        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("EditWindow.fxml"));
                        Parent root = (Parent) fxmlloader.load();
                        //Close Mainwindow and open editwindow which will be used to create new podcast feed
                        Stage openedstage = (Stage) this.mainwindowView.getScene().getWindow();
                        openedstage.close();
                        //Load edit window
                        Stage stage = new Stage();
                        stage.setTitle("Easy Podcast Feed Creator");
                        stage.setScene(new Scene(root));
                        Image icon = new Image(getClass().getResourceAsStream("application_icon.png"));
                        stage.getIcons().add(icon);
                        stage.show();
                        loadPodcastFile();

                    } else {
                        //xml file is not a podcast feed
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setContentText(MainController.languageBundle.getString("errorfilexmlTxt"));
                        alert.show();
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText(MainController.languageBundle.getString("errorfilecorruptTxt"));
                    Optional<ButtonType> result = alert.showAndWait();
                    System.out.println("" + e.getMessage());
                    //Check if MainWindow is opened and reopen it if it was closed
                    try {
                        //Show MainWindow if user did close error dialog.
                        if (result.get() == ButtonType.OK) {
                            Stage openedstage = (Stage) this.mainwindowView.getScene().getWindow();
                            openedstage.show();
                        }
                    } catch (Exception ex) {
                        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
                        Parent root = (Parent) fxmlloader.load();
                        //Load main window
                        Stage stage = new Stage();
                        stage.setTitle("Easy Podcast Feed Creator");
                        stage.setScene(new Scene(root));
                        Image icon = new Image(getClass().getResourceAsStream("application_icon.png"));
                        stage.getIcons().add(icon);
                        stage.show();

                    }

                }

            }
        } catch (NullPointerException e) {
            //User did not select a file to open
        }
    }

    /**
     * Open Podcast Entries Editor Window
     */
    @FXML
    private void openPodcastEntriesWindow() throws IOException {

        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("PodcastEntriesWindow.fxml"));
        Parent root = (Parent) fxmlloader.load();
        //Load Podcast Entries Editor window
        stageEntryWindow = new Stage();
        stageEntryWindow.setTitle(MainController.languageBundle.getString("headerPodcastEntriesTxt"));
        stageEntryWindow.setScene(new Scene(root));
        Image icon = new Image(getClass().getResourceAsStream("application_icon.png"));
        stageEntryWindow.getIcons().add(icon);
        stageEntryWindow.show();

    }

    /**
     *
     */
    @FXML
    public void entriesWindowClose() {

        this.podcastEntries.refresh();
    }

    /**
     * Creates an itunes file from saved elements in this class MainController
     *
     * @param absolutepath
     */
    public void writeItunesFile() {

        //Create list array with lines that will be written into the podcast feed file
        this.editfile = new ArrayList<>();
        //Podcast Description
        String line = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        this.editfile.add(line);
        line = "<rss xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\" version=\"2.0\">";
        this.editfile.add(line);
        line = " <channel>";
        this.editfile.add(line);
        line = "  <title>";
        line += this.titleField.getText();
        line += "</title>";
        this.editfile.add(line);
        line = "  <link>";
        line += this.linkField.getText();
        line += "</link>";
        this.editfile.add(line);
        line = "  <description>";
        line += this.descriptionField.getText();
        line += "</description>";
        this.editfile.add(line);
        line = "  <language>";
        line += this.languageList.getValue().getLanguageCode();
        line += "</language>";
        this.editfile.add(line);
        line = "  <itunes:subtitle>";
        line += this.subtitleField.getText();
        line += "</itunes:subtitle>";
        this.editfile.add(line);
        line = "  <itunes:author>";
        line += this.authorField.getText();
        line += "</itunes:author>";
        this.editfile.add(line);
        line = "  <itunes:summary>";
        line += this.summaryField.getText();
        line += "</itunes:summary>";
        this.editfile.add(line);
        line = "  <itunes:keywords>";
        line += this.keywordsField.getText();
        line += "</itunes:keywords>";
        this.editfile.add(line);
        line = "  <itunes:owner>";
        this.editfile.add(line);
        line = "   <itunes:name>";
        line += this.ownernameField.getText();
        line += "</itunes:name>";
        this.editfile.add(line);
        line = "   <itunes:email>";
        line += this.owneremailField.getText();
        line += "</itunes:email>";
        this.editfile.add(line);
        line = "  </itunes:owner>";
        this.editfile.add(line);
        line = "  <itunes:explicit>";
        if ((((RadioButton) this.explicitGroup.getSelectedToggle()).getText()).equals(MainController.languageBundle.getString("explicityesRadioTxt"))) {
            line += "Yes";
        } else {
            line += "No";
        }
        line += "</itunes:explicit>";
        this.editfile.add(line);
        line = "  <itunes:image href=\"";
        line += this.imageField.getText();
        line += "\"/>";
        this.editfile.add(line);
        line = "  <itunes:category>";
        line += this.categoryCombobox.getValue();
        line += "</itunes:category>";
        this.editfile.add(line);

        //Podcast Entries
        for (PodcastEntry pe : podcastEntriesData) {
            if (pe.getTitle() != null && !pe.getTitle().equals("")) {
                line = "  <item>";
                this.editfile.add(line);

                line = "   <title>";
                line += pe.getTitle();
                line += "</title>";
                this.editfile.add(line);
                line = "   <description>";
                line += pe.getDescription();
                line += "</description>";
                this.editfile.add(line);
                line = "   <link>";
                line += pe.getLink();
                line += "</link>";
                this.editfile.add(line);
                line = "   <enclosure url=\"";
                line += pe.getUrl();
                line += "\" length=\"";
                line += pe.getLength();
                line += "\" type=\"";
                line += pe.getType();
                line += "\"/>";
                this.editfile.add(line);
                line = "   <pubDate>";
                line += pe.getPublishDate();
                line += "</pubDate>";
                this.editfile.add(line);
                line = "   <itunes:summary>";
                line += pe.getSummary();
                line += "</itunes:summary>";
                this.editfile.add(line);
                line = "   <itunes:subtitle>";
                line += pe.getSubtitle();
                line += "</itunes:subtitle>";
                this.editfile.add(line);
                line = "   <itunes:image href=\"";
                line += pe.getUrl();
                line += "\"/>";
                this.editfile.add(line);
                line = "   <itunes:duration>";
                line += pe.getDuration();
                line += "</itunes:duration>";
                this.editfile.add(line);
                line = "   <guid>";
                line += pe.getLink();
                line += "</guid>";

                line = "  </item>";
                this.editfile.add(line);
            }
        }

        line = " </channel>";
        this.editfile.add(line);
        line = "</rss>";
        this.editfile.add(line);

        try {
            if (!(this.xmlfile.getPath()).contains("xml")) {
                //No xml file selected
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText(MainController.languageBundle.getString("errorfiletypeTxt"));
                alert.show();
            } else {

                //Write podcast file
                try (Writer wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.xmlfile.getAbsolutePath()), "utf-8"))) {

                    for (String fileLine : this.editfile) {
                        wr.write(fileLine + "\n");
                    }
                } catch (IOException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText(MainController.languageBundle.getString("errorfilesavingTxt"));
                    alert.show();

                }
                this.filenameTextview.setText(this.xmlfile.getAbsolutePath());
            }
        } catch (NullPointerException e) {
            //User did not select a file to open
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(MainController.languageBundle.getString("errorfilesavingTxt"));
            alert.show();
        }
    }

    public void writeStandardFile() {
        //Create list array with lines that will be written into the podcast feed file
        this.editfile = new ArrayList<>();
        //Podcast Description
        String line = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        this.editfile.add(line);
        line = "<rss version=\"2.0\">";
        this.editfile.add(line);
        line = " <channel>";
        this.editfile.add(line);
        line = "  <title>";
        line += this.titleField.getText();
        line += "</title>";
        this.editfile.add(line);
        line = "  <link>";
        line += this.linkField.getText();
        line += "</link>";
        this.editfile.add(line);
        line = "  <description>";
        line += this.descriptionField.getText();
        line += "</description>";
        this.editfile.add(line);
        line = "  <language>";
        line += this.languageList.getValue().getLanguageCode();
        line += "</language>";
        this.editfile.add(line);

        //Podcast Entries
        for (PodcastEntry pe : podcastEntriesData) {
            if (pe.getTitle() != null && !pe.getTitle().equals("")) {
                line = "  <item>";
                this.editfile.add(line);

                line = "   <title>";
                line += pe.getTitle();
                line += "</title>";
                this.editfile.add(line);
                line = "   <description>";
                line += pe.getDescription();
                line += "</description>";
                this.editfile.add(line);
                line = "   <link>";
                line += pe.getLink();
                line += "</link>";
                this.editfile.add(line);
                line = "   <enclosure url=\"";
                line += pe.getUrl();
                line += "\" length=\"";
                line += pe.getLength();
                line += "\" type=\"";
                line += pe.getType();
                line += "\"/>";
                this.editfile.add(line);
                line = "   <pubDate>";
                line += pe.getPublishDate();
                line += "</pubDate>";
                this.editfile.add(line);

                line = "  </item>";
                this.editfile.add(line);
            }
        }

        line = " </channel>";
        this.editfile.add(line);
        line = "</rss>";
        this.editfile.add(line);

        try {
            if (!(this.xmlfile.getPath()).contains("xml")) {
                //No xml file selected
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText(MainController.languageBundle.getString("errorfiletypeTxt"));
                alert.show();
            } else {

                //Write podcast file
                try (Writer wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.xmlfile.getAbsolutePath()), "utf-8"))) {

                    for (String fileLine : this.editfile) {
                        wr.write(fileLine + "\n");
                    }
                } catch (IOException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText(MainController.languageBundle.getString("errorfilesavingTxt"));
                    alert.show();

                }
                this.filenameTextview.setText(this.xmlfile.getAbsolutePath());
            }

        } catch (NullPointerException e) {
            //User did not select a file to open
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText(MainController.languageBundle.getString("errorfilesavingTxt"));
            alert.show();
        }

    }

    /**
     * Load data from podcast file. Belongs to Open File method
     */
    public void loadPodcastFile() {
        this.fileEdited = true;
        this.filenameTextview.setText(this.xmlfile.getAbsolutePath());
        this.titleField.setText((String) this.editfile.get(1));
    }

    /**
     * MainWindow: Loads the language and the text (all buttons, labels, etc.)
     * for the given lagnuage code. If language code is empty, then English
     * language code will be chosen. If EditWindow is loaded, then
     * loadlanguageEditwindow will be started.
     *
     * @param languageCode Language code in this format e.g. en
     */
    public void loadLanguage(String languageCode) {

        if (languageCode != null && languageCode != "") {
            try {
                locale = new Locale(languageCode);
                MainController.languageBundle = ResourceBundle.getBundle("easypodcastfeedcreator.languages.lang", locale);
                //set text of buttons and labels
                this.newfileButton.setText(MainController.languageBundle.getString("newfileBtn"));
                this.newFileStandardButton.setText(MainController.languageBundle.getString("newfilestandardBtn"));
                this.welcomeText.setText(MainController.languageBundle.getString("welcomeTxt"));
                this.copyrightLabel.setText(MainController.languageBundle.getString("copyrightTxt"));

            } catch (NullPointerException e) {
                //EditWindow is opened by user (User clicked on New File or Open File Button)
                //Load EditWindow interface
                this.loadLanguageEditWindow(locale.getLanguage());
            }
        } else {
            loadLanguage("en");
        }

    }

    /**
     * EditWindow: Loads the language and the text (all buttons, labels, etc.)
     * for EditWindow.
     *
     */
    public void loadLanguageEditWindow(String languageCode) {
        if (languageCode != null && languageCode != "") {
            locale = new Locale(languageCode);
            MainController.languageBundle = ResourceBundle.getBundle("easypodcastfeedcreator.languages.lang", locale);
            //set text of buttons and labels
            this.newfileButton.setText(MainController.languageBundle.getString("newfileBtn"));
            this.newFileStandardButton.setText(MainController.languageBundle.getString("newfilestandardBtn"));
//            this.openfileButton.setText(MainController.languageBundle.getString("openfileBtn"));
            this.titleLabel.setText(MainController.languageBundle.getString("titleLabelTxt"));
            this.linkTitle.setText(MainController.languageBundle.getString("linkTitleTxt"));
            this.descriptionLabel.setText(MainController.languageBundle.getString("descriptionLabelTxt"));
            this.languageLabel.setText(MainController.languageBundle.getString("languageLabelTxt"));
            this.subtitleLabel.setText(MainController.languageBundle.getString("subtitleLabelTxt"));
            this.authorLabel.setText(MainController.languageBundle.getString("authorLabelTxt"));
            this.summaryLabel.setText(MainController.languageBundle.getString("summaryLabelTxt"));
            this.keywordsLabel.setText(MainController.languageBundle.getString("keywordsLabelTxt"));
            this.ownernameLabel.setText(MainController.languageBundle.getString("ownernameLabelTxt"));
            this.owneremailLabel.setText(MainController.languageBundle.getString("owneremailLabelTxt"));
            this.explicitLabel.setText(MainController.languageBundle.getString("explicitLabelTxt"));
            this.explicityesRadio.setText(MainController.languageBundle.getString("explicityesRadioTxt"));
            this.explicitnoRadio.setText(MainController.languageBundle.getString("explicitnoRadioTxt"));
            this.imagesLabel.setText(MainController.languageBundle.getString("imagesLabelTxt"));
            this.categoryLabel.setText(MainController.languageBundle.getString("categoryLabelTxt"));
            this.entryTitleColumn.setText(MainController.languageBundle.getString("entryTitleColumnTxt"));
            this.entryDescriptionColumn.setText(MainController.languageBundle.getString("entryDescriptionColumnTxt"));
            this.entryLinkColumn.setText(MainController.languageBundle.getString("entryLinkColumnTxt"));
            this.entryDateColumn.setText(MainController.languageBundle.getString("entryDateColumnTxt"));
            this.entrySummaryColumn.setText(MainController.languageBundle.getString("entrySummaryColumnTxt"));
            this.entrySubtitleColumn.setText(MainController.languageBundle.getString("entrySubtitleColumnTxt"));
            this.entryImageColumn.setText(MainController.languageBundle.getString("entryImageColumnTxt"));
            this.entryDurationColumn.setText(MainController.languageBundle.getString("entryDurationColumnTxt"));
            this.saveButton.setText(MainController.languageBundle.getString("saveButtonTxt"));
            this.saveandcloseButton.setText(MainController.languageBundle.getString("saveandcloseButtonTxt"));
            this.addpodcastentryButton.setText(MainController.languageBundle.getString("addpodcastentryButtonTxt"));

        } else {
            loadLanguageEditWindow("en");

        }
    }

    /**
     * Load the language that the user chose trhough the combobox
     *
     * @param event
     */
    @FXML
    public void handleLanguageChanged(ActionEvent event) {
        this.loadLanguage(this.getKeyOfValue(this.langList, this.languageSelector.getValue().toString()));
    }

    /**
     * Returns null if value is not available.
     *
     * @param map
     * @param value
     * @return
     */
    public String getKeyOfValue(Map map, String value) {
        for (Object key : map.keySet()) {
            if ((map.get(key)).equals(value)) {
                return (String) key;
            }
        }
        return null;
    }

    public ObservableList getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(ObservableList categoryList) {
        this.categoryList = categoryList;
    }

    public List getPodcastTopTemplateLines() {
        return podcastTopTemplateLines;
    }

    public void setPodcastTopTemplateLines(List podcastTopTemplateLines) {
        this.podcastTopTemplateLines = podcastTopTemplateLines;
    }

    public List getPodcastEntriesTemplateLines() {
        return podcastEntriesTemplateLines;
    }

    public void setPodcastEntriesTemplateLines(List podcastEntriesTemplateLines) {
        this.podcastEntriesTemplateLines = podcastEntriesTemplateLines;
    }

    public List getPodcastFooterTemplateLines() {
        return podcastFooterTemplateLines;
    }

    public void setPodcastFooterTemplateLines(List podcastFooterTemplateLines) {
        this.podcastFooterTemplateLines = podcastFooterTemplateLines;
    }

    public boolean isFileEdited() {
        return fileEdited;
    }

    public void setFileEdited(boolean fileEdited) {
        this.fileEdited = fileEdited;
    }

    public Button getNewfileButton() {
        return newfileButton;
    }

    public void setNewfileButton(Button newfileButton) {
        this.newfileButton = newfileButton;
    }

    public Button getOpenfileButton() {
        return openfileButton;
    }

    public void setOpenfileButton(Button openfileButton) {
        this.openfileButton = openfileButton;
    }

    public Label getWelcomeText() {
        return welcomeText;
    }

    public void setWelcomeText(Label welcomeText) {
        this.welcomeText = welcomeText;
    }

    public ComboBox getLanguageSelector() {
        return languageSelector;
    }

    public void setLanguageSelector(ComboBox languageSelector) {
        this.languageSelector = languageSelector;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public ResourceBundle getLanguageBundle() {
        return languageBundle;
    }

    public void setLanguageBundle(ResourceBundle languageBundle) {
        MainController.languageBundle = languageBundle;
    }

    public Map<String, String> getLangList() {
        return langList;
    }

    public void setLangList(Map<String, String> langList) {
        this.langList = langList;
    }

    public ImageView getLogoApp() {
        return logoApp;
    }

    public void setLogoApp(ImageView logoApp) {
        this.logoApp = logoApp;
    }

    public BorderPane getMainwindowView() {
        return mainwindowView;
    }

    public void setMainwindowView(BorderPane mainwindowView) {
        this.mainwindowView = mainwindowView;
    }

    public BorderPane getEditwindowView() {
        return editwindowView;
    }

    public void setEditwindowView(BorderPane editwindowView) {
        this.editwindowView = editwindowView;
    }

    public TextField getTitleField() {
        return titleField;
    }

    public void setTitleField(TextField titleField) {
        this.titleField = titleField;
    }

    public TextField getDescriptionField() {
        return descriptionField;
    }

    public void setDescriptionField(TextField descriptionField) {
        this.descriptionField = descriptionField;
    }

    public TextField getSubtitleField() {
        return subtitleField;
    }

    public void setSubtitleField(TextField subtitleField) {
        this.subtitleField = subtitleField;
    }

    public TextField getAuthorField() {
        return authorField;
    }

    public void setAuthorField(TextField authorField) {
        this.authorField = authorField;
    }

    public TextField getSummaryField() {
        return summaryField;
    }

    public void setSummaryField(TextField summaryField) {
        this.summaryField = summaryField;
    }

    public TextField getKeywordsField() {
        return keywordsField;
    }

    public void setKeywordsField(TextField keywordsField) {
        this.keywordsField = keywordsField;
    }

    public TextField getOwnernameField() {
        return ownernameField;
    }

    public void setOwnernameField(TextField ownernameField) {
        this.ownernameField = ownernameField;
    }

    public TextField getOwneremailField() {
        return owneremailField;
    }

    public void setOwneremailField(TextField owneremailField) {
        this.owneremailField = owneremailField;
    }

    public ToggleGroup getExplicitGroup() {
        return explicitGroup;
    }

    public void setExplicitGroup(ToggleGroup explicitGroup) {
        this.explicitGroup = explicitGroup;
    }

    public RadioButton getExplicityesRadio() {
        return explicityesRadio;
    }

    public void setExplicityesRadio(RadioButton explicityesRadio) {
        this.explicityesRadio = explicityesRadio;
    }

    public RadioButton getExplicitnoRadio() {
        return explicitnoRadio;
    }

    public void setExplicitnoRadio(RadioButton explicitnoRadio) {
        this.explicitnoRadio = explicitnoRadio;
    }

    public TextField getImageField() {
        return imageField;
    }

    public void setImageField(TextField imageField) {
        this.imageField = imageField;
    }

    public ComboBox getCategoryCombobox() {
        return categoryCombobox;
    }

    public void setCategoryCombobox(ComboBox categoryCombobox) {
        this.categoryCombobox = categoryCombobox;
    }

    public TableColumn getEntryTitleColumn() {
        return entryTitleColumn;
    }

    public void setEntryTitleColumn(TableColumn entryTitleColumn) {
        this.entryTitleColumn = entryTitleColumn;
    }

    public TableColumn getEntryDescriptionColumn() {
        return entryDescriptionColumn;
    }

    public void setEntryDescriptionColumn(TableColumn entryDescriptionColumn) {
        this.entryDescriptionColumn = entryDescriptionColumn;
    }

    public TableColumn getEntryLinkColumn() {
        return entryLinkColumn;
    }

    public void setEntryLinkColumn(TableColumn entryLinkColumn) {
        this.entryLinkColumn = entryLinkColumn;
    }

    public TableColumn getEntryDateColumn() {
        return entryDateColumn;
    }

    public void setEntryDateColumn(TableColumn entryDateColumn) {
        this.entryDateColumn = entryDateColumn;
    }

    public TableColumn getEntrySummaryColumn() {
        return entrySummaryColumn;
    }

    public void setEntrySummaryColumn(TableColumn entrySummaryColumn) {
        this.entrySummaryColumn = entrySummaryColumn;
    }

    public TableColumn getEntrySubtitleColumn() {
        return entrySubtitleColumn;
    }

    public void setEntrySubtitleColumn(TableColumn entrySubtitleColumn) {
        this.entrySubtitleColumn = entrySubtitleColumn;
    }

    public TableColumn getEntryImageColumn() {
        return entryImageColumn;
    }

    public void setEntryImageColumn(TableColumn entryImageColumn) {
        this.entryImageColumn = entryImageColumn;
    }

    public TableColumn getEntryDurationColumn() {
        return entryDurationColumn;
    }

    public void setEntryDurationColumn(TableColumn entryDurationColumn) {
        this.entryDurationColumn = entryDurationColumn;
    }

    public Label getCopyrightLabel() {
        return copyrightLabel;
    }

    public void setCopyrightLabel(Label copyrightLabel) {
        this.copyrightLabel = copyrightLabel;
    }

    public List getEditfile() {
        return editfile;
    }

    public void setEditfile(List editfile) {
        this.editfile = editfile;
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public void setTitleLabel(Label titleLabel) {
        this.titleLabel = titleLabel;
    }

    public Label getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(Label linkTitle) {
        this.linkTitle = linkTitle;
    }

    public Label getDescriptionLabel() {
        return descriptionLabel;
    }

    public void setDescriptionLabel(Label descriptionLabel) {
        this.descriptionLabel = descriptionLabel;
    }

    public Label getLanguageLabel() {
        return languageLabel;
    }

    public void setLanguageLabel(Label languageLabel) {
        this.languageLabel = languageLabel;
    }

    public Label getSubtitleLabel() {
        return subtitleLabel;
    }

    public void setSubtitleLabel(Label subtitleLabel) {
        this.subtitleLabel = subtitleLabel;
    }

    public Label getAuthorLabel() {
        return authorLabel;
    }

    public void setAuthorLabel(Label authorLabel) {
        this.authorLabel = authorLabel;
    }

    public Label getSummaryLabel() {
        return summaryLabel;
    }

    public void setSummaryLabel(Label summaryLabel) {
        this.summaryLabel = summaryLabel;
    }

    public Label getKeywordsLabel() {
        return keywordsLabel;
    }

    public void setKeywordsLabel(Label keywordsLabel) {
        this.keywordsLabel = keywordsLabel;
    }

    public Label getOwnernameLabel() {
        return ownernameLabel;
    }

    public void setOwnernameLabel(Label ownernameLabel) {
        this.ownernameLabel = ownernameLabel;
    }

    public Label getOwneremailLabel() {
        return owneremailLabel;
    }

    public void setOwneremailLabel(Label owneremailLabel) {
        this.owneremailLabel = owneremailLabel;
    }

    public Label getExplicitLabel() {
        return explicitLabel;
    }

    public void setExplicitLabel(Label explicitLabel) {
        this.explicitLabel = explicitLabel;
    }

    public Label getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(Label categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public Button getSaveandcloseButton() {
        return saveandcloseButton;
    }

    public void setSaveandcloseButton(Button saveandcloseButton) {
        this.saveandcloseButton = saveandcloseButton;
    }

    public Label getFilenameTextview() {
        return filenameTextview;
    }

    public void setFilenameTextview(Label filenameTextview) {
        this.filenameTextview = filenameTextview;
    }

    public ComboBox getLanguageList() {
        return languageList;
    }

    public void setLanguageList(ComboBox languageList) {
        this.languageList = languageList;
    }

    public TableView<PodcastEntry> getPodcastEntries() {
        return podcastEntries;
    }

    public void setPodcastEntries(TableView<PodcastEntry> podcastEntries) {
        podcastEntries = podcastEntries;
    }

    public ObservableList<PodcastEntry> getPodcastEntriesData() {
        return podcastEntriesData;
    }

    public void setPodcastEntriesData(ObservableList<PodcastEntry> podcastEntriesData) {
        MainController.podcastEntriesData = podcastEntriesData;
    }

    public Button getAddpodcastentryButton() {
        return addpodcastentryButton;
    }

    public void setAddpodcastentryButton(Button addpodcastentryButton) {
        this.addpodcastentryButton = addpodcastentryButton;
    }

    public Button getDeletepodcastentryButton() {
        return deletepodcastentryButton;
    }

    public void setDeletepodcastentryButton(Button deletepodcastentryButton) {
        this.deletepodcastentryButton = deletepodcastentryButton;
    }

    public Label getImagesLabel() {
        return imagesLabel;
    }

    public void setImagesLabel(Label imagesLabel) {
        this.imagesLabel = imagesLabel;
    }

    public Button getNewFileStandardButton() {
        return newFileStandardButton;
    }

    public void setNewFileStandardButton(Button newFileStandardButton) {
        this.newFileStandardButton = newFileStandardButton;
    }

}
