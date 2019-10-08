package test;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import mafia.MafiaApp;
import mafia.controllers.GameSceneController;
import mafia.models.GameSceneModel;
import mafia.utils.CustomViewMaker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameSceneControllerTest {

    private GameSceneController gameSceneController;

    @BeforeEach
    public void initialize() {
        com.sun.javafx.application.PlatformImpl.startup(() -> {
        });
        gameSceneController = new GameSceneController();
        MafiaApp mafiaApp = new MafiaApp();
        gameSceneController.setGameSceneModel(new GameSceneModel(mafiaApp));
        MafiaApp.setTotalPlayers(10);
        MafiaApp.playerInfo = TestHelper.getDummyPlayers(10);
        gameSceneController.getGameSceneModel().initJobPositionMap();
    }

    @AfterEach
    public void cleanUp() {
        com.sun.javafx.application.PlatformImpl.exit();
        gameSceneController = null;
    }

    @Test
    public void popupsShouldHaveTheSameBaseCharacteristics() {
        VBox popupView = CustomViewMaker.createNightCyclePopup();
        performAndIgnoreException(() -> gameSceneController.resetVigilantePopup());
        performAndIgnoreException(() -> gameSceneController.resetBarmanPopup());
        performAndIgnoreException(() -> gameSceneController.resetBodyguardPopup());
        performAndIgnoreException(() -> gameSceneController.resetHitmanPopup());
        performAndIgnoreException(() -> gameSceneController.resetDoctorPopup());
        performAndIgnoreException(() -> gameSceneController.resetDetectivePopup());
        performAndIgnoreException(() -> gameSceneController.resetGodfatherPopup());


        assert (isTheSame(gameSceneController.vigilanteView, popupView) &&
                isTheSame(gameSceneController.barmanView, popupView) &&
                isTheSame(gameSceneController.bodyguardView, popupView) &&
                isTheSame(gameSceneController.hitmanView, popupView) &&
                isTheSame(gameSceneController.doctorView, popupView) &&
                isTheSame(gameSceneController.detectiveView, popupView) &&
                isTheSame(gameSceneController.godfatherView, popupView)
        );
    }

    private void performAndIgnoreException(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) { }
    }

    private boolean isTheSame(VBox box1, VBox box2) {
        return box1.getStyle().equals(box2.getStyle()) &&
                box1.getPrefHeight() == box2.getPrefHeight() &&
                box1.getPrefWidth() == box2.getPrefWidth() &&
                box1.getAlignment() == box2.getAlignment() &&
                box1.getTranslateY() == box2.getTranslateY();
    }

    @Test
    public void targetersPopupsShouldContainsTargetsListView() {
        performAndIgnoreException(() -> gameSceneController.resetVigilantePopup());
        assert (containsListView(gameSceneController.vigilanteView.getChildren()));
    }

    private boolean containsListView(ObservableList<Node> parent) {
        System.out.println(parent.toString());
        for (Node node : parent) {
            if (node instanceof ListView) return true;
        }
        return false;
    }

}
