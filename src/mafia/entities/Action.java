package mafia.entities;

import mafia.entities.data_types.PlayerStatus;

import java.util.Arrays;
import java.util.HashMap;

import static mafia.MafiaApp.playerInfo;

/**
 * 2017-02-19.
 */
public class Action {

    private int totalPlayers;
    private HashMap<String, Integer> jobPoaitionMap;
    // Roles who can target during the night
    private String[] canTarget = {"Mafia: Hitman", "Doctor", "Mafia- Barman", "Bodyguard",
            "Vigilante", "Detective"};

    public Action(int totalPlayers, HashMap<String, Integer> map) {
        this.totalPlayers = totalPlayers;
        this.jobPoaitionMap = map;
    }

//    public void scenarios(List<Player> playerInfo, int totalPlayers){
//
//        this.totalPlayers = totalPlayers;
//        this.playerInfo = playerInfo;
//
//        //Must be in this order
//        //Calls the barman function only if there is a barman (more than 5 people)
//        if(totalPlayers>5)
//        {barman();
//        }
//        if(totalPlayers>6)
//        {bodyguard();
//        }
//        hitman();
//        if(totalPlayers>9){
//            vigilante();
//        }
//        doctor();
//        if(totalPlayers>8)
//        {godFather();
//        }
//
//    }

    public boolean barman(int target){
        playerInfo.get(target).setInBar(true);
        // if the target is part of those who can target during night
        if (Arrays.asList(canTarget).contains(playerInfo.get(target).getRole()))
            return true;
        else return false;
    }

    public void bodyguard(int target){
        playerInfo.get(target).setStatus(PlayerStatus.PROTECTED);
    }

    public void hitman(int target){
        // if the target is protected, kill bodyguard instead
        if (playerInfo.get(target).getStatus() == PlayerStatus.PROTECTED) {
            int bodyguard = jobPoaitionMap.get("Bodyguard");
            playerInfo.get(bodyguard).setStatus(PlayerStatus.DEAD); //If the target was protected, bodyguard is killed instead
        } else
            playerInfo.get(target).setStatus(PlayerStatus.DEAD); //Sets the target of the hitman to 1. this kills them :)
    }

    public void vigilante(int target){
        // if the target is protected, kill bodyguard instead
        if (playerInfo.get(target).getStatus() == PlayerStatus.PROTECTED) {
            int bodyguard = jobPoaitionMap.get("Bodyguard");
            playerInfo.get(bodyguard).setStatus(PlayerStatus.DEAD);
        } else {
            playerInfo.get(target).setStatus(PlayerStatus.DEAD);
        }
    }

    public void doctor(int target){
        if(playerInfo.get(target).getStatus()==PlayerStatus.DEAD){
            playerInfo.get(target).setStatus(PlayerStatus.HEALED);//Sets the status for the target of the doctor to 2. This saves them from death
        }
    }


    /**
     * This loops through the list of players until it finds the indexValue for the position of the player (role)
     * @param role
     * @return the index value of the position of the player with designated role
     */
    public int search(String role){
        int position = -1;
        for(int i = 0; i<totalPlayers; i++){
            if(playerInfo.get(i).getRole().contains(role)){
                position = i;
                return position;
            }
        }

        return position;
    }

}
