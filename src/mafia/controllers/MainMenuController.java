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
    private ArrayList<String> playerNames = new ArrayList<>();
    private ArrayList<Node> newGameContents = new ArrayList<>();
    private StackPane loadingView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

        btn.setOnAction(e -> setNumOfPlayers(btn));

        return btn;

    }

    private void setNumOfPlayers(Button button) {

        int numOfPlayers = Integer.parseInt(button.getText());
        MafiaApp.setTotalPlayers(numOfPlayers);
        showTextFields();

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
            playerNames.clear();
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
                playerNames.add(tf.getText().trim());
            }
        }
        System.out.println(playerNames.toString());

        if (!isValid){
            playerNames.clear();
            showDialog();
        } else{
            playerInfo.clear();
            MafiaApp.nameOfPlayers(playerNames);
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

        Task<Void> roleAssignmentTask = new RoleAssignmentTask(MafiaApp.getTotalPlayers());
        Thread assign = new Thread(roleAssignmentTask);
        assign.setDaemon(true);
        assign.start();

        roleAssignmentTask.setOnSucceeded(e -> {
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
        });

    }

}

class RoleAssignmentTask extends Task<Void> {

    private static int totalPlayers;

    //Original List of Roles in strong form
    private List<String> jobs = new ArrayList<>();
    //Original List of Roles Descriptions
    private List<String> info = new ArrayList<>();
    //Original List of Goals for each player
    private List<String> goals = new ArrayList<>();

    private List<String> assignments = new ArrayList<>();
    private List<String> assignmentsInfo = new ArrayList<>();
    private List<String> assignmentsGoals = new ArrayList<>();


    private static Random rand = new Random();

    public RoleAssignmentTask(int numTotalPlayers) {
        totalPlayers = numTotalPlayers;
    }

    private void roleOfPlayers(List<String> roles, List<String> rolesInfo, List<String> goals) {

        //Loops through all of the players and assigns them a Role, Info and Goal
        for(int i=0; i< totalPlayers; i++){
            playerInfo.get(i).setRole(roles.get(i));
            playerInfo.get(i).setRoleInfo(rolesInfo.get(i));
            playerInfo.get(i).setGoal(goals.get(i));
            //If the String role of the player contains the word "Mafia:"
            if(playerInfo.get(i).getRole().contains("Mafia:")){
                //This boolean is for the detective checking if the target is part of the Mafia
                //GodFather is not included,z as he is hidden from the detective
                playerInfo.get(i).setIsMafia(true);

            }else{
                playerInfo.get(i).setIsMafia(false);
            }
            if(playerInfo.get(i).getRole().contains("Mafia")){
                //This is a list of the names, used at night to display the ALL Mafia members to other Mafia members
                mafiaMembers.add(playerInfo.get(i).getName());

            }
        }

    }

    private void playerAssignment(){
        initializeRoles();
        randomizeLists();
    }

    private void initializeRoles(){

        jobs.add("Townie");
        info.add("Do nothing at night");
        goals.add("To have only town members left");

        jobs.add("Detective");
        info.add("Reveals the team for one player per night");
        goals.add("To have only town members left");

        jobs.add("Mafia: Hitman");
        info.add("May kill someone each night");
        goals.add("To make the majority of the town mafia members");

        jobs.add("Doctor");
        info.add("May heal one player each night (Can't target same person two nights in a row)");
        goals.add("To have only town members left");

        jobs.add("Survivor");
        info.add("Do nothing at night");
        goals.add("To be the last town member left alive");

        if (totalPlayers > 5){
            jobs.add("Mafia- Barman");
            info.add("May stop the action of another player each night (Can't go same person each night)");
            goals.add("To make the majority of the town mafia members");
        }

        if (totalPlayers > 6){
            jobs.add("Bodyguard");
            info.add("May save another person by stepping infront of the bullet per night. (You will die in their place)");
            goals.add("To have only town members left");
        }

        if (totalPlayers > 7){
            jobs.add("Lyncher");
            info.add("Do nothing at night");
            goals.add("To Lynch a specific player to win solo win the game | ");
        }

        if (totalPlayers > 8){
            jobs.add("Mafiaboss- GodFather");
            info.add("Hidden from the Detective. Can send a message to another Mafia memeber each night");
            goals.add("To make the majority of the town mafia members");
        }

        if (totalPlayers > 9){
            jobs.add("Vigilante");
            info.add("May kill new person each night, but is trying to kill Mafia. Note: Do not have to kill someone each night");
            goals.add("To have only town members left");
        }



    }

    private void randomizeLists(){
        int num;
        String select;
        String select2;
        String select3;
        for(int i=0; i < totalPlayers; i++){
            num = rand.nextInt(totalPlayers-i);

            //Takes instance from this array to be added to new one
            //Then removes the instance from old List (So it can't be re-used)
            select = jobs.get(num);
            select2 = info.get(num);
            select3 = goals.get(num);
            jobs.remove(num);
            info.remove(num);
            goals.remove(num);
            //Adds the randomly selected object to the back of the new List
            assignments.add(select);
            assignmentsInfo.add(select2);
            assignmentsGoals.add(select3);
        }
    }

    @Override
    protected Void call() throws Exception {
        playerAssignment();
        roleOfPlayers(assignments, assignmentsInfo, assignmentsGoals);
        return null;
    }

    @Override
    protected void failed() {
        System.out.println("role assignment task failed");
        super.failed();
    }
}