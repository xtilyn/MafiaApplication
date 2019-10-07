package mafia.entities.player_roles;

public class Lyncher extends Player {

    public Lyncher() {
        super.setRole("Lyncher");
        super.setRoleInfo("Do nothing at night");
        super.setGoal("To Lynch a specific player to win solo win the game | ");
    }

}
