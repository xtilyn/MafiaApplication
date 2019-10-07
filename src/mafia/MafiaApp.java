package mafia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mafia.entities.Story;
import mafia.entities.data_types.PlayerStatus;
import mafia.entities.player_roles.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Game class methods
 */

public class MafiaApp extends Application {

    private final static int WIDTH = 411;
    private final static int HEIGHT = 600;

    //This int stores the total number of PLayers, does not once entered in numberOfPlayers()
    private static int totalPlayers;

    //Runs the game loop until the game is over
    private boolean runTime;

    //Boolean for if the Mafia or town people win at the end of the game
    private boolean mafiaWin;

    public static List<String> mafiaMembers = new ArrayList<>();

    //List of each player (class) and his/her info (name, role, target, position, etc)
    public static List<Player> playerInfo = new ArrayList<>();


    public boolean getMafiaWin() {
        return mafiaWin;
    }

    public static void setTotalPlayers(int num) {
        totalPlayers = num;
    }

    public static int getTotalPlayers() {
        return totalPlayers;
    }

    static void roleOfPlayers(List<String> roles, List<String> rolesInfo, List<String> goals) {

        //Loops through all of the players and assigns them a Role, Info and Goal
        for (int i = 0; i < totalPlayers; i++) {
            playerInfo.get(i).setRole(roles.get(i));
            playerInfo.get(i).setRoleInfo(rolesInfo.get(i));
            playerInfo.get(i).setGoal(goals.get(i));
            if (playerInfo.get(i).getRole().contains("Mafia")) {
                //This is a list of the names, used at night to display the ALL Mafia members to other Mafia members
                mafiaMembers.add(playerInfo.get(i).getName());
            }
        }

    }

    /**
     * This method resets the status for all players that are alive to 1
     * It also calls the printDeath(int player) method if the status of the player is 1 (Dead)
     * and also sets the status to 4 (Dead for more than one turn)
     * If the player was saved (Doctor targeted them to save that night) then prints the player was saved last night
     */
    public String resetStatus() {

        String story = "";
        for (Player player : playerInfo) {
            if (player.getStatus() == PlayerStatus.ALIVE) {
                if (player instanceof Mafia) {
                    ((Mafia) player).setIsTargetSelected(false);
                    ((Mafia) player).setPlayerTarget(-1);
                } else if (player instanceof Doctor) {
                    ((Doctor) player).setIsTargetSelected(false);
                    ((Doctor) player).setPlayerTarget(-1);
                } else if (player instanceof Bodyguard) {
                    ((Bodyguard) player).setIsTargetSelected(false);
                    ((Bodyguard) player).setPlayerTarget(-1);
                } else if (player instanceof Vigilante) {
                    ((Vigilante) player).setIsTargetSelected(false);
                    ((Vigilante) player).setPlayerTarget(-1);
                } else if (player instanceof Detective) {
                    ((Detective) player).setIsTargetSelected(false);
                    ((Detective) player).setPlayerTarget(-1);
                }
            }

            //If the player is in Protected status puts them into Alive Status (0)
            if (player.getStatus() == PlayerStatus.PROTECTED) {
                player.setStatus(PlayerStatus.ALIVE);

                //If the player is Dead prints the death story and puts them into Dead for more than one night status(4)
            } else if (player.getStatus() == PlayerStatus.DEAD) {
                story += getEvent(player.getPlayPosition(), "dead");
                player.setStatus(PlayerStatus.DEAD_FOR_MORE_THAN_ONE_TURN);

                //If the player was Saved, prints the saved story and sets them to Alive status(0)
            } else if (player.getStatus() == PlayerStatus.HEALED) {
                story += getEvent(player.getPlayPosition(), "alive");
                player.setStatus(PlayerStatus.ALIVE);
            }
            player.setInBar(false);//Removes any  player that may have been in the bar out
        }
        return story;
    }

    /**
     * This method determines the death message of any player that may have died during the night
     * Then prints that they were either killed by the attacker or saved by the doctor
     *
     * @param player, status
     */
    private String getEvent(int player, String status) {

        String name = playerInfo.get(player).getName();
        Story s = new Story(name);
        s.initialScenario();
        if (status.equals("dead")) {
            s.dead();
        } else if (status.equals("alive")) {
            s.healed();
        }
        String story = s.printStory();
        System.out.println(story);
        String event = s.getEvent();//The string for if the player lived or died
        System.out.println(event);
        System.out.println();

        return "\n" + story + "\n" + event + "\n";

    }

    /**
     * This method counts the number of alive players for both the Mafia and town members
     * If there is no Mafia members left, the game is over and the town members win
     * If the Mafia has more players then the game is over and the Mafia wins
     */
    public boolean checkGameOver() {
        // Game Over
        boolean gameOver = false;
        int mafiaTotal = 0;
        int townTotal = 0;
        //loops through all of the players and counts the total amount of Mafia and town people that are alive at the end of the round (Status 0)
        for (Player aPlayerInfo : playerInfo) {
            if (aPlayerInfo.getRole().contains("Mafia") && aPlayerInfo.getStatus() == PlayerStatus.ALIVE) {
                mafiaTotal += 1;
            } else if (
                    (aPlayerInfo.getRole().equals("Detective") || aPlayerInfo.getRole().equals("Doctor")
                            || aPlayerInfo.getRole().equals("Vigilante") || aPlayerInfo.getRole().equals("Townie")
                            || aPlayerInfo.getRole().equals("Survivor") || aPlayerInfo.getRole().equals("Bodyguard")
                            || aPlayerInfo.getRole().equals("Lyncher"))
                            && aPlayerInfo.getStatus() == PlayerStatus.ALIVE) {
                townTotal += 1;
            }
        }
        if (mafiaTotal == 0) {
            mafiaWin = false;
            gameOver = true; //Stops the game if there are no Mafia members alive
        } else if (mafiaTotal > townTotal) {
            mafiaWin = true;
            gameOver = true;
        }
        return gameOver;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("ui/mainMenu.fxml"));
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Mafia");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}