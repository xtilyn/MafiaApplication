package mafia.models;

import javafx.concurrent.Task;
import mafia.MafiaApp;

import java.util.ArrayList;
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

    public void setPlayerNames(ArrayList<String> playerNames) {
        this.playerNames = playerNames;
    }

    public void assignRoles(Runnable onSuccess) {
        Task<Void> roleAssignmentTask = new RoleAssignmentTask(MafiaApp.getTotalPlayers());
        Thread assign = new Thread(roleAssignmentTask);
        assign.setDaemon(true);
        assign.start();

        roleAssignmentTask.setOnSucceeded(e -> onSuccess.run());
    }
}

class RoleAssignmentTask extends Task<Void> {

    private static int totalPlayers;

    //Original List of Roles in strong form
    private List<String> jobs = new ArrayList<>();
    //Original List of Roles Descriptions
    private List<String> info = new ArrayList<>();
    //Original List of Goals for each player
    private List<String> goals = new ArrayList<>();

    private List<String> assignments = new ArrayList<>();
    private List<String> assignmentsInfo = new ArrayList<>();
    private List<String> assignmentsGoals = new ArrayList<>();


    private static Random rand = new Random();

    public RoleAssignmentTask(int numTotalPlayers) {
        totalPlayers = numTotalPlayers;
    }

    private void roleOfPlayers(List<String> roles, List<String> rolesInfo, List<String> goals) {

        //Loops through all of the players and assigns them a Role, Info and Goal
        for(int i=0; i< totalPlayers; i++){
            MafiaApp.playerInfo.get(i).setRole(roles.get(i));
            MafiaApp.playerInfo.get(i).setRoleInfo(rolesInfo.get(i));
            MafiaApp.playerInfo.get(i).setGoal(goals.get(i));
            //If the String role of the player contains the word "Mafia:"
            if(MafiaApp.playerInfo.get(i).getRole().contains("Mafia:")){
                //This boolean is for the detective checking if the target is part of the Mafia
                //GodFather is not included,z as he is hidden from the detective
                MafiaApp.playerInfo.get(i).setIsMafia(true);

            }else{
                MafiaApp.playerInfo.get(i).setIsMafia(false);
            }
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

        jobs.add("Townie");
        info.add("Do nothing at night");
        goals.add("To have only town members left");

        jobs.add("Detective");
        info.add("Reveals the team for one player per night");
        goals.add("To have only town members left");

        jobs.add("Mafia: Hitman");
        info.add("May kill someone each night");
        goals.add("To make the majority of the town mafia members");

        jobs.add("Doctor");
        info.add("May heal one player each night (Can't target same person two nights in a row)");
        goals.add("To have only town members left");

        jobs.add("Survivor");
        info.add("Do nothing at night");
        goals.add("To be the last town member left alive");

        if (totalPlayers > 5){
            jobs.add("Mafia- Barman");
            info.add("May stop the action of another player each night (Can't go same person each night)");
            goals.add("To make the majority of the town mafia members");
        }

        if (totalPlayers > 6){
            jobs.add("Bodyguard");
            info.add("May save another person by stepping infront of the bullet per night. (You will die in their place)");
            goals.add("To have only town members left");
        }

        if (totalPlayers > 7){
            jobs.add("Lyncher");
            info.add("Do nothing at night");
            goals.add("To Lynch a specific player to win solo win the game | ");
        }

        if (totalPlayers > 8){
            jobs.add("Mafiaboss- GodFather");
            info.add("Hidden from the Detective. Can send a message to another Mafia memeber each night");
            goals.add("To make the majority of the town mafia members");
        }

        if (totalPlayers > 9){
            jobs.add("Vigilante");
            info.add("May kill new person each night, but is trying to kill Mafia. Note: Do not have to kill someone each night");
            goals.add("To have only town members left");
        }



    }

    private void randomizeLists(){
        int num;
        String select;
        String select2;
        String select3;
        for(int i=0; i < totalPlayers; i++){
            num = rand.nextInt(totalPlayers-i);

            //Takes instance from this array to be added to new one
            //Then removes the instance from old List (So it can't be re-used)
            select = jobs.get(num);
            select2 = info.get(num);
            select3 = goals.get(num);
            jobs.remove(num);
            info.remove(num);
            goals.remove(num);
            //Adds the randomly selected object to the back of the new List
            assignments.add(select);
            assignmentsInfo.add(select2);
            assignmentsGoals.add(select3);
        }
    }

    @Override
    protected Void call() throws Exception {
        playerAssignment();
        roleOfPlayers(assignments, assignmentsInfo, assignmentsGoals);
        return null;
    }

    @Override
    protected void failed() {
        System.out.println("role assignment task failed");
        super.failed();
    }
}