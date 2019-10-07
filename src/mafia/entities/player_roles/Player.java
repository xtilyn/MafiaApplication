package mafia.entities.player_roles;

import mafia.entities.data_types.PlayerStatus;

/**
 * This class stores the data of each player
 */
public abstract class Player {

    private int playPosition;
    private PlayerStatus status;
    private String name;
    private String roleInfo;
    private String goal;
    private String role;
    private boolean inBar;//If true the player cannot do anything that night.

    //Setters//
    public void setName(String name) {
        this.name = name;
    }
    public void setPlayPosition(int playPosition) {
        this.playPosition = playPosition;
    }
    public void setStatus(PlayerStatus status) {
        this.status = status;
    }
    public void setRoleInfo(String roleInfo){
        this.roleInfo = roleInfo;
    }
    public void setInBar(boolean isBar) {
        this.inBar = isBar;
    }
    public void setGoal(String goal) {
        this.goal = goal;
    }

    //Getters//
    public String getName() {
        return name;
    }
    public int getPlayPosition() {
        return playPosition;
    }
    public PlayerStatus getStatus() {
        return status;
    }
    public String getRoleInfo(){
        return roleInfo;
    }
    public boolean inBar() {
        return inBar;
    }
    public String getGoal() {
        return goal;
    }

    // Temporary: Prints out information about the player for easier debugging
    public String toString() {
        return "\nPlayer name: " + this.getName() + ", " +
                "Player position: " + this.getPlayPosition() + ", " +
                "Player status: " + this.getStatus() + ", ";
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
