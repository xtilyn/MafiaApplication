package test;

import mafia.entities.data_types.MafiaType;
import mafia.entities.data_types.PlayerStatus;
import mafia.entities.player_roles.Mafia;
import mafia.entities.player_roles.Player;
import mafia.entities.player_roles.Townie;
import org.junit.jupiter.api.Test;

public class CustomDataClassTest {

    @Test
    public void allMafiaShouldHaveCorrectMafiaTypeAndRole (){
        Mafia barman = new Mafia(MafiaType.MAFIA_BARMAN);
        Mafia hitman = new Mafia(MafiaType.MAFAIA_HITMAN);
        Mafia godfather = new Mafia(MafiaType.MAFIA_GODFATHER);
        assert (barman.getRole().equals("Mafia- Barman") && barman.getMafiaType() == MafiaType.MAFIA_BARMAN &&
                hitman.getRole().equals("Mafia: Hitman") && hitman.getMafiaType() == MafiaType.MAFAIA_HITMAN &&
                godfather.getRole().equals("Mafiaboss- GodFather") && godfather.getMafiaType() == MafiaType.MAFIA_GODFATHER);
    }

    @Test
    public void playerShouldHaveCorrectStatus() {
        Player player = new Townie();
        player.setStatus(PlayerStatus.DEAD);
        assert (player.getStatus() == PlayerStatus.DEAD);
    }
}
