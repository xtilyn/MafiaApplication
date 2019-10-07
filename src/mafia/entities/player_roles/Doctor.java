package mafia.entities.player_roles;

public class Doctor extends Player {
    private int playerTarget;
    private boolean isTargetSelected;

    public Doctor() {
        super.setRole("Doctor");
        super.setRoleInfo("May heal one player each night (Can't target same person two nights in a row)");
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
