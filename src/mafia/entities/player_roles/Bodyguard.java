package mafia.entities.player_roles;

public class Bodyguard extends Player{
    private int playerTarget;
    private boolean isTargetSelected;

    public Bodyguard() {
        super.setRole("Bodyguard");
        super.setRoleInfo("May save another person by stepping infront of the bullet per night. (You will die in their place)");
        super.setGoal("To have only town members left");
    }

    public void setPlayerTarget(int playerTarget){
        this.playerTarget = playerTarget;
    }

    public void setIsTargetSelected(boolean selection) {
        isTargetSelected = selection;
    }

    public boolean isTargetSelected() {
        return isTargetSelected;
    }
}
