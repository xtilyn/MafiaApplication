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
    }

    @AfterAll
    public static void cleanUp() {
        mafiaApp = null;
        mainMenuModel = null;
    }

    @Test
    public void shouldAssignAtLeastOneMafiaRole() {
        MafiaApp.playerInfo = TestHelper.getDummyPlayers();
        mainMenuModel.setPlayerNames(TestHelper.getDummyNames());
        mainMenuModel.assignRoles();
        assert (MafiaApp.mafiaMembers.size() > 0);
    }

    @Test
    public void allPlayersShouldHaveARole() {
        mainMenuModel.setPlayerNames(TestHelper.getDummyNames());
        mainMenuModel.assignRoles();
        int roleCount = (int) MafiaApp.playerInfo.stream().filter(player -> !player.getRoleInfo().isEmpty()).count();
        System.out.println(roleCount);

        assert (roleCount == MafiaApp.getTotalPlayers());
    }

}
