package mafia.entities.player_roles;

/**
 * This class stores the data of each player
 */
public class Player {

    private int playPosition;
    private int status;//0:Alive | 1:Dead | 2:Heal | 3:Protected | 4: Dead for more that one turn
    private int playerTarget;
    private String name;
    private String role;
    private String roleInfo;
    private String goal;
    private boolean isMafia;
    private boolean inBar;//If true the player cannot do anything that night.
    private boolean isTargetSelected;


    //Setters//
    public void setName(String name) {
        this.name = name;
    }
    public void setPlayPosition(int playPosition) {
        this.playPosition = playPosition;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public void setPlayerTarget(int playerTarget){
        this.playerTarget = playerTarget;
    }
    public void setRole(String role){
        this.role = role;
    }
    public void setRoleInfo(String roleInfo){
        this.roleInfo = roleInfo;
    }
    public void setIsMafia(boolean isMafia) {
        this.isMafia = isMafia;
    }
    public void setInBar(boolean isBar) {
        this.inBar = isBar;
    }
    public void setGoal(String goal) {
        this.goal = goal;
    }
    public void setIsTargetSelected(boolean selection) {
        isTargetSelected = selection;
    }

    //Getters//
    public String getName() {
        return name;
    }
    public int getPlayPosition() {
        return playPosition;
    }
    public int getStatus() {
        return status;
    }
    public int getPlayerTarget(){
        return playerTarget;
    }
    public String getRole(){
        return role;
    }
    public String getRoleInfo(){
        return roleInfo;
    }
    public boolean isMafia() {
        return isMafia;
    }
    public boolean inBar() {
        return inBar;
    }
    public String getGoal() {
        return goal;
    }
    public boolean getIsTargetSelected() {
        return isTargetSelected;
    }

    // Temporary: Prints out information about the player for easier debugging
    public String toString() {
        return "\nPlayer name: " + this.getName() + ", " +
                "Player position: " + this.getPlayPosition() + ", " +
                "Player status: " + this.getStatus() + ", " +
                "Player role: " + this.getRole();
    }

}
