package test;

import mafia.MafiaApp;
import mafia.models.MainMenuModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class MainMenuModelTest {

    private static MafiaApp mafiaApp;
    private static MainMenuModel mainMenuModel;

    @BeforeAll
    public static void initialize() {
        mafiaApp = new MafiaApp();
        mainMenuModel = new MainMenuModel();
        MafiaApp.setTotalPlayers(5);
        MafiaApp.playerInfo = TestHelper.getDummyPlayers();
    }

    @AfterAll
    public static void cleanUp() {
        mafiaApp = null;
        mainMenuModel = null;
    }

    @Test
    public void shouldAssignAtLeastOneMafiaRole() {
        mainMenuModel.assignRoles();
        assert (MafiaApp.mafiaMembers.size() > 0);
    }

    @Test
    public void allPlayersShouldHaveARole() {
        mainMenuModel.assignRoles();
        int roleCount = (int) MafiaApp.playerInfo.stream().filter(player -> !player.getRole().isEmpty()).count();
        assert (roleCount == MafiaApp.getTotalPlayers());
    }

}
