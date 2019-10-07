package mafia.entities.player_roles;

public class Vigilante extends Player {
    private int playerTarget;
    private boolean isTargetSelected;

    public Vigilante() {
        super.setRole("Vigilante");
        super.setRoleInfo("May kill new person each night, but is trying to kill Mafia. Note: Do not have to kill someone each night");
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
