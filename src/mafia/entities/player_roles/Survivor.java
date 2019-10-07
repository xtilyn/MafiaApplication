package mafia.entities.player_roles;

public class Survivor extends Player {

    public Survivor() {
        super.setRole("Survivor");
        super.setRoleInfo("Do nothing at night");
        super.setGoal("To be the last town member left alive");
    }

}
