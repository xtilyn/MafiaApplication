package mafia.entities.player_roles;

public class Townie extends Player {

    public Townie() {
        super.setRole("Townie");
        super.setRoleInfo("Do nothing at night");
        super.setGoal("To have only town members left");
    }

}
