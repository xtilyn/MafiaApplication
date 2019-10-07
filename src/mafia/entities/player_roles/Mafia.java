package mafia.entities.player_roles;

public class Mafia extends Player {

    private String mafiaType;
    private int playerTarget;
    private boolean isTargetSelected;

    public Mafia(String mafiaType) {
        this.mafiaType = mafiaType;
        super.setRole(mafiaType);

        if (mafiaType.equals("Mafia: Hitman")) {
            super.setRoleInfo("May kill someone each night");
            super.setGoal("To make the majority of the town mafia members");
        } else if (mafiaType.equals("Mafia- Barman")) {
            super.setRoleInfo("May stop the action of another player each night (Can't go same person each night)");
            super.setGoal("To make the majority of the town mafia members");
        } else { // "Mafiaboss- GodFather"
            super.setRoleInfo("Hidden from the Detective. Can send a message to another Mafia memeber each night");
            super.setGoal("To make the majority of the town mafia members");
        }
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
