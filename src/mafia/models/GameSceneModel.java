package mafia.models;

import mafia.entities.Action;
import mafia.MafiaApp;
import mafia.entities.player_roles.Player;

import java.util.HashMap;
import java.util.function.IntConsumer;

import static mafia.MafiaApp.playerInfo;

public class GameSceneModel {

    private MafiaApp game;
    private Action action;
    private HashMap<String, Integer> jobPositionMap = new HashMap<>();


    public GameSceneModel(MafiaApp game) {
        this.game = game;
    }


    // initialize map to find a player's position using their role
    public void initJobPositionMap() {
        for (Player player : playerInfo) {
            jobPositionMap.put(player.getRole(), player.getPlayPosition());
        }
    }

    // Targets the selected name in the listView from a specific popup based on role
    public void targetPlayer(String myTargetsName, String targetersRole, IntConsumer targetPlayerIsInBar) {
        int target = getPlayerPosition(myTargetsName);
        switch (targetersRole) {
            case "Mafia: Hitman":
                action.hitman(target);
                break;
            case "Doctor":
                action.doctor(target);
                break;
            case "Mafia- Barman":
                boolean memberOfCanTarget = action.barman(target);
                if (memberOfCanTarget)
                    targetPlayerIsInBar.accept(target);
                break;
            case "Bodyguard":
                action.bodyguard(target);
                break;
            case "Vigilante":
                action.vigilante(target);
                break;
        }
    }

    // gets player's position based on their name
    private int getPlayerPosition(String playerName) {
        int pos = 0;
        for (int count = 0; count < playerInfo.size(); count++) {
            if (playerInfo.get(count).getName().equals(playerName)) {
                pos = count;
            }
        }
        return pos;
    }

    // Roles who can target during the night
    private final String[] canTarget = {"Mafia: Hitman", "Doctor", "Mafia- Barman", "Bodyguard",
            "Vigilante", "Detective"};

    public String[] getRolesThatCanTarget() {
        return canTarget;
    }

    public MafiaApp getGame() {
        return game;
    }

    public void setGame(MafiaApp game) {
        this.game = game;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public HashMap<String, Integer> getJobPositionMap() {
        return jobPositionMap;
    }

    public void setJobPositionMap(HashMap<String, Integer> jobPositionMap) {
        this.jobPositionMap = jobPositionMap;
    }
}
