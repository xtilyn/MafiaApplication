package mafia.entities.player_roles;

public class Detective extends Player {
    private int playerTarget;
    private boolean isTargetSelected;

    public Detective() {
        super.setRole("Detective");
        super.setRoleInfo("Reveals the team for one player per night");
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
