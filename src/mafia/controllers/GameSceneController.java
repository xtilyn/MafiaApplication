package mafia.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPopup;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import mafia.entities.Action;
import mafia.MafiaApp;
import mafia.entities.data_types.PlayerStatus;
import mafia.entities.player_roles.*;
import mafia.models.GameSceneModel;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static mafia.MafiaApp.mafiaMembers;
import static mafia.MafiaApp.playerInfo;

/**
 * Each player are assigned their own button and an icon. During the night cycle, each players buttons
 * are set to view a popup specific to their role. There they can select their target (if they are able to target
 * at night). During day cycle, the buttons are set to kill/lynch the player associated to that button.
 */
public class GameSceneController implements Initializable {

    private GameSceneModel gameSceneModel;

    //Key-Value: Role-popupWindow(associated to role)
    private HashMap<String, JFXPopup> popupWindows = new HashMap<>();

    // the message the GodFather can give to Mafia members the next night
    private TextField mafiaMessage;

    @FXML
    private Button centralButton;
    @FXML
    private Label centerLabel;
    @FXML
    private Button settingsButton;
    @FXML
    private JFXPopup settingsPopup;
    @FXML
    private StackPane gameSceneView;

    // ten players are displayed by default
    @FXML
    private Button player1;
    @FXML
    private Button player2;
    @FXML
    private Button player3;
    @FXML
    private Button player4;
    @FXML
    private Button player5;
    @FXML
    private Button player6;
    @FXML
    private Button player7;
    @FXML
    private Button player8;
    @FXML
    private Button player9;
    @FXML
    private Button player10;

    @FXML
    private ImageView player1Icon;
    @FXML
    private ImageView player2Icon;
    @FXML
    private ImageView player3Icon;
    @FXML
    private ImageView player4Icon;
    @FXML
    private ImageView player5Icon;
    @FXML
    private ImageView player6Icon;
    @FXML
    private ImageView player7Icon;
    @FXML
    private ImageView player8Icon;
    @FXML
    private ImageView player9Icon;
    @FXML
    private ImageView player10Icon;

    public VBox vigilanteView;
    public VBox godfatherView;
    public VBox bodyguardView;
    public VBox barmanView;
    public VBox doctorView;
    public VBox hitmanView;
    public VBox detectiveView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        mafiaMessage = new TextField();

        MafiaApp game = new MafiaApp();
        gameSceneModel = new GameSceneModel(game);
        Action action = new Action(MafiaApp.getTotalPlayers(), gameSceneModel.getJobPositionMap());
        gameSceneModel.setAction(action);

        gameSceneModel.initJobPositionMap();

        // set event handler for the center button & settings button
        centralButton.setOnAction(e -> dayCycle());
        initSettingsPopup();
        settingsButton.setOnAction(e -> {
            if (!settingsPopup.isVisible())
                settingsPopup.setVisible(true);
            settingsPopup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT);
        });

        initPlayersPopup();
        initPlayersButtons();
        // add css theme
        String style = getClass().getResource("../theme.css").toExternalForm();
        gameSceneView.getStylesheets().add(style);

    }

    // reset the popup of those who are able to target at night
    private void resetNightCyclePopups() {
        // loops through all of the available roles in the popupWindows Map
        for (String role : popupWindows.keySet()) {
            if (Arrays.asList(gameSceneModel.getRolesThatCanTarget()).contains(role)) {
                switch (role) {
                    case "Detective":
                        resetDetectivePopup();
                        break;
                    case "Mafia: Hitman":
                        resetHitmanPopup();
                        break;
                    case "Doctor":
                        resetDoctorPopup();
                        break;
                    case "Mafia- Barman":
                        resetBarmanPopup();
                        break;
                    case "Bodyguard":
                        resetBodyguardPopup();
                        break;
                    case "Vigilante":
                        resetVigilantePopup();
                        break;
                }
            } else if (role.equals("Mafiaboss- GodFather"))
                resetGodfatherPopup();
        }
    }

    public VBox createNightCyclePopup() {
        VBox nightCyclePlayerPopup = new VBox(10);
        nightCyclePlayerPopup.setStyle("-fx-background-color: black;");
        nightCyclePlayerPopup.setPrefHeight(300);
        nightCyclePlayerPopup.setPrefWidth(411);
        nightCyclePlayerPopup.setAlignment(Pos.TOP_CENTER);
        nightCyclePlayerPopup.setPadding(new Insets(20, 0, 0, 0));
        nightCyclePlayerPopup.setTranslateY(300);
        return nightCyclePlayerPopup;
    }

    // temporary layout for vigilante
    public void resetVigilantePopup() throws NullPointerException{

        int myPosition = gameSceneModel.getJobPositionMap().get("Vigilante");
        ((Vigilante) playerInfo.get(myPosition)).setIsTargetSelected(false);

        vigilanteView = createNightCyclePopup();

        Label selectTargetLabel = new Label("Select your target");
        selectTargetLabel.setFont(new Font("Arial", 20));
        selectTargetLabel.setTextFill(Paint.valueOf("white"));

        Label target = new Label();
        target.setTextFill(Paint.valueOf("white"));
        target.setTextAlignment(TextAlignment.CENTER);
        target.setFont(new Font("Arial", 20));

        JFXButton enterButton = new JFXButton("CONFIRM");
        enterButton.setFont(new Font("Arial", 15));
        enterButton.setTextFill(Paint.valueOf("white"));
        enterButton.setStyle("-fx-background-color: transparent; -fx-border-color: white;");
        enterButton.setVisible(false);

        ListView<String> availableTargets = new ListView<>();
        availableTargets.setMaxHeight(150);
        availableTargets.getItems().add("No one");
        // the enter button is hidden by default. Added a listener to the listView such that
        // whenever user clicks on a selection, the enter button is set to visible
        availableTargets.getSelectionModel().selectedItemProperty().addListener(
                (v, oldSelection, newSelection) -> {
                    if (!enterButton.isVisible())
                        enterButton.setVisible(true);
                }
        );
        // add all of the available targets' names for vigilante in the listView
        // do not include the vigilante himself
        for (Player player : playerInfo) {
            if (!player.getRole().equals("Vigilante") && player.getStatus() == PlayerStatus.ALIVE) {
                availableTargets.getItems().add(player.getName());
            }
        }

        vigilanteView.getChildren().setAll(selectTargetLabel, availableTargets, enterButton);
        popupWindows.get("Vigilante").getChildren().add(vigilanteView);
        int finalMyPosition = myPosition;
        enterButton.setOnAction(e -> {

            try {
                ((Vigilante) playerInfo.get(finalMyPosition)).setIsTargetSelected(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            availableTargets.setDisable(true);
            String myTarget = availableTargets.getSelectionModel().getSelectedItem();
            System.out.println("VIGILANTE'S TARGET: " + myTarget);

            // targets the selected player
            if (!myTarget.equals("No one")) {
                target.setText("Your target: " + myTarget);
                gameSceneModel.targetPlayer(myTarget, "Vigilante", this::setPlayerIsInBarPopup);
            } else {
                target.setText("No target selected");
            }
            vigilanteView.getChildren().add(target);
            enterButton.setVisible(false);

        });


    }

    // temporary layout for godfather
    public void resetGodfatherPopup() throws NullPointerException {

        godfatherView = createNightCyclePopup();

        Label enterMessageLbl = new Label("Send message to other mafia members");
        enterMessageLbl.setFont(new Font("Arial", 20));
        enterMessageLbl.setTextFill(Paint.valueOf("white"));

        Label messageSentLbl = new Label("Message sent.");
        messageSentLbl.setTextFill(Paint.valueOf("white"));
        messageSentLbl.setTextAlignment(TextAlignment.CENTER);
        messageSentLbl.setFont(new Font("Arial", 20));

        JFXButton sendButton = new JFXButton("Send");
        sendButton.setFont(new Font("Arial", 15));
        sendButton.setTextFill(Paint.valueOf("white"));
        sendButton.setStyle("-fx-background-color: transparent; -fx-border-color: white;");

        godfatherView.getChildren().addAll(enterMessageLbl, sendButton);

        // add a textField to bind with other mafias' label for sending message
        if (playerInfo.size() > 8) {
            godfatherView.getChildren().add(mafiaMessage);
        }
        popupWindows.get("Mafiaboss- GodFather").getChildren().add(godfatherView);

        sendButton.setOnAction(e -> {

            godfatherView.getChildren().add(messageSentLbl);
            sendButton.setVisible(false);

        });

    }

    // temporary layout for bodyguard
    public void resetBodyguardPopup() throws NullPointerException {

        int myPosition = gameSceneModel.getJobPositionMap().get("Bodyguard");
        ((Bodyguard) playerInfo.get(myPosition)).setIsTargetSelected(false);

        bodyguardView = createNightCyclePopup();

        Label selectTargetLabel = new Label("Select your target");
        selectTargetLabel.setFont(new Font("Arial", 20));
        selectTargetLabel.setTextFill(Paint.valueOf("white"));

        Label target = new Label();
        target.setTextFill(Paint.valueOf("white"));
        target.setTextAlignment(TextAlignment.CENTER);
        target.setFont(new Font("Arial", 20));

        JFXButton enterButton = new JFXButton("CONFIRM");
        enterButton.setFont(new Font("Arial", 15));
        enterButton.setTextFill(Paint.valueOf("white"));
        enterButton.setStyle("-fx-background-color: transparent; -fx-border-color: white;");
        enterButton.setVisible(false);

        ListView<String> availableTargets = new ListView<>();
        availableTargets.setMaxHeight(150);
        availableTargets.getItems().add("No one");
        // the enter button is hidden by default. Added a listener to the listView such that
        // whenever user clicks on a selection, the enter button is set to visible
        availableTargets.getSelectionModel().selectedItemProperty().addListener(
                (v, oldSelection, newSelection) -> {
                    if (!enterButton.isVisible())
                        enterButton.setVisible(true);
                }
        );
        // add all of the available targets' names for Bodyguard in the listView
        // do not include the Bodyguard himself
        for (Player player : playerInfo) {
            if (!player.getRole().equals("Bodyguard") && player.getStatus() == PlayerStatus.ALIVE) {
                availableTargets.getItems().add(player.getName());
            }
        }

        bodyguardView.getChildren().addAll(selectTargetLabel, availableTargets, enterButton);
        popupWindows.get("Bodyguard").getChildren().add(bodyguardView);

        enterButton.setOnAction(e -> {

            ((Bodyguard) playerInfo.get(myPosition)).setIsTargetSelected(true);
            availableTargets.setDisable(true);
            String myTarget = availableTargets.getSelectionModel().getSelectedItem();
            System.out.println("BODYGUARD'S TARGET: " + myTarget);
            // target the selected player
            if (!myTarget.equals("No one")) {
                target.setText("You protected " + myTarget);
                gameSceneModel.targetPlayer(myTarget, "Bodyguard", this::setPlayerIsInBarPopup);
            } else {
                target.setText("No target selected");
            }
            bodyguardView.getChildren().add(target);
            enterButton.setVisible(false);

        });

    }

    // temporary layout for barman
    public void resetBarmanPopup() throws NullPointerException {

        int myPosition = gameSceneModel.getJobPositionMap().get("Mafia- Barman");
        ((Mafia) playerInfo.get(myPosition)).setIsTargetSelected(false);

        barmanView = createNightCyclePopup();

        Label selectTargetLabel = new Label("Select your target");
        selectTargetLabel.setFont(new Font("Arial", 20));
        selectTargetLabel.setTextFill(Paint.valueOf("white"));

        // if there is a godfather, create a label to bind boss' message
        if (playerInfo.size() > 8) {
            Label messageFromBoss = new Label("A message from the GodFather: ");
            messageFromBoss.setTextFill(Paint.valueOf("white"));
            Label msg = new Label();
            msg.setTextFill(Paint.valueOf("white"));
            msg.textProperty().bind(mafiaMessage.textProperty());
            barmanView.getChildren().addAll(messageFromBoss, msg);
        }

        Label target = new Label();
        target.setTextFill(Paint.valueOf("white"));
        target.setTextAlignment(TextAlignment.CENTER);
        target.setFont(new Font("Arial", 20));

        JFXButton enterButton = new JFXButton("CONFIRM");
        enterButton.setFont(new Font("Arial", 15));
        enterButton.setTextFill(Paint.valueOf("white"));
        enterButton.setStyle("-fx-background-color: transparent; -fx-border-color: white;");
        enterButton.setVisible(false);

        ListView<String> availableTargets = new ListView<>();
        availableTargets.setMaxHeight(150);
        availableTargets.getItems().add("No one");
        // the enter button is hidden by default. Added a listener to the listView such that
        // whenever user clicks on a selection, the enter button is set to visible
        availableTargets.getSelectionModel().selectedItemProperty().addListener(
                (v, oldSelection, newSelection) -> {
                    if (!enterButton.isVisible())
                        enterButton.setVisible(true);
                }
        );
        // add all of the available targets for Barman in the listView
        // do not include the Barman himself
        for (Player player : playerInfo) {
            if (!player.getRole().equals("Mafia- Barman") && player.getStatus() == PlayerStatus.ALIVE) {
                availableTargets.getItems().add(player.getName());
            }
        }

        barmanView.getChildren().addAll(selectTargetLabel, availableTargets, enterButton);
        popupWindows.get("Mafia- Barman").getChildren().add(barmanView);

        enterButton.setOnAction(e -> {

            ((Mafia) playerInfo.get(myPosition)).setIsTargetSelected(true);
            availableTargets.setDisable(true);
            String myTarget = availableTargets.getSelectionModel().getSelectedItem();
            System.out.println("BARMAN'S TARGET: " + myTarget);
            // target the selected player
            if (!myTarget.equals("No one")) {
                target.setText("You stopped " + myTarget + " from performing an action");
                gameSceneModel.targetPlayer(myTarget, "Mafia- Barman", this::setPlayerIsInBarPopup);
            } else {
                target.setText("No target selected");
            }
            barmanView.getChildren().add(target);
            enterButton.setVisible(false);

        });

    }

    // temporary layout for the doctor
    public void resetDoctorPopup() throws NullPointerException {

        int myPosition = gameSceneModel.getJobPositionMap().get("Doctor");
        ((Doctor) playerInfo.get(myPosition)).setIsTargetSelected(false);

        doctorView = createNightCyclePopup();

        Label selectTargetLabel = new Label("Select your target");
        selectTargetLabel.setFont(new Font("Arial", 20));
        selectTargetLabel.setTextFill(Paint.valueOf("white"));

        Label target = new Label();
        target.setTextFill(Paint.valueOf("white"));
        target.setTextAlignment(TextAlignment.CENTER);
        target.setFont(new Font("Arial", 20));

        JFXButton enterButton = new JFXButton("CONFIRM");
        enterButton.setFont(new Font("Arial", 15));
        enterButton.setTextFill(Paint.valueOf("white"));
        enterButton.setStyle("-fx-background-color: transparent; -fx-border-color: white;");
        enterButton.setVisible(false);

        ListView<String> availableTargets = new ListView<>();
        availableTargets.setMaxHeight(150);
        availableTargets.getItems().add("No one");
        // the enter button is hidden by default. Added a listener to the listView such that
        // whenever user clicks on a selection, the enter button is set to visible
        availableTargets.getSelectionModel().selectedItemProperty().addListener(
                (v, oldSelection, newSelection) -> {
                    if (!enterButton.isVisible())
                        enterButton.setVisible(true);
                }
        );
        // add all of the available targets for doctor in the listView
        // do not include the doctor himself
        for (Player player : playerInfo) {
            if (!player.getRole().equals("Doctor") && player.getStatus() == PlayerStatus.ALIVE) {
                availableTargets.getItems().add(player.getName());
            }
        }

        doctorView.getChildren().addAll(selectTargetLabel, availableTargets, enterButton);
        popupWindows.get("Doctor").getChildren().add(doctorView);

        enterButton.setOnAction(e -> {

            ((Doctor) playerInfo.get(myPosition)).setIsTargetSelected(true);
            availableTargets.setDisable(true);
            String myTarget = availableTargets.getSelectionModel().getSelectedItem();
            System.out.println("DOCTOR'S TARGET: " + myTarget);
            // target the selected player
            if (!myTarget.equals("No one")) {
                target.setText("You healed " + myTarget);
                gameSceneModel.targetPlayer(myTarget, "Doctor", this::setPlayerIsInBarPopup);
            } else {
                target.setText("No target selected");
            }
            doctorView.getChildren().add(target);
            enterButton.setVisible(false);

        });

    }

    // temporary layout for hitman
    public void resetHitmanPopup() throws NullPointerException {

        int myPosition = gameSceneModel.getJobPositionMap().get("Mafia: Hitman");
        ((Mafia) playerInfo.get(myPosition)).setIsTargetSelected(false);

        hitmanView = createNightCyclePopup();

        Label selectTargetLabel = new Label("Select your target");
        selectTargetLabel.setFont(new Font("Arial", 20));
        selectTargetLabel.setTextFill(Paint.valueOf("white"));

        // if there is a godfather, create a label to bind boss' message
        if (playerInfo.size() > 8) {
            Label messageFromBoss = new Label("A message from the GodFather: ");
            messageFromBoss.setTextFill(Paint.valueOf("white"));
            Label msg = new Label();
            msg.setTextFill(Paint.valueOf("white"));
            msg.textProperty().bind(mafiaMessage.textProperty());
            hitmanView.getChildren().addAll(messageFromBoss, msg);
        }

        Label target = new Label();
        target.setTextFill(Paint.valueOf("white"));
        target.setTextAlignment(TextAlignment.CENTER);
        target.setFont(new Font("Arial", 20));

        JFXButton enterButton = new JFXButton("CONFIRM");
        enterButton.setFont(new Font("Arial", 15));
        enterButton.setTextFill(Paint.valueOf("white"));
        enterButton.setStyle("-fx-background-color: transparent; -fx-border-color: white;");
        enterButton.setVisible(false);

        ListView<String> availableTargets = new ListView<>();
        availableTargets.setMaxHeight(150);
        availableTargets.getItems().add("No one");
        // the enter button is hidden by default. Added a listener to the listView such that
        // whenever user clicks on a selection, the enter button is set to visible
        availableTargets.getSelectionModel().selectedItemProperty().addListener(
                (v, oldSelection, newSelection) -> {
                    if (!enterButton.isVisible())
                        enterButton.setVisible(true);
                }
        );
        // add all of the available targets for Hitman in the listView
        // do not include the Hitman himself
        for (Player player : playerInfo) {
            if (!player.getRole().equals("Mafia: Hitman") && player.getStatus() == PlayerStatus.ALIVE) {
                availableTargets.getItems().add(player.getName());
            }
        }

        hitmanView.getChildren().addAll(selectTargetLabel, availableTargets, enterButton);
        popupWindows.get("Mafia: Hitman").getChildren().add(hitmanView);

        enterButton.setOnAction(e -> {

            ((Mafia) playerInfo.get(myPosition)).setIsTargetSelected(true);
            availableTargets.setDisable(true);
            String myTarget = availableTargets.getSelectionModel().getSelectedItem();
            System.out.println("HITMAN'S TARGET: " + myTarget);
            // target the selected player
            if (!myTarget.equals("No one")) {
                target.setText("Your target: " + myTarget);
                gameSceneModel.targetPlayer(myTarget, "Mafia: Hitman", this::setPlayerIsInBarPopup);
            } else {
                target.setText("No target selected");
            }
            hitmanView.getChildren().add(target);
            enterButton.setVisible(false);

        });
    }

    // temporary layout for detective
    public void resetDetectivePopup() throws NullPointerException {

        int myPosition = gameSceneModel.getJobPositionMap().get("Detective");
        ((Detective) playerInfo.get(myPosition)).setIsTargetSelected(false);

        detectiveView = createNightCyclePopup();

        Label selectTargetLabel = new Label("Select your target");
        selectTargetLabel.setFont(new Font("Arial", 20));
        selectTargetLabel.setTextFill(Paint.valueOf("white"));

        Label targetsRole = new Label();
        targetsRole.setTextFill(Paint.valueOf("white"));
        targetsRole.setTextAlignment(TextAlignment.CENTER);
        targetsRole.setFont(new Font("Arial", 20));

        JFXButton enterButton = new JFXButton("CONFIRM");
        enterButton.setFont(new Font("Arial", 15));
        enterButton.setTextFill(Paint.valueOf("white"));
        enterButton.setStyle("-fx-background-color: transparent; -fx-border-color: white;");
        enterButton.setVisible(false);

        ListView<String> availableTargets = new ListView<>();
        availableTargets.setMaxHeight(150);
        availableTargets.getItems().add("No one");
        // the enter button is hidden by default. Added a listener to the listView such that
        // whenever user clicks on a selection, the enter button is set to visible
        availableTargets.getSelectionModel().selectedItemProperty().addListener(
                (v, oldSelection, newSelection) -> {
                    if (!enterButton.isVisible())
                        enterButton.setVisible(true);
                }
        );
        // add all of the available targets for detective in the listView
        // do not include the detective himself
        for (Player player : playerInfo) {
            if (!player.getRole().equals("Detective") && player.getStatus() == PlayerStatus.ALIVE) {
                availableTargets.getItems().add(player.getName());
            }
        }

        detectiveView.getChildren().addAll(selectTargetLabel, availableTargets, enterButton);
        popupWindows.get("Detective").getChildren().add(detectiveView);

        enterButton.setOnAction(e -> {

            ((Detective) playerInfo.get(myPosition)).setIsTargetSelected(true);
            availableTargets.setDisable(true);
            String myTarget = availableTargets.getSelectionModel().getSelectedItem();
            System.out.println("DETECTIVE'S TARGET: " + myTarget);

            // default label text
            targetsRole.setText(myTarget.equals("No one") ? "No target selected." :
                    "Target Verified:\n" + myTarget + " is not part of the mafia.");
            // if the selected target is part of the mafiaMembers, change the default label text
            for (String mafiaMember : mafiaMembers) {
                if (myTarget.equals(mafiaMember)) {
                    targetsRole.setText("Target Verified:\n" + myTarget + " is part of the mafia.");
                }
            }
            detectiveView.getChildren().add(targetsRole);
            enterButton.setVisible(false);

        });

    }

    // if the player in inBar, set their popup such that they cannot perform any action
    private void setPlayerIsInBarPopup(int playerToStop) {

        try {
            ((Mafia) playerInfo.get(playerToStop)).setIsTargetSelected(true);
            ((Doctor) playerInfo.get(playerToStop)).setIsTargetSelected(true);
            ((Bodyguard) playerInfo.get(playerToStop)).setIsTargetSelected(true);
            ((Vigilante) playerInfo.get(playerToStop)).setIsTargetSelected(true);
            ((Detective) playerInfo.get(playerToStop)).setIsTargetSelected(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        VBox inBarView = new VBox(20);
        inBarView.setPrefHeight(300);
        inBarView.setPrefWidth(411);
        inBarView.setTranslateY(300);
        inBarView.setPadding(new Insets(20, 0, 0, 0));
        inBarView.setStyle("-fx-background-color: black;");
        inBarView.setAlignment(Pos.TOP_CENTER);

        Label label = new Label("You cannot perform anything tonight.\n" +
                "You have been stopped by the barman");
        label.setFont(new Font("Arial", 20));
        label.setTextFill(Paint.valueOf("white"));

        inBarView.getChildren().addAll(label);

        popupWindows.get(
                playerInfo.get(playerToStop).getRole()
        ).getChildren().addAll(inBarView);

    }

    // assign each popup windows to corresponding player based on role
    private void initPlayersButtons() {

        setButtonsTexts();
        initButtonsVisibility(MafiaApp.getTotalPlayers());
        initPopupsSources();

        player1.setOnAction(e -> popupWindows.get(playerInfo.get(0).getRole()).setVisible(true));
        player2.setOnAction(e -> popupWindows.get(playerInfo.get(1).getRole()).setVisible(true));
        player3.setOnAction(e -> popupWindows.get(playerInfo.get(2).getRole()).setVisible(true));
        player4.setOnAction(e -> popupWindows.get(playerInfo.get(3).getRole()).setVisible(true));
        player5.setOnAction(e -> popupWindows.get(playerInfo.get(4).getRole()).setVisible(true));

        if (MafiaApp.getTotalPlayers() > 5) {
            switch (MafiaApp.getTotalPlayers()) {
                case 6:
                    player6.setOnAction(e -> popupWindows.get(playerInfo.get(5).getRole()).setVisible(true));
                    break;
                case 7:
                    player6.setOnAction(e -> popupWindows.get(playerInfo.get(5).getRole()).setVisible(true));
                    player7.setOnAction(e -> popupWindows.get(playerInfo.get(6).getRole()).setVisible(true));
                    break;
                case 8:
                    player6.setOnAction(e -> popupWindows.get(playerInfo.get(5).getRole()).setVisible(true));
                    player7.setOnAction(e -> popupWindows.get(playerInfo.get(6).getRole()).setVisible(true));
                    player8.setOnAction(e -> popupWindows.get(playerInfo.get(7).getRole()).setVisible(true));
                    break;
                case 9:
                    player6.setOnAction(e -> popupWindows.get(playerInfo.get(5).getRole()).setVisible(true));
                    player7.setOnAction(e -> popupWindows.get(playerInfo.get(6).getRole()).setVisible(true));
                    player8.setOnAction(e -> popupWindows.get(playerInfo.get(7).getRole()).setVisible(true));
                    player9.setOnAction(e -> popupWindows.get(playerInfo.get(8).getRole()).setVisible(true));
                    break;
                default:
                    player6.setOnAction(e -> popupWindows.get(playerInfo.get(5).getRole()).setVisible(true));
                    player7.setOnAction(e -> popupWindows.get(playerInfo.get(6).getRole()).setVisible(true));
                    player8.setOnAction(e -> popupWindows.get(playerInfo.get(7).getRole()).setVisible(true));
                    player9.setOnAction(e -> popupWindows.get(playerInfo.get(8).getRole()).setVisible(true));
                    player10.setOnAction(e -> popupWindows.get(playerInfo.get(9).getRole()).setVisible(true));
                    break;
            }

        }

    }

    // assign each popup windows to corresponding player based on role
    private void initPopupsSources() {
        popupWindows.get(playerInfo.get(0).getRole()).setSource(player1);
        popupWindows.get(playerInfo.get(1).getRole()).setSource(player2);
        popupWindows.get(playerInfo.get(2).getRole()).setSource(player3);
        popupWindows.get(playerInfo.get(3).getRole()).setSource(player4);
        popupWindows.get(playerInfo.get(4).getRole()).setSource(player5);
        if (MafiaApp.getTotalPlayers() > 5) {
            switch (MafiaApp.getTotalPlayers()) {
                case 6:
                    popupWindows.get(playerInfo.get(5).getRole()).setSource(player6);
                    break;
                case 7:
                    popupWindows.get(playerInfo.get(5).getRole()).setSource(player6);
                    popupWindows.get(playerInfo.get(6).getRole()).setSource(player7);
                    break;
                case 8:
                    popupWindows.get(playerInfo.get(5).getRole()).setSource(player6);
                    popupWindows.get(playerInfo.get(6).getRole()).setSource(player7);
                    popupWindows.get(playerInfo.get(7).getRole()).setSource(player8);
                    break;
                case 9:
                    popupWindows.get(playerInfo.get(5).getRole()).setSource(player6);
                    popupWindows.get(playerInfo.get(6).getRole()).setSource(player7);
                    popupWindows.get(playerInfo.get(7).getRole()).setSource(player8);
                    popupWindows.get(playerInfo.get(8).getRole()).setSource(player9);
                    break;
                default:
                    popupWindows.get(playerInfo.get(5).getRole()).setSource(player6);
                    popupWindows.get(playerInfo.get(6).getRole()).setSource(player7);
                    popupWindows.get(playerInfo.get(7).getRole()).setSource(player8);
                    popupWindows.get(playerInfo.get(8).getRole()).setSource(player9);
                    popupWindows.get(playerInfo.get(9).getRole()).setSource(player10);
                    break;
            }
        }
    }

    // hides extra buttons if there are less that 10 players.
    // 10 buttons are displayed by default in the fxml file
    private void initButtonsVisibility(int numTotalPlayers) {
        if (numTotalPlayers > 5) {
            switch (numTotalPlayers) {
                case 6:
                    player7.setVisible(false);
                    player7Icon.setVisible(false);
                    player8.setVisible(false);
                    player8Icon.setVisible(false);
                    player9.setVisible(false);
                    player9Icon.setVisible(false);
                    player10.setVisible(false);
                    player10Icon.setVisible(false);
                    break;
                case 7:
                    player8.setVisible(false);
                    player8Icon.setVisible(false);
                    player9.setVisible(false);
                    player9Icon.setVisible(false);
                    player10.setVisible(false);
                    player10Icon.setVisible(false);
                    break;
                case 8:
                    player9.setVisible(false);
                    player9Icon.setVisible(false);
                    player10.setVisible(false);
                    player10Icon.setVisible(false);
                    break;
                case 9:
                    player10.setVisible(false);
                    player10Icon.setVisible(false);
                    break;
            }
        } else {
            player6.setVisible(false);
            player6Icon.setVisible(false);
            player7.setVisible(false);
            player7Icon.setVisible(false);
            player8.setVisible(false);
            player8Icon.setVisible(false);
            player9.setVisible(false);
            player9Icon.setVisible(false);
            player10.setVisible(false);
            player10Icon.setVisible(false);
        }
    }

    // set each players button text to the name of player associated to that button
    private void setButtonsTexts() {

        player1.setText(playerInfo.get(0).getName());
        player2.setText(playerInfo.get(1).getName());
        player3.setText(playerInfo.get(2).getName());
        player4.setText(playerInfo.get(3).getName());
        player5.setText(playerInfo.get(4).getName());

        if (MafiaApp.getTotalPlayers() > 5) {
            switch (MafiaApp.getTotalPlayers()) {
                case 6:
                    player6.setText(playerInfo.get(5).getName());
                    break;
                case 7:
                    player6.setText(playerInfo.get(5).getName());
                    player7.setText(playerInfo.get(6).getName());
                    break;
                case 8:
                    player6.setText(playerInfo.get(5).getName());
                    player7.setText(playerInfo.get(6).getName());
                    player8.setText(playerInfo.get(7).getName());
                    break;
                case 9:
                    player6.setText(playerInfo.get(5).getName());
                    player7.setText(playerInfo.get(6).getName());
                    player8.setText(playerInfo.get(7).getName());
                    player9.setText(playerInfo.get(8).getName());
                    break;
                default:
                    player6.setText(playerInfo.get(5).getName());
                    player7.setText(playerInfo.get(6).getName());
                    player8.setText(playerInfo.get(7).getName());
                    player9.setText(playerInfo.get(8).getName());
                    player10.setText(playerInfo.get(9).getName());
                    break;
            }
        }

    }

    private void initPlayersPopup() {

        List<Player> players = playerInfo;
        String role;
        for (int count = 0; count < MafiaApp.getTotalPlayers(); count++) {

            role = players.get(count).getRole();
            JFXPopup roleInfoPopup = createPopup(count);
            popupWindows.put(role, roleInfoPopup);
            gameSceneView.getChildren().add(roleInfoPopup);

        }

    }

    // temporary layout for individual popup windows
    private JFXPopup createPopup(int playerPosition) {

        List<Player> players = playerInfo;
        String role = players.get(playerPosition).getRole();

        Image back = new Image(getClass().getResourceAsStream("../../images/backArrow.png"));
        ImageView backArrow = new ImageView(back);
        backArrow.setFitHeight(24);
        backArrow.setFitWidth(39);
        backArrow.setOpacity(0.46);
        backArrow.setPickOnBounds(true);

        Button backButton = new Button();
        backButton.setGraphic(backArrow);
        backButton.setStyle("-fx-background-color: transparent;");

        Label roleLabel = new Label(role);
        roleLabel.setAlignment(Pos.CENTER);
        roleLabel.setPrefWidth(300);
        roleLabel.setTextAlignment(TextAlignment.CENTER);
        roleLabel.setTranslateX(55);
        roleLabel.setTranslateY(50);
        roleLabel.setStyle("-fx-font-family: Arial; -fx-font-size: 30px; -fx-text-fill: white;");

        Label roleDesc = new Label("What you can do: \n" + players.get(playerPosition).getRoleInfo()
                + "\n\nYour goal: \n" + players.get(playerPosition).getGoal());
        roleDesc.setPrefHeight(200);
        roleDesc.setPrefWidth(318);
        roleDesc.setTextAlignment(TextAlignment.CENTER);
        roleDesc.setTranslateX(45);
        roleDesc.setTranslateY(110);
        roleDesc.setStyle("-fx-font-family: Arial; -fx-font-size: 20px; -fx-text-fill: white;");
        roleDesc.setWrapText(true);

        JFXPopup roleInfoPopup = new JFXPopup();
        roleInfoPopup.setStyle("-fx-background-color: black;");
        roleInfoPopup.setPrefHeight(600);
        roleInfoPopup.setPrefWidth(411);
        roleInfoPopup.getChildren().addAll(backButton, roleLabel, roleDesc);

        // display mafia members to other mafia members
        if (playerInfo.size() > 5) {
            if (role.contains("Mafia")) {
                Label mafias = new Label();
                mafias.setTextAlignment(TextAlignment.CENTER);
                mafias.setTranslateX(20);
                mafias.setTranslateY(320);
                mafias.setStyle("-fx-font-family: Arial; -fx-font-size: 17px; -fx-text-fill: white;" +
                        "-fx-border-color: white;");
                String members = "";
                for (int index = 0; index < MafiaApp.mafiaMembers.size(); index++) {
                    members += MafiaApp.mafiaMembers.get(index) + ((index == MafiaApp.mafiaMembers.size() - 1)
                            ? "." : ", ");
                }
                mafias.setText("Mafia members: " + members);
                roleInfoPopup.getChildren().add(mafias);
            }
        }
        backButton.setOnAction(e -> roleInfoPopup.setVisible(false));
        return roleInfoPopup;
    }

    private void initSettingsPopup() {

        JFXButton quitGameButton = new JFXButton("Quit game");
        quitGameButton.setPadding(new Insets(10));
        quitGameButton.setTextFill(Paint.valueOf("white"));

        JFXButton instructionsButton = new JFXButton("Instructions");
        instructionsButton.setPadding(new Insets(10));
        instructionsButton.setTextFill(Paint.valueOf("white"));

        VBox vbox = new VBox(instructionsButton, quitGameButton);
        vbox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-border-color: white;");

        quitGameButton.setOnAction(e -> {
            settingsPopup.setVisible(false);
            confirmQuitGame();
        });

        settingsPopup.setContent(vbox);
        settingsPopup.setSource(settingsButton);

    }

    // shows a confirm quit game dialog if the player wishes to quit the game
    private void confirmQuitGame() {

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: black;");
        layout.setPrefHeight(600);
        layout.setPrefWidth(411);

        Label title = new Label("QUIT GAME");
        title.setFont(new Font("Arial", 30));
        title.setTextFill(Paint.valueOf("white"));

        Label body = new Label("Are you sure you want to guit the game?");
        body.setTextFill(Paint.valueOf("white"));
        body.setFont(new Font("Arial", 15));

        JFXButton noButton = new JFXButton("NO");
        noButton.setStyle("-fx-background-color: transparent; -fx-border-color: white;");
        noButton.setFont(new Font("Arial", 15));
        noButton.setTextFill(Paint.valueOf("white"));
        noButton.setOnAction(e -> gameSceneView.getChildren().remove(layout));

        JFXButton yesButton = new JFXButton("YES");
        yesButton.setStyle("-fx-background-color: transparent; -fx-border-color: white;");
        yesButton.setFont(new Font("Arial", 15));
        yesButton.setTextFill(Paint.valueOf("white"));
        yesButton.setOnAction(e -> {

            playerInfo.clear();
            popupWindows.clear();
            MafiaApp.mafiaMembers.clear();
            goToMainMenu();
        });

        layout.getChildren().addAll(title, body, yesButton, noButton);

        gameSceneView.getChildren().add(layout);

    }

    /**
     * This method will kill one player each day, depending on who the players vote out.
     */
    private void dayCycle() {

        updateScene();
        System.out.println("*DAY TIME*");
        centralButton.setVisible(false);
        centerLabel.setText("DAY\nTIME\n\nSelect a player to be lynched");

        try {
            player1.setOnAction(e -> showLynchPlayerDialog(playerInfo.get(0)));
            player2.setOnAction(e -> showLynchPlayerDialog(playerInfo.get(1)));
            player3.setOnAction(e -> showLynchPlayerDialog(playerInfo.get(2)));
            player4.setOnAction(e -> showLynchPlayerDialog(playerInfo.get(3)));
            player5.setOnAction(e -> showLynchPlayerDialog(playerInfo.get(4)));
            player6.setOnAction(e -> showLynchPlayerDialog(playerInfo.get(5)));
            player7.setOnAction(e -> showLynchPlayerDialog(playerInfo.get(6)));
            player8.setOnAction(e -> showLynchPlayerDialog(playerInfo.get(7)));
            player9.setOnAction(e -> showLynchPlayerDialog(playerInfo.get(8)));
            player10.setOnAction(e -> showLynchPlayerDialog(playerInfo.get(9)));
        } catch (Exception e) {
            System.out.println("Player don't exist");
            e.getMessage();
        }

    }

    // update buttons visibility whenever someone dies
    private void updateScene() {

        for (Player player : playerInfo) {
            if (player.getStatus() == PlayerStatus.DEAD || player.getStatus() == PlayerStatus.DEAD_FOR_MORE_THAN_ONE_TURN) {
                switch (player.getPlayPosition()) {
                    case 0:
                        player1.setVisible(false);
                        player1Icon.setVisible(false);
                        break;
                    case 1:
                        player2.setVisible(false);
                        player2Icon.setVisible(false);
                        break;
                    case 2:
                        player3.setVisible(false);
                        player3Icon.setVisible(false);
                        break;
                    case 3:
                        player4.setVisible(false);
                        player4Icon.setVisible(false);
                        break;
                    case 4:
                        player5.setVisible(false);
                        player5Icon.setVisible(false);
                        break;
                    case 5:
                        player6.setVisible(false);
                        player6Icon.setVisible(false);
                        break;
                    case 6:
                        player7.setVisible(false);
                        player7Icon.setVisible(false);
                        break;
                    case 7:
                        player8.setVisible(false);
                        player8Icon.setVisible(false);
                        break;
                    case 8:
                        player9.setVisible(false);
                        player9Icon.setVisible(false);
                        break;
                    case 9:
                        player10.setVisible(false);
                        player10Icon.setVisible(false);
                        break;
                }
            }
        }

    }

    private void gameOver(boolean mafiaWin) {

        System.out.println("GAME OVER");

        // clear collections
        playerInfo.clear();
        mafiaMembers.clear();
        popupWindows.clear();

        StackPane gameOverLayout = new StackPane();
        gameOverLayout.setPrefWidth(411);
        gameOverLayout.setPrefHeight(600);
        gameOverLayout.setStyle("-fx-background-color: black;");

        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setFont(new Font("Arial", 40));
        gameOverLabel.setTextFill(Paint.valueOf("white"));
        gameOverLabel.setTranslateY(-100);

        Label winner = new Label(mafiaWin ? "The Mafia have taken control of the town" :
                "The Mafia have all been killed");
        winner.setFont(new Font("Arial", 15));
        winner.setTextFill(Paint.valueOf("white"));

        JFXButton goToMainMenu = new JFXButton("Main menu");
        goToMainMenu.setFont(new Font("Arial", 18));
        goToMainMenu.setTextFill(Paint.valueOf("white"));
        goToMainMenu.setStyle("-fx-border-color: white;");
        goToMainMenu.setTranslateY(100);
        goToMainMenu.setOnAction(e -> goToMainMenu());

        gameOverLayout.getChildren().addAll(gameOverLabel, winner, goToMainMenu);
        gameSceneView.getChildren().setAll(gameOverLayout);

    }

    private void goToMainMenu() {
        try {
            StackPane mainMenu = FXMLLoader.load(getClass().getResource("../ui/mainMenu.fxml"));
            gameSceneView.getChildren().setAll(mainMenu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showLynchPlayerDialog(Player player) {

        JFXDialogLayout alertBoxContent = new JFXDialogLayout();
        alertBoxContent.setHeading(new Text("Majority vote"));
        alertBoxContent.setBody(new Text("Are you sure you want to lynch " + player.getName() + "?"));

        JFXDialog alertBox = new JFXDialog(gameSceneView, alertBoxContent, JFXDialog.DialogTransition.CENTER);
        JFXButton noButton = new JFXButton("NO");
        noButton.setTranslateX(-300);
        JFXButton yesButton = new JFXButton("YES");

        yesButton.setOnAction(e -> {
            lynchPlayer(player);
            alertBox.close();
        });
        noButton.setOnAction(e -> alertBox.close());

        alertBoxContent.setActions(yesButton, noButton);
        alertBox.show();

    }

    private void lynchPlayer(Player player) {

        playerInfo.get(player.getPlayPosition()).setStatus(PlayerStatus.DEAD);
        System.out.println("PLAYER " + (player.getPlayPosition() + 1) + " got lynched");
        if (player instanceof Mafia) {
            int mafiaToRemove = -1;
            for (int count = 0; count < MafiaApp.mafiaMembers.size(); count++) {
                if (player.getName().equals(MafiaApp.mafiaMembers.get(count))) {
                    mafiaToRemove = count;
                }
            }
            MafiaApp.mafiaMembers.remove(mafiaToRemove);
        }
        centerLabel.setText(playerInfo.get(player.getPlayPosition()).getName() + " has been lynched.");

        // disable buttons temporarily
        try {
            player1.setDisable(true);
            player2.setDisable(true);
            player3.setDisable(true);
            player4.setDisable(true);
            player5.setDisable(true);
            player6.setDisable(true);
            player7.setDisable(true);
            player8.setDisable(true);
            player9.setDisable(true);
            player10.setDisable(true);
        } catch (Exception e) {
            System.out.println("something went wong");
        }

        // hide the lynched player's button and icon from the scene graph, remove its popup
        JFXPopup popupToRemove;
        try {
            switch (player.getPlayPosition()) {
                case 0:
                    player1.setVisible(false);
                    player1Icon.setVisible(false);
                    popupWindows.remove(playerInfo.get(0).getRole());
                    popupToRemove = popupWindows.get(playerInfo.get(0).getRole());
                    gameSceneView.getChildren().remove(popupToRemove);
                    break;
                case 1:
                    player2.setVisible(false);
                    player2Icon.setVisible(false);
                    popupWindows.remove(playerInfo.get(1).getRole());
                    popupToRemove = popupWindows.get(playerInfo.get(1).getRole());
                    gameSceneView.getChildren().remove(popupToRemove);
                    break;
                case 2:
                    player3.setVisible(false);
                    player3Icon.setVisible(false);
                    popupWindows.remove(playerInfo.get(2).getRole());
                    popupToRemove = popupWindows.get(playerInfo.get(2).getRole());
                    gameSceneView.getChildren().remove(popupToRemove);
                    break;
                case 3:
                    player4.setVisible(false);
                    player4Icon.setVisible(false);
                    popupWindows.remove(playerInfo.get(3).getRole());
                    popupToRemove = popupWindows.get(playerInfo.get(3).getRole());
                    gameSceneView.getChildren().remove(popupToRemove);
                    break;
                case 4:
                    player5.setVisible(false);
                    player5Icon.setVisible(false);
                    popupWindows.remove(playerInfo.get(4).getRole());
                    popupToRemove = popupWindows.get(playerInfo.get(4).getRole());
                    gameSceneView.getChildren().remove(popupToRemove);
                    break;
                case 5:
                    player6.setVisible(false);
                    player6Icon.setVisible(false);
                    popupWindows.remove(playerInfo.get(5).getRole());
                    popupToRemove = popupWindows.get(playerInfo.get(5).getRole());
                    gameSceneView.getChildren().remove(popupToRemove);
                    break;
                case 6:
                    player7.setVisible(false);
                    player7Icon.setVisible(false);
                    popupWindows.remove(playerInfo.get(6).getRole());
                    popupToRemove = popupWindows.get(playerInfo.get(6).getRole());
                    gameSceneView.getChildren().remove(popupToRemove);
                    break;
                case 7:
                    player8.setVisible(false);
                    player8Icon.setVisible(false);
                    popupWindows.remove(playerInfo.get(7).getRole());
                    popupToRemove = popupWindows.get(playerInfo.get(7).getRole());
                    gameSceneView.getChildren().remove(popupToRemove);
                    break;
                case 8:
                    player9.setVisible(false);
                    player9Icon.setVisible(false);
                    popupWindows.remove(playerInfo.get(8).getRole());
                    popupToRemove = popupWindows.get(playerInfo.get(8).getRole());
                    gameSceneView.getChildren().remove(popupToRemove);
                    break;
                case 9:
                    player10.setVisible(false);
                    player10Icon.setVisible(false);
                    popupWindows.remove(playerInfo.get(9).getRole());
                    popupToRemove = popupWindows.get(playerInfo.get(9).getRole());
                    gameSceneView.getChildren().remove(popupToRemove);
                    break;
            }
        } catch (Exception e) {
            e.getMessage();
        }

        boolean isGameOver = gameSceneModel.getGame().checkGameOver();
        if (isGameOver)
            gameOver(gameSceneModel.getGame().getMafiaWin());

        centralButton.setVisible(true);
        centralButton.setText("Continue");
        centralButton.setOnAction(e -> nightCycle());

    }

    /**
     * This method runs the cycle for selecting a target for each player every night
     * If the status of the player is 1 or 4 (Both dead) will skip them as both a player an a target for each player
     */
    private void nightCycle() {

        updateScene();
        resetNightCyclePopups();
        System.out.println("*NIGHT TIME*");

        centerLabel.setText("NIGHT \nTIME");
        centralButton.setOnAction(e -> getNightSceneChoices());

        try {
            player1.setDisable(false);
            player2.setDisable(false);
            player3.setDisable(false);
            player4.setDisable(false);
            player5.setDisable(false);
            player6.setDisable(false);
            player7.setDisable(false);
            player8.setDisable(false);
            player9.setDisable(false);
            player10.setDisable(false);
        } catch (Exception e) {
            e.getMessage();
        }

        try {
            player1.setOnAction(e -> popupWindows.get(playerInfo.get(0).getRole()).setVisible(true));
            player2.setOnAction(e -> popupWindows.get(playerInfo.get(1).getRole()).setVisible(true));
            player3.setOnAction(e -> popupWindows.get(playerInfo.get(2).getRole()).setVisible(true));
            player4.setOnAction(e -> popupWindows.get(playerInfo.get(3).getRole()).setVisible(true));
            player5.setOnAction(e -> popupWindows.get(playerInfo.get(4).getRole()).setVisible(true));
            player6.setOnAction(e -> popupWindows.get(playerInfo.get(5).getRole()).setVisible(true));
            player7.setOnAction(e -> popupWindows.get(playerInfo.get(6).getRole()).setVisible(true));
            player8.setOnAction(e -> popupWindows.get(playerInfo.get(7).getRole()).setVisible(true));
            player9.setOnAction(e -> popupWindows.get(playerInfo.get(8).getRole()).setVisible(true));
            player10.setOnAction(e -> popupWindows.get(playerInfo.get(9).getRole()).setVisible(true));
        } catch (Exception e2) {
            e2.getMessage();
        }

    }

    // checks if the players who can target at night (and are alive) have all chosen their target
    private void getNightSceneChoices() {

        boolean proceed = true;
        for (Player player : playerInfo) {
            if (player instanceof Mafia) {
                proceed = ((Mafia) player).isTargetSelected();
            } else if (player instanceof Doctor) {
                proceed = ((Doctor) player).isTargetSelected();
            } else if (player instanceof Bodyguard) {
                proceed = ((Bodyguard) player).isTargetSelected();
            } else if (player instanceof Vigilante) {
                proceed = ((Vigilante) player).isTargetSelected();
            } else if (player instanceof Detective) {
                proceed = ((Detective) player).isTargetSelected();
            }
        }
        if (proceed) {
            String story = gameSceneModel.getGame().resetStatus();
            displayStory(story);
        } else
            showMissingChoicesDialog();

    }

    private void showMissingChoicesDialog() {

        JFXDialogLayout alertBoxContent = new JFXDialogLayout();
        alertBoxContent.setHeading(new Text("Missing Selections"));
        alertBoxContent.setBody(new Text("One or more player have not yet made a selection."));

        JFXDialog alertBox = new JFXDialog(gameSceneView, alertBoxContent, JFXDialog.DialogTransition.CENTER);
        JFXButton okButton = new JFXButton("Go back");
        okButton.setOnAction(e -> alertBox.close());

        alertBoxContent.setActions(okButton);
        alertBox.show();

    }

    // displays the story of what happened last nyt
    private void displayStory(String story) {

        StackPane storyLayout = new StackPane();
        storyLayout.setPrefHeight(600);
        storyLayout.setPrefWidth(411);
        storyLayout.setStyle("-fx-background-color: black;");

        Label storyLabel = new Label((story.isEmpty()) ? "No one died yay." : story);
        storyLabel.setPrefWidth(300);
        storyLabel.setPrefHeight(500);
        storyLabel.setWrapText(true);
        storyLabel.setTextFill(Paint.valueOf("white"));
        storyLabel.setTranslateY(-100);
        storyLabel.setFont(new Font("Arial", 13));

        JFXButton closeButton = new JFXButton("Close");
        closeButton.setFont(new Font("Arial", 15));
        closeButton.setStyle("-fx-background-color: transparent; -fx-border-color: white");
        closeButton.setTextFill(Paint.valueOf("white"));
        closeButton.setTranslateY(250);

        closeButton.setOnAction(e -> {
            boolean isGameOver = gameSceneModel.getGame().checkGameOver();
            if (isGameOver)
                gameOver(gameSceneModel.getGame().getMafiaWin());

            gameSceneView.getChildren().removeAll(storyLayout, storyLabel, closeButton);
            dayCycle();
        });

        storyLayout.getChildren().addAll(storyLabel, closeButton);
        gameSceneView.getChildren().addAll(storyLayout);
    }

    public GameSceneModel getGameSceneModel() {
        return gameSceneModel;
    }

    public void setGameSceneModel(GameSceneModel gameSceneModel) {
        this.gameSceneModel = gameSceneModel;
    }

}
