package test;

import mafia.entities.player_roles.Player;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public static List<Player> getDummyPlayers() {
        String[] dummyNames = new String[]{
                "john",
                "sam",
                "tim",
                "hortons",
                "mcdonaldo"
        };
        int i = 0;
        List<Player> players = new ArrayList<>();
        for (String name : dummyNames) {
            Player newPlayer = new Player();
            newPlayer.setName(name);
            newPlayer.setPlayPosition(i);
            players.add(newPlayer);
            i++;
        }
        return players;
    }

    public static String[] getDummyRoles() {
        return new String[]{
                "Townie",
                "Detective",
                "Doctor",
                "Mafia: Hitman",
                "Bodyguard"
        };
    }

}
