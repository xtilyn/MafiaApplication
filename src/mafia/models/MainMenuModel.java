package mafia.models;

import javafx.concurrent.Task;
import mafia.MafiaApp;
import mafia.entities.player_roles.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainMenuModel {

    private ArrayList<String> playerNames = new ArrayList<>();

    public void setNumPlayers(int num) {
        MafiaApp.setTotalPlayers(num);
    }

    public ArrayList<String> getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(String[] playerNames) {
        this.playerNames.clear();
        this.playerNames.addAll(Arrays.asList(playerNames));
    }

    public void assignRoles() {
        RoleAssignmentTask roleAssignmentTask = new RoleAssignmentTask(MafiaApp.getTotalPlayers());
        roleAssignmentTask.assignRoles();
        for (int i = 0; i < MafiaApp.getTotalPlayers(); i++) {
            MafiaApp.playerInfo.get(i).setName(playerNames.get(i));
            MafiaApp.playerInfo.get(i).setPlayPosition(i);
            MafiaApp.playerInfo.get(i).setStatus(0);//0:Alive | 1:Dead | 2:Heal | 3:Protected | 4:Dead for more than one night
            MafiaApp.playerInfo.get(i).setInBar(false);
        }
    }
}

class RoleAssignmentTask {

    private static int totalPlayers;

    //Original List of Roles in strong form
    private List<Player> roles = new ArrayList<>();
    private List<Player> assignments = new ArrayList<>();

    private static Random rand = new Random();

    public RoleAssignmentTask(int numTotalPlayers) {
        totalPlayers = numTotalPlayers;
    }

    private void roleOfPlayers(List<Player> roles) {

        //Loops through all of the players and assigns them a Role, Info and Goal
        for(int i=0; i< totalPlayers; i++){
            MafiaApp.playerInfo.add(roles.get(i));

            if(MafiaApp.playerInfo.get(i).getRole().contains("Mafia")){
                //This is a list of the names, used at night to display the ALL Mafia members to other Mafia members
                MafiaApp.mafiaMembers.add(MafiaApp.playerInfo.get(i).getName());

            }
        }

    }

    private void playerAssignment(){
        initializeRoles();
        randomizeLists();
    }

    private void initializeRoles(){

        roles.add(new Townie());
        roles.add(new Detective());
        roles.add(new Mafia("Mafia: Hitman"));
        roles.add(new Doctor());
        roles.add(new Survivor());

        if (totalPlayers > 5){
            roles.add(new Mafia("Mafia- Barman"));
        }

        if (totalPlayers > 6){
            roles.add(new Bodyguard());
        }

        if (totalPlayers > 7){
            roles.add(new Lyncher());
        }

        if (totalPlayers > 8){
            roles.add(new Mafia("Mafiaboss- GodFather"));
        }

        if (totalPlayers > 9){
            roles.add(new Vigilante());
        }

    }

    private void randomizeLists(){
        int num;
        Player select;
        for(int i=0; i < totalPlayers; i++){
            num = rand.nextInt(totalPlayers-i);
            select = roles.get(num);
            roles.remove(num);
            assignments.add(select);
        }
    }

    public void assignRoles() {
        playerAssignment();
        roleOfPlayers(assignments);
    }

}