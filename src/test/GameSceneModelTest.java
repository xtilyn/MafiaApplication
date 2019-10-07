package test;

import mafia.MafiaApp;
import mafia.entities.Action;
import mafia.entities.player_roles.Player;
import mafia.models.GameSceneModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.function.IntConsumer;

public class GameSceneModelTest {

    private static MafiaApp mafiaApp;
    private static GameSceneModel gameSceneModel;

    @BeforeAll
    public static void initialize() {
        mafiaApp = new MafiaApp();
        gameSceneModel = new GameSceneModel(mafiaApp);
        MafiaApp.setTotalPlayers(5);
        MafiaApp.playerInfo = TestHelper.getDummyPlayers();
    }

    @AfterAll
    public static void cleanUp() {
        mafiaApp = null;
        gameSceneModel = null;
    }

    @Test
    public void shouldAssignPlayersPositionsInOrder() {
        assignDummyRoles();

        gameSceneModel.initJobPositionMap();
        int expectedSum = 10; // sum of all indices from 0 - 4 inclusive (5 players)
        int sum = 0;
        for (int i = 0; i<MafiaApp.getTotalPlayers(); i++) {
            String playerRole = MafiaApp.playerInfo.get(i).getRole();
            sum += gameSceneModel.getJobPositionMap().get(playerRole);
        }
        assert(sum == expectedSum);
    }

    private void assignDummyRoles() {
        final int[] count = {0};
        MafiaApp.playerInfo.forEach(player -> {
            player.setRole(TestHelper.getDummyRoles()[count[0]]);
            count[0]++;
        });
    }

    @Test
    public void shouldKillPlayerThatsNotInBar_IfTargetedByMafiaHitman() {
        gameSceneModel.initJobPositionMap();
        gameSceneModel.setAction(new Action(5, gameSceneModel.getJobPositionMap()));

        Player targetPlayer = MafiaApp.playerInfo.get(2); // choose anyone that's not a Mafia
        targetPlayer.setInBar(false);
        String targetName = targetPlayer.getName();

        gameSceneModel.targetPlayer(targetName, "Mafia: Hitman", (i) -> {});
        assert(targetPlayer.getStatus() == 1);
    }

}
