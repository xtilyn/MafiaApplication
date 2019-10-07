package mafia.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import mafia.MafiaApp;
import mafia.entities.player_roles.Player;
import mafia.models.MainMenuModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import static mafia.MafiaApp.mafiaMembers;
import static mafia.MafiaApp.playerInfo;

/**
 * MainMenuController displays instructions and about page through popups.
 * It collects num of players and player names from user input, set the totalPlayers
 * and initializes player instances. It will perform role assignment task before proceeding to
 * the game scene.
 */
public class MainMenuController implements Initializable {

    private final static int MAX_NAME_LENGTH = 10;

    @FXML private StackPane mainMenuView;
    @FXML private JFXPopup instructionsView;
    @FXML private JFXPopup aboutPageView;
    @FXML private Button backFromInstructions;
    @FXML private Button backFromAboutPage;
    @FXML private Button newGameButton;
    @FXML private Button instructionsButton;
    @FXML private Button aboutButton;

    private ImageView background;
    private Button backButton;
    private ArrayList<TextField> textFields = new ArrayList<>();
    private ArrayList<Node> newGameContents = new ArrayList<>();
    private StackPane loadingView;

    private MainMenuModel mainMenuModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        mainMenuModel = new MainMenuModel();

        String style = getClass().getResource("../theme.css").toExternalForm();
        mainMenuView.getStylesheets().add(style);

        // configure main menu buttons
        Image back = new Image(getClass().getResourceAsStream("../../images/backArrow.png"));
        ImageView backArrow = new ImageView(back);
        backArrow.setFitHeight(24); backArrow.setFitWidth(39);
        backArrow.setOpacity(0.46);
        backArrow.setPickOnBounds(true);
        backButton = new Button();
        backButton.setGraphic(backArrow);
        backButton.setStyle("-fx-background-color: transparent;");
        backButton.setTranslateY(-270); backButton.setTranslateX(-170);

        background = new ImageView("@../../images/darkWall.jpeg");
        background.setFitHeight(600); background.setFitWidth(411);
        background.setPickOnBounds(true);

        newGameButton.setOnAction(e -> loadNewGameView());

        aboutButton.setOnAction(e -> aboutPageView.setVisible(true));

        instructionsButton.setOnAction(e -> instructionsView.setVisible(true));

        backFromAboutPage.setOnAction(e -> aboutPageView.setVisible(false));
        backFromInstructions.setOnAction(e -> instructionsView.setVisible(false));

        initLoadingView();
        initNewGameView();

    }

    @FXML
    private void mouseEntered(MouseEvent event) {
        ((Button)event.getSource()).setStyle("-fx-background-color: white;" +
                "-fx-text-fill: black; -fx-opacity: 0.70; -fx-border-radius: 5;");
    }

    @FXML
    private void mouseExited(MouseEvent event) {
        ((Button)event.getSource()).setStyle("-fx-background-color: transparent;" +
                "-fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 5;");
    }

    private void initNewGameView() {

        JFXButton fivePlayers = createCircularBtn(125, 132,90, -30, "5");
        JFXButton sixPlayers = createCircularBtn(98, 96, -110, 100, "6");
        JFXButton sevenPlayers = createCircularBtn( 91, 99, -10, 180, "7");
        JFXButton eightPlayers = createCircularBtn(125, 128, 100, 110, "8");
        JFXButton ninePlayers = createCircularBtn(106, 104, -10, 50, "9");
        JFXButton tenPlayers = createCircularBtn(136, 136, -100, -60, "10");

        Label selectPlayersLabel = new Label("Select number of players");
        selectPlayersLabel.setAlignment(Pos.CENTER);
        selectPlayersLabel.setTranslateY(-180);
        selectPlayersLabel.setPrefHeight(47); selectPlayersLabel.setPrefWidth(363);
        selectPlayersLabel.setTextFill(Paint.valueOf("white"));
        selectPlayersLabel.setFont(new Font("Arial", 30));

        newGameContents.add(fivePlayers);
        newGameContents.add(sixPlayers);
        newGameContents.add(sevenPlayers);
        newGameContents.add(eightPlayers);
        newGameContents.add(ninePlayers);
        newGameContents.add(tenPlayers);
        newGameContents.add(selectPlayersLabel);

    }

    private JFXButton createCircularBtn(int height, int width, int translateX, int translateY, String text) {

        JFXButton btn = new JFXButton();
        btn.setPrefHeight(height); btn.setPrefWidth(width);
        btn.setTranslateX(translateX); btn.setTranslateY(translateY);
        btn.setText(text);

        btn.setAlignment(Pos.CENTER);
        btn.setContentDisplay(ContentDisplay.CENTER);
        btn.setMnemonicParsing(false);

        btn.setId("ipad-dark-grey");

        btn.setOnAction(e -> {
            mainMenuModel.setNumPlayers(Integer.parseInt(btn.getText()));
            showTextFields();
        });

        return btn;

    }

    private void loadNewGameView() {

        backButton.setOnAction(e -> displayMainMenuView());
        mainMenuView.getChildren().setAll(background, backButton);

        for (Node node : newGameContents) {
            mainMenuView.getChildren().add(node);
        }

    }

    private void displayMainMenuView() {

        try {
            StackPane mainMenuFXML = FXMLLoader.load(getClass().getResource("../ui/mainMenu.fxml"));
            mainMenuView.getChildren().setAll(mainMenuFXML);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showTextFields() {

        VBox textFieldsContainer = new VBox(30);
        textFieldsContainer.setId("text-fields-container");

        Label enterNamesLabel = new Label("Enter each players names");
        enterNamesLabel.setAlignment(Pos.CENTER);
        enterNamesLabel.setPrefHeight(9); enterNamesLabel.setPrefWidth(371);
        enterNamesLabel.setFont(new Font("Arial", 25));
        enterNamesLabel.setTextFill(Paint.valueOf("white"));

        textFieldsContainer.getChildren().add(enterNamesLabel);

        Image next = new Image(getClass().getResourceAsStream("../../images/backArrow.png"));
        ImageView nextArrow = new ImageView(next);
        nextArrow.setFitHeight(24); nextArrow.setFitWidth(39);
        nextArrow.setOpacity(0.46);
        nextArrow.setPickOnBounds(true);
        Button nextButton = new Button();
        nextButton.setGraphic(nextArrow);
        nextButton.setStyle("-fx-background-color: transparent;");
        nextButton.setTranslateY(-270); nextButton.setTranslateX(160);
        nextButton.setRotate(180);
        nextButton.setOnAction(e -> validateTextFields());

        int playerNum = 1;
        for (int count = 0; count < MafiaApp.getTotalPlayers(); count++) {
            TextField textField = createTextField();
            textField.setPromptText("Player " + String.valueOf(playerNum));
            textFields.add(textField);
            textFieldsContainer.getChildren().add(textField);
            playerNum++;
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.valueOf("NEVER"));
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.valueOf("AS_NEEDED"));
        scrollPane.setContent(textFieldsContainer);

        backButton.setOnAction(e -> {
            textFields.clear();
            mainMenuModel.getPlayerNames().clear();
            playerInfo.clear();
            loadNewGameView();
        });

        mainMenuView.getChildren().setAll(background, scrollPane, backButton, nextButton);

    }

    private void validateTextFields() {

        boolean isValid = true;

        for (TextField tf : textFields) {
            if (tf.getText().isEmpty()) {
                isValid = false;
            } else {
                mainMenuModel.getPlayerNames().add(tf.getText().trim());
            }
        }
        System.out.println(mainMenuModel.getPlayerNames().toString());

        if (!isValid){
            mainMenuModel.getPlayerNames().clear();
            showDialog();
        } else{
            playerInfo.clear();
            MafiaApp.nameOfPlayers(mainMenuModel.getPlayerNames());
            revealRoles();
        }

    }

    private void showDialog() {

        JFXDialogLayout alertBoxContent = new JFXDialogLayout();

        alertBoxContent.setHeading(new Text("Missing Names"));
        alertBoxContent.setBody(new Text("Please provide names for all players"));

        JFXDialog alertBox = new JFXDialog(mainMenuView, alertBoxContent, JFXDialog.DialogTransition.CENTER);
        JFXButton okButton = new JFXButton("Got it.");
        okButton.setOnAction(e -> alertBox.close());

        alertBoxContent.setActions(okButton);
        alertBox.show();

    }

    private TextField createTextField() {

        TextField playerNameField = new TextField();
        playerNameField.setPrefHeight(20);
        playerNameField.setPrefWidth(371);
        playerNameField.setFont(new Font("Arial", 18));
        playerNameField.setStyle("-fx-background-color: black; -fx-border-color: white;" +
                "-fx-text-fill: white; -fx-opacity: 0.58;");

        playerNameField.textProperty().addListener((v, oldVal, newVal) -> {
            if (playerNameField.getText().length() > MAX_NAME_LENGTH) {
                playerNameField.setText(
                        playerNameField.getText().substring(0, MAX_NAME_LENGTH)
                );
            }
        });

        return playerNameField;

    }

    private void initLoadingView() {

        loadingView = new StackPane();
        loadingView.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        Label progress = new Label("Initializing players...");
        progress.setStyle("-fx-text-fill: white; -fx-font-size: 15px;");
        loadingView.getChildren().add(progress);

    }

    private void revealRoles() {

        mainMenuView.getChildren().add(loadingView);
        mainMenuModel.assignRoles();
        try {
            StackPane gameScene = FXMLLoader.load(getClass().getResource("../ui/gameScene.fxml"));
            mainMenuView.getChildren().setAll(gameScene);

            // TEMP
            System.out.println(playerInfo.toString());
            int count = 1;
            System.out.println("ROLES-----------------------");
            for (Player player : playerInfo) {
                System.out.print("PLAYER " + count + ": " + player.getRole() + ", ");
                count++;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}