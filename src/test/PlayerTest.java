package test;

import mafia.entities.data_types.MafiaType;
import mafia.entities.player_roles.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class PlayerTest {

    @Test
    public void allRolesShouldExtendFromPlayer() {
        assert(new Bodyguard() instanceof Player &&
                new Detective() instanceof Player &&
                new Doctor() instanceof Player &&
                new Lyncher() instanceof Player &&
                new Mafia(MafiaType.MAFAIA_HITMAN) instanceof Player &&
                new Survivor() instanceof Player &&
                new Townie() instanceof Player &&
                new Vigilante() instanceof Player
                );
    }

    @Test
    public void rolesThatCanTargetShouldBeAbleToTargetSomeone() {
        Method bodyguard = null;
        Method detective = null;
        Method doctor = null;
        Method mafia = null;
        Method vigilante = null;
        try {
            bodyguard = Bodyguard.class.getMethod("setPlayerTarget", int.class);
            detective = Detective.class.getMethod("setPlayerTarget", int.class);
            doctor = Doctor.class.getMethod("setPlayerTarget", int.class);
            mafia = Mafia.class.getMethod("setPlayerTarget", int.class);
            vigilante = Vigilante.class.getMethod("setPlayerTarget", int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        assert (bodyguard != null &&
                detective != null &&
                doctor != null &&
                mafia != null &&
                vigilante != null);
    }

}
