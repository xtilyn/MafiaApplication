package test;

import mafia.entities.data_types.MafiaType;
import mafia.entities.player_roles.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestHelper {

    public static List<Player> getDummyPlayers(int numPlayers) {

        String[] names = getDummyNames();
        List<Player> players = getDummyRoles();
        for (int i = 0; i<numPlayers; i++) {
            Player newPlayer = players.get(i);
            newPlayer.setName(names[i]);
            newPlayer.setPlayPosition(i);
            players.add(newPlayer);
        }
        return players;
    }

    public static List<Player> getDummyRoles() {
        return new ArrayList<>(
                Arrays.asList(
                        new Townie(),
                        new Detective(),
                        new Doctor(),
                        new Mafia(MafiaType.MAFAIA_HITMAN),
                        new Bodyguard(),
                        new Survivor(),
                        new Mafia(MafiaType.MAFIA_BARMAN),
                        new Lyncher(),
                        new Mafia(MafiaType.MAFIA_GODFATHER),
                        new Vigilante()
                )
        );
    }

    public static String[] getDummyNames() {
        return new String[]{
                "john",
                "sam",
                "tim",
                "hortons",
                "mcdonaldo",
                "sara",
                "mcSara",
                "haha",
                "hehe",
                "hoho"
        };
    }

}
