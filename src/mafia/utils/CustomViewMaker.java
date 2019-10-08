package mafia.utils;

import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import mafia.entities.data_types.PlayerStatus;
import mafia.entities.player_roles.Player;

public class CustomViewMaker {

    public static VBox createNightCyclePopup() {
        VBox nightCyclePlayerPopup = new VBox(10);
        nightCyclePlayerPopup.setStyle("-fx-background-color: black;");
        nightCyclePlayerPopup.setPrefHeight(300);
        nightCyclePlayerPopup.setPrefWidth(411);
        nightCyclePlayerPopup.setAlignment(Pos.TOP_CENTER);
        nightCyclePlayerPopup.setPadding(new Insets(20, 0, 0, 0));
        nightCyclePlayerPopup.setTranslateY(300);
        return nightCyclePlayerPopup;
    }

    public static Label createLabel(String message) {
        Label label = new Label(message);
        label.setFont(new Font("Arial", 20));
        label.setTextFill(Paint.valueOf("white"));
        return label;
    }

    public static Label createLabel(String message, TextAlignment textAlignment) {
        Label label = new Label(message);
        label.setFont(new Font("Arial", 20));
        label.setTextFill(Paint.valueOf("white"));
        label.setTextAlignment(textAlignment);
        return label;
    }

    public static Label createTargetLabel() {
        Label target = new Label();
        target.setTextFill(Paint.valueOf("white"));
        target.setTextAlignment(TextAlignment.CENTER);
        target.setFont(new Font("Arial", 20));
        return target;
    }

    public static JFXButton createConfirmButton(String title) {
        JFXButton enterButton = new JFXButton(title);
        enterButton.setFont(new Font("Arial", 15));
        enterButton.setTextFill(Paint.valueOf("white"));
        enterButton.setStyle("-fx-background-color: transparent; -fx-border-color: white;");
        enterButton.setVisible(false);
        return enterButton;
    }

    public static ListView<String> createTargetsListView(String[] targetNames, ChangeListener<String> changeListener) {
        ListView<String> availableTargets = new ListView<>();
        availableTargets.setMaxHeight(150);
        availableTargets.getItems().add("No one");
        availableTargets.getSelectionModel().selectedItemProperty().addListener(changeListener);

        for (String name : targetNames) {
            availableTargets.getItems().add(name);
        }

        return availableTargets;
    }

    public static Label createSimpleLabel(String message) {
        Label simpleLabel = new Label(message);
        simpleLabel.setTextFill(Paint.valueOf("white"));
        return simpleLabel;
    }

}
