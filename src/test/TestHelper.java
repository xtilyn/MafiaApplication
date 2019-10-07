package test;

import mafia.entities.player_roles.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestHelper {

    public static List<Player> getDummyPlayers() {

        int i = 0;
        List<Player> players = getDummyRoles();
        for (String name : getDummyNames()) {
            Player newPlayer = players.get(i);
            newPlayer.setName(name);
            newPlayer.setPlayPosition(i);
            players.add(newPlayer);
            i++;
        }
        return players;
    }

    public static List<Player> getDummyRoles() {
        return new ArrayList<>(
                Arrays.asList(
                        new Townie(),
                        new Detective(),
                        new Doctor(),
                        new Mafia("Mafia: Hitman"),
                        new Bodyguard()
                )
        );
    }

    public static String[] getDummyNames() {
        return new String[]{
                "john",
                "sam",
                "tim",
                "hortons",
                "mcdonaldo"
        };
    }

}
