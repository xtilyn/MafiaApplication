package mafia.entities.player_roles;

import mafia.entities.data_types.MafiaType;

public class Mafia extends Player {

    private MafiaType mafiaType;
    private int playerTarget;
    private boolean isTargetSelected;

    public Mafia(MafiaType mafiaType) {
        this.mafiaType = mafiaType;

        if (mafiaType == MafiaType.MAFAIA_HITMAN) {
            super.setRole("Mafia: Hitman");
            super.setRoleInfo("May kill someone each night");
            super.setGoal("To make the majority of the town mafia members");
        } else if (mafiaType == MafiaType.MAFIA_BARMAN) {
            super.setRole("Mafia- Barman");
            super.setRoleInfo("May stop the action of another player each night (Can't go same person each night)");
            super.setGoal("To make the majority of the town mafia members");
        } else { // "Mafiaboss- GodFather"
            super.setRole("Mafiaboss- GodFather");
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

    public MafiaType getMafiaType() {
        return mafiaType;
    }

    public void setMafiaType(MafiaType mafiaType) {
        this.mafiaType = mafiaType;
    }
}
